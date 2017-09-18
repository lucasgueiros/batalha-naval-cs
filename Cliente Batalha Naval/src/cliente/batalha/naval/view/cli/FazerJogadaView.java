/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.batalha.naval.view.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author lucas
 */
public class FazerJogadaView {
    
    private int[] jogada = new int[2];
    private Scanner scan;
    private String jogador;
    private final InputStream input;
    private final PrintStream output;
    
    public FazerJogadaView(InputStream input, PrintStream output, String jogador) {
        this.scan = new Scanner(input);
        this.jogador = jogador;
        this.input = input;
        this.output = output;
    }
    
    public void lerJogada() {
        output.println(jogador + ", escolha onde quer atirar!");
        output.print("Linha: ");
        jogada[0] = scan.nextInt();
        output.print("Coluna: ");
        jogada[1] = scan.nextInt();
    }
    
    public int getLinha() {
        return jogada[0];
    }
    
    public int getColuna() {
        return jogada[1];
    }
}
