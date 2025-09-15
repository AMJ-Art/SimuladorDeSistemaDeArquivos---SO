
package sistemadearquivos;
import java.util.*;
import java.util.concurrent.TimeUnit;

// Classe principal que contem o método main e fluxo do programa
public class SistemaDeArquivos {
    
    private static Scanner input = new Scanner(System.in);
    private static SA SA;
    
    public static void main(String[] args) {
        
        System.out.println("=== SIMULADOR DE SISTEMAS DE ARQUIVOS ===");
        boolean run = true;
        
        while (run) {
            
            if (SA == null) {
                metodoDeAlocacao();
            } else {
                menuPrincipal();
                int opcao = getIntInput("Escolha uma opcao: ");
                
                switch (opcao) {
                    case 1 -> criarArquivo();
                    case 2 -> extenderArquivo();
                    case 3 -> deletarArquivo();
                    case 4 -> lerArquivo();
                    case 5 -> mostrarDisco();
                    case 6 -> mostrarMetricas();
                    case 7 -> mostrarDiretorio();
                    case 8 -> SA = null; // Reinicia o programa
                    case 9 -> {
                        run = false;
                        System.out.println("Encerrando simulador...");
                    }
                    default -> System.out.println("Opcao invalida!");
                }
            }
        }
    }
    
    private static void metodoDeAlocacao() {
        System.out.println("\n--- CONFIGURACAO DO SISTEMA DE ARQUIVOS ---");
        
        int tamanhoDisco = getIntInput("Tamanho do disco (em blocos): ");
        System.out.println("\nMetodos de alocacao disponiveis:");
        System.out.println("1. Contigua");
        System.out.println("2. Encadeada");
        System.out.println("3. Indexada (i-nodes)");
        
        int metodo = getIntInput("Escolha o metodo de alocacao: ");
        
        switch (metodo) {

            case 1 -> SA = new mContiguo(tamanhoDisco);
            case 2 -> SA = new mEncadeado(tamanhoDisco);
            case 3 -> SA = new mIndexado(tamanhoDisco);
            default -> {
                System.out.println("Opcao invalida! Usando alocacao contigua como padrao.");
                SA = new mContiguo(tamanhoDisco);
                
            }
        }
        System.out.println("Sistema de arquivos configurado com sucesso!");
    }
    
    private static void menuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Criar arquivo");
        System.out.println("2. Estender arquivo");
        System.out.println("3. Deletar arquivo");
        System.out.println("4. Ler arquivo");
        System.out.println("5. Visualizar disco");
        System.out.println("6. Ver metricas");
        System.out.println("7. Ver diretorio");
        System.out.println("8. Reconfigurar sistema");
        System.out.println("9. Sair");
    }
    
    private static void criarArquivo() {
        
        String nomeArquivo = getStringInput("Nome do arquivo: ");
        int size = getIntInput("Tamanho (em blocos): ");
        SA.criarArquivo(nomeArquivo, size);
    }
    
    private static void extenderArquivo() {
        
        String nomeArquivo = getStringInput("Nome do arquivo: ");
        int additionalSize = getIntInput("Blocos adicionais: ");
        SA.extenderArquivo(nomeArquivo, additionalSize);
    }
    
    private static void deletarArquivo() {
        
        String nomeArquivo = getStringInput("Nome do arquivo: ");
        SA.deletarArquivo(nomeArquivo);
    }
    
    private static void lerArquivo() {
        
        String nomeArquivo = getStringInput("Nome do arquivo: ");
        System.out.println("Modo de leitura:");
        System.out.println("1. Sequencial");
        System.out.println("2. Aleatorio");
        int opcao = getIntInput("Escolha: ");
        
        boolean sequencial = (opcao == 1);
        SA.lerArquivo(nomeArquivo, sequencial);
    }
    
    private static void mostrarDisco() {
        SA.mostrarDisco();
    }
    
    private static void mostrarMetricas() {
        SA.mostrarMetricas();
    }
    
    private static void mostrarDiretorio() {
        SA.mostrarDiretorio();
    }
    
    private static int getIntInput(String entrada) {
        
        System.out.print(entrada);
        while (!input.hasNextInt()) {
            System.out.print("Por favor, digite um numero: ");
            input.next();
        }
        return input.nextInt();
    }
    
    private static String getStringInput(String entrada) {
        
        System.out.print(entrada);
        input.nextLine(); // Limpar buffer
        return input.nextLine();
    }
}

