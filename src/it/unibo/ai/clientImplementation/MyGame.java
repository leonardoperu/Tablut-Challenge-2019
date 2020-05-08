package it.unibo.ai.clientImplementation;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

/**
 * 
 * @author Altena, Minerva, Perugini, Rossi
 *
 */

public abstract class MyGame implements Game<State, Action, Turn> {
	
	protected List<Pawn[][]> storico;
	protected Turn player;
	
	public MyGame(String player) {
		storico=new ArrayList<Pawn[][]>();
		if(player.equals("white"))
			this.player=Turn.WHITE;
		else this.player=Turn.BLACK;
	}

	/*
	 * Ritorna le mosse che posso fare dato lo stato state
	 */
	@Override
	public abstract List<Action> getActions(State state);

	/*
	 * Ritorna lo stato risultante dallo stato state applicando l'azione a.
	 * Suppongo che l'azione sia legale.
	 */
	@Override
	public State getResult(State state, Action a) {
		State newState=state.clone();
		Pawn[][] newBoard=newState.getBoard();
		
		newBoard[a.getRowTo()][a.getColumnTo()] = newState.getPawn(a.getRowFrom(), a.getColumnFrom());
		newBoard[a.getRowFrom()][a.getColumnFrom()] = State.Pawn.EMPTY;
		
		newState.setBoard(newBoard);
		
		if(a.getTurn().equalsTurn("W")) {
			checkCaptureWhite(newState, a); //SE C'E' UNA CATTURA RIMUOVE DALLO STATO LA PEDINA
			newState.setTurn(Turn.BLACK); //cambio il turno nello stato
		}
		else {
			checkCaptureBlack(newState, a);
			newState.setTurn(Turn.WHITE);
		}			
		
		// TODO LO STATO VIENE CREATO, SI MODIFICA IL PRECEDENTE 
		return newState;
	}
	
	protected abstract State checkCaptureWhite(State state, Action a);
	protected abstract State checkCaptureBlack(State state, Action a);
		
	/*
	 * Ritorna lo stato iniziale dal gioco, cioe' quello da cui parte
	 */
	@Override
	public State getInitialState() {
		return new StateTablut();
	}
	
	/*
	 * Ritorna il giocatore a cui spetta il turno
	 */
	@Override
	public Turn getPlayer(State stato) {
		return player;
	}

	/*
	 * Ritorna i giocatori
	 */
	@Override
	public Turn[] getPlayers() {
		Turn[] res= new Turn[2];
		res[0]=State.Turn.WHITE;
		res[1]=State.Turn.BLACK;
		return res;
	}
	
	protected boolean storicoContains(State currentState, Action a) {
		boolean uguale=true;
		Pawn [][] newBoard = getResult(currentState.clone(), a).getBoard(); 
		
		for (Pawn[][] s : storico) {
			for (int i=0; i<newBoard.length && uguale; i++) {
				for (int j=0; j<newBoard.length && uguale; j++) {
					if (!newBoard[i][j].equals(s[i][j]))
						uguale = false;
				}
			}
			if (uguale)
				return true;
			else
				uguale = true;
		}
		return false;
	}

	/*
	 * controlla se la pedina specificata si trova in quella riga
	 */
	private boolean checkRiga(State state, String pedina, int indiceRiga) {
		for (int i=0; i<state.getBoard().length; i++)
			if (state.getPawn(indiceRiga, i).equalsPawn(pedina)) {
				return true;
			}
		return false;
	}
	
	/*
	 * controlla se la pedina specificata si trova in quella colonna
	 */
	private boolean checkColonna(State state, String pedina, int indiceColonna) {
		for (int i=0; i<state.getBoard().length; i++)
			if (state.getPawn(i, indiceColonna).equalsPawn(pedina)) {
				return true;
			}
		return false;
	}

	public abstract void updateStorico(State s);
	
	public List<Pawn[][]> getStorico(){
		return this.storico;
	}
	
	public Pawn[][] getLastStorico(){
		return this.storico.get(this.storico.size()-1);
	}
}
