package lab.s2jh.core.test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import javax.persistence.Column;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于反射的用于生成测试Entity对象示例的辅助工具类
 */
public class TestObjectUtils {

    private final static Logger logger = LoggerFactory.getLogger(TestObjectUtils.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <X> X buildMockObject(Class<X> clazz) {
        X x = null;
        try {
            x = clazz.newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                String mn = method.getName();
                if (mn.startsWith("set")) {
                    Class[] parameters = method.getParameterTypes();
                    if (parameters.length == 1) {
                        Method getMethod = MethodUtils.getAccessibleMethod(clazz, "get" + mn.substring(3), null);
                        if (getMethod != null) {
                            if (getMethod.getName().equals("getId")) {
                                continue;
                            }
                            Object value = null;
                            Class parameter = parameters[0];
                            if (parameter.isAssignableFrom(String.class)) {
                                Column column = getMethod.getAnnotation(Column.class);
                                int columnLength = 32;
                                if (column != null && column.length() < columnLength) {
                                    columnLength = column.length();
                                }
                                value = RandomStringUtils.randomAlphabetic(columnLength);
                            } else if (parameter.isAssignableFrom(Date.class)) {
                                value = new Date();
                            } else if (parameter.isAssignableFrom(BigDecimal.class)) {
                                value = new BigDecimal(new Random().nextDouble());
                            } else if (parameter.isAssignableFrom(Integer.class)) {
                                value = new Random().nextInt();
                            } else if (parameter.isAssignableFrom(Boolean.class)) {
                                value = new Random().nextBoolean();
                            } else if (parameter.isEnum()) {
                                Method m = parameter.getDeclaredMethod("values", null);
                                Object[] result = (Object[]) m.invoke(parameter.getEnumConstants()[0], null);
                                value = result[new Random().nextInt(result.length)];
                            }
                            if (value != null) {
                                MethodUtils.invokeMethod(x, mn, value);
                                logger.debug("{}={}", method.getName(), value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

    public static class TestVO {
        private String str;
        private Date dt;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public Date getDt() {
            return dt;
        }

        public void setDt(Date dt) {
            this.dt = dt;
        }
    }

    public static void main(String[] args) {
        TestObjectUtils.buildMockObject(TestVO.class);
    }
}
