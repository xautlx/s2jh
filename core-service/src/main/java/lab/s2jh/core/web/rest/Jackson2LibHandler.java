package lab.s2jh.core.web.rest;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.exception.ExceptionLogger;
import lab.s2jh.core.web.json.HibernateAwareObjectMapper;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.rest.handler.ContentTypeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.inject.Inject;

public class Jackson2LibHandler implements ContentTypeHandler {

    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    private String defaultEncoding = "UTF-8";

    public static final String DEFAULT_JSON_FILTER_NAME = "s2jhFilter";

    private static final SimpleFilterProvider serializeAllFilterProvider = new SimpleFilterProvider().addFilter(
            DEFAULT_JSON_FILTER_NAME, SimpleBeanPropertyFilter.serializeAllExcept());

    private static final ThreadLocal<SimpleBeanPropertyFilter> simpleBeanPropertyFilterContainer = new ThreadLocal<SimpleBeanPropertyFilter>();

    public void toObject(Reader in, Object target) throws IOException {
        ObjectReader or = HibernateAwareObjectMapper.getInstance().readerForUpdating(target);
        or.readValue(in); //, new TypeReference<clazz>);
    }

    public String fromObject(Object obj, String resultCode, Writer stream) throws IOException {
        ObjectMapper mapper = HibernateAwareObjectMapper.getInstance();
        if (obj instanceof Throwable) {
            HttpServletRequest request = ServletActionContext.getRequest();
            String msg = ExceptionLogger.logForHttpRequest((Throwable) obj, request);
            Map<String, String> errors = Maps.newHashMap();
            errors.put("type", "error");
            errors.put("message", msg);
            mapper.writeValue(stream, errors);
        } else {
            SimpleBeanPropertyFilter simpleBeanPropertyFilter = simpleBeanPropertyFilterContainer.get();
            FilterProvider filters = null;
            if (simpleBeanPropertyFilter != null) {
                filters = new SimpleFilterProvider().addFilter(DEFAULT_JSON_FILTER_NAME, simpleBeanPropertyFilter);
            } else {
                filters = serializeAllFilterProvider;
            }
            mapper.writer(filters).writeValue(stream, obj);
        }
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

    public static void setJsonPropertyFilter(SimpleBeanPropertyFilter simpleBeanPropertyFilter) {
        simpleBeanPropertyFilterContainer.set(simpleBeanPropertyFilter);
    }
}