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
  
  No Linux execute no Bash: "sudo apt install openjdk-17-jdk";
                                              
  No Windows, baixe por meio do site oficial da Oracle: https://www.oracle.com/java/technologies/downloads/;

  E verifique a instalação com os seguintes comandos: 
  *  "java -version"
  *  "javac -version"

#### Rodando o programa:

  Vá para o local em que está armazenado o arquivo e execute os seguintes comandos:
  
   - "javac SistemaDeArquivos.java"
   - "java SistemaDeArquivos"

## Inicando o simulador:

Ao iniciar o programa, será pedido um valor referente a quantidade de blocos (equivalente ao tamanho do disco).

Então surgira 3 opções de método de alocação:

* Alocação Contigua           --> Onde o arquivo ocupara os blocos de forma alinhada adjecentemente, só podendo serem armazenados sequencialmente;
* Alocação Encadeada          --> Onde cada bloco aponta para o próximo bloco correspondente a continuidade do arquivo, podendo serem armazenados de forma dispersa pelo drive;
* Alocação Indexada (i-nodes) --> Onde há um bloco por arquivo que armazena os enderenços correspondete aos blocos integrantes do arquivo, podendo serem armazenados de forma dispersa pelo disco;

Após a seleção, surgira o Menu Principal:

--- MENU PRINCIPAL ---

1. Criar arquivo        --> Cria um arquivo(com nome e tamanho) 
2. Estender arquivo     --> Aumenta o tamanho de um arquivo já existente
3. Deletar arquivo      --> Libera espaço em disco ("removendo" o arquivo)
4. Ler arquivo          --> Mostra o tempo para se ler o arquivo (seja sequencial ou aleatório)
5. Visualizar disco     --> Visualiza o disco (dividido em blocos)
6. Ver metricas         --> Mostra informações sobre o disco (fragmentação interna e externa e a porcentagem de ocupação do disco)
7. Ver diretorio        --> Mostra os nomes do arquivos presentes no disco
8. Reconfigurar sistema --> Reinicia o sistema (podendo reconfigurar o tamanho de disco e o método de alocação)
9. Sair                 --> Fecha o programa

A partir desta modesta ferramenta é possível trabalhar e desenvolver conceitos básicos de sistema de arquivos. 
