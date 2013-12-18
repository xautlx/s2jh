package lab.s2jh.crawl.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 解析过滤链
 */
public class ParseFilterChain implements ParseFilter {

    public final static String IMG_ROOT_DIR = "IMG_ROOT_DIR";

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
        return (String) params.get(IMG_ROOT_DIR);
    }

    public void addParam(String key, String value) {
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
