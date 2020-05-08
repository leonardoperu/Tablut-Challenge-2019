package it.unibo.ai.clientImplementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

/**
 * 
 * @author Altena, Minerva, Perugini, Rossi
 *
 */

public class MyGameAshtonTablut extends MyGame {
	
	private short numPedineStoricoWhite = 9; //compreso il re
	private short numPedineStoricoBlack = 16;
	
	private List<String> citadels;
	
	public MyGameAshtonTablut(String player) {
		super(player);
		
		super.storico=new ArrayList<Pawn[][]>();
		
		this.citadels = new ArrayList<String>();
		this.citadels.add("a4");
		this.citadels.add("a5");
		this.citadels.add("a6");
		this.citadels.add("b5");
		this.citadels.add("d1");
		this.citadels.add("e1");
		this.citadels.add("f1");
		this.citadels.add("e2");
		this.citadels.add("i4");
		this.citadels.add("i5");
		this.citadels.add("i6");
		this.citadels.add("h5");
		this.citadels.add("d9");
		this.citadels.add("e9");
		this.citadels.add("f9");
		this.citadels.add("e8");		
	}

	/*
	 * Ritorna le mosse che posso fare dato lo stato state
	 */
	@Override
	public List<Action> getActions(State state) {
		Turn myPlayer = state.getTurn();
		List<Action> res=new ArrayList<Action>();
		
		for (int i = 0; i< state.getBoard().length; i++)//riga
			for (int j = 0; j< state.getBoard().length; j++) {//colonna
				if(state.getPawn(i, j).equalsPawn(myPlayer.toString()) ||
						(state.getTurn().equals(Turn.WHITE) && state.getPawn(i, j).equalsPawn("K")) ) {//questa pedina e' la mia
					
					//direzione nord
					for(int k=i-1; k>=0; k--) {//riga
						if (state.getPawn(k, j).equalsPawn("O")) {
							String to = state.getBox(k, j);  //(String.valueOf('a'+j) + k);
							String from = state.getBox(i, j); //(String.valueOf('a'+j) + i);
							if (!citadels.contains(to) ||
									(citadels.contains(from) && i-k<=2)) { 
								try {
									Action a = new Action(from, to, myPlayer);
									/****
									 * CONTROLLO PAREGGIO
									 * .clone per essere sicuri che il calcolo dello stato dopo la mossa
									 *  non vada a modificare lo stato attuale
									 *
									if (!storico.contains(super.getResult(state.clone(), a).getBoard()))
											res.add(a);
									*/
									if (!super.storicoContains(state, a))
										res.add(a);
								} catch(IOException e) {
									e.printStackTrace();
								}
							} else {
								break;	//esco se incontro cittadella (e non partivo da cittadella)
							}
						}
						else
							break;		//esco se trovo casella occupata (B/W/K/T)
					}
					
					//direzione sud
					for(int k=i+1; k<state.getBoard().length; k++) {//riga
						if (state.getPawn(k, j).equalsPawn("O")) {
							String to = state.getBox(k, j); //(String.valueOf('a'+j) + k);
							String from = state.getBox(i, j); //(String.valueOf('a'+j) + i);
							if (!citadels.contains(to) ||
									(citadels.contains(from) && k-i<=2)) { 
								try {
									Action a = new Action(from, to, myPlayer);
									//if (!storico.contains(super.getResult(state.clone(), a).getBoard()))
									//		res.add(a);
									if (!super.storicoContains(state, a))
										res.add(a);
								} catch(IOException e) {
									e.printStackTrace();
								}
							} else {
								break;	//esco se incontro cittadella (e non partivo da cittadella)
							}
						}
						else
							break;		//esco se trovo casella occupata (B/W/K/T)
					}
					
					//direzione ovest
					for(int z=j-1; z>=0; z--) {//colonna
						if (state.getPawn(i, z).equalsPawn("O")) {
							String to = state.getBox(i, z); //(String.valueOf('a'+z) + i);
							String from = state.getBox(i, j); //(String.valueOf('a'+j) + i);
							if (!citadels.contains(to) ||
									(citadels.contains(from) && j-z<=2)) { 
								try {
									Action a = new Action(from, to, myPlayer);
									//if (!storico.contains(super.getResult(state.clone(), a).getBoard()))
									//	res.add(a);
									if (!super.storicoContains(state, a))
										res.add(a);
								} catch(IOException e) {
									e.printStackTrace();
								}
							} else {
								break;	//esco se incontro cittadella (e non partivo da cittadella)
							}
						}
						else
							break;		//esco se trovo casella occupata (B/W/K/T)
					}
					
					//direzione est
					for(int z=j+1; z<state.getBoard().length; z++) {//colonna
						if (state.getPawn(i, z).equalsPawn("O")) {
							String to = state.getBox(i, z); //(String.valueOf('a'+z) + i);
							String from = state.getBox(i, j); //(String.valueOf('a'+j) + i);
							if (!citadels.contains(to) ||
									(citadels.contains(from) && z-j<=2)) { 
								try {
									Action a = new Action(from, to, myPlayer);
									//if (!storico.contains(super.getResult(state.clone(), a).getBoard()))
									//	res.add(a);
									if (!super.storicoContains(state, a))
										res.add(a);
								} catch(IOException e) {
									e.printStackTrace();
								}
							} else {
								break;	//esco se incontro cittadella (e non partivo da cittadella)
							}
						}
						else
							break;		//esco se trovo casella occupata (B/W/K/T)
					}
				}
			}
		
		return res;
	}	

