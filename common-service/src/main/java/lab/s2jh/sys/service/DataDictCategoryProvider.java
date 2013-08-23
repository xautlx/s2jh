package lab.s2jh.sys.service;

import java.lang.reflect.Field;
import java.util.Map;

import lab.s2jh.core.annotation.MetaData;

import org.springframework.util.Assert;

import com.google.common.collect.Maps;

/**
 * DataDict对象的category作为分组标识，一般采用英文代码
 * 业务模块可向此容器中添加注册Key-Label结构数据，实现分类定义的友好的中英文对照
 */
public class DataDictCategoryProvider {

    private static Map<String, String> dataDictCategoryKeyLabelMap = Maps.newHashMap();

    /**
     * 单一的添加key-label字符串数据
     * @param key
     * @param label
     */
    public static void addKeyLabelData(String key, String label) {
        dataDictCategoryKeyLabelMap.put(key, label);
    }

    /**
     * 直接以Map结构数据追加，数据来源可以是从数据库查询或直接Spring的map元素声明方式定义等
     * @param keyLabels
     */
    public static void addKeyLabelData(Map<String, String> keyLabels) {
        dataDictCategoryKeyLabelMap.putAll(keyLabels);
    }

    /**
     * 添加枚举类型，以枚举类型的name为KEY和MetaData注解的title属性值为LABEL
     * @param elementType 各枚举定义有MetaData注解的枚举类型
     */
    public static <E extends Enum<E>> void addKeyLabelData(Class<E> elementType) {
        for (Field enumfield : elementType.getFields()) {
            MetaData entityComment = enumfield.getAnnotation(MetaData.class);
            Assert.notNull(entityComment, "Enum as data dict category provider must have MetaData annotation.");
            String key = enumfield.getName();
            String label = entityComment.title();
            addKeyLabelData(key, label);
        }
    }

    public static Map<String, String> getDataDictCategoryKeyLabelMap() {
        return dataDictCategoryKeyLabelMap;
    }
}