// Interface que define os métodos comuns para todos os sistemas de arquivos
interface SA { //(Sistema de Arquivos)
    void criarArquivo(String fileName, int size);
    void extenderArquivo(String fileName, int additionalSize);
    void deletarArquivo(String fileName);
    void lerArquivo(String fileName, boolean sequential);
    void mostrarDisco();
    void mostrarMetricas();
    void mostrarDiretorio();
}

// Classe abstrata com funcionalidades comuns
abstract class SAIndex implements SA {
    
    protected int tamanhoDisco;
    protected int[] disco;
    protected Map<String, AEntry> diretorio;
    protected int fragmentacaoInterna;
    protected int fragmentacaoExterna;
    
    public SAIndex(int tamanhoDisco) {
        
        this.tamanhoDisco = tamanhoDisco;
        this.disco = new int[tamanhoDisco];
        this.diretorio = new HashMap<>();
        Arrays.fill(disco, 0); // 0 significa bloco livre
    }
    
    @Override
    public void mostrarMetricas() {
        
        System.out.println("\n--- METRICAS DO SISTEMA ---");
        System.out.println("Fragmentacao interna: " + fragmentacaoInterna + " blocos");
        System.out.println("Fragmentacao externa: " + fragmentacaoExterna + " blocos");
        
        int blocosUsados = 0;
        for (int bloco : disco) {
            if (bloco != 0) blocosUsados++;
        }
        
        System.out.println("Blocos utilizados: " + blocosUsados + "/" + tamanhoDisco);
        System.out.println("Taxa de utilizacao: " + (blocosUsados * 100 / tamanhoDisco) + "%");
    }
    
    @Override
    public void mostrarDiretorio() {
        
        System.out.println("\n--- DIRETORIO DE ARQUIVOS ---");
        if (diretorio.isEmpty()) {
            System.out.println("Nenhum arquivo no sistema.");
            return;
        }
        
        for (Map.Entry<String, AEntry> entry : diretorio.entrySet()) {
            AEntry file = entry.getValue();
            System.out.println("Nome: " + entry.getKey() + 
                             " | Tamanho: " + file.tamanho + " blocos" +
                             " | Blocos: " + file.blocos.toString());
        }
    }
    
