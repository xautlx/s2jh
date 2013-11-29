package lab.s2jh.crawl.filter;

import java.util.regex.Pattern;

import lab.s2jh.crawl.service.HtmlunitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class AbstractParseFilter implements ParseFilter {
    
    public static final Logger LOG = LoggerFactory.getLogger(AbstractParseFilter.class);
    
    protected Pattern urlMatchPattern;

    public void setUrlMatchRegex(String urlMatchRegex) {
        urlMatchPattern = Pattern.compile(urlMatchRegex);
    }

    @Override
    public void doFilter(String url, ParseFilterChain filterChain) {
        if (urlMatchPattern.matcher(url).find()) {
            doFilterInternal(url, filterChain);
        } else {
            filterChain.doFilter(url, filterChain);
        }
    }

    protected HtmlPage fetchHtmlPage(String url) {
        return HtmlunitService.fetchHtmlPage(url);
    }

    public abstract void doFilterInternal(String url, ParseFilterChain filterChain);

}
