package lab.s2jh.crawl.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import lab.s2jh.crawl.filter.ParseFilter;
import lab.s2jh.crawl.filter.ParseFilterChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.google.common.collect.Sets;

/**
 * 爬虫主服务
 * 说明：此套爬虫设计主要用于一些定向网站数据抓取解析，如电商商品、交友信息等，抓取和解析特定页面的属性数据。
 * 如果是全网爬取之类的页面采集需求建议使用更专业的爬虫工具，如Apache Nutch
 */
public class CrawlService {

    private final Logger logger = LoggerFactory.getLogger(CrawlService.class);

    private ThreadPoolTaskExecutor taskExecutor;

    private List<ParseFilter> parseFilters;

    private AsyncCrawlService asyncCrawlService;

    private Date startTime;

    public void setAsyncCrawlService(AsyncCrawlService asyncCrawlService) {
        this.asyncCrawlService = asyncCrawlService;
    }

    public void setParseFilters(List<ParseFilter> parseFilters) {
        this.parseFilters = parseFilters;
    }

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * 以同步等待方式执行批量URL抓取，方法会一直等待所有爬虫处理完成才返回
     * @param urls 待爬取的种子URL列表
     * @return 爬取处理'成功'的URL集合
     */
    public Set<String> startSyncCrawl(String... urls) {
        if (startTime == null) {
            startTime = new Date();
        }
        Set<String> successUrls = Sets.newHashSet();
        Set<Future<String>> futures = scheduleAsyncCrawl(urls);
        //定时检测Future返回状态，直到所有线程都返回后才最终返回方法调用
        try {
            boolean tobeWait = false;
            do {
                tobeWait = false;
                for (Future<String> future : futures) {
                    if (future.get() == null) {
                        tobeWait = true;
                        Thread.sleep(1000);
                        break;
                    }
                    successUrls.add(future.get());
                }
            } while (tobeWait);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        printLog();
        return successUrls;
    }

    /**
     * 以异步处理方式执行批量URL抓取，方法会在提交安排后台爬虫作业后立即返回（即不会等待爬虫执行完毕）
     * @param urls 待爬取的种子URL列表
     */
    @Async
    public void startAsyncCrawl(String... urls) {
        startSyncCrawl(urls);
    }

    /**
     * 内部以线程池方式安排页面爬取作业
     * @param urls
     */
    private Set<Future<String>> scheduleAsyncCrawl(String... urls) {
        logger.debug("Prepare to add {} urls to crawl queue.", urls.length);
        Set<Future<String>> successUrls = Sets.newHashSet();
        for (String url : urls) {
            try {
                while (taskExecutor.getActiveCount() >= taskExecutor.getMaxPoolSize()) {
                    try {
                        logger.info("No available thread: {}/{}/{}, sleep a moment and try to schedule again.",
                                taskExecutor.getActiveCount(), taskExecutor.getPoolSize(),
                                taskExecutor.getMaxPoolSize());
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                logger.info("Scheduled crawl url: {}", url);
                ParseFilterChain parseFilterChain = new ParseFilterChain(parseFilters, true);
                successUrls.add(asyncCrawlService.startAsyncCrawl(url, parseFilterChain));
            } catch (Exception e) {
                //Just logger error to continue next url crawl
                logger.error("htmlunit.page.error", e);
            }
        }
        return successUrls;
    }

    /**
     * 定时任务接口用于定时打印爬虫统计信息
     */
    @Scheduled(fixedRate = 3000)
    public void printLog() {
        if (startTime != null) {
            HtmlunitService.printLog(startTime);
        }
    }
}
