package lab.s2jh.ctx.service.test;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.ctx.MailService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations = { "classpath*:/service/spring-mail.xml" })
@TransactionConfiguration(defaultRollback = false)
public class MailServiceWithTransactionNoAutoTest extends SpringTransactionalTestCase {

    @Autowired
    private MailService mailService;

    @Test
    public void sendHtmlMail() {
        mailService.sendHtmlMail("Test", "ABC", false, "admin@xxx.com");
    }
}
