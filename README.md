## Pipeline de CI/CD
A pipeline de CI/CD deste projeto consiste em uma instância ec2 que atua como servidor do Jenkins
No Jenkins temos as seguintes etapas para publicação da biblioteca no Codeartifact
- Geração do token do codeartifact por meio do comando aws e grava em um arquivo propsfile
```sh
echo CODEARTIFACT_AUTH_TOKEN=`aws codeartifact get-authorization-token --domain agama --domain-owner 270167558056 --query authorizationToken --output text --region us-east-1` > propsfile
```
- Utilização do EnvInject para injetar uma variável de ambiente dinâmica com base no propsfile gerado pelo comando anterior
  - O EnvInject é um plugin que permite a inserção de variáveis de ambiente dinâmicas e específicas para um job
- Copia o settings customizado do projeto para o diretório m2 do usuário do Jenkins
```sh
cp ./settings.xml /var/lib/jenkins/.m2/settings.xml
```
- Executa os testes do projeto por meio do comando maven
```sh
mvn test
```
- Remove o Snapshot da versão em desenvolvimento para preparar a publicação da iteração atual da biblioteca
```sh
mvn versions:set -DremoveSnapshot
```
- Faz o upload da versão atual para o Codeartifact
```sh
mvn -s settings.xml clean deploy
```
- Incrementa o pom para a próxima versão Snapshot de desenvolvimento
```sh
mvn validate -DbumpMajor
```
- Faz o commit do incremento de versão para que o pom com o último Snapshot esteja disponível para os desenvolvedores
```sh
git add pom.xml
git commit -m "Nova versão"
```
- Como etapa de pós-build adicionamos o plugin do Git Push responsável por enviar o último commit para o repositório remoto
## Configurações e instalação do Jenkins
- Criação da KeyPair para acesso ssh
- Criação da instância ec2 que irá hospedar o Jenkins
- [Instalação do Jenkins](https://www.jenkins.io/doc/book/installing/linux/#unlocking-jenkins)
- Com a senha de administrador em mãos realizar o unlock do Jenkins
  - Encontrar a senha de administrador após a instalação no log por meio do comando
```sh
journalctl -u jenkins.service
```
- Inicializar instância no ec2 vinculado ao KeyPair e também a um grupo de segurança com acesso SSH e porta 8080
  - Configurar o acesso a conta via aws configure ou atribuir as variáveis de ambiente `AWS_DEFAULT_REGION`, `AWS_ACCESS_KEY_ID` e `AWS_SECRET_ACCESS_KEY`
- Adicionar a variável de ambiente global `PATH+EXTRA` na configuração para ter disponível os comandos instalados pelo ec2-user como por exemplo a jdk e o maven
- Gerar uma chave ssh para acesso aos repositórios na conta do github e em seguida criar uma Credential no Jenkins com o conteúdo da chave ssh privada criada para permitir o pull e push para o repositório