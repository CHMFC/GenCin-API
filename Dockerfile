# Usar uma imagem base do OpenJDK
FROM openjdk:17-jdk

# Definir um diretório de trabalho opcional (não estritamente necessário, mas recomendado)
WORKDIR /app

# Copiar o arquivo .jar gerado para dentro do contêiner
COPY target/GenCin-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta padrão do Spring Boot
EXPOSE 8080

# Comando de inicialização da aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
