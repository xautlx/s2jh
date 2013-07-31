package lab.s2jh.core.security;

import java.util.Collection;
import java.util.Map;

public interface AclService {
    /**
     * 将ACL Code转换为对应的ACL Type
     * 
     * @param aclCode
     * @return
     */
    Integer aclCodeToType(String aclCode);

    /**
     * 获取所有ACL Type类型定义Map
     * 
     * @return
     */
    Map<Integer, String> getAclTypeMap();

    /**
     * 获取所有ACL Code Map结构数据
     * 
     * @return
     */
    Map<String, String> findAclCodesMap();

    /**
     * 获取ACL Code前缀
     * 
     * @return
     */
    String getAclCodePrefix(String aclCode);

    /**
     * 基于一个单一ACL Code返回其可以访问的ACL Code前缀集合
     * 如用户ACL Code为120000，根据业务规则其访问前缀集合可转化12, AA12,BB12等
     * @return
     */
    Collection<String> getStatAclCodePrefixs(String aclCode);

    /**
     * 假设以仿照国家行政区划码方式定义ACL控制代码，如112100，获取其连续0后缀之前部分内容
     * 用于与数据ACL代码进行比对以判定用户对数据的访问权限
     * 
     * @exception 如果判断无权访问,则抛出运行异常
     */
    void validateAuthUserAclCodePermission(String... dataAclCode);

    /**
     * 获取初始化用户对应ACL Type，用于标识当前系统的ACL Type
     * @return
     */
    Integer getInitAclType();

    /**
     * 获取初始化用户对应ACL Code，用于标识当前系统的ACL Code
     * @return
     */
    String getInitAclCode();
}
