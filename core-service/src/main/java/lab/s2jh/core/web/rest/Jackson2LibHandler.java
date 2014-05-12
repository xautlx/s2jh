package lab.s2jh.core.web.rest;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import lab.s2jh.core.web.json.HibernateAwareObjectMapper;

import org.apache.struts2.StrutsConstants;
import org.apache.struts2.rest.handler.ContentTypeHandler;

import com.fasterxml.jackson.databind.ObjectReader;
import com.opensymphony.xwork2.inject.Inject;

public class Jackson2LibHandler implements ContentTypeHandler {

    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    private String defaultEncoding = "UTF-8";

    public void toObject(Reader in, Object target) throws IOException {
        ObjectReader or = HibernateAwareObjectMapper.getInstance().readerForUpdating(target);
        or.readValue(in); //, new TypeReference<clazz>);
    }

    public String fromObject(Object obj, String resultCode, Writer stream) throws IOException {
        HibernateAwareObjectMapper.getInstance().writeValue(stream, obj);
        return null;
    }

    public String getContentType() {
        return DEFAULT_CONTENT_TYPE + ";charset=" + this.defaultEncoding;
    }

    public String getExtension() {
        return "json";
    }

    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String val) {
        this.defaultEncoding = val;
    }
}