    protected void simulateReadAccess(List<Integer> blocos, boolean sequencial) {
        
        System.out.println("Simulando acesso " + (sequencial ? "sequencial" : "aleatorio") + "...");
        
        long startTime = System.nanoTime();
        
        if (sequencial) {
            // Acesso sequencial (mais rápido)
            for (int bloco : blocos) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10); // Simula tempo de acesso
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            // Acesso aleatório (mais lento)
            List<Integer> blocosAleatorios = new ArrayList<>(blocos);
            Collections.shuffle(blocosAleatorios);
            
            for (int bloco : blocosAleatorios) {
                try {
                    TimeUnit.MILLISECONDS.sleep(50); // Tempo maior para acesso aleatório
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        long endTime = System.nanoTime();
        long duracao = (endTime - startTime) / 1000000; // Converter para milissegundos
        
        System.out.println("Tempo de acesso: " + duracao + " ms");
    }
    
    protected int AcharBlocosLivres(int input) {
        int count = 0;
        for (int i = 0; i < tamanhoDisco; i++) {
            if (disco[i] == 0) {
                count++;
                if (count == input) {
                    return i - input + 1;
                }
            } else {
                count = 0;
            }
        }
        return -1;
    }
    
    protected int contarBlocosLivres() {
        
        int count = 0;
        for (int i = 0; i < tamanhoDisco; i++) {
            if (disco[i] == 0) count++;
        }
        return count;
    }
    
    protected void calcularFragmentacao() {
        
        // Cálculo da fragmentação
        fragmentacaoInterna = 0;
        fragmentacaoExterna = 0;
        
        // Verifica fragmentação externa (espaço livre fragmentado)
        boolean blocoLivreAchado = false;
        int countBlocosLivres = 0;
        for (int i = 0; i < tamanhoDisco; i++) {
            if (disco[i] == 0) {
                countBlocosLivres++;
                blocoLivreAchado = true;
            } else if (blocoLivreAchado) {
                fragmentacaoExterna += countBlocosLivres;
                countBlocosLivres = 0;
                blocoLivreAchado = false;
            }
        }
        if (blocoLivreAchado) {
            fragmentacaoExterna += countBlocosLivres;
        }
    }
}

// Classe para representar uma entrada de arquivo
class AEntry {
    String nome;
    int tamanho;
    List<Integer> blocos;
    
    public AEntry(String nome, int tamanho, List<Integer> blocos) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.blocos = blocos;
    }
}

// Implementação do sistema de arquivos com alocação contígua
class mContiguo extends SAIndex {
    
    public mContiguo(int tamanhoDisco) {
        super(tamanhoDisco);
    }
    
    @Override
    public void criarArquivo(String nomeArquivo, int tamanho) {
        if (diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo ja existe!");
            return;
        }
        
        if (tamanho > contarBlocosLivres()) {
            System.out.println("Erro: Espaco insuficiente no disco!");
            return;
        }
        
        int primeiroBloco = AcharBlocosLivres(tamanho);
        if (primeiroBloco == -1) {
            System.out.println("Erro: Nao ha espaco contiguo suficiente!");
            return;
        }
        
        // Alocar blocos
        List<Integer> blocos = new ArrayList<>();
        for (int i = primeiroBloco; i < primeiroBloco + tamanho; i++) {
            disco[i] = 1; // Marcar como ocupado
            blocos.add(i);
        }
        
        diretorio.put(nomeArquivo, new AEntry(nomeArquivo, tamanho, blocos));
        calcularFragmentacao();
        System.out.println("Arquivo '" + nomeArquivo + "' criado com sucesso!");
    }
    
    @Override
    public void extenderArquivo(String nomeArquivo, int tamanhoExtra) {
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        //block
        AEntry arquivo = diretorio.get(nomeArquivo);
        int ultimoBloco = arquivo.blocos.get(arquivo.blocos.size() - 1);
        
        // Verificar se há espaço contíguo após o último bloco
        if (ultimoBloco + 1 < tamanhoDisco && disco[ultimoBloco + 1] == 0) {
            int espacoPossivel = 0;
            for (int i = ultimoBloco + 1; i < tamanhoDisco && disco[i] == 0; i++) {
                espacoPossivel++;
            }
            
            int extencao = Math.min(tamanhoExtra, espacoPossivel);
            
            // Estender o arquivo
            for (int i = ultimoBloco + 1; i <= ultimoBloco + extencao; i++) {
                disco[i] = 1;
                arquivo.blocos.add(i);
            }
            
            arquivo.tamanho += extencao;
            diretorio.put(nomeArquivo, arquivo);
            calcularFragmentacao();
            
            if (extencao < tamanhoExtra) {
                System.out.println("Arquivo estendido parcialmente. Apenas " + extencao + 
                                 " blocos adicionados devido a falta de espaco contiguo.");
            } else {
                System.out.println("Arquivo estendido com sucesso!");
            }
        } else {
            System.out.println("Erro: Nao eh possivel estender - nao ha espaco contiguo disponivel.");
        }
    }
    
