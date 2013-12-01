package lab.s2jh.crawl.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析过滤链
 */
public class ParseFilterChain implements ParseFilter {

    private List<ParseFilter> filters;

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
}
