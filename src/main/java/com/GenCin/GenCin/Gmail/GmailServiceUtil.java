package com.GenCin.GenCin.Gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

public class GmailServiceUtil {

    private static final String APPLICATION_NAME = "Minha Aplicação Spring Boot";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final java.io.File TOKENS_DIRECTORY = new java.io.File("tokens");
    private static final java.util.List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/gmail.send");

    public static Credential getCredentials() throws Exception {
        // Carrega o arquivo de credenciais do resources
        InputStream in = GmailServiceUtil.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new Exception("Arquivo de credenciais não encontrado.");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                .setDataStoreFactory(new com.google.api.client.util.store.FileDataStoreFactory(TOKENS_DIRECTORY))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("gencin-api-dev.uk").setPort(443).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Gmail getGmailService() throws Exception {
        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
