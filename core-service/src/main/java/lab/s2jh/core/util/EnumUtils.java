package lab.s2jh.core.util;

import java.lang.reflect.Field;
import java.util.Map;

import lab.s2jh.core.annotation.MetaData;

import com.google.common.collect.Maps;

public class EnumUtils {

	private static Map<Class<?>, Map<String, String>> enumDatasContainer = Maps.newHashMap();

	/**
	 * 基于Enum类返回对应的key-value Map构建对象
	 * @param enumClass
	 * @return
	 */
	public static Map<String, String> getEnumDataMap(Class<?> enumClass) {
		Map<String, String> enumDataMap = enumDatasContainer.get(enumClass);
		if (enumDataMap != null) {
			return enumDataMap;
		}
		enumDataMap = Maps.newLinkedHashMap();
		enumDatasContainer.put(enumClass, enumDataMap);
		Field[] fields = enumClass.getFields();
		for (Field field : fields) {
			String name = field.getName();
			String label = name;
			MetaData entityComment = field.getAnnotation(MetaData.class);
			if (entityComment != null) {
				label = entityComment.value();
			}
			enumDataMap.put(name, label);
		}
		return enumDataMap;
	}

	public static String getEnumLabel(Enum elemnt) {
		Map<String, String> enumDataMap = getEnumDataMap(elemnt.getClass());
		return enumDataMap.get(elemnt.name());
	}
	
	public static String getEnumLabel(Class<?> enumClass,String name) {
		Map<String, String> enumDataMap = getEnumDataMap(enumClass);
		return enumDataMap.get(name);
	}
}
