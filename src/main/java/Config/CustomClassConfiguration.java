package Config;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Properties;


public class CustomClassConfiguration implements PersistenceUnitInfo {
    private final HikariDataSource dataSource;

    public CustomClassConfiguration() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/HJPA_DB?createDatabaseIfNotExist=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setMaximumPoolSize(10);
    }
    @Override
    public String getPersistenceUnitName() {
        return "my-persistence-unit";
    }

    @Override
    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    @Override
    public String getScopeAnnotationName() {
        return "";
    }

    @Override
    public List<String> getQualifierAnnotationNames() {
        return List.of();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        return dataSource;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return null;
    }

    @Override
    public List<String> getMappingFileNames() {
        return List.of();
    }

    @Override
    public List<URL> getJarFileUrls() {
        return List.of();
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public List<String> getManagedClassNames() {
        return List.of("org.hibernatejpa.entity.Student");
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.ENABLE_SELECTIVE;
    }

    @Override
    public ValidationMode getValidationMode() {
        return ValidationMode.NONE;
    }

    @Override
    public Properties getProperties() {
         Properties props = new Properties();

        props.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        props.put("jakarta.persistence.jdbc.url",
                "jdbc:mysql://localhost:3306/HJPA_DB?createDatabaseIfNotExist=true");
        props.put("jakarta.persistence.jdbc.user", "root");
        props.put("jakarta.persistence.jdbc.password", "root");

        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "create");

        return props;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "3.0";
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void addTransformer(ClassTransformer classTransformer) {

    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
