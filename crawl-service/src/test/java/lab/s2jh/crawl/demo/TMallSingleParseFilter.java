package lab.s2jh.crawl.demo;

import lab.s2jh.crawl.filter.AbstractParseFilter;
import lab.s2jh.crawl.filter.ParseFilterChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 天猫单品页面抓取解析
 */
public class TMallSingleParseFilter extends AbstractParseFilter {

    private final Logger logger = LoggerFactory.getLogger(TMallSingleParseFilter.class);

    @Override
    public void doFilterInternal(String url, ParseFilterChain filterChain) {
        HtmlPage htmlPage = fetchHtmlPage(url);

        HtmlElement titleNode = htmlPage.getFirstByXPath("//DIV[@id='detail']//DIV[@class='tb-detail-hd']/H3");
        String title = titleNode.asText().trim();
        logger.debug("Product title: {}", title);
        
        //TODO 其他属性解析处理逻辑，然后根据需要持久化数据到文件或数据库等
    }

}
