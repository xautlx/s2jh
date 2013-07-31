package lab.s2jh.biz.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import lab.s2jh.auth.dao.UserDao;
import lab.s2jh.biz.sys.dao.RegionCodeDao;
import lab.s2jh.biz.xx.dao.XxJcxxDao;
import lab.s2jh.biz.xx.entity.XxJcxx;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("aclService")
public class RegionAclService implements AclService {

    private static final Logger logger = LoggerFactory.getLogger(RegionAclService.class);

    @Autowired
    private RegionCodeDao regionCodeDao;

    @Autowired
    private XxJcxxDao xxJcxxDao;

    @Autowired
    private UserDao userDao;

    public final static String ZY_USER_ACL_CODE = "000000"; //中央用户标识ACL CODE 

    public final static Integer ACL_TYPE_ZY = 80;//中央
    public final static Integer ACL_TYPE_SJ = 70;//省级
    public final static Integer ACL_TYPE_DS = 60;//地市
    public final static Integer ACL_TYPE_QX = 50;//区县
    public final static Integer ACL_TYPE_XX = 40;//学校

    private static Map<Integer, String> aclTypeMap = Maps.newLinkedHashMap();
    static {
        aclTypeMap.put(ACL_TYPE_ZY, "中央");
        aclTypeMap.put(ACL_TYPE_SJ, "省级");
        aclTypeMap.put(ACL_TYPE_DS, "地市");
        aclTypeMap.put(ACL_TYPE_QX, "区县");
        aclTypeMap.put(ACL_TYPE_XX, "学校");
    }

    @Override
    public Integer aclCodeToType(String aclCode) {
        if (StringUtils.isBlank(aclCode)) {
            return null;
        }
        if (aclCode.equals(ZY_USER_ACL_CODE)) {
            return ACL_TYPE_ZY;
        }
        if (aclCode.length() > 6) {
            return ACL_TYPE_XX;
        } else if (aclCode.length() == 6) {
            if (aclCode.endsWith("0000")) {
                return ACL_TYPE_SJ;
            } else if (aclCode.endsWith("00")) {
                return ACL_TYPE_DS;
            } else {
                return ACL_TYPE_QX;
            }
        } else {
            throw new IllegalArgumentException("Undefined ACL CODE: " + aclCode);
        }
    }

    @Override
    public Map<Integer, String> getAclTypeMap() {
        return aclTypeMap;
    }

    @Override
    public Map<String, String> findAclCodesMap() {
        Map<String, String> keyValueMap = Maps.newLinkedHashMap();
        //行政区划数据
        for (Object[] objects : regionCodeDao.findKeyValueInfo()) {
            keyValueMap.put(String.valueOf(objects[0]), String.valueOf(objects[1]));
        }
        //学校数据
        for (Object[] objects : xxJcxxDao.findKeyValueInfo()) {
            keyValueMap.put(String.valueOf(objects[0]), String.valueOf(objects[1]));
        }
        return keyValueMap;
    }

    public String getAclCodePrefix(String aclCode) {
        if (StringUtils.isBlank(aclCode) || aclCode.equals(ZY_USER_ACL_CODE)) {
            return "";
        }
        if (aclCode.endsWith("0000")) {
            return StringUtils.substring(aclCode, 0, 2);
        } else if (aclCode.endsWith("00")) {
            return StringUtils.substring(aclCode, 0, 4);
        }
        return aclCode;
    }

    public Collection<String> getStatAclCodePrefixs(String aclCode) {
        Collection<String> prefixs = Lists.newArrayList();
        if (StringUtils.isBlank(aclCode) || aclCode.equals(ZY_USER_ACL_CODE)) {
            return prefixs;
        }
        if (aclCode.length() != 6) {
            prefixs.add(aclCode);
        } else {
            prefixs.add(getAclCodePrefix(aclCode));
            List<XxJcxx> xxJcxxs = xxJcxxDao.findBySszgdwm(aclCode);
            for (XxJcxx xxJcxx : xxJcxxs) {
                prefixs.add(xxJcxx.getXxdm());
            }
        }
        return prefixs;
    }

    @Override
    public void validateAuthUserAclCodePermission(String... dataAclCodes) {
        for (String dataAclCode : dataAclCodes) {
            if (StringUtils.isBlank(dataAclCode)) {
                return;
            }
        }
        Collection<String> userAclCodePrefixs = AuthContextHolder.getAuthUserDetails().getAclCodePrefixs();
        if (CollectionUtils.isEmpty(userAclCodePrefixs)) {
            return;
        }

        for (String userAclCodePrefix : userAclCodePrefixs) {
            if (StringUtils.isBlank(userAclCodePrefix)) {
                return;
            }
        }

        for (String dataAclCode : dataAclCodes) {
            for (String userAclCodePrefix : userAclCodePrefixs) {
                if (dataAclCode.startsWith(userAclCodePrefix)) {
                    return;
                }
            }

        }
        if (logger.isWarnEnabled()) {
            logger.warn("No acl match for user acl code prefix: {}, data acl code: {}",
                    StringUtils.join(userAclCodePrefixs, ","), StringUtils.join(dataAclCodes, ","));
        }
        throw new AccessDeniedException("数据访问权限不足");
    }

    @Override
    public Integer getInitAclType() {
        return userDao.findByInitSetupUser(true).getAclType();
    }

    @Override
    public String getInitAclCode() {
        return userDao.findByInitSetupUser(true).getAclCode();
    }
}
