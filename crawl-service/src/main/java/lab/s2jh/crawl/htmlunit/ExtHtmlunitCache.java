package lab.s2jh.crawl.htmlunit;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Add Cache-Control header info process for htmlunit cache
 */
public class ExtHtmlunitCache extends Cache {

    protected boolean isDynamicContent(final WebResponse response) {
        final String cacheControl = response.getResponseHeaderValue("Cache-Control");
        if (StringUtils.isNotBlank(cacheControl) && cacheControl.toLowerCase().indexOf("max-age") > -1) {
            return false;
        }
        
        return super.isDynamicContent(response);
    }
}
