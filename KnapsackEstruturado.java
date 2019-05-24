import java.util.Arrays;
import java.util.Scanner;

public class Knapsack {
    private int capacidadeTotal;
    private int numItens;
    private int[] valores;
    private int[] pesos;
    private int[][] tabelaDP;

    /**
     * Construtor para inicializar tudo por parametros.
     */
    public Knapsack(int c, int n, int[] v, int[] p) {
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
    public Knapsack(int c, int n, boolean doRandom) {
        this.capacidadeTotal = c;
        this.numItens = n;
        this.introduzirValores(doRandom);
        this.introduzirPesos(doRandom);
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Construtor que inicializa tudo pela consola.
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public Knapsack(boolean doRandom) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Capacidade total da mochila: ");
        this.capacidadeTotal = scan.nextInt();
        System.out.print("Numero total de itens: ");
        this.numItens = scan.nextInt();
        scan.close();
        this.introduzirValores(doRandom);
        this.introduzirPesos(doRandom);
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Algoritmo Principal.
     * 
     * De forma resumida, o que vai acontecer é a cada item que vai sendo adicionado
     * à mochila(*) verifica para cada tamanho da mochila(**) se é possível inserir
     * esse tal item e se sim soma o seu valor a essa posição da matriz. [nota (*):
     * o índice de cada linha corresponde ao número de itens que existem dentro da
     * mochila nesse momento] [nota (**): isto é, cada coluna corresponde a uma
     * capacidade possível da mochila e o que o algoritmo faz é verificar a
     * possibilidade de introduzir o item que está a ser verificado em cada uma
     * dessas colunas. É isto que faz este algoritmo funcionar, porque desta maneira
     * fica possível fazer 'backtracking' das escolhas anteriores.]
     * 
     * Links para a bibliografia sobre este algoritmo:
     * https://medium.freecodecamp.org/how-to-solve-the-knapsack-problem-with-dynamic-programming-eb88c706d3cf
     * The Knapsack Problem using shortest paths (2011) [livro do moodle, só este é
     * que importa]
     */
    public int algoritmoPrincipal() {
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
        }
        for (int l = 0; l < this.numItens + 1; l++) {
            this.tabelaDP[l][0] = 0;
        }

        /* Onde a magia acontece */
        for (int item = 1; item <= this.numItens; item++) {
            for (int capacidade = 1; capacidade <= this.capacidadeTotal; capacidade++) {
                // Pesquisar o valor que já existe dentro da mochila v
                int valorMaximoExistente = tabelaDP[item - 1][capacidade];
                int novoValorMaximo = 0; // Valor atual dentro da mochila (ainda vai ser calculado)

                int pesoItemAtual = pesos[item - 1]; // Pesquisar o peso do item atual
                // (ter em conta que o índice da 'tabelaDP' está "adiantado" em relação ao
                // índice no vetor 'pesos')

                if (capacidade >= pesoItemAtual) { // Verificar se existe espaço disponível para o item atual
                    novoValorMaximo = valores[item - 1]; // Adicionar o valor do item escolhido
                    // Subtrair o peso do item à capacidade da mochila
                    int capacidadeRestante = capacidade - pesoItemAtual;

                    // Adicionar o valor já existente na mochila
                    novoValorMaximo += this.tabelaDP[item - 1][capacidadeRestante];
                }
                // Escolher o maior dos dois valores
                this.tabelaDP[item][capacidade] = Math.max(valorMaximoExistente, novoValorMaximo);
                /*
                 * NOTA: quando não há espaço disponível na mochila, o item não é escolhido ou
                 * seja não é adicionado valor. É desta forma que depois se vai conseguir saber
                 * quais os itens que foram selecionados.
                 */
            }
        }
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
     * capacidade desse item à capacidade total da mochila. TL;DR k = item atual; n
     * = total items; g = capacidade da mochila Repeat for k = n, n - 1, ..., 1; |
     * If f(k,g) != f(k-1,g), item k is in the selection Exemplo no link abaixo.
     * 
     * Link sobre isto (adicionar tambem):
     * http://www.mafy.lut.fi/study/DiscreteOpt/DYNKNAP.pdf
     */
    public int[] algoritmoBacktracking() {
        int[] itensEscolhidos = new int[numItens];
        int capacidadeMochila = capacidadeTotal;
        for (int item = numItens; item > 0; item--) {
            if (tabelaDP[item][capacidadeMochila] != tabelaDP[item - 1][capacidadeMochila]) {
                itensEscolhidos[item - 1] = 1; // Item foi escolhido
                capacidadeMochila = capacidadeMochila - pesos[item - 1]; // Subtrair o peso do item só quando escolhido
            } else {
                itensEscolhidos[item - 1] = 0; // Item não foi escolhido
            }
        }
        return itensEscolhidos;
    }

    public static void main(String args[]) {
        /*
         * exemplo do problema: int capacidadeTotal = 10; int numItens = 4; int[]
         * valores = {10, 40, 30, 50}; int[] pesos = {5, 4, 6, 3};
         */
        // Instância para INPUTS e para correr o ALGORITMO
        Knapsack knapsack = new Knapsack(10, 4, new int[] { 10, 40, 30, 50 }, new int[] { 5, 4, 6, 3 });

        long startTime = System.nanoTime(); // Variavel para calcular o tempo de execução
        int valorTotal = knapsack.algoritmoPrincipal(); // Algoritmo principal
        long mainTime = System.nanoTime() - startTime; // Calcular o tempo de execução
        System.out.println("Valor total dentro da mochila: " + valorTotal + "\n----------------");

        startTime = System.nanoTime(); // Reset para calcular o tempo do proximo algoritmo
        int[] itensEscolhidos = knapsack.algoritmoBacktracking();
        long backtrackingTime = System.nanoTime() - startTime;

        // OUTPUTS
        System.out.print("Itens escolhidos: \n[ ");
        for (int i = 0; i < itensEscolhidos.length; i++) {
            if (itensEscolhidos[i] == 1) {
                System.out.print((i + 1) + " ");
            }
        }
        System.out.println("]\n----------------");
        System.out.println("Tempos de Execucao: \n" + "\tAlgoritmo Principal: "
                + Math.floor((mainTime / 1000000.0) * 1000) / 1000 + " ms" + "\n\t2o Algoritmo: "
                + Math.floor((backtrackingTime / 1000000.0) * 1000) / 1000 + " ms\n----------------");
        // System.out.println("Tabela: \n" + Arrays.deepToString(knapsack.getTabela()));
    }

    /**
     * Preenche o vetor com os valores dos itens.
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public void introduzirValores(boolean doRandom) {
        this.valores = new int[numItens];
        if (doRandom) {
            for (int i = 0; i < this.numItens; i++) {
                this.valores[i] = (int) Math.floor(Math.random() * 10);
            }
        } else {
            Scanner scan = new Scanner(System.in);
            System.out.println("Numero de Itens: " + this.numItens);
            System.out.println("Valor para cada item:");
            for (int i = 0; i < this.numItens; i++) {
                System.out.print((i + 1) + ". -> ");
                this.valores[i] = scan.nextInt();
            }
            scan.close();
        }
        System.out.println("Valores Adicionados.\n----------------------------------------");
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Preenche o vetor com os pesos dos itens.
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public void introduzirPesos(boolean doRandom) {
        this.pesos = new int[numItens];
        if (doRandom) {
            for (int i = 0; i < this.numItens; i++) {
                this.pesos[i] = (int) Math.floor(Math.random() * 10);
            }
        } else {
            Scanner scan = new Scanner(System.in);
            System.out.println("Numero de Itens: " + this.numItens);
            System.out.println("Peso de cada item:");
            for (int i = 0; i < this.numItens; i++) {
                System.out.print((i + 1) + ". -> ");
                this.pesos[i] = scan.nextInt();
            }
            scan.close();
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
     * Faz reset dos valores e pesos, tal como da tabela principal quando se muda o
     * nº Itens.
     */
    public void setNumItens(int n) {
        this.numItens = n;
        this.introduzirValores(false);
        this.introduzirPesos(false);
        this.tabelaDP = new int[this.numItens + 1][this.capacidadeTotal + 1];
    }

    /**
     * Retornar a tabela de resolução do problema.
     */
    public int[][] getTabela() {
        return this.tabelaDP;
    }
}