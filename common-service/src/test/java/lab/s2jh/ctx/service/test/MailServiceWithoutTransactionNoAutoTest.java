package lab.s2jh.ctx.service.test;

import lab.s2jh.ctx.MailService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:/context/context-profiles.xml", "classpath:/context/spring*.xml",
        "classpath:/service/spring-mail.xml" })
public class MailServiceWithoutTransactionNoAutoTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private MailService mailService;

    @Test
    public void sendHtmlMail() {
        mailService.sendHtmlMail("Test", "ABC", false, "admin@xxx.com");
    }
}
