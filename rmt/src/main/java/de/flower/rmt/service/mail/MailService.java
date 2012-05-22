package de.flower.rmt.service.mail;

import de.flower.common.util.Check;
import de.flower.rmt.model.db.entity.User;
import de.flower.rmt.model.dto.Notification;
import de.flower.rmt.service.security.ISecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flowerrrr
 */
@Service
public class MailService implements IMailService {

    private final static Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private SimpleMailMessage templateMessage;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private ISecurityService securityService;

    @PostConstruct
    public void init() {
        if (mailSender instanceof JavaMailSenderImpl) {
            String host = ((JavaMailSenderImpl) mailSender).getHost();
            log.info("*************************************************************");
            log.info("Smtp Mail Host: " + host);
            log.info("*************************************************************");
        }
    }

    @Override
    public void sendMassMail(final Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        List<String> recipients = new ArrayList<String>();
        for (InternetAddress iAddress : notification.getRecipients()) {
            recipients.add(iAddress.toString());
        }
        if (notification.isBccMySelf()) {
            recipients.add(getCurrentUserEmail());
        }
        message.setBcc(recipients.toArray(new String[] {}));
        message.setReplyTo(getCurrentUserEmail());
        message.setSubject(notification.getSubject());
        message.setText(notification.getBody());
         // fields like sender, reply-to, to are preset by default mail template.
        sendMail(message);
    }

    @Override
    public void sendMail(final String receiver, final String bcc, final String subject, final String content) {
        // mail to single person gets managers email as reply to.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setReplyTo(getCurrentUserEmail());
        message.setTo(receiver);
        message.setBcc(bcc);
        message.setSubject(subject);
        message.setText(content);
        sendMail(message);
    }

    /**
     * Send mail.
     *
     * @param sender  the sender
     * @param toList  the to list
     * @param ccList  the cc list
     * @param subject the subject
     * @param content the content
     * @param bccList the bcc list
     * @throws RuntimeException the mail interface exception
     */
    public final void sendMail(SimpleMailMessage message) {
        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
        message.copyTo(msg);
        // use default undisclosed recipients address if no recipient is defined (like in mass mails)
        // check if template is configured correctly.
        String[] tmp = msg.getTo();
        Check.isTrue(tmp.length > 0);

        try {
            this.mailSender.send(msg);
        } catch (MailException e) {
            log.error("Error sending mail.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Preset reply-to address with email of user that is triggering the email.
     * @return
     */
    private String getCurrentUserEmail() {
        User user = securityService.getUser();
        if (user != null) {
            return user.getEmail();
        } else {
            return null;
        }
    }

 }
