
# GenCin-API

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/IF977/if977-project-standards.svg)](https://github.com/IF977/if977-project-standards/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/IF977/if977-project-standards.svg)](https://github.com/IF977/if977-project-standards/pulls)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

## Descrição

Esta é uma API desenvolvida em Java utilizando o framework SpringBoot. O objetivo do projeto é processar todas as funcionalidades de Backend para o aplicativo GenAPP.

## Início Rápido

Essas instruções fornecerão uma cópia do projeto instalada e funcionando na sua máquina local para fins de desenvolvimento e teste.

### Pré-requisitos

O que é necessário para instalar o software e como instalá-lo:

```bash
# Java 11 ou superior
# Maven 3.6.0 ou superior
```

### Instalação

Um passo a passo da série de exemplos que informam como fazer um ambiente de desenvolvimento em execução:

```bash
# Clone o repositório
git clone https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git

# Entre no diretório do projeto
cd SEU-REPOSITORIO

# Compile e instale as dependências
mvn clean install

# Inicie a aplicação
mvn spring-boot:run

```


## Uso

Demonstrações de como usar o projeto:

@RestController
@RequestMapping("/api")
public class ExemploController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}


## Funcionalidades

- Criação de Aluno
- Autenticação de Aluno
- Operações CRUD para dados específicos
- Retornar as salas cadastradas de um Aluno
- Criação de Professor
- Autenticação de Professor
- Criação de uma Sala

## Documentação

[[Documentação](link-para-documentação)](https://github.com/CHMFC/GenCin-API/main/README.md)

## Como Contribuir

Contribuições são sempre bem-vindas, veja como você pode ajudar:
1. Clone o repositório e crie sua branch a partir de `main`.
2. `git checkout -b minha-nova-feature`
3. Faça suas alterações e commit.
4. Envie para a branch.
5. Abra um Pull Request.

### Diretrizes de Contribuição

Leia através do [CONTRIBUTING.md](link-para-contributing.md)

## Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE.md](LICENSE) para detalhes.

## Créditos

- Carlos Henrique Fontaine (@CHMFC)
- Outros colaboradores

## FAQ

**Pergunta 1:** Como faço para configurar o ambiente?

**Resposta:** Siga as instruções de instalação e qualquer etapa de configuração adicional indicada acima.

## Estado do Projeto

Este projeto está em desenvolvimento ativo. Você pode esperar mudanças frequentes e atualizações.
