# SIMULADOR de Sistema de Arquivos

## Descrição:

Esta aplicativo tendo por objetivo simular um Sistema de Arquivos da perspectiva e um Sistema Operacinal,
assim evidênciando os conceitos de Sistemas Operacionais aboraddos em aula e que forneça uma expereiência simples 
e interativa. Ele foi programado na linguagem de programação Java e desenvolvido na IDE NetBeans, sua interface é apresentada
via o terminal de execução.

## Sobre a execução do simulador:

Basta baixar o source code e executalo na IDE de sua preferência ou diretamente pelo seu terminal.

### Execução pelo Terminal passo a passo:

#### Pré-requisitos:

  Ter instalado o JDK(Java Development Kit)
  
  Caso não tenha siga os passos abaixo:
    No Linux execute no Bash: "sudo apt install openjdk-17-jdk"
    E verifique a instalação com os comandos: "java -version"
                                              "javac -version"
    Já no Windows, baixe por meio do site oficial da Oracle: "https://www.oracle.com/java/technologies/downloads/"

#### Rodando o programa:

  Vá para o local em que está o arquivo e execute os seguintes comandos:
  
   - "javac SistemaDeArquivos.java"
   - "java SistemaDeArquivos"

## Inicando o simulador:

Ao iniciar o programa, será pedido um valor referente a quantidade de blocos (equivalente ao tamanho do disco),
então surgira 3 opções de método de alocação:

* Alocação Contigua           --> Onde o arquivo ocupara os blocos de forma alinhada adjecentemente, só podendo serem armazenados sequencialmente;
* Alocação Encadeada          --> Onde cada bloco aponta para o próximo bloco correspondente a continuidade do arquivo, podendo serem armazenados de forma dispersa pelo drive;
* Alocação Indexada (i-nodes) --> Onde há um bloco por arquivo que armazena os enderenços correspondete aos blocos integrantes do arquivo, podendo serem armazenados de forma dispersa pelo disco;

Então surgira o Menu Principal, com 9 opções:

1. Criar arquivo
2. Estender arquivo
3. Deletar arquivo
4. Ler arquivo
5. Visualizar disco
6. Ver metricas
7. Ver diretorio
8. Reconfigurar sistema
9. Sair
