# GroupChatMessenger
**by Felipe Pestana** (usuário asd19)

Documentação criada para dar suporte ao projeto desenvolvido como parte da Lista 1 da disciplina de Sistemas Distribuídos para 2019.1.
Esse arquivo se encontra disponível em duas versões diferentes, uma definida em Markdown e a outra em Html, a ser utilizada de acordo com a preferência do usuário.

## Sumário do Projeto
Esse projeto consiste de um sistema de mensagens curtas visando estabelecer um chat em grupo utilizando comunicações via RMI e sockets Multicast como meios intermediários de comunicação. A arquitetura do programa foi desenvolvido de acordo com as especificações definidas e se encontra dividida principalmente em três módulos de processos diferentes a serem executados:

* Um cliente, presente dentro do diretório GroupChatClient;
* Um servidor, presente dentro do diretório GroupChatServer;
* Um classe para gerência do Registro RMI necessário, presente dentro do diretório GroupChatRMI.
 
Além disso, temos algumas classes de suporte compartilhadas e utilizados por mais de um desses processos, arquivos de configurações individuais para cada módulo e scripts utilizados para inicialização e compilação adequada dos códigos e execução individual de cada processo principal.

## Organização de diretórios na máquina virtual

Todos os arquivos necessários para execução do program encontram-se dentro da pasta Trabalho, localizada no diretório principal do aluno. Para cada um dos módulos desenvolvidos, dentro de suas respectivas pastas, seus arquivos relacionados estão organizados da seguinte maneira:

* Um diretório **bin**, contendo as classes Java compiladas no comando anterior;
* Um diretório **resources**, contendo a classe de configuração referente a cada um dos módulos;
* Um diretório **src**, contendo os códigos desenvolvidos em Java.

Além disso, temos um diretório extra Stubs, destinado a conter as classes Stubs geradas para execução.

Para arquivos Java de classes a serem reaproveitadas e utilizadas em diferentes módulos, optou-se por replicar o código utilizado dentro dos diretórios de cada módulo, visando uma maior facilidade no momeno de avaliação e entendimento geral dos processos.
## Passos para Execução

Esse tópico detalha um pouco das etapas necessárias para execução do programa, assim como descrição de funcionalidades dos scripts desenvolvidos, e descreve um pouco da organização atual aplicada ao projeto.

### Scripts de Inicialização

No diretório principal do trabalho, encontram-se, além das pastas de descritas anteriormente, um total de quatro scripts diferentes: um para preparação dos códigos e um para a execução individual de cada um dos processos definidos. Todos já se encontram corretamente configurados para uso e foram desenvolvidos para se adequar com a organização de diretórios definidas acima, definido onde deveriam encontrar cada um dos arquivos esperados e para onde deveriam ser gerados novos.

Todos os arquivos necessários já se encontram previamente compilados, podendo ser facilmente recompilados pelo processo descrito a seguir se desejado. Atualmente, todos os Scripts possuem as devidas permissões para serem executados dentro da pasta de usuário.

### Compilação e preparação do código

Para compilação de todos os códigos necessários, assim como geração das classes Stubs para o RMI, basta navegar para esse diretório citado e executar o script CompileCode através do comando abaixo:

    ./CompileCode
    
Esse Script, além de compilar separadamente as classes para cada um dos processos, gera também os Stubs envolvidos na execução. 

**Obs**: Apesar da nova versão do RMI Java se utilizar da geração dinâmica desses arquivos, optou-se por incluir o comando explícito *rmic* para geração dessas classes no Script de inicialização para garantir a execução correta do programa como um todo.

### Execução dos processos

O primeiro passo para execução de fato do programa é, após terminado a etapa de compilação, executar em consoles diferentes e separadamente os comandos para ativação de cada um dos scripts responsáveis pela inicialização de cada processo. Ressaltando que os mesmos devem ser executados na ordem em que são listados a seguir:

