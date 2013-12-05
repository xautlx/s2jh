package lab.s2jh.crawl.service;

import java.util.concurrent.Future;

import lab.s2jh.crawl.filter.ParseFilterChain;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

/**
 * 爬虫异步服务。一般不单独使用，根据Spring Async原理不能同Service调用Async接口，因此单独分离这样一个Service接口方法
 * @see CrawlService#scheduleCrawl(String...)
 */
@Service
public class AsyncCrawlService {

    /**
     * 异步执行URL集合数据抓取
     * 
     */
    @Async("crawlTaskExecutor")
    public Future<String> startAsyncCrawl(String url, ParseFilterChain parseFilterChain) {
        parseFilterChain.doFilter(url, parseFilterChain);
        return new AsyncResult<String>(url);
    }
}
