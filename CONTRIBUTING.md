# CONTRIBUTING.md

## Como Contribuir para o GenCin-API (SpringBoot)

### Requisitos
Certifique-se de que você atenda aos seguintes requisitos antes de contribuir:
- Java 11 ou superior instalado.
- Maven 3.6.0 ou superior instalado.
- Um ambiente de desenvolvimento configurado (ex.: IntelliJ IDEA, Eclipse ou VS Code com extensão para Java).
- Banco de dados configurado (MySQL, PostgreSQL ou outro suportado).

### Processo de Contribuição
1. **Clone o Repositório**:
   ```bash
   git clone https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git
   cd SEU-REPOSITORIO/api
   ```

2. **Crie uma Branch para sua Feature ou Correção de Bug**:
   ```bash
   git checkout -b minha-nova-feature
   ```

3. **Faça suas Alterações no Código**:
   - Certifique-se de seguir as boas práticas de codificação em Java.
   - Adicione testes unitários para novas funcionalidades ou alterações.

4. **Configure o Banco de Dados**:
   - Certifique-se de que o banco de dados está em execução e configurado no arquivo `application.properties` ou `application.yml`.

5. **Compile e Execute os Testes**:
   ```bash
   mvn clean install
   ```

6. **Faça o Commit das Suas Mudanças**:
   ```bash
   git commit -m "Descreva brevemente sua alteração"
   ```

7. **Envie para o Repositório**:
   ```bash
   git push origin minha-nova-feature
   ```

8. **Abra um Pull Request**:
   - Acesse o repositório no GitHub.
   - Clique em "New Pull Request" e selecione sua branch.

9. **Aguarde a Revisão**:
   - O mantenedor do projeto revisará seu Pull Request e pode solicitar alterações antes de aprovar.

### Boas Práticas
- Siga as convenções de nomenclatura e estilo de código do projeto.
- Adicione documentação para novas funcionalidades no código e em arquivos relacionados.
- Atualize os arquivos de configuração ou documentação conforme necessário.
- Certifique-se de que todas as alterações sejam testadas antes de enviar.

---

## Diretrizes para Testes
- **Testes Unitários**:
  - Use o framework JUnit para escrever testes unitários.
  - Certifique-se de que os testes sejam independentes e bem documentados.

- **Cobertura de Testes**:
  - Certifique-se de que sua contribuição mantenha ou aumente a cobertura de testes do projeto.
  - Utilize ferramentas como `JaCoCo` para verificar a cobertura.

---

Obrigado por contribuir para o GenCin-API! Seu apoio ajuda a tornar o projeto mais robusto e eficiente. 😊
