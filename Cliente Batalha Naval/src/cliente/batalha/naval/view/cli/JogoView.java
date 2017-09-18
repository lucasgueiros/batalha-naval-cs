/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.batalha.naval.view.cli;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Mostra dois tabuleiros: um do próprio jogador e outro do adversário. Não
 * cuida de nada da lógica, apenas exibe as informações que lhe forem
 * concedidadas.
 *
 * @author lucas
 */
public class JogoView {

    private final InputStream input;
    private final PrintStream output;

    public JogoView(InputStream in, PrintStream out, int tam) {
        this.tam = tam;
        proprio = new char[tam][tam];
        adversario = new char[tam][tam];
        this.input = in;
        this.output = out;
    }

    private final int tam;
    private final char[][] proprio;
    private final char[][] adversario;
    private String estado = "Inicio";

    public void setEspacoAdversario(int linha, int coluna, char valor) {
        this.adversario[linha][coluna] = valor;
    }

    public void setEspacoProprio(int linha, int coluna, char valor) {
        this.proprio[linha][coluna] = valor;
    }

    public char getEspacoProprio(int linha, int coluna) {
        return this.proprio[linha][coluna];
    }

    public char getEspacoAdversario(int linha, int coluna) {
        return this.adversario[linha][coluna];
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void mostrarTabuleiroProprio() {
        output.println("Seu tabuleiro:\n");
        output.println(tabuleiroToString(proprio));
    }

    public void mostrarTabuleiroAdversario() {
        output.println("Tabuleiro adversário:\n");
        output.println(tabuleiroToString(adversario));
    }

    private String tabuleiroToString(char[][] tabuleiro) {
        String saida = "+ 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 +\n";
        saida += "+---+---+---+---+---+---+---+---+---+---+\n";
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                saida += "| " + tabuleiro[i][j] + " ";
            }
            saida += "|\n+---+---+---+---+---+---+---+---+---+---+\n";
        }
        return saida;
    }

    public void mostrarEstado() {
        output.println("Situação atual do jogo: " + estado);
    }

    public void voceGanhou() {
        estado = "Você ganhou!";
    }

    public void vocePerdeu() {
        estado = "Você perdeu!";
    }

    public void jogoIniciado() {
        estado = "Jogo em andamento";
    }
    
}
