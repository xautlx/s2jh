package lab.s2jh.core.web.convert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 对于BigDecimal默认会转换为科学计数法或位数较长
 * 定制化转化实现指定显示小数位数
 */
public class BigDecimalConvert extends StrutsTypeConverter {

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (BigDecimal.class.equals(toClass)) {
            String str = values[0];
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            BigDecimal d = new BigDecimal(str);
            return d;
        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof BigDecimal) {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format((BigDecimal)o);
        }
        return "";
    }
}