    @Override
    public void deletarArquivo(String nomeArquivo) {
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        AEntry file = diretorio.get(nomeArquivo);
        for (int bloco : file.blocos) {
            disco[bloco] = 0; // Liberar bloco
        }
        
        diretorio.remove(nomeArquivo);
        calcularFragmentacao();
        System.out.println("Arquivo '" + nomeArquivo + "' deletado com sucesso!");
    }
    
    @Override
    public void lerArquivo(String nomeArquivo, boolean sequencial) {
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        AEntry arquivo = diretorio.get(nomeArquivo);
        simulateReadAccess(arquivo.blocos, sequencial);
    }
    
    @Override
    public void mostrarDisco() {
        System.out.println("\n--- VISUALIZACAO DO DISCO (Alocacao Contigua) ---");
        
        for (int i = 0; i < tamanhoDisco; i++) {
            if (i % 20 == 0) System.out.println();
            
            if (disco[i] == 0) {
                System.out.print("[ ]"); // Bloco livre
            } else {
                // Encontrar qual arquivo ocupa este bloco
                String dono = "?";
                for (Map.Entry<String, AEntry> entry : diretorio.entrySet()) {
                    if (entry.getValue().blocos.contains(i)) {
                        dono = entry.getKey().substring(0, 1).toUpperCase();
                        break;
                    }
                }
                System.out.print("[" + dono + "]"); // Bloco ocupado
            }
        }
        System.out.println("\n");
    }
}

// Implementação do sistema de arquivos com alocação encadeada
class mEncadeado extends SAIndex {
    private int[] nextPointers; // Armazena o próximo bloco para cada bloco
    
    public mEncadeado(int tamanhoDisco) {
        super(tamanhoDisco);
        this.nextPointers = new int[tamanhoDisco];
        Arrays.fill(nextPointers, -1); // -1 significa fim da cadeia
    }
    
    @Override
    public void criarArquivo(String nomeArquivo, int tamanho) {
        if (diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo ja existe!");
            return;
        }
        
        if (tamanho > contarBlocosLivres()) {
            System.out.println("Erro: Espaco insuficiente no disco!");
            return;
        }
        
        List<Integer> blocosLivres = AcharBlocosLivresNaLista(tamanho);
        if (blocosLivres.size() < tamanho) {
            System.out.println("Erro: Nao ha blocos livres suficientes!");
            return;
        }
        
        // Alocar blocos
        List<Integer> blocos = new ArrayList<>();
        for (int i = 0; i < tamanho; i++) {
            int bloco = blocosLivres.get(i);
            disco[bloco] = 1; // Marcar como ocupado
            blocos.add(bloco);
            
            // Configurar ponteiro para o próximo bloco
            if (i < tamanho - 1) {
                nextPointers[bloco] = blocosLivres.get(i + 1);
            } else {
                nextPointers[bloco] = -1; // Fim da cadeia
            }
        }
        
        diretorio.put(nomeArquivo, new AEntry(nomeArquivo, tamanho, blocos));
        calcularFragmentacao();
        System.out.println("Arquivo '" + nomeArquivo + "' criado com sucesso!");
    }
    
    @Override
    public void extenderArquivo(String nomeArquivo, int tamanhoExtra) {
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        if (tamanhoExtra > contarBlocosLivres()) {
            System.out.println("Erro: Espaco insuficiente no disco!");
            return;
        }
        
        AEntry arq = diretorio.get(nomeArquivo);
        List<Integer> blocosLivres = AcharBlocosLivresNaLista(tamanhoExtra);
        
        if (blocosLivres.size() < tamanhoExtra) {
            System.out.println("Erro: Nao ha blocos livres suficientes!");
            return;
        }
        
        // Encontrar o último bloco do arquivo
        int ultimoBloco = arq.blocos.get(arq.blocos.size() - 1);
        
        // Adicionar novos blocos à cadeia
        for (int i = 0; i < tamanhoExtra; i++) {
            int bloco = blocosLivres.get(i);
            disco[bloco] = 1;
            arq.blocos.add(bloco);
            
            // Atualizar ponteiros
            if (i == 0) {
                nextPointers[ultimoBloco] = bloco; // Ligar ao último bloco existente
            }
            if (i < tamanhoExtra - 1) {
                nextPointers[bloco] = blocosLivres.get(i + 1);
            } else {
                nextPointers[bloco] = -1; // Fim da cadeia
            }
        }
        
        arq.tamanho += tamanhoExtra;
        diretorio.put(nomeArquivo, arq);
        calcularFragmentacao();
        System.out.println("Arquivo estendido com sucesso!");
    }
    
