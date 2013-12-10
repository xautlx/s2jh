package lab.s2jh.crawl.service;

import java.util.List;

import lab.s2jh.crawl.filter.ParseFilter;
import lab.s2jh.crawl.filter.ParseFilterChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

    public void setAsyncCrawlService(AsyncCrawlService asyncCrawlService) {
        this.asyncCrawlService = asyncCrawlService;
    }

    public void setParseFilters(List<ParseFilter> parseFilters) {
        this.parseFilters = parseFilters;
    }

    public void setCrawlTaskExecutor(ThreadPoolTaskExecutor crawlTaskExecutor) {
        this.crawlTaskExecutor = crawlTaskExecutor;
    }

    /**
     * 以同步等待方式执行批量URL抓取，方法会一直等待所有爬虫处理完成才返回
     * @param urls 待爬取的种子URL列表
     */
    public void startSyncCrawl(String... urls) {
        startSyncCrawlWithImgRootDir(null, urls);
    }

    /**
     * 以异步处理方式执行批量URL抓取，方法会在提交安排后台爬虫作业后立即返回（即不会等待爬虫执行完毕）
     * @param urls 待爬取的种子URL列表
     */
    public void startSyncCrawlWithImgRootDir(String imgRootDir, String... urls) {
        logger.debug("Main sync thread: {}", Thread.currentThread().getId());
        scheduleAsyncCrawl(imgRootDir, urls);
        //定时检测Future返回状态，直到所有线程都返回后才最终返回方法调用
        try {
            boolean tobeWait = true;
            while (tobeWait) {
                Thread.sleep(1000);
                logger.info("Main sync thread is sleep for waiting..., crawlTaskExecutor stat info: {}/{}/{}",
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
     * 以异步处理方式执行批量URL抓取，方法会在提交安排后台爬虫作业后立即返回（即不会等待爬虫执行完毕）
     * @param urls 待爬取的种子URL列表
     */
    public void startAsyncCrawl(String... urls) {
        startAsyncCrawlWithImgRootDir(null, urls);
    }

    /**
     * 以异步处理方式执行批量URL抓取，方法会在提交安排后台爬虫作业后立即返回（即不会等待爬虫执行完毕）
     * @param urls 待爬取的种子URL列表
     */
    public void startAsyncCrawlWithImgRootDir(String imgRootDir, String... urls) {
        scheduleAsyncCrawl(imgRootDir, urls);
    }

    /**
     * 内部以线程池方式安排页面爬取作业
     * @param urls
     */
    private void scheduleAsyncCrawl(String imgRootDir, String... urls) {
        logger.debug("Prepare to add {} urls to crawl queue.", urls.length);
        for (String url : urls) {
            try {
                logger.info("Scheduled crawl url: {}, crawlTaskExecutor stat info: {}/{}/{}", url,
                        crawlTaskExecutor.getActiveCount(), crawlTaskExecutor.getPoolSize(),
                        crawlTaskExecutor.getMaxPoolSize());
                ParseFilterChain parseFilterChain = new ParseFilterChain(parseFilters, true);
                if (imgRootDir != null) {
                    parseFilterChain.addParam(ParseFilterChain.IMG_ROOT_DIR, imgRootDir);
                }
                asyncCrawlService.startAsyncCrawl(url, parseFilterChain);
            } catch (Exception e) {
                //Just logger error to continue next url crawl
                logger.error("htmlunit.page.error", e);
            }
        }
    }
}
