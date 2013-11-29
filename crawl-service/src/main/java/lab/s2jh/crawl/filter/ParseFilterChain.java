package lab.s2jh.crawl.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析过滤链
 */
public class ParseFilterChain implements ParseFilter {

    List<ParseFilter> filters = new ArrayList<ParseFilter>();

    private int i = 0;

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

    public void reset() {
        i = 0;
    }
}