* Execução do processo de inicialização e gerência do *RMIRegistry* local. Deve permanecer em funcionamento para manter o Registro RMI corretamente conectado:

      ./runRegistry
    
* Execução do processo **Servidor** local da máquina. Necessita que o anterior tenha sido executado e que exista um Registro RMI na porta configurada:

      ./runServer
    
* Execução de um processo **Cliente** na máquina. Necessita que o Registro RMI esteja rodando na porta configurada e que o processo Servidor esteja em execução:

      ./runClient
    
**Obs**: Esse último script é utilizado para iniciar a execução de um único cliente em uma máquina, entretanto, diferentemente dos anteriores, o mesmo pode ser executado diversas vezes. Dessa forma, podemos utilizá-lo para se ter *n* clientes diferentes em execução.

## Arquivos de configuração e parâmetro alteráveis

Para facilitar o gerenciamento e implantação desse programa em diferentes máquinas, foram separados algumas variáveis de ambiente que podem ser configuráveis no momento de execução para se adequar a cada caso. Para isso, cada um dos três processos principais possui o seu próprio arquivo de configuração, denominado de **config.properties** e localizado dentro da sua respectiva pasta *resource*.

Esse arquivo é organizado em uma estrutura de chave e valor, semelhante a um dicionário e suas informações são extraídas diretamente pelo código Java no momento de execução. Suas localizações dentro da árvore de diretórios da máquina são passadas pelo script de execução no momento de chamada do programa a ser executado.

Observou-se a necessidade de definir três parâmetros diferentes para execução:

1. Endereço Ip Multicast ('multicastIP'): Utilizado somente pelo processo Servidor para o envio e recebimento de mensagens via Multicast. Definido atualmente como *230.0.0.19* ;

2. Número de porta para Multicast ('multicastPort'): Também presesnte somente na configuração do processo Servidor para a passagem de informações via Sockets. Definido atualmente como *9819* ;

3. Número de porta para o Registro RMI ('registryPort'): Utilizado pelo processo gerenciador do Registro para inicializá-lo dentro dessa porta predefinida e pelos demais processo, Clientes e Servidor, para acessar o Registro criado. Atualmente definido como *9919* .

## Descrição individual de cada Módulo e decisões de Design

Esse tópico detalha as organizações internas de cada um dos módulos definidos anteriormente e trabalha algumas decisões de Design adotadas durante a elaboração desse projeto.

A estruturação geral desse projeto busca a definição de programas, que, embora necessitam ser rodados em conjunto para a execução de fato do objetivo proposto, possuam suas separações bem definidas de acordo com as funções atribuídas a cada um deles.  

### Classes compartilhadas

Neste tópico, temos as classes que funcionam como interfaces a serem utilizadas para comunicação entre diferentes tipos de processos ou que encapsulam métodos e buscam fornecer funcionalidades que podem ser aplicadas e reaproveitadas em diversas etapas do projeto. Todas as classe definidas aqui devem estar presentes para a execução de cada um dos módulos do programa.

#### Message.java

Essa classe, busca simplesmente agrupar os campos que devem estar presentes em uma mensagem a ser enviada e propagada dentro desse Chat, agrupando-os dentro da estrutura de um Objeto. Além de definir um construtor padrão para a classe, possui também os seguintes campos definidos:

* Username: String contendo a identificação de quem enviou;

* Body: String contendo o conteúdo (corpo) da mensagem;

* SendDate: Variável do tipo Date, inicializado no momento de envio da mensagem;

* receiveDate: Variável do tipo Date, atribuída no momento de recebimento da mensagem pelo cliente final;  

* isSystemMessage: Variável do tipo Booleano que identifica se a mensagem se trata ou não de uma definida e enviada por um Servidor gerenciador do Chat.

#### ServerInterface.java

Define as assinaturas dos métodos a serem oferecidos pela parte *Servidor RMI* dentro do processo de Servidor do Chat. Essa interface deve ser compilada e estar presente na execução de todos os processos que se utilizarem da comunicação por RMI com esse processo ou que interajam com o Registo local RMI. 

