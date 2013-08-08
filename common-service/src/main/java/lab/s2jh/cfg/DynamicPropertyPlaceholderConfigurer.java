package lab.s2jh.cfg;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * 基于数据库加载动态配置参数
 * 框架扩展属性加载：Spring除了从.properties加载属性数据，还会从T_SYS_CFG_PROP加载属性数据
 * 并且数据库如果存在同名属性则优先取数据库的属性值覆盖配置文件中的值
 * 为了避免意外的数据库配置导致系统崩溃，约定以cfg打头标识的参数表示可以被数据库参数覆写，其余的则不会覆盖文件定义的属性值
 */
public class DynamicPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private final static Logger logger = LoggerFactory.getLogger(DynamicPropertyPlaceholderConfigurer.class);

    private DataSource dataSource;
    private String nameColumn;
    private String valueColumn;
    private String tableName;

    private static Properties propertiesContainer = new Properties();

    @Override
    protected void loadProperties(final Properties props) throws IOException {
        super.loadProperties(props);
        propertiesContainer.putAll(props);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = String.format("select %s, %s from %s", nameColumn, valueColumn, tableName);
        logger.info("Reading configuration properties from database");

        try {
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    String name = rs.getString(nameColumn);
                    String value = rs.getString(valueColumn);
                    if (null == name || null == value) {
                        throw new SQLException("Configuration database contains empty data. Name='" + name
                                + "' Value='" + value + "'");
                    }
                    //为了避免意外的数据库配置导致系统崩溃，约定以cfg打头标识的参数表示可以被数据库参数覆写，其余的则不会覆盖文件定义的属性值
                    if (name.startsWith("cfg.")) {
                        logger.info("Setup database property: {}={}", name, value);
                        props.put(name, value);
                    }
                }
            });
        } catch (DataAccessException e) {
            logger.warn("Database configuration data read error: {}", e.getMessage());
        }

        if (props.size() == 0) {
            logger.warn("The configuration database could not be reached or does not contain any properties in '"
                    + tableName + "'");
        }

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public void setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static Properties getPropertiesContainer() {
        return propertiesContainer;
    }
}