    @Override
    public void deletarArquivo(String nomeArquivo) {
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        AEntry file = diretorio.get(nomeArquivo);
        for (int bloco : file.blocos) {
            disco[bloco] = 0;           // Liberar bloco
            nextPointers[bloco] = -1;   // Remover ponteiro
        }
        
        diretorio.remove(nomeArquivo);
        calcularFragmentacao();
        System.out.println("Arquivo '" + nomeArquivo + "' deletado com sucesso!");
    }
    
    @Override
    public void lerArquivo(String nomeArquivo, boolean sequencial) {
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        AEntry arq = diretorio.get(nomeArquivo);
        
        // Para leitura sequencial, seguir a cadeia de ponteiros
        List<Integer> ordenacao;
        if (sequencial) {
            ordenacao = new ArrayList<>();
            int blocoAtual = arq.blocos.get(0);
            while (blocoAtual != -1) {
                ordenacao.add(blocoAtual);
                blocoAtual = nextPointers[blocoAtual];
            }
        } else {
            // Para leitura aleatória, usar a ordem original dos blocos
            ordenacao = new ArrayList<>(arq.blocos);
            Collections.shuffle(ordenacao);
        }
        
        simulateReadAccess(ordenacao, sequencial);
    }
    
    @Override
    public void mostrarDisco() {
        System.out.println("\n--- VISUALIZACAO DO DISCO (Alocacao Encadeada) ---");
        
        for (int i = 0; i < tamanhoDisco; i++) {
            if (i % 10 == 0) System.out.println();
            
            if (disco[i] == 0) {
                System.out.print("[ ]"); // Bloco livre
            } else {
                // Encontrar qual arquivo ocupa este bloco
                String dono = "?";
                for (Map.Entry<String, AEntry> entry : diretorio.entrySet()) {
                    if (entry.getValue().blocos.contains(i)) {
                        dono = entry.getKey().substring(0, 1).toUpperCase();
                        break;
                    }
                }
                System.out.print("[" + dono + "→" + 
                               (nextPointers[i] == -1 ? "X" : nextPointers[i]) + "]");
            }
        }
        System.out.println("\n");
    }
    
    private List<Integer> AcharBlocosLivresNaLista(int count) {
        List<Integer> blocosLivres = new ArrayList<>();
        for (int i = 0; i < tamanhoDisco && blocosLivres.size() < count; i++) {
            if (disco[i] == 0) {
                blocosLivres.add(i);
            }
        }
        return blocosLivres;
    }
}

// Implementação do sistema de arquivos com alocação indexada (i-nodes)
class mIndexado extends SAIndex {
    
    private Map<Integer, List<Integer>> blocosIndex;        // Mapeia blocos de indice para listas de blocos de dados
    private List<Integer> blocosIndexLivres; 
    
    public mIndexado(int tamanhoDisco) {
        super(tamanhoDisco);
        this.blocosIndex = new HashMap<>();
        this.blocosIndexLivres = new ArrayList<>();
        
        // Reservar os primeiros blocos para i-nodes
        int countIndexBlocos = Math.max(1, tamanhoDisco / 20); // 5% do disco para i-nodes
        for (int i = 0; i < countIndexBlocos; i++) {
            disco[i] = 2; // Marcar como bloco de índice
            blocosIndex.put(i, new ArrayList<>());
            blocosIndexLivres.add(i);
        }
    }
    
