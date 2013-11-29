package lab.s2jh.crawl.filter;


public interface ParseFilter {

    void doFilter(String url, ParseFilterChain filterChain);
}