Possibilita a chamada das seguintes funções via comunicação RMI:

* sendMessage: Recebe um objeto do tipo **Mensagem** ;

* logIn: Recebe o nome do usuário que entrou na sala e informações de data e horário relativos a esse acontecimento. Retorna um inteiro identificador do usuário que está se conectando.

* logOff: Recebe os mesmo parâmetros do método anterior, nome de usuário e data/horário do momento de chamada da função. Não retorna informação.

#### ClientInterface.java

Semelhante ao anterior, porém define agora as assinaturas dos métodos a serem oferecidos pela parte *Servidor RMI* dentro do processo de Cliente do Chat. Novamente, essa interface deve ser compilada e estar presente na execução de todos os processos que se utilizarem da comunicação por RMI com o processo Cliente ou que interajam com o Registo local RMI. 

Possibilita a chamada somente de uma única função via comunicação RMI com esse módulo:

* printMessage: Recebe um objeto do tipo **Mensagem** como parâmetro de entrada.

#### GetConfig.java

Essa classe, encapsula funcionalidades de abertura e leitura de arquivos de configuração como os definidos anteriormente ( do tipo *.properties*), oferecendo abstrações e métodos que facilitam o acesso e a obtenção de determinados valores presentes neles. Ela foi desenvolvida buscando generalizar operações de acessos a arquivos externos que estavam presente de forma semelhante em todos os processos principais do projeto.

Seu construtor recebe como parâmetro o nome do arquivo a ser procurado dentro dos ClassPath definidos em tempo de execução, buscando salvar dentro de uma variável interna do objeto de tipo Properties as informações encontradas dentro dele. Essas informações podem então ser exibidas através da chamada de um método GetProperties, que recebe uma string contendo a chave identificadora a qual se deseja consultar e retorna o seu valor associado encontrado no arquivo. 

### GroupChatRMI

O mais simples dos módulos, foi desenvolvido visando separar a execução e inicialização do RMI Registry local, necessário a execução do programa, dos demais componentes. Apesar do mesmo poder ser gerado dentro do mesmo processo principal do Servidor, até porque este registro deve estar rodando para que este possa ser executado corretamente, optou-se por instanciá-lo separadamente, de forma a deixar claro que trata-se de um serviço separado e que pode funcionar sem a existência de um servidor para outras aplicações. 

Ao mesmo tempo, ao invés de simplesmente utilizar a chamada de estabelecimento do registro através da chamada de execução via prompt *rmiregistry portNumber*, buscou-se utilizar dos recursos já desenvolvidos em Java para leitura de arquivos de configuração. Além disso, se adicionou uma funcionalidade de um **ShutdownHook** a ser executado no momento de encerramento dessa aplicação, garantindo a interrupção correta da sua execução em caso de encerramento do programa, o que inclui a realização de um unexport do objeto remoto de Registro e encerramento do loop de execução que mantinha o Registro funcionando e acessível as demais aplicações.

#### RunRegistry.java

Como descrito, essa classe possui apenas a leitura do arquivo de configuração, a inicialização do registro e a manuntenção de um loop que o mantenha aberto até que uma interrupção o encerre, além dos devidos tratamentos de erros e mensagens de comunicação. Necessita ter acesso as interfaces a serem exportadas no registro e pode ser corretamente encerrada no prompt através do comando **Ctrl + c**

### GroupChatServer

Passando agora de fato para as implementações principais do projeto, temos nesse tópico as classes relativas a parte Servidor do serviço de Chat em grupo. Esse módulo se utiliza tanto da comunicação via Sockets Multicast, para propagar informações para e receber dados de servidores que estão executando em diversas máquinas diferentes, como também do middleware RMI para realizar a troca de mensagens com  os clientes que estão em execução dentro da mesma máquina. Somente uma instância desse módulo deve estar ativa ao mesmo tempo dentro de uma mesma máquina.

