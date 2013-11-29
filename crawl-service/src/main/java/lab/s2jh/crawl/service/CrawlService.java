package lab.s2jh.crawl.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import lab.s2jh.crawl.filter.ParseFilter;
import lab.s2jh.crawl.filter.ParseFilterChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import com.google.common.collect.Sets;

public class CrawlService {

    private final Logger logger = LoggerFactory.getLogger(CrawlService.class);

    private ParseFilterChain parseFilterChain;

    public void setParseFilters(List<ParseFilter> parseFilters) {
        parseFilterChain = new ParseFilterChain();
        logger.info("ParseFilterChain List: ");
        for (ParseFilter parseFilter : parseFilters) {
            logger.info(" - {}", parseFilter.getClass());
            parseFilterChain.addFilter(parseFilter);
        }
    }

    /**
     * 同步执行URL集合数据抓取
     * @param urls
     */
    public Set<String> startCrawl(String... urls) {
        Set<String> successUrls = Sets.newHashSet();
        for (String url : urls) {
            logger.debug("Start crawl url: {}", url);
            try {
                parseFilterChain.doFilter(url, parseFilterChain);
                successUrls.add(url);
            } catch (Exception e) {
                //Just logger error to continue next url crawl
                logger.error("htmlunit.page.error", e);
            }
        }
        return successUrls;
    }

    /**
     * 异步执行URL集合数据抓取
     * @param urls
     */
    @Async
    public Future<Set<String>> startCrawlAsync(String... urls) {
        startCrawl(urls);
        return new AsyncResult<Set<String>>(startCrawl(urls));
    }
}
