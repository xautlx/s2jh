package lab.s2jh.profile.web.action;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.profile.entity.ProfileParamVal;
import lab.s2jh.profile.service.ProfileParamValService;
import lab.s2jh.sys.service.DataDictService;
import lab.s2jh.web.action.BaseController;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@MetaData("个性化配置参数数据管理")
public class ProfileParamValController extends BaseController<ProfileParamVal, String> {

    private final Logger logger = LoggerFactory.getLogger(ProfileParamValController.class);

    @Autowired
    private ProfileParamValService profileParamValService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private DataSource dataSource;

    @Override
    protected BaseService<ProfileParamVal, String> getEntityService() {
        return profileParamValService;
    }

    @Override
    protected void checkEntityAclPermission(ProfileParamVal entity) {
        // TODO Add acl check code logic
    }

    @MetaData("[TODO方法作用]")
    public HttpHeaders todo() {
        //TODO
        setModel(OperationResult.buildSuccessResult("TODO操作完成"));
        return buildDefaultHttpHeaders();
    }

    public void prepareEdit() {

    }

    @Override
    @MetaData("保存")
    public HttpHeaders doSave() {
        return super.doSave();
    }

    @Override
    @MetaData("删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    /**
    * 查询数据字典类别对应数据集合Map
    * @param category 数据字典类别代码
    * @return
    */
    public Map<String, String> getDataDictKeyValueMap(String category) {
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        try {
            dataMap = dataDictService.findMapDataByPrimaryKey(category);
        } catch (Exception e) {
            logger.error("DataDict parse error: " + category, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }

    /**
    * 根据SQL查询对应数据集合Map
    * @param sql 返回Key-Value形式的SQL语句
    * @return
    */
    public Map<String, String> getSQLKeyValueMap(String sql) {
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        try {
            if (StringUtils.isNotBlank(sql)) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
                while (row.next()) {
                    dataMap.put(row.getString(1), row.getString(2));
                }
            }
        } catch (Exception e) {
            logger.error("SQL parse error: " + sql, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }

    /**
    * 根据Enum Class字符串获取对应数据集合Map
    * @param enumClass Enum Class对应的完整类路径
    * @return
    */
    @SuppressWarnings("rawtypes")
    public Map<String, String> getEnumKeyValueMap(String enumClass) {
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        try {
            if (StringUtils.isNotBlank(enumClass)) {
                Class clazz = Class.forName(enumClass);
                for (Field enumfield : clazz.getFields()) {
                    MetaData entityComment = enumfield.getAnnotation(MetaData.class);
                    String value = enumfield.getName();
                    if (entityComment != null) {
                        value = entityComment.value();
                    }
                    dataMap.put(enumfield.getName(), value);
                }
            }
        } catch (Exception e) {
            //由于此异常出现在JSP页面解析过程，无法转向到全局的errors错误显示页面，因此采用logger记录error信息
            logger.error("Enum parse error: " + enumClass, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }

    /**
    * 将OGNL字符串转化为对应数据集合Map
    * @param ognl OGNL语法字符串，如：#{'A':'ClassA','B':'ClassB'}
    * @return
    */
    @SuppressWarnings("rawtypes")
    public Map getOGNLKeyValueMap(String ognl) throws OgnlException {
        Map dataMap = new LinkedHashMap();
        try {
            if (StringUtils.isNotBlank(ognl)) {
                dataMap = (Map) Ognl.getValue(ognl, null);
            }
        } catch (Exception e) {
            //由于此异常出现在JSP页面解析过程，无法转向到全局的errors错误显示页面，因此采用logger记录error信息
            logger.error("Ognl parse error: " + ognl, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }
}