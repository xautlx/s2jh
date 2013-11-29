package lab.s2jh.crawl.service;

import java.util.Set;

import lab.s2jh.crawl.htmlunit.ExtHtmlunitCache;
import lab.s2jh.crawl.htmlunit.RegexHttpWebConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlunitService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlService.class);

    private static ThreadLocal<WebClient> threadWebClient = new ThreadLocal<WebClient>();

    private static Set<String> fetchUrlRules;

    public void setFetchUrlRules(Set<String> fetchUrlRules) {
        HtmlunitService.fetchUrlRules = fetchUrlRules;
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
        }
        return webClient;
    }

    public static HtmlPage fetchHtmlPage(String url) {
        try {
            HtmlPage page = buildWebClient().getPage(url);
            return page;
        } catch (Exception e) {
            throw new RuntimeException("htmlunit.page.error", e);
        }
    }
}
