package lab.s2jh.crawl.filter;

import lab.s2jh.crawl.service.HtmlunitService;

/**
 * URL解析过滤接口
 * 基本流程：
 * 基于URL和Pattern匹配则执行当前过滤器，根据URL调用{@link HtmlunitService#fetchHtmlPage(String)}
 * 进行页面爬取，然后根据返回的HtmlPage对象进行数据解析
 */
public interface ParseFilter {

    void doFilter(String url, ParseFilterChain filterChain);
}
