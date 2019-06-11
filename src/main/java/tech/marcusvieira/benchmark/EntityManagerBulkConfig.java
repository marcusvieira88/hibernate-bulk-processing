package tech.marcusvieira.benchmark;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;

public class EntityManagerBulkConfig {

    private final EntityManagerFactory entityManagerFactory;

    public EntityManagerBulkConfig() {
        entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(
            new PersistenceUnitInfo() {
                @Override
                public String getPersistenceUnitName() {
                    return "benchmarkPU";
                }

                @Override
                public String getPersistenceProviderClassName() {
                    return "org.hibernate.jpa.HibernatePersistenceProvider";
                }

                @Override
                public PersistenceUnitTransactionType getTransactionType() {
                    return PersistenceUnitTransactionType.RESOURCE_LOCAL;
                }

                @Override
                public DataSource getJtaDataSource() {
                    return null;
                }

                @Override
                public DataSource getNonJtaDataSource() {
                    return null;
                }

                @Override
                public List<String> getMappingFileNames() {
                    return Collections.emptyList();
                }

                @Override
                public List<URL> getJarFileUrls() {
                    return Collections.emptyList();
                }

                @Override
                public URL getPersistenceUnitRootUrl() {
                    return null;
                }

                @Override
                public List<String> getManagedClassNames() {
                    return Arrays.asList(
                        "tech.marcusvieira.model.PersonEntity"
                    );
                }

                @Override
                public boolean excludeUnlistedClasses() {
                    return false;
                }

                @Override
                public SharedCacheMode getSharedCacheMode() {
                    return null;
                }

                @Override
                public ValidationMode getValidationMode() {
                    return null;
                }

                @Override
                public Properties getProperties() {
                    return new Properties();
                }

                @Override
                public String getPersistenceXMLSchemaVersion() {
                    return null;
                }

                @Override
                public ClassLoader getClassLoader() {
                    return null;
                }

                @Override
                public void addTransformer(ClassTransformer transformer) {

                }

                @Override
                public ClassLoader getNewTempClassLoader() {
                    return null;
                }
            },
            Collections.unmodifiableMap(new HashMap<String, Object>() {{
                put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
                put("hibernate.hikari.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
                put("hibernate.hikari.dataSource.url", "jdbc:postgresql://localhost:5432/benchmark");
                put("hibernate.hikari.dataSource.user", "admin");
                put("hibernate.hikari.dataSource.password", "admin");
                put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
                put("hibernate.hbm2ddl.auto", "update");
                //Bulk configuration
                // Reference Docs: https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/chapters/batch/Batching.html
                put("hibernate.jdbc.batch_size","30");
                put("hibernate.show_sql", false);
            }})
        );
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public EntityManager createEntityManager() { return entityManagerFactory.createEntityManager(); }
}
