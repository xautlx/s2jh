package lab.s2jh.biz;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexSmokeMock {

    private Logger logger = LoggerFactory.getLogger(IndexSmokeMock.class);

    @Test
    public void touch() throws Exception {
        logger.debug("Init WebDriver...");
        WebDriver driver = new HtmlUnitDriver();
        try {
            logger.debug("Get Testing URL...");
            driver.get("http://localhost:8888/showcase/pub/signin");
            logger.debug("Sleep 30 seconds...");
            Thread.sleep(30000);//wait 30 seconds
            String title = driver.getTitle();
            logger.debug("WebDriver title: " + title);
            Assert.assertTrue(title.startsWith("Admin Console Signin"));
            logger.debug("Smoke mock test successfullly.");
        } finally {
            driver.close();
        }
    }
}
