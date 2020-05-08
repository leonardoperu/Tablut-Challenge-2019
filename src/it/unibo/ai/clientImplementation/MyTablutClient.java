package it.unibo.ai.clientImplementation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

/**
 * 
 * @author D.Altena, M.Minerva, L.Perugini, M.Rossi
 *
 */

public class MyTablutClient extends TablutClient {
	private static int tempo=59;

	public MyTablutClient(String player) throws UnknownHostException, IOException {
		super(player, "IterativeCoffeeSearch");
	}

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);

		if(args.length>1)
			try {
				tempo=Integer.parseInt(args[1])-1;
			}catch (Exception e) {
				e.printStackTrace();
			}
		
		TablutClient client = new MyTablutClient(args[0]);

		client.run();

	}

	@Override
	public void run() {
		System.out.println("You are player " + this.getPlayer().toString() + "!");
		Action action;
		
		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (this.getPlayer() == Turn.WHITE) {
			MyGame game = new MyGameAshtonTablut("white");
			IterativeDeepeningAlphaBetaSearch<State, Action, Turn> search = new MySearch(game, -500.0, 500.0, tempo); // TODO tempo in secondi, sistemare!
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			BufferedWriter writer_ricerca=null, writer_partita=null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");  
			//String nomeFile_ricerca="tablutLogWhite"+formatter.format(new Date())+"_ricerca.txt", nomeFile_partita="tablutLogWhite"+formatter.format(new Date())+"_partita.txt";
			File dir = new File("Log");
			if (!dir.exists())
				dir.mkdir();
			String dir_str = "Log"+File.separator+"White"+formatter.format(new Date());
			dir = new File(dir_str);
			dir.mkdir();
			String nomeFile_ricerca = dir_str+File.separator+"LogWhite"+"_ricerca.txt",
					nomeFile_partita = dir_str+File.separator+"LogWhite"+"_partita.txt";
			while (true) {
				try {
					this.read();
					game.updateStorico(this.getCurrentState());

					System.out.println("Current state:");
					System.out.println(this.getCurrentState().toString());
					writer_ricerca = new BufferedWriter(new FileWriter(nomeFile_ricerca, true));
					writer_partita = new BufferedWriter(new FileWriter(nomeFile_partita, true));
					writer_partita.append("Current state:");
					writer_partita.newLine();
					writer_partita.append(this.getCurrentState().toString());
					writer_partita.newLine();
					if (this.getCurrentState().getTurn().equals(this.getPlayer())) {
						action = search.makeDecision(getCurrentState());
						this.write(action);
						
						writer_ricerca.append(search.getMetrics().toString());
						writer_ricerca.newLine();
						
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
						System.out.println("YOU WIN!");
						break;
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
						System.out.println("YOU LOSE!");
						break;
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
						System.out.println("DRAW!");
						break;
					}
					writer_partita.close();
					writer_ricerca.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			System.exit(0);
		} else {
			MyGame game = new MyGameAshtonTablut("black");
			IterativeDeepeningAlphaBetaSearch<State, Action, Turn> search = new MySearch(game, -500.0, 500.0, tempo); // TODO tempo in secondi, sistemare!
			System.out.println("You are player " + this.getPlayer().toString() + "!");
			BufferedWriter writer_ricerca=null, writer_partita=null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");  
			//String nomeFile_ricerca="tablutLogBlack"+formatter.format(new Date())+"_ricerca.txt", nomeFile_partita="tablutLogBlack"+formatter.format(new Date())+"_partita.txt";
			File dir = new File("Log");
			if (!dir.exists())
				dir.mkdir();
			String dir_str = "Log"+File.separator+"Black"+formatter.format(new Date());
			dir = new File(dir_str);
			dir.mkdir();
			String nomeFile_ricerca = dir_str+File.separator+"LogBlack"+"_ricerca.txt",
					nomeFile_partita = dir_str+File.separator+"LogBlack"+"_partita.txt";
			while (true) {
				try {
					this.read();
					game.updateStorico(this.getCurrentState());
					
					System.out.println("Current state:");
					System.out.println(this.getCurrentState().toString());
					writer_ricerca = new BufferedWriter(new FileWriter(nomeFile_ricerca, true));
					writer_partita = new BufferedWriter(new FileWriter(nomeFile_partita, true));
					writer_partita.append("Current state:");
					writer_partita.newLine();
					writer_partita.append(this.getCurrentState().toString());
					writer_partita.newLine();
					if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
						action = search.makeDecision(getCurrentState());
						this.write(action);
						
						writer_ricerca.append(search.getMetrics().toString());
						writer_ricerca.newLine();
						
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
						System.out.println("Waiting for your opponent move... ");
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
						System.out.println("YOU LOSE!");
						break;
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
						System.out.println("YOU WIN!");
						break;
					} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
						System.out.println("DRAW!");
						break;
					}
					writer_partita.close();
					writer_ricerca.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				
			}
			System.exit(0);
		}
	}

}
