# Quake 3 Arena Analyzer

Esse projeto analisa arquivos de logs do jogo Quake 3 Arena. Através de uma api desenvolvida em java com spring boot, um parser dos arquivos de logs do jogo desenvolvido utilizando scala que alimenta a api e uma tela desenvolvida com angular que consome a api e exibe as informações.

## Rodando o projeto

O projeto executa a api e o front-end em imagens Docker, por isso é necessário que ele esteja instalado na sua máquina localmente. Já o parser é executando localmente é necessário que também tenha instalado o Java 8 e o Maven.

Resumindo os requisitos:
* Docker
* Java 8
* Scala 2.11.12
* Maven

Para facilitar a execução do projeto existe um script na raiz do projeto chamado `start.sh` que basta ser executado passando como parâmetro o diretório que contém os arquivos de logs que devem ser executado, como por exemplo:

`$ ./start.sh minha-maquina/arquivos/arena3/logs`

Como exemplo existe um arquivo dentro da pastas `/logs` e caso queira usá-lo basta executar no seu terminal:

`$ ./start.sh $(pwd)/logs`

Para os arquivos serem logados eles precisam ter a extensão `.log` e o parser renomeia os arquivos logados, fique atento nos arquivos.

Caso queira também é possível rodar cada aplicação individualmente, basta entrar na pasta da aplicação que queira executar. As instruçãoes para isso ainda estão sendo descritas, mas o arquivo `start.sh` é um bom caminho.

## Utilizando o projeto

Após executar todos os projetos a api estará disponível em `localhost:8080` e o web estará disponível em `localhost:4200` 