Essa aplicação é divida em duas classes diferentes: uma responsável por atuar como a parte *servidor* de mensagens vindas através do  socket Multicast e *cliente* via RMI, e a outra como *servidor* via chamadas RMI vinda de Clientes e *cliente* no envio de mensagens via canais Multicast.

#### ChatServer.java

Essa classe é responsável por implementar a InterfaceServer definida anteriormente, oferecendo implementações para os métodos fornecidos por esta via chamadas RMI, além de executar o envio de mensagens via Socket Multicast. Possui como variáveis internas um valor de IP Multicast, um Socket utilizado para comunicação e o número de porta onde está inicializado o RegistroRMI.

Essa classe implementa uma nova função conhecida como BroadcastMessage, que recebe um objeto do tipo Mensagem e simplesmente o encaminha para todos os processos que estão escutando no endereço definido pelo IP e pela porta passados como parâmetro no momento de criação e exportação da classe remota. Foi definido um valor máximo de tentativas, para tentar novamente no caso de ocorrer alguma exceção no momento de envio dessa mensagem.

Além disso, essa classe implementa todas as funções definidas anteriormente pela interface. Para o caso de sendMessage, ela simplesmente executa o broadcast da mensagem. Já no caso de logOff, ela define uma mensagem padrão de saída do sistema a ser enviada aos demais participantes. O mesmo ocorre no caso de logIn, só que com uma mensagem de entrada. Além disso, nessa função optou-se por retornar um inteiro identificador diretamente ao cliente como parâmetro de retorno, para que esse possa por sua vez realizar o seu registro dentro do RMIRegistry local com um endereço único, composto do prefixo "Client" + esse valor de retorno. Para garantir que esse valor seja único, o servidor obtém a lista de todos os participantes presentes nesse registro e verifica o menor número ainda não utilizado.

É utilizada como base para a geração de Server Stubs e a cada execução de um desses métodos remotos, uma mensagem de log é gerada no prompt para acompanhamento das requisições.

#### RunServer.java

Esse código representa a classe principal, com o método main, para execução dos Servidores desse serviço de Chat criado, sendo responsável pela inicialização das conexões e dos objetos remotos, além de atuar como servidor recebedor de mensagens via Socket e cliente de chamadas RMI para os Clientes do chat.

Ela se inicia utilizando-se das funções padrões de leituras do arquivo de configuração, com seus devidos tratamentos de erros. Em seguida, ela executa uma conexão ao endereço de Multicast definido nos parâmetros, definindo um objeto do tipo Socket e se juntando ao grupo que está se utilizando desse serviço. 

Para encerrar a etapa de inicialização, a aplicação verifica a existência do registro RMI na porta passada, define um objeto do tipo *ChatServer*, passando os parâmetros necessários e configurados do arquivo extra de properties, e o exporta sobre o endereço adequadamente e explicitamente definido para evitar erros durante a execução. Todas essas etapas são acompanhadas por usas próprias mensagens de erro e verificações particuçares.

Além disso, é definido um ShutdownHook próprio a ser executado no momento de interrupção desse Servidor. Essa rotina, irá encerrar corretamente a participação desse processo no grupo referente ao Socket Multicast definido, irá remover o objeto remoto exportado do registro de endereços RMI local e por último notificar o Status de saída. Novamente, o processo pode ser corretamente encerrada no prompt através do comando **Ctrl + c**.

Por último a rotina principal do programa, irá executar um loop indefinidamente, escutando e esperando receber novas mensagens via a conexão criada através do Socket anteriormente, devendo no caso do recebimento, extrair os bytes recebidos por essa conexão. Ela deve então tentar interpretar esses dados recebidos como um objeto do tipo Mensagem, devendo gerar uma mensagem de erro no caso de não conseguir. Caso seja bem sucedida, ela inicia então uma nova Thread que deve se responsabilizar pelo envio dessa mensagem para os processos Clientes, passando essa mensagem recebida a ela e voltando a escutar, aguardando novas mensagens.

