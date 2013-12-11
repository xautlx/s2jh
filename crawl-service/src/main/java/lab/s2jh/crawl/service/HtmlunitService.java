package lab.s2jh.crawl.service;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import lab.s2jh.crawl.CrawlLogger;
import lab.s2jh.crawl.htmlunit.ExtHtmlunitCache;
import lab.s2jh.crawl.htmlunit.RegexHttpWebConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Sets;

/**
 * 基于Htmlunit的数据抓取服务接口
 */
public class HtmlunitService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlService.class);

    private static ThreadLocal<WebClient> threadWebClient = new ThreadLocal<WebClient>();

    //全局抓取URL规则：'+'前缀或无前缀=include url; '-'前缀=exclude url
    private static Set<String> fetchUrlRules = Sets.newHashSet();

    private static int totalFetchedCount = 0;
    private static int totalFetchTimes = 0;

    //全局的cookie值
    public static String COOKIE;

    public void setFetchUrlRules(Set<String> fetchUrlRules) {
        HtmlunitService.fetchUrlRules.addAll(fetchUrlRules);
    }

    public static void addUrlRule(String rule) {
        HtmlunitService.fetchUrlRules.add(rule);
    }

    public static WebClient buildWebClient() {
        WebClient webClient = threadWebClient.get();
        if (webClient == null) {
            logger.info("Initing web client for thread: {}", Thread.currentThread().getId());
            webClient = new WebClient(BrowserVersion.FIREFOX_17);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setAppletEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            // AJAX support
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            // Use extension version htmlunit cache process
            webClient.setCache(new ExtHtmlunitCache());
            // Enhanced WebConnection based on urlfilter
            webClient.setWebConnection(new RegexHttpWebConnection(webClient, fetchUrlRules));
            webClient.waitForBackgroundJavaScript(600 * 1000);
            threadWebClient.set(webClient);
        }
        return webClient;
    }

    /**
     * 抓取URL指定页面
     * @param url
     * @return
     */
    public static HtmlPage fetchHtmlPage(String url) {
        return fetchHtmlPage(url, null);
    }

    /**
     * 基于指定URL及特定请求头信息抓取页面，如设置了cooki登录信息、reference、host等特定服务器需要的头信息
     * @param url
     * @param additionalHeaders
     * @return
     */
    public static HtmlPage fetchHtmlPage(String url, Map<String, String> additionalHeaders) {
        try {
            totalFetchedCount++;

            long start = new Date().getTime();
            CrawlLogger.LOG_RUN_INFO.info("No.: {}, Fetching: {}", totalFetchedCount, url);
            WebRequest webRequest = new WebRequest(new URL(url));
            if (additionalHeaders != null) {
                webRequest.setAdditionalHeaders(additionalHeaders);
            }
            HtmlPage page = buildWebClient().getPage(webRequest);
            long end = new Date().getTime();
            totalFetchTimes += (end - start);

            printLog();
            return page;
        } catch (Exception e) {
            throw new RuntimeException("htmlunit.page.error", e);
        }
    }

    /**
     * 打印统计日志信息
     */
    private static void printLog() {
        long dur = totalFetchTimes / 1000;
        CrawlLogger.LOG_RUN_INFO.info("Total fetched pages: {}, use time: {} seconds, avg speed: {} pages/second ",
                totalFetchedCount, dur, Float.valueOf(totalFetchedCount) / dur);
    }
}
