package lab.s2jh.ctx;

import java.util.Set;

import javax.mail.internet.MimeMessage;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.exception.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;

@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    private static final ThreadLocal<Set<MailMessage>> mimeMessages = new NamedThreadLocal<Set<MailMessage>>(
            "Transaction Mail MimeMessages");

    public boolean isEnabled() {
        return javaMailSender != null;
    }

    public void sendHtmlMail(String subject, String text, boolean singleMode, String... toAddrs) {
        Assert.isTrue(isEnabled(), "Mail service unavailable");
        if (logger.isDebugEnabled()) {
            logger.debug("Submit tobe send  mail: TO: {} ,Subject: {}", StringUtils.join(toAddrs, ","), subject);
        }
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Register mails with database Transaction Synchronization...");
            Set<MailMessage> mails = mimeMessages.get();
            if (mails == null) {
                mails = Sets.newHashSet();
                mimeMessages.set(mails);
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCompletion(int status) {
                        logger.debug("Processing afterCompletion[status={}] of TransactionSynchronizationManager...",
                                status);
                        Set<MailMessage> transactionMails = mimeMessages.get();
                        for (MailMessage mail : transactionMails) {
                            sendMail(mail.getSubject(), mail.getText(), mail.getSingleMode(), true, mail.getToAddrs());
                        }
                    }
                });
            }
            MailMessage mail = new MailMessage();
            mail.setSubject(subject);
            mail.setText(text);
            mail.setSingleMode(singleMode);
            mail.setToAddrs(toAddrs);
            mails.add(mail);
        } else {
            this.sendMail(subject, text, singleMode, false, toAddrs);
        }
    }

    private void sendMail(String subject, String text, boolean singleMode, boolean transactional, String... toAddrs) {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Sending mail: \nTO: {} \nSubject: {} \nSingle Mode: {} \nTransactional Mode: {} \nContent:\n---------\n{}\n----------",
                    StringUtils.join(toAddrs, ","), subject, singleMode, transactional, text);
        }

        if (dynamicConfigService.getBoolean("cfg.mail.mock.mode", false)) {
            logger.debug("Mock sending  mail...");
            return;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        String from = dynamicConfigService.getString("cfg.mail.from", null);
        Assert.notNull(from);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            if (StringUtils.isNotBlank(from)) {
                helper.setFrom(from);
            }
            helper.setSubject(subject);
            helper.setText(text, true);

            if (singleMode == false) {
                helper.setTo(toAddrs);
                javaMailSender.send(message);
            } else {
                for (String to : toAddrs) {
                    helper.setTo(to);
                    javaMailSender.send(message);
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private static class MailMessage {
        private String subject;
        private String text;
        @MetaData("单用户发送模式")
        private boolean singleMode;
        private String[] toAddrs;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String[] getToAddrs() {
            return toAddrs;
        }

        public void setToAddrs(String[] toAddrs) {
            this.toAddrs = toAddrs;
        }

        public Boolean getSingleMode() {
            return singleMode;
        }

        public void setSingleMode(Boolean singleMode) {
            this.singleMode = singleMode;
        }
    }
}
