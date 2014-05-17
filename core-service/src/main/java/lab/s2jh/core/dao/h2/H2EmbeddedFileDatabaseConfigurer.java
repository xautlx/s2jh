package lab.s2jh.core.dao.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;

/**
 * Spring JDBC默认只支持内存模式的H2嵌入数据库 @see H2EmbeddedDatabaseConfigurer
 * 扩展实现基于File文件模式的H2嵌入式数据库，开发过程就不再需要每次初始化数据库
 * 在实际配置过程中注意以databaseName指定H2数据绝对路径文件， 配置示例:
        <bean id="dataSource"
            class="org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean">
            <property name="databaseName" value="${user.dir}\h2\prototype" />
            <property name="databaseConfigurer">
                <bean
                    class="lab.s2jh.core.dao.h2.H2EmbeddedFileDatabaseConfigurer"
                    factory-method="getInstance">
                </bean>
            </property>
        </bean>     
 */

public class H2EmbeddedFileDatabaseConfigurer implements EmbeddedDatabaseConfigurer {

    private static Logger logger = LoggerFactory.getLogger(H2EmbeddedFileDatabaseConfigurer.class);

    public void shutdown(DataSource dataSource, String databaseName) {
        try {
            Connection connection = dataSource.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("SHUTDOWN");
        } catch (SQLException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Could not shutdown embedded database", ex);
            }
        }
    }

    public void configureConnectionProperties(ConnectionProperties properties, String databaseName) {
        properties.setDriverClass(org.h2.Driver.class);
        logger.info("Using H2 EmbeddedFileDatabase: {}", databaseName);
        properties.setUrl(String.format("jdbc:h2:file:%s;DB_CLOSE_DELAY=-1", databaseName));
        properties.setUsername("sa");
        properties.setPassword("");
    }
}