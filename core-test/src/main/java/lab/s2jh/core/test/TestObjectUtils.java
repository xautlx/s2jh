package lab.s2jh.core.test;

import java.util.Date;

import lab.s2jh.core.util.MockEntityUtils;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基于反射的用于生成测试Entity对象示例的辅助工具类
 */
public class TestObjectUtils {

    public static <X> X buildMockObject(Class<X> clazz) {
        return MockEntityUtils.buildMockObject(clazz);
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
        TestVO testVO = TestObjectUtils.buildMockObject(TestVO.class);
        System.out
                .println("Mock Entity: " + ReflectionToStringBuilder.toString(testVO, ToStringStyle.MULTI_LINE_STYLE));
    }
}
