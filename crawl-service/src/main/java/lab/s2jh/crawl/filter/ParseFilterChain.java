package lab.s2jh.crawl.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.collect.Maps;

/**
 * 解析过滤链
 */
public class ParseFilterChain implements ParseFilter {

    public final static String KEY_IMG_ROOT_DIR = "KEY_IMG_ROOT_DIR";
    
    public final static String KEY_LOGIN_COOKIES = "KEY_LOGIN_COOKIES";
    
    private List<ParseFilter> filters;

    private Map<String, Object> params;

    private int i = 0;

    public ParseFilterChain() {
        filters = new ArrayList<ParseFilter>();
    }

    public ParseFilterChain(List<ParseFilter> filters, boolean async) {
        this.filters = filters;
    }

    public ParseFilterChain addFilter(ParseFilter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public void doFilter(String url, ParseFilterChain filterChain) {
        if (i == filters.size()) {
            return;
        }
        filters.get(i++).doFilter(url, filterChain);
    }

    /**
     * 传递图片存储根目录
     * @return
     */
    public String getImgRootDir() {
        return (String) params.get(KEY_IMG_ROOT_DIR);
    }
    
    public Set<Cookie> getLoginCookies() {
        return (Set<Cookie>) params.get(KEY_LOGIN_COOKIES);
    }

    public void addParam(String key, Object value) {
        if (params == null) {
            params = Maps.newHashMap();
        }
        params.put(key, value);
    }

    @Override
    public boolean isAcceptUrl(String url) {
        return false;
    }
}
