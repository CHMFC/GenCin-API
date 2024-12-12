# BUILD.md

## Como Construir e Executar o GenCin-API (SpringBoot)

### Requisitos
Para construir e executar o GenCin-API, certifique-se de que os seguintes itens estejam instalados e configurados no seu ambiente:

- **Java**: Versão 11 ou superior.
- **Maven**: Versão 3.6.0 ou superior.
- **Banco de Dados**: MySQL, PostgreSQL ou outro banco de dados suportado, configurado e em execução.
- **IDE para Java** (opcional, mas recomendado): IntelliJ IDEA, Eclipse, ou VS Code com extensão para Java.

### Passos para Construir e Executar o Projeto

1. **Clone o Repositório**
   ```bash
   git clone https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git
   cd SEU-REPOSITORIO/api
   ```

2. **Configure o Banco de Dados**
   - Certifique-se de que o banco de dados está em execução.
   - Atualize o arquivo `application.properties` ou `application.yml` com as credenciais do seu banco de dados. Exemplo para MySQL:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
     spring.datasource.username=seu_usuario
     spring.datasource.password=sua_senha
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Compile o Projeto**
   - Execute o seguinte comando para compilar o projeto e baixar as dependências:
     ```bash
     mvn clean install
     ```

4. **Execute a Aplicação**
   - Inicie o servidor com o seguinte comando:
     ```bash
     mvn spring-boot:run
     ```

5. **Acesse a API**
   - A aplicação será iniciada em `http://localhost:8080` (ou na porta especificada no arquivo de configuração).

### Executando Testes
- Para garantir que tudo está funcionando corretamente, execute os testes automatizados:
  ```bash
  mvn test
  ```

### Configuração Adicional

- **Porta Personalizada**:
  - Para alterar a porta padrão (8080), edite o arquivo `application.properties`:
    ```properties
    server.port=8081
    ```

- **Perfil de Produção**:
  - Para rodar o sistema no modo produção, configure o perfil no arquivo `application.properties`:
    ```properties
    spring.profiles.active=prod
    ```

### Solução de Problemas
- **Erro de Conexão com o Banco de Dados**:
  - Verifique as credenciais do banco no arquivo de configuração.
  - Certifique-se de que o banco de dados está em execução.

- **Erro de Dependência**:
  - Execute o comando abaixo para forçar a atualização das dependências:
    ```bash
    mvn dependency:resolve
    ```

- **Problemas com a Porta**:
  - Certifique-se de que a porta especificada está livre ou altere a porta no arquivo `application.properties`.

---

Agora você está pronto para construir e executar o GenCin-API! Caso encontre algum problema ou tenha dúvidas, consulte a documentação oficial do SpringBoot ou abra um problema no repositório.

