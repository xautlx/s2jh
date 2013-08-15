package lab.s2jh.tool.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 基本CRUD框架代码生成工具类，Maven方式运行工程的pom.xml调用生成代码
 * 配置文件在：generator/entity_list.properties
 * 生成的代码在：generator/codes目录下，其中standalone是一个Entity一个目录，用于偶尔重复生成拷贝某一个Entity相关代码之用，integrate是整合到一起的目录结构
 * 注意：生成的代码都在当前tools工程中，需自行根据需要拷贝所需相关代码到对应的业务工程目录或Package下，然后在业务工程中基于框架代码添加相关业务逻辑代码
 * 
 * 模板文件位置：project-tools\src\main\resources\lab\s2jh\tool\builder\freemarker，可自行根据项目需要调整模板定义格式
 */
public class SourceCodeFrameworkBuilder {

    public static void main(String[] args) throws Exception {
        Configuration cfg = new Configuration();
        // 设置FreeMarker的模版文件位置
        cfg.setClassForTemplateLoading(SourceCodeFrameworkBuilder.class, "/lab/s2jh/tool/builder/freemarker");
        cfg.setDefaultEncoding("UTF-8");
        String rootPath = args[0];

        Set<String> entityNames = new HashSet<String>();

        String entityListFile = rootPath + "entity_list.properties";
        BufferedReader reader = new BufferedReader(new FileReader(entityListFile));
        String line;
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
                entityNames.add(line);
            }
        }

        new File(rootPath + "\\codes").mkdir();
        new File(rootPath + "\\codes\\integrate").mkdir();
        new File(rootPath + "\\codes\\standalone").mkdir();

        for (String entityName : entityNames) {

            String integrateRootPath = rootPath + "\\codes\\integrate\\";
            String standaloneRootPath = rootPath + "\\codes\\standalone\\";

            String rootPackage = StringUtils.substringBetween(entityName, "[", "]");
            String rootPackagePath = StringUtils.replace(rootPackage, ".", "\\");

            String className = StringUtils.substringAfterLast(entityName, ".");
            String classFullName = StringUtils.replaceEach(entityName, new String[] { "[", "]" },
                    new String[] { "", "" });

            String modelName = StringUtils.substringBetween(entityName, "].", ".entity");
            String modelPath = StringUtils.replace(modelName, ".", "/");
            modelPath = "/" + modelPath;
            String modelPackagePath = StringUtils.replace(modelName, ".", "\\");
            modelPackagePath = "\\" + modelPackagePath;

            Map<String, Object> root = new HashMap<String, Object>();
            String nameField = propertyToField(StringUtils.uncapitalize(className)).toLowerCase();
            root.put("model_name", modelName);
            root.put("model_path", modelPath);
            root.put("entity_name", className);
            root.put("entity_name_uncapitalize", StringUtils.uncapitalize(className));
            root.put("entity_name_field", nameField);
            root.put("root_package", rootPackage + "." + modelName);
            root.put("action_package", rootPackage);
            root.put("table_name", "T_TODO_" + className.toUpperCase());
            root.put("base", "${base}");
            debug("Entity Data Map=" + root);
            Class entityClass = Class.forName(classFullName);
            MetaData classEntityComment = (MetaData) entityClass.getAnnotation(MetaData.class);
            if (classEntityComment != null) {
                root.put("model_title", classEntityComment.title());
            } else {
                root.put("model_title", entityName);
            }

            Set<Field> fields = new HashSet<Field>();
            Field[] curfields = entityClass.getDeclaredFields();
            for (Field field : curfields) {
                fields.add(field);
            }
            Field[] parentfields = entityClass.getSuperclass().getDeclaredFields();
            for (Field field : parentfields) {
                fields.add(field);
            }
            List<EntityCodeField> entityFields = new ArrayList<EntityCodeField>();
            for (Field field : fields) {
                debug(" - Field=" + field);
                Class fieldType = field.getType();
                EntityAutoCode entityAutoCode = field.getAnnotation(EntityAutoCode.class);
                if (entityAutoCode == null) {
                    continue;
                }

                EntityCodeField entityCodeField = null;
                if (fieldType == String.class) {
                    entityCodeField = new EntityCodeField();
                } else if (fieldType == Boolean.class) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);
                    //entityCodeField.setListWidth(60);
                    entityCodeField.setListAlign("center");
                } else if (Number.class.isAssignableFrom(fieldType)) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);
                    entityCodeField.setListWidth(60);
                    entityCodeField.setListAlign("right");
                } else if (fieldType.isEnum()) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);
                    entityCodeField.setListWidth(80);
                    entityCodeField.setListAlign("center");
                } else if (fieldType == Date.class) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);
                    //entityCodeField.setListWidth(120);
                    entityCodeField.setListAlign("center");
                }

                if (entityCodeField != null) {
                    if (fieldType.isEnum()) {
                        entityCodeField.setEnumField(true);
                    }
                    entityCodeField.setFieldType(fieldType.getSimpleName());
                    entityCodeField.setSearch(entityAutoCode.search());
                    entityCodeField.setListHidden(entityAutoCode.listHidden());
                    entityCodeField.setEdit(entityAutoCode.edit());
                    entityCodeField.setList(entityAutoCode.listHidden() || entityAutoCode.listShow());
                    MetaData entityComment = field.getAnnotation(MetaData.class);
                    entityCodeField.setTitle(entityComment.title());
                    entityCodeField.setFieldName(field.getName());
                    if (entityAutoCode != null) {
                        entityCodeField.setOrder(entityAutoCode.order());
                    }
                    entityFields.add(entityCodeField);
                }
            }
            Collections.sort(entityFields);
            root.put("entityFields", entityFields);

            integrateRootPath = integrateRootPath + rootPackagePath + modelPackagePath;
            //process(cfg.getTemplate("Entity.ftl"), root, integrateRootPath + "\\entity\\", className + ".java");
            process(cfg.getTemplate("Dao.ftl"), root, integrateRootPath + "\\dao\\", className + "Dao.java");
            process(cfg.getTemplate("Service.ftl"), root, integrateRootPath + "\\service\\", className + "Service.java");
            process(cfg.getTemplate("Controller.ftl"), root, integrateRootPath + "\\web\\action\\", className
                    + "Controller.java");
            process(cfg.getTemplate("Test.ftl"), root, integrateRootPath + "\\test\\service\\", className
                    + "ServiceTest.java");
            process(cfg.getTemplate("JSP_Index.ftl"), root, integrateRootPath + "\\jsp\\", nameField + "-index.jsp");
            process(cfg.getTemplate("JSP_Input_Tabs.ftl"), root, integrateRootPath + "\\jsp\\", nameField
                    + "-inputTabs.jsp");
            process(cfg.getTemplate("JSP_Input_Basic.ftl"), root, integrateRootPath + "\\jsp\\", nameField
                    + "-inputBasic.jsp");
            process(cfg.getTemplate("JSP_View_Tabs.ftl"), root, integrateRootPath + "\\jsp\\", nameField
                    + "-viewTabs.jsp");
            process(cfg.getTemplate("JSP_View_Basic.ftl"), root, integrateRootPath + "\\jsp\\", nameField
                    + "-viewBasic.jsp");

            standaloneRootPath = standaloneRootPath + rootPackagePath + modelPackagePath + "\\" + className;
            //process(cfg.getTemplate("Entity.ftl"), root, standaloneRootPath + "\\entity\\", className + ".java");
            process(cfg.getTemplate("Dao.ftl"), root, standaloneRootPath + "\\dao\\", className + "Dao.java");
            process(cfg.getTemplate("Service.ftl"), root, standaloneRootPath + "\\service\\", className
                    + "Service.java");
            process(cfg.getTemplate("Controller.ftl"), root, standaloneRootPath + "\\web\\action\\", className
                    + "Controller.java");
            process(cfg.getTemplate("Test.ftl"), root, standaloneRootPath + "\\test\\service\\", className
                    + "ServiceTest.java");
            process(cfg.getTemplate("JSP_Index.ftl"), root, standaloneRootPath + "\\jsp\\", nameField + "-index.jsp");
            process(cfg.getTemplate("JSP_Input_Tabs.ftl"), root, standaloneRootPath + "\\jsp\\", nameField
                    + "-inputTabs.jsp");
            process(cfg.getTemplate("JSP_Input_Basic.ftl"), root, standaloneRootPath + "\\jsp\\", nameField
                    + "-inputBasic.jsp");
            process(cfg.getTemplate("JSP_View_Tabs.ftl"), root, standaloneRootPath + "\\jsp\\", nameField
                    + "-viewTabs.jsp");
            process(cfg.getTemplate("JSP_View_Basic.ftl"), root, standaloneRootPath + "\\jsp\\", nameField
                    + "-viewBasic.jsp");
        }
    }

    private static void debug(String message) {
        System.out.println(message);
    }

    private static void process(Template template, Map<String, Object> root, String dir, String fileName)
            throws Exception, FileNotFoundException {
        if ((dir + fileName).length() > 300) {
            throw new IllegalArgumentException("Dir path too long.");
        }
        File newsDir = new File(dir);
        if (!newsDir.exists()) {
            newsDir.mkdirs();
        }
        Writer out = new OutputStreamWriter(new FileOutputStream(dir + fileName), "UTF-8");
        template.process(root, out);
    }

    /**
     * 对象属性转换为字段 例如：userName to user_name
     * 
     * @param property
     *            字段名
     * @return
     */
    public static String propertyToField(String property) {
        if (null == property) {
            return "";
        }
        char[] chars = property.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : chars) {
            if (CharUtils.isAsciiAlphaUpper(c)) {
                sb.append("-" + StringUtils.lowerCase(CharUtils.toString(c)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().toUpperCase();
    }
}
