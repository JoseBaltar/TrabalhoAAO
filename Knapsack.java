import java.util.Arrays;
import java.util.Scanner;

public class Knapsack {
    public static void main(String args[]) {
        /*
        int capacidadeTotal = 10;
        int numItens = 4;
        int[] valores = {10, 40, 30, 50};
        int[] pesos = {5, 4, 6, 3};
        */
        int capacidadeTotal;
        int numItens;
        /*Scanner scan = new Scanner(System.in);
        System.out.print("Capacidade total da mochila: ");
        capacidadeTotal = scan.nextInt();
        System.out.print("Numero total de itens: ");
        numItens = scan.nextInt();
        System.out.println("Valor para cada item:");
        */
        capacidadeTotal = 100;
        numItens = 30;
        short[] valores = new short[numItens];
        for (int i = 0; i < numItens; i++) {
            /*System.out.print((i+1)+". -> ");
            valores[i] = scan.nextInt();*/
            valores[i] = (short) Math.floor(Math.random() * 10);
        }
        System.out.println("Peso de cada item:");
        short[] pesos = new short[numItens];
        for (int i = 0; i < numItens; i++) {
            /*System.out.print((i+1)+". -> ");
            pesos[i] = scan.nextInt();*/
            pesos[i] = (short) Math.floor(Math.random() * 10);
        }
        //scan.close();
        long startTime = System.nanoTime();
        /* 
        Preencher os casos triviais (inicializar a 0):
        - Quando não temos itens na mochila 
        (tabelaDP[0][c], primeira linha da tabela);
        - No caso de uma mochila com capacidade 0 
        (tabelaDP[l][0], primeira coluna da tabela).
        */
        int[][] tabelaDP = new int[numItens + 1][capacidadeTotal + 1];
        for (int c = 0; c < capacidadeTotal + 1; c++) {
            tabelaDP[0][c] = 0;
        }
        for (int l = 0; l < numItens + 1; l++) {
            tabelaDP[l][0] = 0;
        }
        
        // Algoritmo Principal
        /*
        De forma resumida, o que vai acontecer é a cada item que vai sendo adicionado à mochila(*) 
        verifica para cada tamanho da mochila(**) se é possível inserir esse tal item e se sim soma o seu valor a essa posição da matriz.
        [nota (*): o índice de cada linha corresponde ao número de itens que existem dentro da mochila nesse momento]
        [nota (**): isto é, cada coluna corresponde a uma capacidade possível da mochila e o que o algoritmo faz é verificar a possibilidade 
        de introduzir o item que está a ser verificado em cada uma dessas colunas. É isto que faz este algoritmo funcionar, porque desta maneira
        fica possível fazer 'backtracking' das escolhas anteriores.]

        Links para a bibliografia sobre este algoritmo:
        https://medium.freecodecamp.org/how-to-solve-the-knapsack-problem-with-dynamic-programming-eb88c706d3cf
        The Knapsack Problem using shortest paths (2011) [livro do moodle, só este é que importa]
        */
        for (int item = 1; item <= numItens; item++) {
            for (int capacidade = 1; capacidade <= capacidadeTotal; capacidade++) {

                int valorMaximoExistente = tabelaDP[item - 1][capacidade]; // Pesquisar o valor que já existe dentro da mochila
                int novoValorMaximo = 0; // Valor atual dentro da mochila (ainda vai ser calculado)

                int pesoItemAtual = pesos[item - 1]; // Pesquisar o peso do item atual 
                /* (ter em conta que o índice da 'tabelaDP' está "adiantado" em relação ao índice no vetor 'pesos') */
                
                if (capacidade >= pesoItemAtual) { // Verificar se existe espaço disponível para o item atual
                    novoValorMaximo = valores[item - 1]; // Adicionar o valor do item escolhido
                    int capacidadeRestante = capacidade - pesoItemAtual; // Subtrair o peso do item à capacidade da mochila
                    
                    novoValorMaximo += tabelaDP[item - 1][capacidadeRestante]; // Adicionar o valor já existente na mochila
                }
                
                tabelaDP[item][capacidade] = Math.max(valorMaximoExistente, novoValorMaximo); // Escolher o maior dos dois valores
                /* Ter em conta que quando não há espaço disponível na mochila, o item não é escolhido ou seja não é adicionado valor.
                É desta forma que depois se vai conseguir saber quais os itens que foram selecionados. */
            }
        }
        long mainTime = System.nanoTime() - startTime;
        long startTime2 = System.nanoTime();
        System.out.println("----------------\n" +
            "Valor total dentro da mochila: " + tabelaDP[numItens][capacidadeTotal]); // Resposta final do problema
        
        // 'Backtracking' da solução para saber quais os itens que foram escolhidos
        /*
        O raciocínio aqui passa por comparar e verificar se o valor que existe dentro da mochila alterou de uma iteração para outra,
        ou seja, começando pelo último item e na capacidade máxima, se houve alteração do valor dentro da mochila então significa que o
        item foi escolhido (neste caso, o último). Para a próxima iteração, somente quando o item foi escolhido, deve subtrair-se a capacidade
        desse item à capacidade total da mochila.
        TL;DR
        k = item atual; n = total items; g = capacidade da mochila
        Repeat for k = n, n - 1, ..., 1; | If f(k,g) != f(k-1,g), item k is in the selection
        Exemplo no link abaixo.

        Link sobre isto (adicionar tambem):
        http://www.mafy.lut.fi/study/DiscreteOpt/DYNKNAP.pdf
        */
        int[] itensEscolhidos = new int[numItens];
        int capacidadeMochila = capacidadeTotal;
        for (int item = numItens; item > 0; item--) {
            if (tabelaDP[item][capacidadeMochila] != tabelaDP[item-1][capacidadeMochila]) {
                itensEscolhidos[item - 1] = 1; // Item foi escolhido
                capacidadeMochila = capacidadeMochila - pesos[item - 1]; // Subtrair o peso do item só quando escolhido
            } else {
                itensEscolhidos[item - 1] = 0; // Item não foi escolhido
            }
        }

        long mainTime2 = System.nanoTime() - startTime2;
        System.out.print("----------------\n" + "Itens escolhidos: \n[");
        for (int i = 0; i < itensEscolhidos.length; i++) {
            if (itensEscolhidos[i] == 1) {
                System.out.print((i+1));
                if ( i != (itensEscolhidos.length - 1)) {
                    System.out.print(", ");
                }
            }
        } System.out.println("]");

        System.out.print("----------------\n" + "Tempos de Execucao: \n" + 
            "\tAlgoritmo Principal: " + 
            //Math.floor((mainTime / 1000000000.0) * 1000) / 1000 + " s\n\t\t" +
            Math.floor((mainTime / 1000000.0) * 1000) / 1000 + " ms" +
            "\n\t2o Algoritmo: " + 
            //Math.floor((mainTime2 / 1000000000.0) * 1000) / 1000 + " s\n\t\t" +
            Math.floor((mainTime2 / 1000000.0) * 1000) / 1000 + " ms");
        // System.out.println("\n----------------\n" + "Tabela: \n" + Arrays.deepToString(tabelaDP)); // Mostrar tabela
    }
}