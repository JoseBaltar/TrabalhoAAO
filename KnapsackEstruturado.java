
import java.util.Arrays;
import java.util.Scanner;

public class KnapsackEstruturado {
    private int capacidadeTotal;
    private int numItens;
    private int[] valores;
    private int[] pesos;
    private int[][] tabelaDP;

    // Contadores de análise da complexidade
    private int mainConditionCounter = 0;
    private int backtrackConditionCounter = 0;

    /**
     * Construtor para inicializar tudo por parametros.
     */
    public KnapsackEstruturado(int c, int n, int[] v, int[] p) {
        this.capacidadeTotal = c;
        this.numItens = n;
        this.valores = v;
        this.pesos = p;
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Construtor que inicializa as variaveis numéricas por parâmetro e scan dos
     * outros valores
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public KnapsackEstruturado(int c, int n, boolean doRandom) {
        Scanner scan = new Scanner(System.in);
        this.capacidadeTotal = c;
        this.numItens = n;
        this.introduzirValores(doRandom, scan);
        this.introduzirPesos(doRandom, scan);
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
        scan.close();
    }

    /**
     * Construtor que inicializa tudo pela consola.
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public KnapsackEstruturado(boolean doRandom) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Capacidade total da mochila: ");
        this.capacidadeTotal = scan.nextInt();
        System.out.print("Numero total de itens: ");
        this.numItens = scan.nextInt();
        this.introduzirValores(doRandom, scan);
        this.introduzirPesos(doRandom, scan);
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
        scan.close();
    }

    /**
     * Algoritmo Principal.
     * 
     * De forma resumida, o que vai acontecer é a cada item que vai sendo adicionado
     * à mochila(*) verifica para cada tamanho da mochila(**) se é possível inserir
     * esse tal item e se sim soma o seu valor a essa posição da matriz.
     * <p>
     * [nota (*): o índice de cada linha corresponde ao número de itens que existem
     * dentro da mochila nesse momento]
     * <p>
     * [nota (**): isto é, cada coluna corresponde a uma capacidade possível da
     * mochila e o que o algoritmo faz é verificar a possibilidade de introduzir o
     * item que está a ser verificado em cada uma dessas colunas. É isto que faz
     * este algoritmo funcionar, porque desta maneira fica possível fazer
     * 'backtracking' das escolhas anteriores.]
     * 
     * Links para a bibliografia sobre este algoritmo:
     * https://medium.freecodecamp.org/how-to-solve-the-knapsack-problem-with-dynamic-programming-eb88c706d3cf
     * The Knapsack Problem using shortest paths (2011) [livro do moodle, só este é
     * que importa]
     * 
     * 
     * UPDATE: Counters para análise da complexidade já foram adicionados. Como o
     * código que temos aqui tem poucas condições, praticamente só existem counters
     * nos ciclos. No entanto, é preciso explicar isto no relatório na mesma.
     * 
     * Neste caso, temos 1 condição para cada ciclo e uma condição dentro do segundo
     * ciclo do algoritmo principal que é sempre avaliada ou seja, counter+1 para
     * cada iteração de cada ciclo 'for' e counter+2 para cada iteração do segundo
     * ciclo 'for' do algoritmo principal.
     */
    public int algoritmoPrincipal() {
        this.mainConditionCounter = 0; // contador de condições realizadas no algoritmo.
        /*
         * Preencher os casos triviais (inicializar a 0):
         * 
         * - Quando não temos itens na mochila (tabelaDP[0][c], primeira linha da
         * tabela);
         * 
         * - No caso de uma mochila com capacidade 0 (tabelaDP[l][0], primeira coluna da
         * tabela).
         */
        for (int c = 0; c < this.capacidadeTotal + 1; c++) {
            this.tabelaDP[0][c] = 0;
            this.mainConditionCounter++; // +1 condição por cada iteração do ciclo
        }
        this.mainConditionCounter++; // +1 para conpensar a verificação realizada para a saída do ciclo
        for (int l = 0; l < this.numItens + 1; l++) {
            this.tabelaDP[l][0] = 0;
            this.mainConditionCounter++; // +1 condição por cada iteração do ciclo
        }
        this.mainConditionCounter++; // +1 para conpensar a verificação realizada para a saída do ciclo

        /* Algoritmo Principal */
        for (int item = 1; item <= this.numItens; item++) {
            for (int capacidade = 1; capacidade <= this.capacidadeTotal; capacidade++) {
                int valorMaximoExistente = tabelaDP[item - 1][capacidade]; // Valor dentro da mochila
                int novoValorMaximo = 0; // Valor atual dentro da mochila (ainda vai ser calculado)

                int pesoItemAtual = pesos[item - 1]; // Pesquisar o peso do item atual
                // (ter em conta que o índice da 'tabelaDP' está "adiantado" em relação ao
                // índice no vetor 'pesos')

                if (capacidade >= pesoItemAtual) { // Verificar se existe espaço disponível para o item atual
                    novoValorMaximo = valores[item - 1]; // Adicionar o valor do item escolhido
                    int capacidadeRestante = capacidade - pesoItemAtual; // Subtrair o peso à capacidade atual

                    novoValorMaximo += this.tabelaDP[item - 1][capacidadeRestante];
                    /*
                     * NOTA: O valor adicionado acima refere-se ao valor dos items que já existem na
                     * mochila que poderão ser adicionados se a capacidade restante (com o item
                     * atual incluido) for suficiente (isto é, se existirem itens já adicionados)
                     */
                }
                this.tabelaDP[item][capacidade] = // Escolher o maior dos dois valores
                        Math.max(valorMaximoExistente, novoValorMaximo);
                /*
                 * NOTA: quando não há espaço disponível na mochila, o item não é escolhido ou
                 * seja não é adicionado valor. É desta forma que depois se vai conseguir saber
                 * quais os itens que foram selecionados.
                 */
                this.mainConditionCounter += 2; // +1 por cada iteração do ciclo +1 pela condição intrínseca (ver nota)
            }
            this.mainConditionCounter++; // +1 por cada iteração do ciclo
        }
        this.mainConditionCounter += 2; // +2 para conpensar a verificação realizada para a saída do ciclo
        return this.tabelaDP[this.numItens][this.capacidadeTotal];
    }

    /**
     * 'Backtracking' da solução para saber quais os itens que foram escolhidos.
     * 
     * O raciocínio aqui passa por comparar e verificar se o valor que existe dentro
     * da mochila alterou de uma iteração para outra, ou seja, começando pelo último
     * item e na capacidade máxima, se houve alteração do valor dentro da mochila
     * então significa que o item foi escolhido (neste caso, o último). Para a
     * próxima iteração, somente quando o item foi escolhido, deve subtrair-se a
     * capacidade desse item à capacidade total da mochila.
     * <p>
     * TL;DR k = item atual; n = total items; g = capacidade da mochila
     * <p>
     * Repeat for k = n, n - 1, ..., 1; | If f(k,g) != f(k-1,g), item k is in the
     * selection
     * <p>
     * Exemplo no link abaixo.
     * 
     * Link sobre isto (adicionar tambem):
     * http://www.mafy.lut.fi/study/DiscreteOpt/DYNKNAP.pdf
     * 
     * 
     * UPDATE: Counters para análise da complexidade já foram adicionados. Como o
     * código que temos aqui tem poucas condições, praticamente só existem counters
     * nos ciclos. No entanto, é preciso explicar isto no relatório na mesma.
     * 
     * Neste caso, temos 1 condição para cada iteração ciclo e uma condição dentro
     * desse ciclo que será sempre avaliada ou seja, counter+2 para cada iteração do
     * ciclo e a única condição avaliada.
     */
    public int[] algoritmoBacktracking() {
        this.backtrackConditionCounter = 0;
        int[] itensEscolhidos = new int[numItens];
        int capacidadeMochila = capacidadeTotal;
        int item = 0;
        for (item = numItens; capacidadeMochila > 0 && item > 0; item--) {
            if (tabelaDP[item][capacidadeMochila] != tabelaDP[item - 1][capacidadeMochila]) {
                itensEscolhidos[item - 1] = 1; // Item foi escolhido
                capacidadeMochila = capacidadeMochila - pesos[item - 1]; // Subtrair o peso do item só quando escolhido
            } else {
                itensEscolhidos[item - 1] = 0; // Item não foi escolhido
            }
            this.backtrackConditionCounter += 3; // +2 por cada iteração do ciclo +1 pela condição intrínseca
        }
        if (item <= 0) { // Se a primeira condição for falsa, já não é analisada a segunda!
            this.mainConditionCounter += 2; // +2 para conpensar a verificação realizada para a saída do ciclo
        } else {
            this.backtrackConditionCounter++; // +1 para conpensar a verificação realizada para a saída do ciclo
        }
        return itensEscolhidos;

    }

    /**
     * Preenche o vetor com os valores dos itens.
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public void introduzirValores(boolean doRandom, Scanner scan) {
        this.valores = new int[numItens];
        if (doRandom) {
            for (int i = 0; i < this.numItens; i++) {
                this.valores[i] = (int) Math.floor(Math.random() * 10);
            }
        } else {
            System.out.println("Numero de Itens: " + this.numItens);
            System.out.println("Valor para cada item:");
            for (int i = 0; i < this.numItens; i++) {
                System.out.print((i + 1) + ". -> ");
                this.valores[i] = scan.nextInt();
            }
        }
        System.out.println("Valores Adicionados.\n----------------------------------------");
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Preenche o vetor com os pesos dos itens.
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public void introduzirPesos(boolean doRandom, Scanner scan) {
        this.pesos = new int[numItens];
        if (doRandom) {
            for (int i = 0; i < this.numItens; i++) {
                this.pesos[i] = (int) Math.floor(Math.random() * 10);
            }
        } else {
            System.out.println("Numero de Itens: " + this.numItens);
            System.out.println("Peso de cada item:");
            for (int i = 0; i < this.numItens; i++) {
                System.out.print((i + 1) + ". -> ");
                this.pesos[i] = scan.nextInt();
            }
        }
        System.out.println("Pesos Adicionados.\n----------------------------------------");
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Faz reset da tabela quando se muda a capacidade.
     */
    public void setCapacidade(int c) {
        this.capacidadeTotal = c;
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Retornar a tabela de resolução do problema.
     * 
     * @return matriz com os valores da resolução
     */
    public int[][] getTabela() {
        return this.tabelaDP;
    }

    /**
     * Retorna o contador de condições para análise de complexidade do algoritmo
     * principal.
     * 
     * @return número de condições contadas
     */
    public int getContadorPrincipal() {
        return this.mainConditionCounter;
    }

    /**
     * Retorna o contador de condições para análise de complexidade do algoritmo de
     * backtracking.
     * 
     * @return número de condições contadas
     */
    public int getContadorBacktracking() {
        return this.backtrackConditionCounter;
    }

    /**
     * Retornar os valores dos itens NOTA: cada item corresponde a cada índice + 1.
     * 
     * @return array numérico com os valores de cada item
     */
    public int[] getValores() {
        return this.valores;
    }

    /**
     * Retornar os pesos dos itens. NOTA: cada item corresponde a cada índice + 1.
     * 
     * @return array númerico com os pesos de cada item
     */
    public int[] getPesos() {
        return this.pesos;
    }

    /**
     * Retornar a capacidade da mochila.
     * 
     * @return capacidade da mochila
     */
    public int getCapacidade() {
        return this.capacidadeTotal;
    }

    public static void main(String args[]) {
        /**
         * <pre>
         * EXEMPLOS
         * 4 itens: int capacidadeTotal = 8;
         *          int numItens = 4;
         *          int[] valores = {15, 10, 9, 5}; 
         *          int[] pesos = {1, 5, 3, 4};
         * </pre>
         */
        KnapsackEstruturado knapsack = // Instância para INPUTS e para correr o ALGORITMO
                // new KnapsackEstruturado(7, 3, new int[] { 8, 4, 2 }, new int[] { 1, 2, 3 });
                // new KnapsackEstruturado(7, 6, new int[] { 3, 10, 6, 8, 9, 7 }, new int[] { 1,
                // 2, 3, 4, 5, 6 });
                new KnapsackEstruturado(7, 12, new int[] { 2, 3, 6, 7, 3, 9, 4, 6, 1, 3, 7, 8 },
                        new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });

        long startTime = System.nanoTime(); // Variavel para calcular o tempo de execução
        int valorTotal = knapsack.algoritmoPrincipal(); // Algoritmo principal
        long mainTime = System.nanoTime() - startTime; // Calcular o tempo de execução

        startTime = System.nanoTime(); // Reset para calcular o tempo do proximo algoritmo
        int[] itensEscolhidos = knapsack.algoritmoBacktracking();
        long backtrackingTime = System.nanoTime() - startTime;

        // OUTPUTS
        // Informações
        /*
         * Imprimir a tabela de resolução do problema:
         * 
         * for (int[] linha : knapsack.getTabela()) {
         * System.out.println(Arrays.toString(linha)); }
         */
        System.out.println("--------------------------------");
        System.out.println("Valores para cada item: " + Arrays.toString(knapsack.getValores()));
        System.out.println("Pesos para cada item: " + Arrays.toString(knapsack.getPesos()));
        System.out.println("--------------------------------");
        System.out.println("Tempos de Execucao: \n" + "\tAlgoritmo Principal: "
                + Math.floor((mainTime / 1000000.0) * 1000) / 1000 + " ms" + "\n\tAlgoritmo Backtracking: "
                + Math.floor((backtrackingTime / 1000000.0) * 1000) / 1000 + " ms");
        System.out.println("Numero de condicoes executadas: \n" + "\tAlgoritmo Principal: "
                + knapsack.getContadorPrincipal() + "\n\tAlgoritmo Backtracking: " + knapsack.getContadorBacktracking()
                + "\n--------------------------------");
        // Resultados
        System.out.print("Itens escolhidos: ");
        int pesoTotal = 0;
        for (int i = 0; i < itensEscolhidos.length; i++) {
            if (itensEscolhidos[i] == 1) {
                System.out.print((i + 1) + ", ");
                pesoTotal += knapsack.pesos[i];
            }
        }
        System.out.println("\n--------------------------------");
        System.out.println("Capacidade da mochila: " + knapsack.getCapacidade());
        System.out.println("Total peso da mochila utilizado: " + pesoTotal);
        System.out.println("Valor total dentro da mochila: " + valorTotal);
        System.out.println("--------------------------------");
    }
}