/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.batalha.naval;

import cliente.batalha.naval.view.cli.FazerJogadaView;
import cliente.batalha.naval.view.cli.JogoView;
import cliente.batalha.naval.view.cli.PosicionarNaviosView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lucas
 */
public class ClienteBatalhaNaval {

    /* PROTOCOLO BATALHA NAVAL (PBN)
    
     */
    private static final String JOGADOR = "jogador"; // string
    private static final String ESTADO = "estado"; // String
    private static final String TAMANHO_TABULEIRO = "tamanhoTabuleiro"; // Integer
    private static final String QUANTIDADE_NAVIOS = "quantidadeNavios"; // Integer
    private static final String QUERO_RESULTADOS = "resultados";
    private static final String FUNCAO = "funcao"; // (String) O que será feito?
    private static final String CHAR = "char"; // char
    private static final String SETS = "sets"; // Object[4] -> {String, int, int, value} -> FUNCAO(String), linha(int), coluna(int), VALOR(char)

    /* Lista de funcoes de FUNCAO*/
    private static final String FUNCAO_MOSTRA_TABULEIRO_PROPRIO = "tabuleiroProprio";
    private static final String FUNCAO_MOSTRA_TABULEIRO_ADVERSARIO = "tabuleiroAdversario";
    private static final String FUNCAO_MOSTRA_ESTADO = "mostraEstado";
    private static final String FUNCAO_LER_JOGADA = "lerJogada";
    private static final String FUNCAO_LER_POSICOES = "lerPosicoes";
    private static final String FUNCAO_VOCE_PERDEU = "perdeu";
    private static final String FUNCAO_VOCE_GANHOU = "ganhou";
    private static final String FUNCAO_RESULTADOS = "resultados";
    private static final String FUNCAO_ACABAR = "acabou";

    /* Lista de funções de SET*/
    private static final String SET_ESPACO_PROPRIO = "setEspacoProprio";
    private static final String SET_ESPACO_ADVERSARIO = "setEspacoAdversario";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new ClienteBatalhaNaval().go();
    }

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private FazerJogadaView fazerJogadaView;
    private PosicionarNaviosView posicionarNaviosView;
    private JogoView jogoView;

    public ClienteBatalhaNaval() throws IOException {
        socket = new Socket("127.0.0.1", 12345);
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());

    }

    boolean acabou;

    private void go() throws IOException, ClassNotFoundException {
        acabou = false;
        while (!acabou) {
            Map<String, Object> pacote = (Map<String, Object>) input.readObject();
            processar(pacote);
            if ((boolean) pacote.get(QUERO_RESULTADOS)) {
                enviarResultados();
            }
        }
    }

    private void processar(Map<String, Object> pacote) throws IOException {
        if (jogoView == null) { // crie!
            String jogador = (String) pacote.get(JOGADOR);
            int tamanho = (int) pacote.get(TAMANHO_TABULEIRO);
            fazerJogadaView = new FazerJogadaView(System.in, System.out, jogador);
            posicionarNaviosView = new PosicionarNaviosView(System.in, System.out, jogador);
            jogoView = new JogoView(System.in, System.out, tamanho);
        }

        // INFORMACOES QUE NÂO TEM FUNCÃO
        String estado = (String) pacote.get(ESTADO);
        String funcao = (String) pacote.get(FUNCAO);

        jogoView.setEstado(estado);

        // MUDANCAS
        List<Object[]> mudancas = (List<Object[]>) pacote.get(SETS);
        for (Object[] mudanca : mudancas) {
            int linha = (int) mudanca[1];
            int coluna = (int) mudanca[2];
            char valor = (char) mudanca[3];
            // Object[4] -> {String, int, int, value} -> FUNCAO(String), linha(int), coluna(int), VALOR(char)
            switch ((String) mudanca[0]) {
                case SET_ESPACO_ADVERSARIO:
                    jogoView.setEspacoAdversario(linha, coluna, valor);
                    break;
                case SET_ESPACO_PROPRIO:
                    jogoView.setEspacoProprio(linha, coluna, valor);
                    break;
            }
        }

        switch (funcao) {
            case FUNCAO_MOSTRA_TABULEIRO_PROPRIO:
                jogoView.mostrarTabuleiroProprio();
                break;
            case FUNCAO_MOSTRA_TABULEIRO_ADVERSARIO:
                jogoView.mostrarTabuleiroAdversario();
                break;
            case FUNCAO_MOSTRA_ESTADO:
                jogoView.mostrarEstado();
                break;
            case FUNCAO_LER_JOGADA:
                fazerJogadaView.lerJogada();
                break;
            case FUNCAO_LER_POSICOES:
                posicionarNaviosView.lerPosicoes();
                break;
            case FUNCAO_VOCE_PERDEU:
                jogoView.vocePerdeu();
                break;
            case FUNCAO_VOCE_GANHOU:
                jogoView.voceGanhou();
                break;
            case FUNCAO_ACABAR:
                acabou = true;
                socket.close();
                System.out.println("FIM");
                break;
        }
    }

    public void enviarResultados() throws IOException {
        int[][] resultados = new int[4][2];
        resultados[3][0] = fazerJogadaView.getLinha();
        resultados[3][1] = fazerJogadaView.getColuna();
        for (int i = 0; i < 3; i++) {
            resultados[i][0] = posicionarNaviosView.getLinha(i);
            resultados[i][1] = posicionarNaviosView.getColuna(i);
        }
        output.writeObject(resultados);
        output.flush();
        output.reset();
    }

}
