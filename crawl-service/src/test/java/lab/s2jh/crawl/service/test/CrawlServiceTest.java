package lab.s2jh.crawl.service.test;

import lab.s2jh.crawl.service.CrawlService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath*:/spring*.xml" })
public class CrawlServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private CrawlService crawlService;

    @Test
    public void testCrawlTMall() {
        String url = "http://disney.tmall.com/category.htm?spm=a1z10.5.w4011-2440936552.401.4aSw9J&mid=w-2440936552-0&pageNo=1#anchor";

        logger.debug("Start sync crawl...");
        crawlService.injectUrls(url).startCrawlSync();;
        logger.debug("End sync crawl.");

        logger.debug("Start async crawl...");
        crawlService.injectUrls(url).startCrawlAsync();;
        logger.debug("Return async crawl.");
    }

}
