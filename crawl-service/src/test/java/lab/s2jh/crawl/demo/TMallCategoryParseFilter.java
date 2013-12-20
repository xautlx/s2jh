package lab.s2jh.crawl.demo;

import java.util.List;
import java.util.Set;

import lab.s2jh.crawl.filter.AbstractParseFilter;
import lab.s2jh.crawl.filter.ParseFilterChain;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Sets;

/**
 * 天猫商铺商品分类页面抓取，采集分类页面的所有商品链接然后添加到爬虫作业触发商品单页解析过滤器处理
 * 用法示例：可以给定一个特定的分类页面URL，如果需要分类下面所有分页的商品数据则只需把分类URL中的pageNo=参数值修改为*即可
 */
public class TMallCategoryParseFilter extends AbstractParseFilter {

    private final Logger logger = LoggerFactory.getLogger(TMallCategoryParseFilter.class);

    @Override
    public void doFilterInternal(String url, ParseFilterChain filterChain) {
        //http://disney.tmall.com/category.htm?spm=a1z10.5.w4011-2440936552.399.2fa9My&mid=w-2440936552-0&pageNo=2#anchor
        //http://disney.tmall.com/category.htm?spm=a1z10.5.w4011-2440936552.401.4aSw9J&mid=w-2440936552-0&pageNo=1#anchor
        String pageNo = StringUtils.substringBetween(url, "pageNo=", "#");
        int pager = 1;
        boolean continueLoop = true;
        if (!pageNo.equals("*")) {
            pager = Integer.valueOf(pageNo);
            continueLoop = false;
        }

        String pageNoBefore = StringUtils.substringBefore(url, "pageNo=");
        String pageNoAfter = StringUtils.substringAfter(StringUtils.substringAfter(url, "pageNo="), "&");

        do {
            String catPageUrl = pageNoBefore + "pageNo=" + +(pager++) + pageNoAfter;
            logger.info("Process category page: " + catPageUrl);
            HtmlPage catPage = fetchHtmlPage(catPageUrl);

            @SuppressWarnings("rawtypes")
            List links = catPage.getByXPath("//DIV[@class='J_TItems']//DD[@class='detail']//A");
            if (links == null || links.size() == 0) {
                System.out.println("No more valid links...");
                continueLoop = false;
            } else {
                Set<String> pageUrls = Sets.newHashSet();
                for (Object element : links) {
                    HtmlAnchor anchor = (HtmlAnchor) element;
                    String href = anchor.getAttribute("href");
                    //http://detail.tmall.com/item.htm?spm=a1z10.3.w4011-2877147662.72.ZAiF31&id=17347946555&rn=93461b8808ac12eb3c7007291f23c337
                    if (href.startsWith("http://detail.tmall.com/item")) {
                        logger.info("Found new valid url: {}", href);
                        pageUrls.add(href);
                    }
                }
                crawlService.injectUrls(pageUrls.toArray(new String[] {}));
                
            }
        } while (continueLoop);
    }

}
