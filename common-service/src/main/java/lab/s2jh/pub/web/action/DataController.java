package lab.s2jh.pub.web.action;

import javax.servlet.ServletContext;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.web.SimpleController;
import lab.s2jh.core.web.listener.ApplicationContextPostListener;
import lab.s2jh.sys.service.DataDictService;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 公共数据服务处理
 */
public class DataController extends SimpleController {

    @Autowired
    private DataDictService dataDictService;

    /**
     * @see ApplicationContextPostListener
     * @return
     */
    @MetaData("枚举数据集合")
    public HttpHeaders enums() {
        ServletContext sc = ServletActionContext.getServletContext();
        setModel(sc.getAttribute("enums"));
        return buildDefaultHttpHeaders();
    }

    /**
     * @see ApplicationContextPostListener
     * @return
     */
    @MetaData("数据字典数据集合")
    public HttpHeaders dictDatas() {
        setModel(dataDictService.findAllCached());
        return buildDefaultHttpHeaders();
    }
}