Essas Threads são definidas dentro de uma classe interna denominada ListenThread, que possuem em seu método principal run(), a ser executado paralelamente, o objetivo de encaminhar uma mensagem a todos os Clientes do Chat em grupo registrados naquela máquina. Para isso, essa função recupera todos os valores presentes no Registro RMI local, e para cada entrada que possua o identificador Cliente, ele recupera seu endereço exportado. Com esse endereço conhecido, ele pode então importar uma referência ao objeto remoto que atua como *servidor RMI do cliente* e realiza a chamada do seu método printMessage, passando o objeto Mensagem recebido como parâmetro. Esse processo é realizado para todos os Clientes encontrados no registro, com o devido número máximo de tentativas e tratamentos de erros. Todas as Threads são devidamente encerradas ao fim de sua execução.

### GroupChatClient

Esse módulo do Cliente desse serviço de Chat em Grupo, possui uma arquitetura geral similar a definida para um Servidor, com uma classe representando a parte que atua como *servidor RMI*, implementando uma interface a agindo como um objeto remoto e a outra atuando como a parte principal da inicialização, inclusive contendo o método main a ser iniciado e parte *cliente do RMI*. 

Porém, ao contrário do caso anterior, esse Cliente só se utiliza da comunicação via RMI e interage somente com o servidor local, que esteja presente dentro do mesmo servidor de resgistros locais. Além disso, um total de n diferentes instâncias desse processo podem estar em execução ao mesmo tempo em uma mesma máquina.

#### ChatClient.java

Essa é a classe do Cliente responsável que implementa a interface Client definida anteriormente, e, que portanto, oferece uma implementação para o assinatura de método definido dentro da mesma.

Nesse caso, como variável interna ao objeto, ela possui uma lista de objetos do tipo Mensagem e que se trata de um ArrayList sincronizado, ou seja, que é compartilhada por todas as instâncias de threads que tentam acessar o objeto exportado, as quais são geradas por diferentes chamadas RMI ao método de um mesmo objeto, e que deve ter sua operações de acesso sincronizadas, garatindo a execução correta de operações pontuais sobre ela mesmo em chamadas que operam de forma paralela. Essa lista é utilizada para armazenar Mensagens recebidas durante a execução do método remoto.

Na execução do método de printMessage, é passado como parâmetro de entrada um objeto do tipo Mensagem que deve ter suas informações impressas na prompt de comando do usuário onde o processo Cliente está executando. Para isso, o valor do campo receiveDate é atribuído a mensagem no momento de seu recebimento pela chamada e em seguida essa mensagem é adicionada a lista de Mensagens internas desse objeto. A rotina que está a executar essa função para sua execução então por um intervalo de tempo de algun milisegundos, definido arbitrariamente. 

Ao fim desse período, é realizado uma ordenação sobre a lista de mensagens disponíveis, através de um Comparator definido internamente na função que ordena as mensagens de forma crescente a partir da sua data de envio. Esse sort é executado dentro uma sincronização externa da lista, visando garantir que não ocorra problemas com outras rotinas em execução, já que essas operações podem vir a causar problemas de paralelismo. O elemento mais antigo é então extraído da lista, sua mensagem é formatada, juntando seus campos em uma única String que é então exibida na tela.

As mensagens são exibidas da seguinte forma:
  \[Data de Envio - Data de Chegada\] Nome de usuário: Corpo da mensagem
**Obs**: No caso de mensagens do sistem o nome de usuário é substituído por *Server Message* e o corpo trata-se de uma mensagem predefinida.
**Obs2**: Para facilitar exibição e comparação, todas as datas são formatadas da seguinte forma: dd/MM/yyyy HH:mm:ss.SSS

