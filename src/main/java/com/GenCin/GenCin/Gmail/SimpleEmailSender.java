package com.GenCin.GenCin.Gmail;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

public class SimpleEmailSender {

    /**
     * Envia um e-mail para o endereço informado com o código de verificação.
     *
     * @param mail           O e-mail do destinatário.
     * @param messageContent O código de verificação a ser enviado.
     * @throws Exception Em caso de erro na autenticação ou no envio do e-mail.
     */
    public static void sendEmailWithMessage(String mail, String messageContent) throws Exception {
        String recipient = mail;
        String sender = "gencin.dev@gmail.com";
        String subject = "Seu código de verificação do GenCin é (" + messageContent + "), acesse agora a sua conta";

        Gmail service = GmailServiceUtil.getGmailService();

        String corpo = "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <style>\n" +
                "      body { \n" +
                "        font-family: Arial, sans-serif;\n" +
                "        background-color: #FF0000;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "      .container { \n" +
                "        max-width: 600px;\n" +
                "        margin: 20px auto;\n" +
                "        background-color: #ffffff;\n" +
                "        padding: 20px;\n" +
                "        border: 1px solid #ddd;\n" +
                "        box-shadow: 0 2px 3px rgba(0,0,0,0.1);\n" +
                "      }\n" +
                "      .header { \n" +
                "        background-color: #FF0000;\n" +
                "        color: #ffffff;\n" +
                "        padding: 10px 20px;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "      .header h1 { \n" +
                "        margin: 0;\n" +
                "        font-size: 24px;\n" +
                "      }\n" +
                "      .content { \n" +
                "        padding: 20px;\n" +
                "        line-height: 1.6;\n" +
                "        color: #333333;\n" +
                "      }\n" +
                "      .code-label { \n" +
                "        font-size: 18px; \n" +
                "        font-weight: bold; \n" +
                "        margin-top: 20px;\n" +
                "        margin-bottom: 5px;\n" +
                "      }\n" +
                "      .code { \n" +
                "        font-size: 24px; \n" +
                "        font-weight: bold; \n" +
                "        color: #FF0000;\n" +
                "        background-color: #e9f5ff;\n" +
                "        padding: 10px;\n" +
                "        border-radius: 5px;\n" +
                "        text-align: center;\n" +
                "        margin-bottom: 20px;\n" +
                "      }\n" +
                "      .footer { \n" +
                "        margin-top: 20px;\n" +
                "        font-size: 12px;\n" +
                "        color: #777777;\n" +
                "        text-align: center;\n" +
                "        border-top: 1px solid #ddd;\n" +
                "        padding-top: 10px;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <div class=\"header\">\n" +
                "<!-- Imagem inline referenciada pelo Content-ID logoImage -->\n" +
                "        <img src=\"cid:logoImage\" alt=\"Logo\" style=\"width: 180px;\" />\n" +
                "      </div>\n" +
                "      <div class=\"content\">\n" +
                "        <p>Olá,</p>\n" +
                "        <p>Seja bem-vindo(a) ao GenCin, o seu gerenciador integrado de salas, turmas e atividades, " +
                "desenvolvido especialmente para o Centro de Informática da UFPE!</p>\n" +
                "        <p>Para que você comece a aproveitar todas as funcionalidades do nosso app, precisamos confirmar sua identidade:</p>\n" +
                "        <div class=\"code-label\">CÓDIGO DE VERIFICAÇÃO:</div>\n" +
                "        <div class=\"code\">" + messageContent + "</div>\n" +
                "        <p>Com o GenCin, você poderá acessar suas turmas utilizando os códigos diretos das cadeiras, " +
                "visualizar o calendário atualizado de cada disciplina em tempo real e muito mais!</p>\n" +
                "        <p>Não perca tempo: confirme seu login e comece a organizar seus estudos e atividades de forma prática e inovadora.</p>\n" +
                "        <p>Caso você não tenha solicitado essa verificação, desconsidere este e-mail. Nossa equipe está sempre à disposição " +
                "para garantir a segurança e a privacidade da sua conta.</p>\n" +
                "        <p>Estamos ansiosos para ter você conosco nessa nova experiência!</p>\n" +
                "        <p>Atenciosamente,<br>Equipe GenCin</p>\n" +
                "      </div>\n" +
                "      <div class=\"footer\">\n" +
                "        <p>Este é um e-mail automático. Por favor, não responda.</p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";

        InputStream imageStream = SimpleEmailSender.class.getResourceAsStream("/assets/logo.png");
        if (imageStream == null) {
            throw new Exception("Imagem logo.png não encontrada no caminho /assets/logo.png");
        }

        MimeMessage email = GmailEmailSender.createEmailWithInlineImage(recipient, sender, subject, corpo, imageStream, "image/png", "logoImage");

        Message sentMessage = GmailEmailSender.sendMessage(service, "me", email);
    }
}