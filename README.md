# GroupChatMessenger
**by Felipe Pestana** (usuário asd19)

Documentação criada para dar suporte ao projeto desenvolvido como parte da Lista 1 da disciplina de Sistemas Distribuídos para 2019.1.
Esse arquivo se encontra disponível em duas versões diferentes, uma definida em Markdown e a outra em Html, a ser utilizada de acordo com a preferência do usuário.

## Sumário do Projeto
Esse projeto consiste de um sistema de mensagens curtas visando estabelecer um chat em grupo utilizando comunicações via RMI e sockets Multicast como meios intermediários de comunicação. A arquitetura do programa foi desenvolvido de acordo com as especificações definidas e se encontra dividida principalmente em três módulos de processos diferentes a serem executados:

* Um cliente, presente dentro do diretório GroupChatClient;
* Um servidor, presente dentro do diretório GroupChatServer;
* Um classe para gerência do Registro RMI necessário, presente dentro do diretório GroupCharRMI.
 
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

1. Endereço Ip Multicast: Utilizado somente pelo processo Servidor para o envio e recebimento de mensagens via Multicast. Definido atualmente como *230.0.0.19* ;

2. Número de porta para Multicast: Também presesnte somente na configuração do processo Servidor para a passagem de informações via Sockets. Definido atualmente como *9819* ;

3. Número de porta para o Registro RMI: Utilizado pelo processo gerenciador do Registro para inicializá-lo dentro dessa porta predefinida e pelos demais processo, Clientes e Servidor, para acessar o Registro criado. Atualmente definido como *9919* .

## Descrição individual de cada Módulo e decisões de Design

Esse tópico
### RMIRegistryManager
### Servidor
### Cliente
