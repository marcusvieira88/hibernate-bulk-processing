package tech.marcusvieira.benchmark;

import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import tech.marcusvieira.model.PersonEntity;

public class BulkProcessingBenchmark {

    private static final int AMOUNT_OF_ITEMS = 10000;
    private static final int BATCH_SIZE = 30;

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Warmup(iterations = 1, time = 1)
    public void regularPersist() {

        final EntityManagerConfig entityManagerConfig = new EntityManagerConfig();
        EntityManager entityManager = entityManagerConfig.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            for (int i = 0; i < AMOUNT_OF_ITEMS; i++) {
                PersonEntity personEntity = new PersonEntity();
                personEntity.setFirstName("FirstName" + i);
                personEntity.setLastName("LastName" + i);
                personEntity.setAge(i);
                entityManager.persist(personEntity);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close();
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Warmup(iterations = 1, time = 1)
    public void bulkPersist() {

        final EntityManagerBulkConfig entityManagerConfig = new EntityManagerBulkConfig();
        EntityManager entityManager = entityManagerConfig.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            for (int i = 0; i < AMOUNT_OF_ITEMS; i++) {

                if (i > 0 && i % BATCH_SIZE == 0) {
                    //flush a batch of inserts and release memory
                    entityManager.flush();
                    entityManager.clear();
                }

                PersonEntity personEntity = new PersonEntity();
                personEntity.setFirstName("FirstName" + i);
                personEntity.setLastName("LastName" + i);
                personEntity.setAge(i);
                entityManager.persist(personEntity);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close();
        }
    }
}
