# Portal Qualidade

### O que é
O Portal da Qualidade é um sistema de gerenciamento de documentos produzidos pelo Escritório da Qualidade do Hospital Regional Universitário de Maringá (HUM) que tem como objetivo aumentar a produtividade dos funcionários do escritório por meio da automação de tarefas rotineiras.

Os documentos administrados pelo Escritório da Qualidade, como os Procedimentos Operacionais Padrões (POPs) são essenciais ao funcionamento do hospital, pois garantem a eficiência e segurança das ações realizadas por formalizarem como tudo deve ser feito, desde trocar uma lâmpada até atender um paciente em parada cardiorrespiratória.

Além disso, sendo o Escritório da Qualidade um setor do HUM, suas atividades passam pelo escrutínio dos diretores do hospital, que exigem a prestação de contas em relação aos trabalhos feitos no escritório. Essa prestação de contas costuma ser feita por meio de indicadores de produtividade, como número de documentos renovados em um determinado semestre ou o número de documentos que não puderam ser postados por conta de atrasos na etapa de revisão ou assinatura.

Tendo isso em mente, os funcionários do Escritório da Qualidade, em conversa com a equipe de alunos, determinou que necessitam de um sistema que compra as seguintes funções gerais principais:
- Que envie mensagens pré definidas de forma automática por e-mail;
- Que monitore os documentos próximos do prazo de validade;


### ZK Framework e Bootstrap

- Projeto em Maven para desenvolvimento em Java utilizando ZK Framework com Bootstrap para desenvolvimento *frontend* e Hibernate para comunicação com o banco de dados. 
- Para desenvolvimento _front-end_ utiliza-se o [ZK Framework 9](https://www.zkoss.org) para manipulação dos elementos DOM, isso significa que, para criar os _inputs_ e _buttons_ (elementos HTML que contém valores e ações) em vez de utilizar as tags HTML você utilizará as tags XML do ZK. A vantagem é que você terá um desenvolvimento mais rápido sem precisar saber JavaScript, pois apenas com Java e ZK todas as manipulações dos elementos do _front-end_ acontecerá no _back-end_ utilizando-se [Ajax](https://pt.wikipedia.org/wiki/Ajax_%28programa%C3%A7%C3%A3o%29). Para saber todas as funcionalidades que o ZK oferece acesse o _demo_ em [https://www.zkoss.org/zkdemo/](https://www.zkoss.org/zkdemo/).
- Para desenvolvimento de layouts responsivos, utiliza-se o [Bootstrap 5](https://getbootstrap.com), o qual tem um sistema de *grid* possibilitando especificar para cada tamanho de tela qual o espaço que cada componente irá ocupar. Utilizamos também o CSS do Bootstrap para aperfeiçoar os componentes ZK como *inputs*, *selectbox* e *buttons*, além de utilizar o JavaScript do Bootstrap para incrementar as funcionalidades.

### Java

-   Para desenvolvimento utiliza-se o [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11).

### Servidor

-   Necessário o [Apache Tomcat 9](https://tomcat.apache.org/download-90.cgi) rodando com Java 11.

### Banco de Dados

-   Está sendo utilizado o banco de dados MySQL. Favor alterar o arquivo `src/main/resources/hibernate.cfg.xml` para a base de desenvolvimento de sua equipe.
- No geral, ele tentará se conectar com um banco de dados `portalqualidade` em `localhost:3306` com usuário e senhas ambos `admin`, mas que podem ser alterados no arquivo de configuração.
- Todo banco é construído com Hibernate, mas um Dump com dados de exemplo também pode ser encontrado nesse repositório. Você apenas precisa garantir ter uma conexão aberta na porta acima e com mesmo nome e login.

### Comandos de Run

- Garanta estar colocando o endereço correto para seu Apache Tomcat
- Código com comandos Windows, mas fácil para adaptar

```
call mvn clean package

del /q "{pasta do apache tomcat}\webapps\*.war"

rmdir /s /q "{pasta do apache tomcat}\webapps\portal"

copy /y ".\target\portal.war" "{pasta do apache tomcat}\webapps\portal.war"

call "{pasta do apache tomcat}\bin\startup.bat"
```

### Acesso Inicial
1. Rode o scrip acima (em um .bat ou no terminal, por exemplo), ou faça a configuração manual do start com maven e Tomcat.
2. Garanta que seu banco de dados MySQL está ativo com as especificações descritas acima.
3. Acesse a URL `http://localhost:8080/portal`, que também pode ser alterada com as configurações do tomcat e do arquivo .war gerado.
4. Inicialmente ele carregará uma tela de login cuja senha mestra é `SENHA_MESTRA`.
5. Está pronto para utilizar.