package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.util.EmailConfirmationToken;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(EmailConfirmationToken emailConfirmationToken, String expirationTime)
            throws MessagingException {

        String subject = "Activate Your Account and Set Password | Delta Smart Tech";
        String content = "<html>" +
                    "<body>" +
                    "<h3>Dear " + emailConfirmationToken.getUser().getName() + ",</h2>" +
                    "<br /> Please click on the link below to activate your account and set password." +
                    "<br/><a href='" + generateConfirmationLink(emailConfirmationToken.getVerificationCode(), 1) + "'>Activate your account!</a>" +
                    "<br/>" + "This verification link will expire on " + expirationTime +
                    "<br/>Regards,<br/>" +
                    "Delta Smart Tech." +
                    "</body>" +
                    "</html>";

        sendMail(emailConfirmationToken, subject, content);

    }

    public void sendResetPasswordEmail(EmailConfirmationToken emailConfirmationToken, String expirationTime)
            throws MessagingException {

        String subject = "Reset Your Password | Delta Smart Tech";
        String content = "<html>" +
                "<body>" +
                "<h3>Dear " + emailConfirmationToken.getUser().getName() + ",</h2>" +
                "<br /> Please click on the link below to reset your password." +
                "<br/><a href='" + generateConfirmationLink(emailConfirmationToken.getVerificationCode(), 2) + "'>Reset your password!</a>" +
                "<br/>" + "This verification link will expire on " + expirationTime +
                "<br/><br/>Regards,<br/>" +
                "Delta Smart Tech." +
                "</body>" +
                "</html>";

        sendMail(emailConfirmationToken, subject, content);
    }

    private void sendMail(EmailConfirmationToken emailConfirmationToken, String subject, String content) {

        Properties props = new Properties();
        String host = "smtp.gmail.com";
        String username = "techdeltasmart@gmail.com";
        String password = "iobr gtrt rplf iger";

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props);

        try {

            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(username));

            InternetAddress toAddress = new InternetAddress(emailConfirmationToken.getUser().getEmail());
            helper.setTo(toAddress);

            helper.setSubject(subject);

            helper.setText(content, true);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateConfirmationLink(String token, Integer type) {
        return "http://localhost:3000/set-password?token=" + token + "&type=" + type;
    }
}
