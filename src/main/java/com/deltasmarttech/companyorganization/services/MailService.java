package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.util.EmailConfirmationToken;
import jakarta.mail.MessagingException;
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


    public void sendVerificationEmail(EmailConfirmationToken emailConfirmationToken)
            throws MessagingException {
/*
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailConfirmationToken.getUser().getUsername());
        helper.setSubject("Delta Smart Tech. Registration - Confirm your e-mail");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear "+ emailConfirmationToken.getUser().getName() + ",</h2>"
                        + "<br /> Please click on below link to confirm your account."
                        + "<br/> "  + generateConfirmationLink(emailConfirmationToken.getToken())+"" +
                        "<br/> Regards,<br/>" +
                        "Delta Smart Tech." +
                        "</body>" +
                        "</html>"
                , true);

        mailSender.send(message);

 */

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

            helper.setSubject("Delta Smart Tech - Activate Your Account and Set Password");

            String content = "<html>" +
                    "<body>" +
                    "<h2>Dear " + emailConfirmationToken.getUser().getName() + ",</h2>" +
                    "<br /> Please click on the link below to activate your account and set password." +
                    "<br/><a href='" + generateConfirmationLink(emailConfirmationToken.getVerificationCode()) + "'>Activate your account!</a>" +
                    "<br/><br/>Regards,<br/>" +
                    "Delta Smart Tech." +
                    "</body>" +
                    "</html>";

            helper.setText(content, true);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        }

    }

    private String generateConfirmationLink(String token) {
        return "http://localhost:8080/api/v1/auth/set-password?token=" + token ;
    }
}