	public List<String> getCitadels() {
		return citadels;
	}

	protected State checkCaptureWhite(State state, Action a) {	//SE C'E' UNA CATTURA RIMUOVE DALLO STATO LA PEDINA
		// controllo se mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2
			&& state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("B")
			&& (state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("W")
			|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T")
			|| state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("K")
			|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2)) &&!(a.getColumnTo()+2==8&&a.getRowTo()==4)&&!(a.getColumnTo()+2==4&&a.getRowTo()==0)&&!(a.getColumnTo()+2==4&&a.getRowTo()==8)&&!(a.getColumnTo()+2==0&&a.getRowTo()==4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
		}
		// controllo se mangio a sinistra
		if (a.getColumnTo() > 1 && state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("B")
			&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("W")
			|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
			|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("K")
			|| (this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2)) &&!(a.getColumnTo()-2==8&&a.getRowTo()==4)&&!(a.getColumnTo()-2==4&&a.getRowTo()==0)&&!(a.getColumnTo()-2==4&&a.getRowTo()==8)&&!(a.getColumnTo()-2==0&&a.getRowTo()==4)))) {
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
		}
		// controllo se mangio sopra
		if (a.getRowTo() > 1 && state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("B")
			&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("W")
			|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
			|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("K")
			|| (this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))&&!(a.getColumnTo()==8&&a.getRowTo()-2==4)&&!(a.getColumnTo()==4&&a.getRowTo()-2==0)&&!(a.getColumnTo()==4&&a.getRowTo()-2==8)&&!(a.getColumnTo()==0&&a.getRowTo()-2==4)) )) {
			state.removePawn(a.getRowTo() - 1, a.getColumnTo());
		}
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
			&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("B")
			&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("W")
			|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
			|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("K")
			|| (this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))&&!(a.getColumnTo()==8&&a.getRowTo()+2==4)&&!(a.getColumnTo()==4&&a.getRowTo()+2==0)&&!(a.getColumnTo()==4&&a.getRowTo()+2==8)&&!(a.getColumnTo()==0&&a.getRowTo()+2==4)))) {
			state.removePawn(a.getRowTo() + 1, a.getColumnTo());
		}
		// controllo se ho vinto
		if (a.getRowTo() == 0 || a.getRowTo() == state.getBoard().length - 1 || a.getColumnTo() == 0
			|| a.getColumnTo() == state.getBoard().length - 1) {
			if (state.getPawn(a.getRowTo(), a.getColumnTo()).equalsPawn("K")) {
				state.setTurn(State.Turn.WHITEWIN);
			}
		}
		return state;
	}
	
	
	private State checkCaptureBlackKingLeft(State state, Action a){
		//ho il re sulla sinistra
		if (a.getColumnTo()>1 && state.getPawn(a.getRowTo(), a.getColumnTo()-1).equalsPawn("K"))
		{
			//re sul trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()-1);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e4"))
			{
				if(state.getPawn(2, 4).equalsPawn("B")
						&& state.getPawn(3, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()-1);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e6"))
			{
				if(state.getPawn(5, 3).equalsPawn("B")
						&& state.getPawn(6, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()-1);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("f5"))
			{
				if(state.getPawn(3, 5).equalsPawn("B")
						&& state.getPawn(5, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()-1);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()-1).equals("f5"))
			{
				if(state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo()-2)))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()-1);
				}					
			}
		}		
		return state;
	}
	
	private State checkCaptureBlackKingRight(State state, Action a){
		//ho il re sulla destra
		if (a.getColumnTo()<state.getBoard().length-2 && (state.getPawn(a.getRowTo(),a.getColumnTo()+1).equalsPawn("K")))				
		{
			//re sul trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(5, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()+1);
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e4"))
			{
				if(state.getPawn(2, 4).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()+1);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e6"))
			{
				if(state.getPawn(5, 5).equalsPawn("B")
						&& state.getPawn(6, 4).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()+1);
				}
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("d5"))
			{
				if(state.getPawn(3, 3).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B")) //cambiato: prima era (3, 5), quindi non catturava il re nel momento giusto!
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()+1);
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("d5")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e6")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e4")
					&& !state.getBox(a.getRowTo(), a.getColumnTo()+1).equals("e5"))
			{
				if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B")){
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()+1);
				}		
				if(this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2))){
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo(), a.getColumnTo()+1);
				}		
			}
		}
		return state;
	}
	
	private State checkCaptureBlackKingDown(State state, Action a){
		//ho il re sotto
		if (a.getRowTo()<state.getBoard().length-2&&state.getPawn(a.getRowTo()+1,a.getColumnTo()).equalsPawn("K"))
		{
			//System.out.println("Ho il re sotto");
			//re sul trono
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(5, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()+1, a.getColumnTo());
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e4"))
			{
				if(state.getPawn(3, 3).equalsPawn("B")
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()+1, a.getColumnTo());
				}
			}
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("d5"))
			{
				if(state.getPawn(4, 2).equalsPawn("B")
						&& state.getPawn(5, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()+1, a.getColumnTo());
				}
			}
			if(state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("f5"))
			{
				if(state.getPawn(4, 6).equalsPawn("B")
						&& state.getPawn(5, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()+1, a.getColumnTo());
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo()+1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(a.getRowTo()+2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo()+2, a.getColumnTo())))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()+1, a.getColumnTo());
				}					
			}			
		}		
		return state;
	}
	
	private State checkCaptureBlackKingUp(State state, Action a){
		//ho il re sopra
		if (a.getRowTo()>1&&state.getPawn(a.getRowTo()-1, a.getColumnTo()).equalsPawn("K"))
		{
			//re sul trono
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(3, 4).equalsPawn("B")
						&& state.getPawn(4, 5).equalsPawn("B")
						&& state.getPawn(4, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()-1, a.getColumnTo());
				}
			}
			//re adiacente al trono
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e6"))
			{
				if(state.getPawn(5, 3).equalsPawn("B")
						&& state.getPawn(5, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()-1, a.getColumnTo());
				}
			}
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("d5"))
			{
				if(state.getPawn(4, 2).equalsPawn("B")
						&& state.getPawn(3, 3).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()-1, a.getColumnTo());
				}
			}
			if(state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("f5"))
			{
				if(state.getPawn(4, 6).equalsPawn("B") //cambiato: prima era (4,4)
						&& state.getPawn(3, 5).equalsPawn("B"))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()-1, a.getColumnTo());
				}
			}
			//sono fuori dalle zone del trono
			if(!state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("d5")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e4")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("f5")
					&& !state.getBox(a.getRowTo()-1, a.getColumnTo()).equals("e5"))
			{
				if(state.getPawn(a.getRowTo()-2, a.getColumnTo()).equalsPawn("B")
						|| this.citadels.contains(state.getBox(a.getRowTo()-2, a.getColumnTo())))
				{
					state.setTurn(State.Turn.BLACKWIN);
					state.removePawn(a.getRowTo()-1, a.getColumnTo());
				}					
			}	
		}
		return state;
	}
	
	private State checkCaptureBlackPawnRight(State state, Action a)	{
		//mangio a destra
		if (a.getColumnTo() < state.getBoard().length - 2 && state.getPawn(a.getRowTo(), a.getColumnTo() + 1).equalsPawn("W"))
		{
			if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("B"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}
			if(state.getPawn(a.getRowTo(), a.getColumnTo() + 2).equalsPawn("T"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}
			if(this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() + 2)))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}
			if(state.getBox(a.getRowTo(), a.getColumnTo()+2).equals("e5"))
			{
				state.removePawn(a.getRowTo(), a.getColumnTo() + 1);
			}
			
		}
		
		return state;
	}
	
	private State checkCaptureBlackPawnLeft(State state, Action a){
		//mangio a sinistra
		if (a.getColumnTo() > 1
				&& state.getPawn(a.getRowTo(), a.getColumnTo() - 1).equalsPawn("W")
				&& (state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("B")
						|| state.getPawn(a.getRowTo(), a.getColumnTo() - 2).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo(), a.getColumnTo() - 2))
						|| (state.getBox(a.getRowTo(), a.getColumnTo()-2).equals("e5"))))
		{
			state.removePawn(a.getRowTo(), a.getColumnTo() - 1);
		}
		return state;
	}
	
	private State checkCaptureBlackPawnUp(State state, Action a){
		// controllo se mangio sopra
		if (a.getRowTo() > 1
				&& state.getPawn(a.getRowTo() - 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() - 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() - 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo()-2, a.getColumnTo()).equals("e5"))))
		{
			state.removePawn(a.getRowTo()-1, a.getColumnTo());
		}
		return state;
	}
	
	private State checkCaptureBlackPawnDown(State state, Action a){
		// controllo se mangio sotto
		if (a.getRowTo() < state.getBoard().length - 2
				&& state.getPawn(a.getRowTo() + 1, a.getColumnTo()).equalsPawn("W")
				&& (state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("B")
						|| state.getPawn(a.getRowTo() + 2, a.getColumnTo()).equalsPawn("T")
						|| this.citadels.contains(state.getBox(a.getRowTo() + 2, a.getColumnTo()))
						|| (state.getBox(a.getRowTo()+2, a.getColumnTo()).equals("e5"))))
		{
			state.removePawn(a.getRowTo()+1, a.getColumnTo());
		}
		return state;
	}


	protected State checkCaptureBlack(State state, Action a) {
		this.checkCaptureBlackPawnRight(state, a);
		this.checkCaptureBlackPawnLeft(state, a);
		this.checkCaptureBlackPawnUp(state, a);
		this.checkCaptureBlackPawnDown(state, a);
		this.checkCaptureBlackKingRight(state, a);
		this.checkCaptureBlackKingLeft(state, a);
		this.checkCaptureBlackKingDown(state, a);
		this.checkCaptureBlackKingUp(state, a);

		return state;
	}

	/*
	 * Ritorna +1 se il giocatore player nello stato state ha vinto, -1 se ha perso, 0 in caso di pareggio
	 */
	@Override
	public double getUtility(State state, Turn player) {
		// controllo se ha vinto il bianco
		if (checkRiga(state, "K", 0) || checkRiga(state, "K", state.getBoard().length - 1) || checkColonna(state, "K", 0) || checkColonna(state, "K", state.getBoard().length - 1)) {
			if(player.equalsTurn("W"))
				return 500.0;
			else return -500.0;
		}
				
		// controllo se ha vinto il nero
		boolean foundRe=false;
		for(int i=0; i<state.getBoard().length; i++) {
			if (checkRiga(state, "K", i))
				foundRe=true;
		}
		if(!foundRe) {
			if(player.equalsTurn("W"))
				return -500.0;
			else return 500.0;
		}		
			
		// CASO DEL PAREGGIO
		return 0.0;
	}

	@Override
	public boolean isTerminal(State state) {
		// controllo se ha vinto il bianco
		if (checkRiga(state, "K", 0) || checkRiga(state, "K", state.getBoard().length - 1) || checkColonna(state, "K", 0) || checkColonna(state, "K", state.getBoard().length - 1)) {
			return true;
		}
		
		// controllo se ha vinto il nero
		boolean foundRe=false;
		for(int i=0; i<state.getBoard().length; i++) {
			if (checkRiga(state, "K", i))
				foundRe=true;
		}
		return !foundRe;
		
		// TODO FAI IL CASO DEL PAREGGIO
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

	
	public void updateStorico(State s) {
		short numB=0, numW=0;
		for (int i=0; i<s.getBoard().length; i++) {
			for (int j=0; j<s.getBoard().length; j++) {
				if (s.getBox(i, j).equals("B"))
					numB++;
				else if (s.getBox(i, j).equals("W") || s.getBox(i, j).equals("K"))
					numW++;
			}
		}
		if (numB+numW < this.numPedineStoricoWhite+this.numPedineStoricoBlack) { //se sono state mangiate delle pedine cancello lo storico
			this.numPedineStoricoWhite = numW;
			this.numPedineStoricoBlack = numB;

			this.storico.clear();
		} 
		storico.add(s.getBoard());
	}

	public short getNumPedineStoricoWhite() {
		return numPedineStoricoWhite;
	}

	public short getNumPedineStoricoBlack() {
		return numPedineStoricoBlack;
	}
	
}
