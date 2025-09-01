package iuh.fit.se.mail;

import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import jakarta.servlet.http.Part;

import java.io.*;
import java.util.Properties;

//@WebServlet("/sendMail")
@MultipartConfig
public class SendMailServlet extends HttpServlet {

    private static final String senderEmail = "duchuynhngoc1902@gmail.com";
    private static final String senderPassword = "ctnp jwsj epsz kfgh";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String recipient = req.getParameter("recipient");
        String subject = req.getParameter("subject");
        String body = req.getParameter("body");

        Part filePart = req.getPart("attachment");
        String fileName = filePart != null ? filePart.getSubmittedFileName() : null;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            // body
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            // file
            if (fileName != null && !fileName.isEmpty()) {
                MimeBodyPart attachPart = new MimeBodyPart();
                InputStream fileContent = filePart.getInputStream();
                DataSource source = new ByteArrayDataSource(fileContent, filePart.getContentType());
                attachPart.setDataHandler(new DataHandler(source));
                attachPart.setFileName(fileName);
                multipart.addBodyPart(attachPart);
            }

            message.setContent(multipart);

            Transport.send(message);

            resp.getWriter().println("Email sent successfully to: " + recipient);

        } catch (MessagingException e) {
            e.printStackTrace();
            resp.getWriter().println("Error sending email: " + e.getMessage());
        }
    }
}
