package net.sf.log4jdbc;

import org.hibernate.engine.jdbc.internal.JdbcCoordinatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 增强log4jdbc日志处理
 */
public class ExtSlf4jSpyLogDelegator extends Slf4jSpyLogDelegator {

    private static final Logger jdbcLogger = LoggerFactory.getLogger("jdbc.audit");

    private final Logger resultSetTableLogger = LoggerFactory.getLogger("jdbc.resultsettable");

    static {
        jdbcLogger.info("Static init ExtSlf4jSpyLogDelegator...");
        SpyLogFactory.setSpyLogDelegator(new ExtSlf4jSpyLogDelegator());
    }

    /**
     * 抑制Hibernate在关闭Statement后调用getMaxRows方法异常
     * @see JdbcCoordinatorImpl.close
     */
    @Override
    public void exceptionOccured(Spy spy, String methodCall, Exception e, String sql, long execTime) {
        if ("getMaxRows()".equals(methodCall)) {
            if (jdbcLogger.isDebugEnabled()) {
                String classType = spy.getClassType();
                Integer spyNo = spy.getConnectionNumber();
                String header = spyNo + ". " + classType + "." + methodCall;
                jdbcLogger.error(header, e);
            }
            return;
        }
        super.exceptionOccured(spy, methodCall, e, sql, execTime);
    }

    public void sqlTimingOccured(Spy spy, long execTime, String methodCall, String sql) {

    }

    public String sqlOccured(Spy spy, String methodCall, String sql) {
        //忽略频繁执行的Quartz SQL语句
        if (sql.indexOf("QRTZ_TRIGGERS") > -1 || sql.indexOf("QRTZ_SCHEDULER_STATE") > -1) {
            return "";
        }
        return super.sqlOccured(spy, methodCall, sql);
    }

    /**
     * 优化结果集打印处理：对于太长的大文本字段截取只显示部分
     */
    @Override
    public void resultSetCollected(ResultSetCollector resultSetCollector) {
        new ExtResultSetCollectorPrinter(resultSetTableLogger).printResultSet(resultSetCollector);
    }
}
