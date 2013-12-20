package lab.s2jh.crawl.service;

import java.util.List;
import java.util.Set;

import lab.s2jh.crawl.filter.ParseFilter;
import lab.s2jh.crawl.filter.ParseFilterChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * 爬虫主服务
 * 说明：此套爬虫设计主要用于一些定向网站数据抓取解析，如电商商品、交友信息等，抓取和解析特定页面的属性数据。
 * 如果是全网爬取之类的页面采集需求建议使用更专业的爬虫工具，如Apache Nutch
 */
public class CrawlService {

    private final Logger logger = LoggerFactory.getLogger(CrawlService.class);

    private ThreadPoolTaskExecutor crawlTaskExecutor;

    private List<ParseFilter> parseFilters;

    private AsyncCrawlService asyncCrawlService;

    private String imageRootDir;

    private boolean stopUrlInject;

    public void setAsyncCrawlService(AsyncCrawlService asyncCrawlService) {
        this.asyncCrawlService = asyncCrawlService;
    }

    public void setParseFilters(List<ParseFilter> parseFilters) {
        this.parseFilters = parseFilters;
    }

    public void setCrawlTaskExecutor(ThreadPoolTaskExecutor crawlTaskExecutor) {
        this.crawlTaskExecutor = crawlTaskExecutor;
    }

    public void startCrawlAsync() {
        stopUrlInject = false;
    }

    public void startCrawlSync() {
        stopUrlInject = false;
        //定时检测Future返回状态，直到所有线程都返回后才最终返回方法调用
        try {
            boolean tobeWait = true;
            while (tobeWait) {
                Thread.sleep(1000);
                logger.info("Main sync thread [{}] is waiting to complete...", Thread.currentThread().getId());
                logger.info("CrawlTaskExecutor ActiveCount/PoolSize/MaxPoolSize: {}/{}/{}",
                        crawlTaskExecutor.getActiveCount(), crawlTaskExecutor.getPoolSize(),
                        crawlTaskExecutor.getMaxPoolSize());
                if (crawlTaskExecutor.getPoolSize() > 0 && crawlTaskExecutor.getActiveCount() == 0) {
                    tobeWait = false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 如果不涉及登录调用此接口注入待爬取URL集合
     * @param urls
     * @return
     */
    public CrawlService injectUrls(String... urls) {
        return injectUrls(null, urls);
    }

    /**
     * 如果涉及登录调用此接口注入外部登录获取的cookies数据和待爬取URL集合
     * @param cookies 
     * @param urls
     * @return
     */
    public CrawlService injectUrls(Set<Cookie> cookies, String... urls) {
        if (stopUrlInject) {
            logger.debug("URL inject rejected as user request.");
        }
        logger.debug("Prepare to add {} urls to crawl queue.", urls.length);
        for (String url : urls) {
            try {
                url = url.trim();
                logger.info("Injected crawl url: {}", url);
                logger.info("CrawlTaskExecutor ActiveCount/PoolSize/MaxPoolSize: {}/{}/{}",
                        crawlTaskExecutor.getActiveCount(), crawlTaskExecutor.getPoolSize(),
                        crawlTaskExecutor.getMaxPoolSize());
                ParseFilterChain parseFilterChain = new ParseFilterChain(parseFilters, true);
                if (imageRootDir != null) {
                    parseFilterChain.addParam(ParseFilterChain.KEY_IMG_ROOT_DIR, imageRootDir);
                }
                if (cookies != null) {
                    parseFilterChain.addParam(ParseFilterChain.KEY_LOGIN_COOKIES, cookies);
                }
                asyncCrawlService.startAsyncCrawl(url, parseFilterChain);
            } catch (Exception e) {
                //Just logger error to continue next url crawl
                logger.error("htmlunit.page.error", e);
            }
        }
        return this;
    }

    public void stopCrawl() {
        logger.info("Stop crawl signal received.");
        stopUrlInject = true;
    }

    public void setImageRootDir(String imageRootDir) {
        this.imageRootDir = imageRootDir;
    }

    public List<ParseFilter> getParseFilters() {
        return parseFilters;
    }
}