    @Override
    public void criarArquivo(String nomeArquivo, int tamanho) {
        
        if (diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo ja existe!");
            return;
        }
        
        if (tamanho > contarBlocosLivres()) {
            System.out.println("Erro: Espaco insuficiente no disco!");
            return;
        }
        
        // Verificar se há blocos de índice disponíveis
        if (blocosIndexLivres.isEmpty()) {
            System.out.println("Erro: Nao ha i-nodes disponiveis!");
            return;
        }
        
        // Pegar o primeiro bloco de índice livre
        int blocoIndex = blocosIndexLivres.remove(0);
        
        // Alocar blocos de dados
        List<Integer> blocosDeDados = new ArrayList<>();
        List<Integer> blocosLivres = acharBlocosLivres(tamanho);
        
        if (blocosLivres.size() < tamanho) {
            System.out.println("Erro: Nao ha blocos livres suficientes!");
            blocosIndexLivres.add(blocoIndex); // Devolver o bloco de índice
            return;
        }
        
        for (int i = 0; i < tamanho; i++) {
            int bloco = blocosLivres.get(i);
            disco[bloco] = 1; // Marcar como ocupado (dados)
            blocosDeDados.add(bloco);
        }
        
        // Associar blocos de dados ao bloco de índice
        blocosIndex.put(blocoIndex, blocosDeDados);
        diretorio.put(nomeArquivo, new AEntry(nomeArquivo, tamanho, Arrays.asList(blocoIndex)));
        calcularFragmentacao();
        System.out.println("Arquivo '" + nomeArquivo + "' criado com sucesso!");
        System.out.println("Bloco de indice: " + blocoIndex);
    }
    
    @Override
    public void extenderArquivo(String nomeArquivo, int tamanhoAdicional) {
        
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        if (tamanhoAdicional > contarBlocosLivres()) {
            System.out.println("Erro: Espaco insuficiente no disco!");
            return;
        }
        
        AEntry arquivo = diretorio.get(nomeArquivo);
        int blocoIndex = arquivo.blocos.get(0);
        List<Integer> blocosDeDados = blocosIndex.get(blocoIndex);
        
        // Encontrar blocos livres adicionais
        List<Integer> blocosLivres = acharBlocosLivres(tamanhoAdicional);
        
        if (blocosLivres.size() < tamanhoAdicional) {
            System.out.println("Erro: Nao ha blocos livres suficientes!");
            return;
        }
        
        for (int i = 0; i < tamanhoAdicional; i++) {
            int bloco = blocosLivres.get(i);
            disco[bloco] = 1; // Marcar como ocupado
            blocosDeDados.add(bloco);
        }
        
        blocosIndex.put(blocoIndex, blocosDeDados);
        arquivo.tamanho += tamanhoAdicional;
        diretorio.put(nomeArquivo, arquivo);
        calcularFragmentacao();
        System.out.println("Arquivo estendido com sucesso!");
    }
    
    @Override
    public void deletarArquivo(String nomeArquivo) {
        
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        AEntry arquivo = diretorio.get(nomeArquivo);
        int blocoIndex = arquivo.blocos.get(0);
        List<Integer> blocosDeDados = blocosIndex.get(blocoIndex);
        
        // Liberar blocos de dados
        for (int bloco : blocosDeDados) {
            disco[bloco] = 0;
        }
        
        // Liberar bloco de índice (adicionar à lista de livres)
        blocosIndexLivres.add(blocoIndex);
        blocosIndex.put(blocoIndex, new ArrayList<>()); // Limpar a lista
        
        diretorio.remove(nomeArquivo);
        calcularFragmentacao();
        System.out.println("Arquivo '" + nomeArquivo + "' deletado com sucesso!");
    }
    
