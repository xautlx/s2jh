package lab.s2jh.crawl.htmlunit;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Default htmlunit will request all parsed HTTP requests (html, js, css, etc.)
 * But only a few requests used for parse specified information,
 * So introduce urlfilter support configurable htmlunit HTTP request list.
 */
public class RegexHttpWebConnection extends HttpWebConnection {

    public static final Logger logger = LoggerFactory.getLogger(RegexHttpWebConnection.class);

    /** An array of applicable rules */
    private List<Rule> rules = new ArrayList<Rule>();

    public RegexHttpWebConnection(WebClient webClient, Set<String> fetchUrlRules) {
        super(webClient);
        logger.info("HttpWebConnection Fetch URL Rule List: ");
        if (fetchUrlRules != null) {
            for (String line : fetchUrlRules) {
                line = line.trim();
                char first = line.charAt(0);
                if ('#' == first) {
                    return;
                }
                String regex = null;
                boolean sign = true;
                if ('-' == first) {
                    sign = false;
                    regex = line.substring(1);
                } else if ('+' == first) {
                    sign = false;
                    regex = line.substring(1);
                } else {
                    regex = line;
                }
                Rule rule = new Rule(sign, regex);
                logger.info(" {} {}", sign ? "+" : "-", regex);
                rules.add(rule);
            }
        }
    }

    @Override
    public WebResponse getResponse(final WebRequest request) throws IOException {
        final URL url = request.getUrl();
        if (StringUtils.isBlank(filter(url.toString())) || url.toString().indexOf("robots.txt") > -1) {
            logger.info("Thread: {}, - Http Excluding URL: {}", Thread.currentThread().getId(), url);
            return new StringWebResponse("", url);
        }
        if (logger.isInfoEnabled()) {
            logger.info("Thread: {}, + Http Fetching URL: {}", Thread.currentThread().getId(), url);
        }
        return super.getResponse(request);
    }

    public String filter(String url) {
        if (rules == null || rules.size() == 0) {
            return url;
        }
        for (Rule rule : rules) {
            //LOG.info("rule=" + rule + ", url=" + url);
            if (rule.match(url)) {
                return rule.accept() ? url : null;
            }
        }
        return null;
    }

    public class Rule {

        private final boolean sign;

        private Pattern pattern;

        Rule(boolean sign, String regex) {
            this.sign = sign;
            pattern = Pattern.compile(regex);
        }

        protected boolean accept() {
            return sign;
        }

        public boolean match(String url) {
            return pattern.matcher(url).find();
        }

        @Override
        public String toString() {
            return "Rule [sign=" + sign + ", pattern=" + pattern + "]";
        }
    }
}