A utilização dessa lista sincronizada e do seu processo de ordenação dentro desse método é uma tentativa de implementar uma solução para a **questão bônus 1** definida no enunciado da lista. Para isso, busca-se aguardar um tempo para que mensagens atrasadas possam ter tempo de chegar e ser inseridas na lista, garantingo sempre que na hora da impressão final do conteúdo, a mensagem a ser impressa seja aquela que tenha sido enviada a mais tempo. Dessa forma, espera-se que, supondo que todas as mensagens consigam chegar dentro desse período de tempo determinado, a ordem de exibição seria a mesma para qualquer cliente. Além disso, se pudessemos garantir que todos os relógios estejam corretamente sincronizados, todas as mensagens seriam exibidas na ordem de correta de envio.

#### RunClient.java

Por último, temos a classe principal, contendo o método main, responsável pela instanciação e inicialização de um Cliente dentro desse serviço de Chat. Novamente, é essa classe a responsável pela leitura do arquivo de configuração e pela inicialização de um objeto remoto a ser exportado, além de atuar como *cliente RMI* e realizar a leitura inicial das mensagens enviadas pelo usuário via linha de comando. 

Inicialmente, o processo executa a leitura do arquivo de configuração através da classe GetConfig definida anteriormente. Uma vez que os dados necessários sejam obtidos do mesmo, nesse caso somente o número da porta onde se encontra o Registro local, é iniciado então o processo para conexão. Primeiro verifica-se se o mesmo encontra-se disponível e em execução na porta recebida, e, caso esteja, em seguida é realizada a consulta em busca do endereço padrão onde o servidor local do Chat deve ser encontrado. Novamente optou-se por passar a referência completa explicitamente do endereço a ser consultado no registro. Caso algum deles não seja encontrado, é gerada a mensagem de erro correspondente.

O programa então requisita que o usuário entre com um nome de usuário, o qual não necessita ser único e diferente dos demais, e aguarda que o mesmo entre com essa informação via linha de comando. Em seguida, com a referência ao servidor obtida anteriormente, é chamada o método de logIn oferecido por esse objeto remoto, passando-se o nome de usuário recebido e a data e hora local. Esse método por sua vez retorna um inteiro utilizado como identificador para que essa instância do cliente possa se registrar no servidor RMI local, utilizando uma referência composta de "Client" + id. Uma vez que essa referência é obtida, é então utilizada em conjunto com o número da porta para montar o endereço completo e exportar um objeto remoto do tipo ChatClient, definido acima e que será chamado por um Servidor.

Terminada essa etapa de iniciação, o programa avisa ao usuário que o mesmo está devidamente conectado ao chat, estando pronto a receber mensagens vindas de outros participantes, já que seu objeto remoto que irá ser responsável pela impressão das mesmas na tela já está devidamente registrada e exportada remotamente. Esse processo principal então entra em um loop indefinido, aguardando que o usuário entre com uma mensagem de texto. 

Ao ser lido essa mensagem, o program imprime um comando para remover a última linha impressa, buscando remover o input do usuário do texto impresso na tela e fazendo com que o mesmo tenha que percorrer o mesmo caminho das demais mensagens. Esse caminho foi escolhido para buscar com que todas as mensagens sejam recebidas na mesma ordem por todos os participantes do grupo e para facilitar a exibição de todas elas sobre a mesma formatação, inclusive com exibição de informações de datas/horas e nomes de usuário. Cada linha lida é então transformada em um objeto mensagem, com o nome de usuário definido na inicialização e a data local no momento de envio da mesma. Essa mensagem é enviada então via RMI, através da função sendMessage, para o Servidor local, que se encarrega de redistribuí-la para os demais clientes e servidores.

Esse loop do Cliente permanece até que a mensagem de encerramento, definida como '/exit' e informada ao cliente no início do chat, é lida como entrada. No momento que isso ocorre é encerrada então a execução desse loop de leitura, o objeto remoto referente a parte *servidor RMI* do Cliente, responsável pela exibição de mensagens é então removida do registro, deixando de ser exportado o objeto remoto e removendo o endereço associado ao mesmo. Por último a função de logOff, disponível no ChatServer é chamada via objeto remoto, enviando uma mensagem de saída aos demais participantes. 