    @Override
    public void lerArquivo(String nomeArquivo, boolean sequencial) {
        
        if (!diretorio.containsKey(nomeArquivo)) {
            System.out.println("Erro: Arquivo nao encontrado!");
            return;
        }
        
        AEntry arquivo = diretorio.get(nomeArquivo);
        int blocoIndex = arquivo.blocos.get(0);
        List<Integer> blocosDeDados = blocosIndex.get(blocoIndex);
        
        if (sequencial) {
            simulateReadAccess(blocosDeDados, true);
        } else {
                                                                // Para leitura aleatória, acessamos o bloco de índice primeiro
            List<Integer> ordemAcesso = new ArrayList<>();
            ordemAcesso.add(blocoIndex);                        // Acesso ao bloco de índice
            
            List<Integer> blocosAleatorios = new ArrayList<>(blocosDeDados);
            Collections.shuffle(blocosAleatorios);
            ordemAcesso.addAll(blocosAleatorios);
            
            simulateReadAccess(ordemAcesso, false);
        }
    }
    
    @Override
    public void mostrarDisco() {
        
        System.out.println("\n--- VISUALIZACAO DO DISCO (Alocacao Indexada) ---");
        System.out.println("Legenda: [I] = Bloco de indice, [D] = Bloco de dados, [ ] = Livre");
        
        for (int i = 0; i < tamanhoDisco; i++) {
            
            if (i % 10 == 0) System.out.println();
            
            if (disco[i] == 0) {
                System.out.print("[ ]");    // Bloco livre
            } else if (disco[i] == 2) {
                System.out.print("[I]");    // Bloco de índice
            } else {
                                            // Encontrar qual arquivo ocupa este bloco de dados
                String dono = "?";
                for (Map.Entry<String, AEntry> AEntry : diretorio.entrySet()) {
                    int blocoIndex = AEntry.getValue().blocos.get(0);
                    if (blocosIndex.get(blocoIndex).contains(i)) {
                        dono = AEntry.getKey().substring(0, 1).toUpperCase();
                        break;
                    }
                }
                System.out.print("[D" + dono + "]"); // Bloco de dados
            }
        }
        System.out.println("\n");
        
        // Mostrar detalhes dos i-nodes
        System.out.println("--- BLOCO DE INDICE (I-NODES) ---");
        for (int i = 0; i < tamanhoDisco; i++) {
            if (disco[i] == 2) { // É um bloco de índice
                List<Integer> ponteiros = blocosIndex.get(i);
                if (!ponteiros.isEmpty()) {
                    // Encontrar qual arquivo usa este i-node
                    String nomeArquivo = "?";
                    for (Map.Entry<String, AEntry> entry : diretorio.entrySet()) {
                        if (entry.getValue().blocos.get(0) == i) {
                            nomeArquivo = entry.getKey();
                            break;
                        }
                    }
                    
                    System.out.println("I-node " + i + " (" + nomeArquivo + "): " + ponteiros.toString());
                }
            }
        }
        
        // Mostrar blocos de índice livres
        System.out.println("Blocos de indice livres: " + blocosIndexLivres.toString());
    }
    
    private List<Integer> acharBlocosLivres(int count) {
        List<Integer> blocosLivres = new ArrayList<>();
        
        // Começar a buscar após a área reservada para i-nodes
        int inicioBusca = blocosIndexLivres.size(); // Primeiro bloco após i-nodes
        
        for (int i = inicioBusca; i < tamanhoDisco && blocosLivres.size() < count; i++) {
            if (disco[i] == 0) {
                blocosLivres.add(i);
            }
        }
        return blocosLivres;
    }
    
    @Override
    protected int contarBlocosLivres() {
        int count = 0;
        // Não contar blocos de índice como livres para dados
        for (int i = blocosIndexLivres.size(); i < tamanhoDisco; i++) {
            if (disco[i] == 0) count++;
        }
        return count;
    }
}
