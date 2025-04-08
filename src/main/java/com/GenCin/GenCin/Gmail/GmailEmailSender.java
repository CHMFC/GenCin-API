package com.GenCin.GenCin.Gmail;

import com.google.api.services.gmail.model.Message;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class GmailEmailSender {

    // Método para criar um e-mail simples com conteúdo HTML
    public static MimeMessage createEmail(String to, String from, String subject, String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        // Configura o conteúdo como HTML com charset UTF-8
        email.setContent(bodyText, "text/html; charset=UTF-8");
        return email;
    }

    // Método para criar um e-mail com conteúdo HTML e imagem inline
    public static MimeMessage createEmailWithInlineImage(String to, String from, String subject, String htmlBody,
                                                         InputStream imageStream, String imageMimeType, String contentId)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // Cria um container multipart com subtype "related"
        MimeMultipart multipart = new MimeMultipart("related");

        // Parte 1: HTML
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
        multipart.addBodyPart(htmlPart);

        // Parte 2: Imagem inline
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource fds = new ByteArrayDataSource(imageStream, imageMimeType);
        imagePart.setDataHandler(new DataHandler(fds));
        // Define o Content-ID (a referência no HTML será "cid:logoImage", por exemplo)
        imagePart.setHeader("Content-ID", "<" + contentId + ">");
        imagePart.setDisposition(MimeBodyPart.INLINE);
        multipart.addBodyPart(imagePart);

        // Define o conteúdo multipart no e-mail e salva as alterações
        email.setContent(multipart);
        email.saveChanges();

        return email;
    }

    // Método para converter o MimeMessage em um Message (formato da API do Gmail)
    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    // Método para enviar o e-mail utilizando a API do Gmail
    public static Message sendMessage(com.google.api.services.gmail.Gmail service, String userId, MimeMessage emailContent)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();
        return message;
    }
}
