package lab.s2jh.crawl.filter;

import java.util.regex.Pattern;

import lab.s2jh.crawl.service.CrawlService;
import lab.s2jh.crawl.service.HtmlunitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * URL解析过滤抽象基类
 */
public abstract class AbstractParseFilter implements ParseFilter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractParseFilter.class);

    protected Pattern urlMatchPattern;

    protected CrawlService crawlService;

    /**
     * 注入爬虫服务，对于查询或分类页面解析出来一批链接后需要调用爬虫服务 {@link CrawlService#scheduleCrawl(String...)}
     * 安排新的爬虫作业到线程池
     * @param crawlService
     */
    public void setCrawlService(CrawlService crawlService) {
        this.crawlService = crawlService;
    }

    /**
     * 外部XML Bean定义配置设定当前过滤器匹配的URL正则表达式
     * @param urlMatchRegex
     */
    public void setUrlMatchRegex(String urlMatchRegex) {
        HtmlunitService.addUrlRule(urlMatchRegex);
        urlMatchPattern = Pattern.compile(urlMatchRegex);
    }

    @Override
    public void doFilter(String url, ParseFilterChain filterChain) {
        //基于UIRL和配置的正则表达式进行比对判断是否执行当前解析逻辑
        if (urlMatchPattern.matcher(url).find()) {
            logger.debug("Thread: {}, Invoking Filter: {}", Thread.currentThread().getId(), this.getClass());
            try {
                doFilterInternal(url, filterChain);
            } catch (Exception e) {
                logger.error("error.parse.url: " + url, e);
            }
        } else {
            filterChain.doFilter(url, filterChain);
        }
    }

    /**
     * 帮助类方法：基于当前HtmlPage对象和xpath返回对应的文本字符串
     * @param htmlPage
     * @param xpath
     * @return
     */
    protected String getSingleTextByXPath(HtmlPage htmlPage, String xpath) {
        HtmlElement el = htmlPage.getFirstByXPath(xpath);
        if (el == null) {
            return "";
        }
        String text = el.asText();
        if (text == null) {
            return "";
        }
        return text.trim();
    }

    /**
     * 帮助类方法：基于url调用Htmlunit接口获取页面HtmlPage内容
     * @param url
     * @return
     */
    protected HtmlPage fetchHtmlPage(String url) {
        return HtmlunitService.fetchHtmlPage(url);
    }

    /**
     * 解析过滤器子类实现逻辑抽象定义
     * @param url
     * @param filterChain
     */
    public abstract void doFilterInternal(String url, ParseFilterChain filterChain) throws Exception;
}
