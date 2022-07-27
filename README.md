## Componentes de código
> br.com.futechat.commons

O pacote de configuração conta com a definição programática da conexão com os dois bancos de dados, sendo um deles o RDS com PostgreSQL e o H2 para o escopo de testes
Utilizamos um Bean do tipo `DataSource` criado a partir das propriedades obtidas do Parameter Store. Nas classes `DatabaseConfig` e `H2Config` também é feita a localização do pacote contendo as entidades Jpa do projeto, bem como definição de propriedades do hibernate e criação do `TransactionManager` que é o objeto responsável por gerir as transações com o banco de dados
```java
	@Bean(name = "rdsDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
```
A configuração do [Flyway](https://www.baeldung.com/database-migrations-with-flyway) que é a ferramenta de versionamento e evolução de schema do banco de dados também está no pacote de configurações. Por convenção o spring localiza os arquivos de migração dentro de db/migration
Nesse pacote encontramos também a anotação customizada `EnableFutechatCommons`, responsável por expor a aplicação que importar a biblioteca os objetos e configurações apropriados

> br.com.futechat.commons.api.client

Nesse pacote temos a configuração do Client de API
Atualmente nosso principal provedor de informação relacionado ao mundo do futebol é o [API-FOOTBALL](https://www.api-football.com/) que é uma API REST que provê dados atualizados de diversas ligas ao redor do mundo, lá encontramos endpoints para diversos tipos de dados como estatísticas de uma partida, atributos de jogadores, informações sobre ligas e times, etc.
Temos uma subscrição no [RapidAPI](https://rapidapi.com/api-sports/api/api-football) que é um hub de APIs onde podemos aderir e gerir de forma centralizada a assinatura de APIs dos mais diversos tipos e provedores. A subscrição nos dá direito a uma chave de consumo ou API key através da qual usufruímos de nossa cota de requisições contratada no plano escolhido
Dentro do arquivo `ApiFootballClient` temos os endpoints atualmente consumidos necessários para prover as funcionalidades disponíveis do futechat. O Feign nos permite mapear os endpoints de maneira sucinta e elegante por meio de anotações e assinatura de método, o tipo de retorno, verbo HTTP, path, parâmetros e tipos de retorno estão todos mapeados conforme a [documentação](https://www.api-football.com/documentation-v3) do provedor
Em `FeignConfig` temos o interceptador responsável por adicionar no header da requisição a Key de autorização para consumo da API bem como o host, por sua vez esses parâmetros são obtidos na conta da aws
> br.com.futechat.commons.api.model

O pacote model contém os DTOs relacionados a requisição e resposta do API-FOOTBALL conforme os atributos listados na documentação e no estilo record do Java

> br.com.futechat.commons.aspect

Nesse pacote habilitamos o AspectJ que nos permite utilizar o paradigma AOP
Em nosso caso o comportamento transversal adicionado é a interceptação dos métodos relacionados as funcionalidades dos comandos do Futechat no qual ao se deparar com as exceções customizadas lançadas quando uma liga, time ou jogador não são encontrados alteramos a resposta para apresentar um texto amigável ao usuário informando tal situação
Estamos em resumo utilizando AOP basicamente para o tratamento global de erros desse tipo na aplicação de maneira a minimizar os pontos de código em que teríamos que tratar as situações em que uma informação não é encontrada e alterar a resposta correspondente ao usuário
Os métodos interceptados são definidos pela expressão abaixo
```java
	@Pointcut("execution(* br.com.futechat.commons.service.text.*.*(..))")
	private void whenToCall() {
	}
```

> br.com.futechat.commons.batch

Fazemos uso do Spring Batch que é um módulo do framework responsável por facilitar a criação e gestão de Jobs de processamento em lote
Nosso caso de uso é a sincronização dos dados de ligas, times e jogadores provenientes da API do provedor para o nosso banco de dados RDS. Essa sincronização se dá por uma série de razões, dentre elas:
- Economia do número de chamadas para a API
    - Uma vez que somos cobrados por quantidade de chamadas a API com base em nossa cota de uso definida na subscrição é mister que tenhamos alguns dos dados de entidades que não sofrem grande alteração em nosso banco de dados interno ao invés de fazer a requisição HTTP a todo momento
- Otimização do tempo de resposta
    - Devido ao fato de que algumas funcionalidades exigem mais de uma chamada a API para concatenar informações e devido a algumas requisições serem chamadas com base nos Ids internos da API a iniciativa de manter uma base de dados própria se faz necessária para reduzir o número de requisições HTTP e tornar o tempo de resposta mais ágil
- Escalabilidade, cruzamento de informações e redução de dependência com o provedor de informação
    - Manter e evoluir uma base de dados própria nos permite oferecer algumas funcionalidades sem depender de nosso provedor de informação que é a API. Também nos permite cruzar informações caso tenhamos uma funcionalidade que depende de dados obtidos de mais de um provedor, além disso nos permite trocar ou adicionar provedores externos de informação a nossa aplicação de maneira mais ágil possibilitando funcionalidades cada vez mais robustas

Na classe `BatchConfig` temos a configuração das tabelas internas do Spring Batch que serão criadas a partir dos arquivos sql de schema no postgresql
Temos uma extensão de `BatchConfigurer` para customizar e vincular nosso `DataSource` e `TransactionManager` de forma que os mesmos sejam usados dentro dos Jobs de persistência
Temos três Jobs
- `LeaguesDBSyncJob`
- `TeamsDBSyncJob`
- `PlayersDBSyncJob`

Respectivamente responsáveis pela sincronização das tabelas de ligas ou campeonatos, times de futebol e jogadores
Os dois primeiros são baseados em Tasklet (Modalidade onde uma task é completada em um step de forma atômica e cada task é responsável por um único step) e o terceiro foi implementado na modalidade de chunks (Onde os dados são divididos em vários pedaços e processados de maneira separadas em ciclos)
Para times e ligas como somos capazes de obter toda a lista com uma única requisição HTTP a abordagem de Tasklet executa os passos do Job (Leitura, processamento e saída) uma única vez. Já para jogadores temos um dataset massivo onde cada liga possui centenas ou milhares de jogadores e até por isso essa requisição é paginada, dessa forma a abordagem de chunks que processa separadamente porções de dados e repete o ciclo do Job para cada uma dessas porções se mostrou mais adequado 
```java
	@Bean(name = "leaguePlayersUpdateStep")
	public Step leaguePlayersUpdateStep(ItemReader<List<Player>> reader,
			ItemProcessor<List<Player>, List<Player>> processor, ItemWriter<List<Player>> writer) {
		return stepBuilderFactory.get("leaguePlayersUpdateStep").<List<Player>, List<Player>>chunk(1).reader(reader)
				.processor(processor).writer(writer).build();
	}
```
O tamanho do chunk é definido como 1 pois processaremos os jogadores de uma liga por vez
Na etapa de leitura presente em ApiFootballPlayerReader temos uma fila ou Queue com as ligas observadas (Uma vez que carregaremos apenas jogadores de ligas específicas de interesse pois seria inviável carregar todos os jogadores de todas as ligas do mundo). Na inicialização do Reader carregamos as ligas de interesse que por sua vez estão parametrizadas no Parameter Store (Dessa forma podemos escolher de maneira mais dinâmica de quais ligas iremos carregar os jogadores). A cada leitura é feito o poll que recupera e remove a liga atual da Queue e com base nela fazemos a requisição paginada dos jogadores junto ao API-FOOTBALL

```java
	private Queue<League> observedLeagues;
	
	@Autowired
	public ApiFootballPlayerReader(ApiFootballService apiFootballService) {
		this.apiFootballService = apiFootballService;
		this.observedLeagues = new LinkedList<>(apiFootballService.getLeaguesOfInterest());
	}

	@Override
	public List<Player> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Optional<League> currentLeague = Optional.ofNullable(this.observedLeagues.poll());
		if (currentLeague.isPresent()) {
			
			List<Player> playersFromLeague = apiFootballService.getPlayersFromLeague(currentLeague.get().getApiFootballId());
			LOGGER.info("{} players were found from the league {}", playersFromLeague.size(), currentLeague.get().getApiFootballId());
			return playersFromLeague;
		}
		return null;
	}
```

No Processor filtramos os registros que já existem no banco de dados e no Writer finalmente fazemos a persistência dos restantes
Como os Jobs fazem um número massivo de requisições e chamadas ao banco de dados há um mecanismo de Feature Flag com base na linha abaixo

```java
@ConditionalOnProperty(prefix = "apiFootball", name = "playersSynchronizationEnabled", havingValue = "true")
```

> br.com.futechat.commons.entity

Mapeamento das entidades da Jpa relacionadas as tabelas do futechat

> br.com.futechat.commons.exception

Exceções customizadas lançadas ao longo das funcionalidades e cujo tratamento e interceptação visa entregar um feedback mais intuitivo e amigável ao usuário final em caso de erro

> br.com.futechat.commons.mapper

Utilizamos o mapstruct para mapeamento entre os diferentes DTOs da aplicação

Temos os DTOs relacionados a requisição e resposta junto a API, os objetos de persistência e por fim os objetos da camada de negócio da aplicação. Eles são separados visando o desacoplamento entre as camadas e a `FutechatMapper` que é a abstração base provê a assinatura de mapeamento entre os objetos conforme anotações e convenções do  [mapstruct](https://www.baeldung.com/mapstruct) que por sua vez gera a implementação na pasta target

> br.com.futechat.commons.service

Nessa camada de negócio temos os serviços de alto nível da aplicação onde a abstração FutechatService provê o contrato das funcionalidades e sua única implementação até o momento que é ApiFootballService provê as mesmas com base no provedor de informação

Como nossas funcionalidades são uma mescla de chamadas de API com informações do banco de dados interno , temos um Adapter de persistência que desacopla a interação com o banco de dados das interações com a API e nos permite evoluir e alterar essas camadas de maneira independente

> br.com.futechat.commons.service.text

É a camada responsável por concatenar as respostas como texto de acordo com cada funcionalidade, por exemplo o método getFixtureStatistics pega o objeto com os dados da estatística de uma partida e devolve uma String com um texto amigável para o usuário final com as métricas daquele jogo. Esse pacote funciona como a camada de apresentação ou front-end pois é o texto final que o usuário receberá após usar um comando do futechat, a existência dessa camada desacopla a apresentação do mecanismo subjacente que obtém os dados da funcionalidade em si e permite o reuso e uma maior legibilidade do código
### Testes
Na camada de testes usamos o WireMock para simular respostas da API do provedor e testar a camada de serviço de maneira isolada

Os payloads estáticos para teste estão localizados e são encontrados via convenção no diretório __files

O mapeamento das requisições com os respectivos payloads estão localizados via convenção no diretório mappings

O DataSource do H2 é utilizado devido a configuração do Profile de testes

Temos o arquivo `player_sample_record.sql` no pacote de testes que por meio da anotação `@Sql` insere alguns registros de teste no banco em memória
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