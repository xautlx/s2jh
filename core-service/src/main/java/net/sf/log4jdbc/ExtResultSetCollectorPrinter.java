package net.sf.log4jdbc;

import java.util.List;

import org.slf4j.Logger;

/**
 * 增强：对于字符串太长的数据trim截取避免太长干扰显示
 */
public class ExtResultSetCollectorPrinter extends ResultSetCollectorPrinter{

    public ExtResultSetCollectorPrinter(Logger log) {
        super(log);
    }
    

    public void printResultSet(ResultSetCollector resultSetCollector) {

        int columnCount = resultSetCollector.getColumnCount();
        int maxLength[] = new int[columnCount];

        for (int column = 1; column <= columnCount; column++) {
            maxLength[column - 1] = resultSetCollector.getColumnName(column)
                    .length();
        }
        if (resultSetCollector.getRows() != null) {
            for (List<Object> printRow : resultSetCollector.getRows()) {
                int colIndex = 0;
                for (Object v : printRow) {
                    if (v != null) {
                        int length = v.toString().length();
                        if (length > maxLength[colIndex]) {
                            maxLength[colIndex] = length;
                        }
                    }
                    colIndex++;
                }
            }
        }
        for (int column = 1; column <= columnCount; column++) {
            maxLength[column - 1] = maxLength[column - 1] + 1;
        }

        print("|");
        for (int column = 1; column <= columnCount; column++) {
            print(padRight("-", maxLength[column - 1]).replaceAll(" ", "-")
                    + "|");
        }
        println();
        print("|");
        for (int column = 1; column <= columnCount; column++) {
            print(padRight(resultSetCollector.getColumnName(column),
                    maxLength[column - 1])
                    + "|");
        }
        println();
        print("|");
        for (int column = 1; column <= columnCount; column++) {
            print(padRight("-", maxLength[column - 1]).replaceAll(" ", "-")
                    + "|");
        }
        println();
        if (resultSetCollector.getRows() != null) {
            for (List<Object> printRow : resultSetCollector.getRows()) {
                int colIndex = 0;
                print("|");
                for (Object v : printRow) {
                    print(padRight(v == null ? "null" : v.toString(),
                            maxLength[colIndex])
                            + "|");
                    colIndex++;
                }
                println();
            }
        }
        print("|");
        for (int column = 1; column <= columnCount; column++) {
            print(padRight("-", maxLength[column - 1]).replaceAll(" ", "-")
                    + "|");
        }
        println();
        resultSetCollector.reset();
    }

    /**
     * 增强：对于字符串太长的数据trim截取避免太长干扰显示
     */
    public static String padRight(String s, int n) {
        if(s!=null && s.length()>100){
            s=s.substring(0, 99)+"...";
        }
        return String.format("%1$-" + n + "s", s);
    }
}
