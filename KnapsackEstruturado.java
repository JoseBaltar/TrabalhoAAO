
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class KnapsackEstruturado {
    private int capacidadeTotal;
    private int numItens;
    private int[] valores;
    private int[] pesos;
    private int[][] tabelaDP;
	private final int RANDOM_LIMIT = 49;

    // Contadores de análise da complexidade
    private int mainConditionCounter = 0;
    private int backtrackConditionCounter = 0;

    /**
     * Construtor para inicializar tudo por parametros.
	 * 
	 * @param c capacidade total da mochila
	 * @param n numero total de items
	 * @param v valores dos items (índice corresponde ao item)
	 * @param p pesos dos items (índice corresponde ao item)
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
	 * @param c capacidade total da mochila
	 * @param n numero total de items
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
     * <p>
     */
    public int algoritmoPrincipal() {
        this.mainConditionCounter = 0; // contador de condições realizadas no algoritmo.
        /*
         * Preencher os casos triviais (inicializar a 0):
         * 
         * - Quando não temos itens na mochila (tabelaDP[0][c], primeira linha da
         * tabela);
         * - No caso de uma mochila com capacidade 0 (tabelaDP[l][0], primeira coluna da
         * tabela).
         */
        for (int c = 0; c < this.capacidadeTotal + 1; c++) 
		{
            this.tabelaDP[0][c] = 0;
            this.mainConditionCounter++; // +1 condição por cada iteração do ciclo
        }
        this.mainConditionCounter++; // +1 para a verificação realizada para na saída do ciclo
		
        for (int l = 0; l < this.numItens + 1; l++) 
		{
            this.tabelaDP[l][0] = 0;
            this.mainConditionCounter++;
        }
        this.mainConditionCounter++;

        /* Algoritmo Principal */
        for (int item = 1; item <= this.numItens; item++) 
		{
            for (int capacidade = 1; capacidade <= this.capacidadeTotal; capacidade++) 
			{
                int valorMaximoExistente = tabelaDP[item - 1][capacidade]; // Valor dentro da mochila
                int novoValorMaximo = 0; // Valor atual dentro da mochila

                int pesoItemAtual = pesos[item - 1]; // Pesquisar o peso do item atual
                // índice da 'tabelaDP' está "adiantado" em relação ao índice no vetor 'pesos')

                if (capacidade >= pesoItemAtual) 
				{ // Verificar se existe espaço disponível para o item atual
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
                this.mainConditionCounter += 2;
            }
            this.mainConditionCounter++;
        }
        this.mainConditionCounter += 2;
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
     */
    public int[] algoritmoBacktracking() {
        this.backtrackConditionCounter = 0;
        int[] itensEscolhidos = new int[numItens];
        int capacidadeMochila = capacidadeTotal;
        int item = 0;
		
        for (item = numItens; capacidadeMochila > 0 && item > 0; item--) 
		{
            if (tabelaDP[item][capacidadeMochila] != tabelaDP[item - 1][capacidadeMochila]) 
			{ // Item foi escolhido
                itensEscolhidos[item - 1] = 1; 
                capacidadeMochila = capacidadeMochila - pesos[item - 1];
            } else 
			{ // Item não foi escolhido
                itensEscolhidos[item - 1] = 0; 
            }
            this.backtrackConditionCounter += 3;
        }
		
        if (item <= 0) 
		{ 
            this.mainConditionCounter += 2; 
        } else 
		{ // Se a primeira condição for falsa, já não é analisada a segunda. Condition counter +1 em vez de +2!
            this.backtrackConditionCounter++;
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
        if (doRandom) 
		{
            for (int i = 0; i < this.numItens; i++) 
			{
                this.valores[i] = (int) Math.floor(Math.random() * RANDOM_LIMIT);
            }
        } else 
		{
            System.out.println("Numero de Itens: " + this.numItens);
            System.out.println("Valor para cada item:");
            for (int i = 0; i < this.numItens; i++) 
			{
                System.out.print((i + 1) + ". -> ");
                this.valores[i] = scan.nextInt();
            }
        }
        System.out.println("\nValores Adicionados.\n----------------------------------------");
        this.tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
    }

    /**
     * Preenche o vetor com os pesos dos itens.
     * 
     * @param doRandom flag para gerar os valores e pesos aleatoriamente
     */
    public void introduzirPesos(boolean doRandom, Scanner scan) {
        this.pesos = new int[numItens];
        if (doRandom) 
		{
            for (int i = 0; i < this.numItens; i++) 
			{
                this.pesos[i] = (int) Math.floor((Math.random() * RANDOM_LIMIT) + 1);
            }
        } else 
		{
            System.out.println("Numero de Itens: " + this.numItens);
            System.out.println("Peso de cada item:");
            for (int i = 0; i < this.numItens; i++) 
			{
                System.out.print((i + 1) + ". -> ");
                this.pesos[i] = scan.nextInt();
            }
        }
        System.out.println("\nPesos Adicionados.\n----------------------------------------");
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
	
	/**
     * Retornar o número total de items para o algoritmo
     * 
     * @return numero total de items
     */
    public int getNumItems() {
        return this.numItens;
    }

    public static void main(String args[]) {
		
		final String usage = "Utilização:\n"
								+ "Introduzir informação customizada para a capacidade da mochila e numero de items:"
								+ "> java KnapsackEstruturado <capacidadeMochila> <numeroDeItems> <gerarValoredAleatorios = true | false>\n"
								+ "Utilizar valores por defeito para a mochila e numero de items:"
								+ "> java KnapsackEstruturado <gerarValoresAleatorios = true | false>\n"
								+ "Executar um exemplo do Knapsack Problem com tudo por defeito:"
								+ "> java KnapsackEstruturado\n";
		KnapsackEstruturado knapsack = null;
		// ## INICIALIZAÇÃO
		if (args.length == 3) {
			try {
				int bagCap = Integer.parseInt(args[0]),
					 numItems = Integer.parseInt(args[1]);
				boolean random = Boolean.parseBoolean(args[2]); // true só quando string igual "true" ignorecase
				knapsack = new KnapsackEstruturado(bagCap, numItems, random);
				
			} catch (NumberFormatException exc) {
				System.out.println(usage);
				System.exit(-1);
			}
			
		} else if (args.length == 1) {
			boolean random = Boolean.parseBoolean(args[0]); // true só quando string igual "true" ignorecase
			knapsack = new KnapsackEstruturado(random);
			
		} else if (args.length == 0) {
			knapsack = 
				// new KnapsackEstruturado(7, 3, new int[] { 8, 4, 2 }, new int[] { 1, 2, 3 });
                new KnapsackEstruturado(7, 6, new int[] { 3, 10, 6, 8, 9, 7 }, new int[] { 1, 2, 3, 4, 5, 6 });
			
		} else {
			System.out.println(usage);
			System.exit(-1);
		}
		
		
		// ## ALGORITMO PRINCIPAL
		System.out.println("\n| BEM VINDO AO SIMULADOR KNAPSACK PROBLEM |");
        long startTime = System.nanoTime(); // Variavel para calcular o tempo de execução
        int valorTotal = knapsack.algoritmoPrincipal(); // Algoritmo principal
        long mainTime = System.nanoTime() - startTime; // Calcular o tempo de execução
		
		
		// ## BACKTRACKING DA SOLUÇÃO
        startTime = System.nanoTime(); // Reset para calcular o tempo do proximo algoritmo
        int[] itensEscolhidos = knapsack.algoritmoBacktracking();
        long backtrackingTime = System.nanoTime() - startTime;


        // ## OUTPUTS
        // Imprimir a tabela de resolução do problema:
		// for (int[] linha : knapsack.getTabela()) {
		//	System.out.println(Arrays.toString(linha)); 
		// }
        System.out.println("\n############################################\n"
						+ "## Informacao inicial\n" 
						+ "\n> Capacidade da mochila: " + knapsack.getCapacidade()
						+ "\n> Numero total de items: " + knapsack.getNumItems()
						+ "\n> Valor total: " + IntStream.of(knapsack.getValores()).sum()
        				+ "\n> Peso total: " + IntStream.of(knapsack.getPesos()).sum()
        				+ "\n> Valores para cada item (10 primeiros): " + Arrays.toString(IntStream.of(knapsack.getValores()).limit(10).toArray())
        				+ "\n> Pesos para cada item (10 primeiros): " + Arrays.toString(IntStream.of(knapsack.getPesos()).limit(10).toArray()));
        // RESULTADOS OBTIDOS
        System.out.print("\n############################################\n"
						+ "## Resultados obtidos\n" 
						+ "\n> Items dentro da mochila: ");
        int pesoTotal = 0;
        for (int i = 0; i < itensEscolhidos.length; i++) {
            if (itensEscolhidos[i] == 1) {
                System.out.print((i + 1) + ", ");
                pesoTotal += knapsack.pesos[i];
            }
        }
        System.out.println("\n> Peso total dentro da mochila: " + pesoTotal
        				+ "\n> Valor total dentro da mochila: " + valorTotal);
		// INFORMAÇÃO EXECUÇÃO DO ALGORITMO
		System.out.println("\n############################################\n"
						+ "## Informacao sobre a execucao do algoritmo\n" 
						+ "\n> Tempos de Execucao: \n" + "\tAlgoritmo Principal: "
                + Math.floor((mainTime / 1000000.0) * 1000) / 1000 + " ms" + "\n\tAlgoritmo Backtracking: "
                + Math.floor((backtrackingTime / 1000000.0) * 1000) / 1000 + " ms"
        				+ "\n> Numero de condicoes executadas: \n" + "\tAlgoritmo Principal: "
                + knapsack.getContadorPrincipal() + "\n\tAlgoritmo Backtracking: " + knapsack.getContadorBacktracking());
				
		System.out.println("\n| FINISHED |\n");
    }
}