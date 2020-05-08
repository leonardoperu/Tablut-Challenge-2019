package it.unibo.ai.clientImplementation;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;

/**
 * 
 * @author Altena, Minerva, Perugini, Rossi
 *
 */

public class MySearch extends IterativeDeepeningAlphaBetaSearch<State, Action, Turn> {
	
	/*
	 * punteggi per l'euristica
	 */
	private final double punteggioBiancheMangiate=-3;
	private final double punteggioNereMangiate=-2;
	private final double[][] punteggiNere= {{+0.0, +0.2, -0.1, +0.0, +0.0},
											{+0.2, +0.0, +0.2, -0.1, +0.0},
											{-0.1, +0.2, +0.3, +0.1, -0.1}, 
											{+0.0, -0.1, +0.1, +0.0, -0.1}, 
											{+0.0, +0.0, -0.1, -0.1, +0.0}};
	private final double[][] punteggiBianche=  {{+0.0, -0.1, -0.2, +0.0, +0.0},
												{-0.1, +0.0, -0.1, -0.1, +0.0},
												{-0.2, -0.1, +0.0, +0.0, -0.1},
												{+0.0, -0.1, +0.0, +0.0, -0.1},
												{+0.0, +0.0, -0.1, -0.1, +0.0}};			 
	private final double[][] punteggiRe=   {{+0.0, +0.0, +0.0, +0.0, +0.0},
											{+0.0, +0.0, +0.0, -0.5, +0.0},
											{+0.0, +0.0, +0.5, +0.2, -0.3}, 
											{+0.0, -0.5, +0.2, +0.0, +0.2}, 
											{+0.0, +0.0, -0.3, +0.2, +0.1}};
	private final double[][] punteggiNereQuadranteLibero= { {-1.0, -1.0, -1.0, -0.6, +0.0},
			   												{-1.0, -1.0, -1.0, -1.0, +0.0},
			   												{-1.0, -1.0, -1.0, +1.5, +0.0}, 
			   												{-0.6, -1.0, +1.5, +0.0, +0.0}, 
			   												{+0.0, +0.0, +0.0, +0.0, +0.0} };
	private final double[][] punteggiBiancheQuadranteRe=  { {+0.0, +0.0, +1.0, +0.0, +0.0},
															{+0.0, +0.0, +0.0, +1.0, +0.0},
															{+1.0, +0.0, -0.5, +0.0, +0.8}, 
															{+0.0, +1.0, +0.0, +0.0, +0.8}, 
															{+0.0, +0.0, +0.8, +0.8, +0.0} };
	
	private final double bianchiRe4=-0.4;
	private final double bianchiRe3=+0.0;
	private final double bianchiRe1=+0.5;
	private final double bianchiRe2Opposti=+0.4;
	private final double bianchiRe2DiversaDir=+1.0;
	private final double rischoAttaccoRe=-50.0;
	private final double reRigaLibera = -50.0;
	private final double avvicinamentoAlRe=+3.5; /// CAMBIARE SU ONEDRIVE!!!! ///
												 /// con +2.0 il nero stravince (contro nostro W), ma perde contro BrainlessNick White. Nero previene strategia Terzi.
												 /// con +5.0 il bianco vince, il nero vince contro BrainlessNick e perde contro Terzi (ma avevamo anche 5 e 3 ai mangiare)
												 /// con +3.5 il nero vince e il nero vince contro BrainlessNick e previene strategia Terzi.
												 /// con +4.0 il bianco vince (molto equilibrata), nero vince contro BrainlessNick
												 /// con +2.5 nero vince contro BrainlessNick
	//private int[] wWins = {1,2,6,7}; 
	
	public MySearch(Game<State, Action, Turn> game, double utilMin, double utilMax, int time) {
		super(game, utilMin, utilMax, time);
	}
	
	/*
	 * player e' il risultato di game.getPlayer(state), che nel nostro caso ritorna player
	 * */
	@Override
	protected double eval(State state, Turn player) {
		super.eval(state, player);
		
		if (game.isTerminal(state)) {
	            return game.getUtility(state, player);
	    } else {
	    	double punteggio=0.0;
	    	
	    	if (player.equalsTurn("W")) {
	    		punteggio=evalWhite(state)-evalBlack(state);
			} else {
				punteggio=evalBlack(state)-evalWhite(state);
			}
	    	return punteggio;
	    }
	}
	
	/*
	 * funzioni che valutano lo stato della board in modo "assoluto" guardandola dal punto di vista del bianco o del nero
	 */
	private double evalWhite(State s) {
		double punteggio=0.0;
		int rigaRe=0, colonnaRe=0;
		int quadranteRe=0;
		
		//punteggio derivante dal fatto di avere pedine bianche mangiate
		short numW=0;
		for (int i=0; i<s.getBoard().length; i++) 
			for (int j=0; j<s.getBoard().length; j++) {
				if (s.getPawn(i, j).equalsPawn("W") || s.getPawn(i, j).equalsPawn("K"))
					numW++;
				if(s.getPawn(i, j).equalsPawn("K")) {
					if (i<4 && j<4) quadranteRe=1; //primo quadrante (alto sx)
					if (i>4 && j<4) quadranteRe=2; //secondo quadrante (basso sx)
					if (i>4 && j>4) quadranteRe=3; //terzo quadrante (basso dx)
					if (i<4 && j>4) quadranteRe=4; //quarto quadrante (alto dx)
				}
			}
		int biancheMangiate=((MyGameAshtonTablut)super.game).getNumPedineStoricoWhite()-numW;
		
		punteggio+=biancheMangiate*punteggioBiancheMangiate;
		
		//punteggio derivante dalla posizione della singola pedina sulla scacchiera
		for (int i=0; i<s.getBoard().length; i++) 
			for (int j=0; j<s.getBoard().length; j++) 
				if (s.getPawn(i, j).equalsPawn("W")) {
					int indexRiga, indexColonna; //indici per accedere alle matrici dei punteggi
					if(i<5)
						indexRiga=i;
					else indexRiga=s.getBoard().length-1-i;
					if(j<5)
						indexColonna=j;
					else indexColonna=s.getBoard().length-1-j;					
					punteggio+=this.punteggiBianche[indexRiga][indexColonna];
					if (i<5 && j<5 && quadranteRe==1)
						punteggio+=this.punteggiBiancheQuadranteRe[indexRiga][indexColonna];
					if (i>3 && j<5 && quadranteRe==2)
						punteggio+=this.punteggiBiancheQuadranteRe[indexRiga][indexColonna];
					if (i>3 && j>3 && quadranteRe==3)
						punteggio+=this.punteggiBiancheQuadranteRe[indexRiga][indexColonna];
					if (i<5 && j>3 && quadranteRe==4)
						punteggio+=this.punteggiBiancheQuadranteRe[indexRiga][indexColonna];
				} else if (s.getPawn(i, j).equalsPawn("K")) {
					//salvo le coordinate del re (mi servono dopo)
					rigaRe=i;
					colonnaRe=j;
					
					int indexRiga, indexColonna; //indici per accedere alle matrici dei punteggi
					if(i<5)
						indexRiga=i;
					else indexRiga=s.getBoard().length-1-i;
					if(j<5)
						indexColonna=j;
					else indexColonna=s.getBoard().length-1-j;					
					punteggio+=this.punteggiRe[indexRiga][indexColonna];
				}
		
		
		//punteggio derivante dalla posizione relativa delle pedine nella scacchiera
		//consideriamo solo le bianche normali perche' questi punteggi per il re vengono calcolati indirettamente dagli altri
		
		//re
		//punteggio positivo se ha dei bianchi di fianco
		short nord=0, sud=0, ovest=0, est=0;
		
		if(s.getPawn(rigaRe, colonnaRe-1).equalsPawn("W"))
			ovest=1;
		if(s.getPawn(rigaRe, colonnaRe+1).equalsPawn("W"))
			est=1;
		if(s.getPawn(rigaRe-1, colonnaRe).equalsPawn("W"))
			nord=1;
		if(s.getPawn(rigaRe+1, colonnaRe).equalsPawn("W"))
			sud=1;
		if(nord+sud+ovest+est==4)
			punteggio+=bianchiRe4;
		else if(nord+sud+ovest+est==3)
			punteggio+=bianchiRe3;
		else if(nord+sud+ovest+est==1)
			punteggio+=bianchiRe1;
		else if(nord+sud==1 && est+ovest==1)
			punteggio+=bianchiRe2DiversaDir;
		else if (nord+sud==2 || est+ovest==2)
			punteggio+=bianchiRe2Opposti;
		
		//punteggio negativo se ha dei neri o delle cittadelle di fianco (=rischio di essere mangiato)
		//non rischio di uscire dai bound della scacchiera perche' se il re e' nel bordo ha vinto -> questa funzione non viene chiamata
		//queste configurazioni non comprendono le configurazioni in cui il re si infila in mezzo a 2 nere (che non sono rischiose per il re)
		if(s.getPawn(rigaRe, colonnaRe-1).equalsPawn("B") || ((MyGameAshtonTablut)super.game).getCitadels().contains(s.getBox(rigaRe, colonnaRe-1))) {
			if(trovaAttaccante(s, rigaRe, colonnaRe+1, Pawn.WHITE))
				punteggio+=rischoAttaccoRe;
		}
		if(s.getPawn(rigaRe, colonnaRe+1).equalsPawn("B")|| ((MyGameAshtonTablut)super.game).getCitadels().contains(s.getBox(rigaRe, colonnaRe+1))) {
			if(trovaAttaccante(s, rigaRe, colonnaRe-1, Pawn.WHITE))
				punteggio+=rischoAttaccoRe;
		}
		if(s.getPawn(rigaRe-1, colonnaRe).equalsPawn("B")|| ((MyGameAshtonTablut)super.game).getCitadels().contains(s.getBox(rigaRe-1, colonnaRe))) {
			if(trovaAttaccante(s, rigaRe+1, colonnaRe, Pawn.WHITE))
				punteggio+=rischoAttaccoRe;
		}
		if(s.getPawn(rigaRe+1, colonnaRe).equalsPawn("B")|| ((MyGameAshtonTablut)super.game).getCitadels().contains(s.getBox(rigaRe+1, colonnaRe))) {
			if(trovaAttaccante(s, rigaRe-1, colonnaRe, Pawn.WHITE))
				punteggio+=rischoAttaccoRe;
		}
			
		//TODO bianchi normali -> scegliamo di non dare punteggio per questo per semplificare
		//punteggio positivo se ha dei bianchi di fianco
		//punteggio negativo se ha dei neri o delle cittadelle o il castello di fianco (=rischio di essere mangiato)
		
		return punteggio;
	}
	
	private double evalBlack(State s) {
		double punteggio=0.0;
		short q1_b=0, q2_b=0, q3_b=0, q4_b=0; //numero pedine nere in ogni quadrante
		short q1_w=0, q2_w=0, q3_w=0, q4_w=0; //numero pedine bianche in ogni quadrante
		
		//punteggio derivante dal fatto di avere pedine nere mangiate
		short numB=0, numW=1;
		for (int i=0; i<s.getBoard().length; i++) 
			for (int j=0; j<s.getBoard().length; j++) 
				if (s.getPawn(i, j).equalsPawn("B")) {
					numB++;
					if (i<4 && j<4) q1_b++; //nera nel primo quadrante (alto sx)
					if (i>4 && j<4) q2_b++; //nera nel secondo quadrante (basso sx)
					if (i>4 && j>4) q3_b++; //nera nel terzo quadrante (basso dx)
					if (i<4 && j>4) q4_b++; //nera nel quarto quadrante (alto dx)
				} else if (s.getPawn(i, j).equalsPawn("W")) {
					numW++;
					if (i<4 && j<4 && !(i==3 && j==3)) q1_w++; //bianca nel primo quadrante (alto sx)
					if (i>4 && j<4 && !(i==5 && j==3)) q2_w++; //bianca nel secondo quadrante (basso sx)
					if (i>4 && j>4 && !(i==5 && j==5)) q3_w++; //bianca nel terzo quadrante (basso dx)
					if (i<4 && j>4 && !(i==3 && j==5)) q4_w++; //bianca nel quarto quadrante (alto dx)
				}
		int nereMangiate=((MyGameAshtonTablut)super.game).getNumPedineStoricoBlack()-numB;
				
		punteggio+=((((double)numB)/numW)>=1.2) ? nereMangiate*punteggioNereMangiate : nereMangiate*punteggioBiancheMangiate;
		
		//punteggio derivante dalla posizione della singola pedina sulla scacchiera
		for (int i=0; i<s.getBoard().length; i++) 
			for (int j=0; j<s.getBoard().length; j++) 
				if (s.getPawn(i, j).equalsPawn("B")) {
					int indexRiga, indexColonna; //indici per accedere alle matrici dei punteggi
					if(i<5)
						indexRiga=i;
					else indexRiga=s.getBoard().length-1-i;
					if(j<5)
						indexColonna=j;
					else indexColonna=s.getBoard().length-1-j;					
					punteggio+=this.punteggiNere[indexRiga][indexColonna];
					if (i<5 && j<5 && q1_b>0 && q1_w==0)
						punteggio+=this.punteggiNereQuadranteLibero[indexRiga][indexColonna];
					if (i>4 && j<5 && q2_b>0 && q2_w==0)
						punteggio+=this.punteggiNereQuadranteLibero[indexRiga][indexColonna];
					if (i>4 && j>4 && q3_b>0 && q3_w==0)
						punteggio+=this.punteggiNereQuadranteLibero[indexRiga][indexColonna];
					if (i<5 && j>4 && q4_b>0 && q4_w==0)
						punteggio+=this.punteggiNereQuadranteLibero[indexRiga][indexColonna];
				}
		
		//punteggio derivante dalla posizione relativa delle pedine nella scacchiera
		int rigaRe=0, colonnaRe=0;
		for (int i=0; i<s.getBoard().length; i++) 
			for (int j=0; j<s.getBoard().length; j++) 
				if (s.getPawn(i, j).equalsPawn("K")) {
					//salvo le coordinate del re 
					rigaRe=i;
					colonnaRe=j;
				}
		
		if(s.getPawn(rigaRe, colonnaRe-1).equalsPawn("B")) {
			if(colonnaRe-2>=0 && !trovaAttaccante(s, rigaRe, colonnaRe-2, Pawn.WHITE))
				punteggio+=avvicinamentoAlRe;
		}
		if(s.getPawn(rigaRe, colonnaRe+1).equalsPawn("B")) {
			if(colonnaRe+2<=s.getBoard().length-1 && !trovaAttaccante(s, rigaRe, colonnaRe+2, Pawn.WHITE))
				punteggio+=avvicinamentoAlRe;
		}
		if(s.getPawn(rigaRe-1, colonnaRe).equalsPawn("B")) {
			if(rigaRe-2>=0 && !trovaAttaccante(s, rigaRe-2, colonnaRe, Pawn.WHITE))
				punteggio+=avvicinamentoAlRe;
		}
		if(s.getPawn(rigaRe+1, colonnaRe).equalsPawn("B")) {
			if(rigaRe+2<=s.getBoard().length-1 && !trovaAttaccante(s, rigaRe+2, colonnaRe, Pawn.WHITE))
				punteggio+=avvicinamentoAlRe;
		}
		
		//TODO nere -> scegliamo di non dare punteggio per questo per semplificare
		//punteggio positivo se ha dei neri di fianco
		//punteggio negativo se ha dei bianchi o delle cittadelle o il castello di fianco (=rischio di essere mangiato)
		
		/****/if (kingInEmptyCol(s, rigaRe, colonnaRe) || this.kingInEmptyRow(s, rigaRe, colonnaRe))
			/****/punteggio += reRigaLibera;
		
		return punteggio;
	}
	
	/*
	 * guarda se esiste una pedina di tipo pedinaAttaccante che puo' arrivare nella posizione (riga, colonna)
	 */
	private boolean trovaAttaccante(State state, int riga, int colonna, Pawn pedinaAttaccante) {
		if(!state.getPawn(riga, colonna).equalsPawn("O"))
			return false;
		
		//direzione nord
		for(int k=riga-1; k>=0; k--) {//riga
			if (state.getPawn(k, colonna).equalsPawn(pedinaAttaccante.toString()) || (pedinaAttaccante.equalsPawn("W") && state.getPawn(k, colonna).equalsPawn("K"))) 
				return true;
			else {
				if(state.getPawn(k, colonna).equalsPawn("O") //se la casella e' vuota..
						&& (!((MyGameAshtonTablut)super.game).getCitadels().contains(state.getBox(k, colonna)) || (colonna==state.getBoard().length/2 && pedinaAttaccante.equalsPawn("B")))) //..e non e' una cittadella, oppure e' una cittadella(che se sono arrivato qui e' vuota) ma ha di fianco un'altra cittadella, dalla quale puo' arrivare un nero a mangiarmi..
					continue; //continuo ad esplorare
				else break; //altrimenti (trovo un trono o una cittadella o una pedina dell'altro colore rispetto a pedinaattaccante) mi fermo
			}
		}
		
		//direzione sud
		for(int k=riga+1; k<state.getBoard().length; k++) {//riga
			if (state.getPawn(k, colonna).equalsPawn(pedinaAttaccante.toString()) || (pedinaAttaccante.equalsPawn("W") && state.getPawn(k, colonna).equalsPawn("K"))) 
				return true;
			else {
				if(state.getPawn(k, colonna).equalsPawn("O") //se la casella e' vuota..
						&& !(((MyGameAshtonTablut)super.game).getCitadels().contains(state.getBox(k, colonna)) || (colonna==state.getBoard().length/2 && pedinaAttaccante.equalsPawn("B")))) //..e non e' una cittadella..
					continue; //continuo ad esplorare
				else break; //altrimenti (trovo un trono o una cittadella o una pedina dell'altro colore rispetto a pedinaattaccante) mi fermo
			}
		}
		
		//direzione ovest
		for(int z=colonna-1; z>=0; z--) {//colonna
			if (state.getPawn(riga, z).equalsPawn(pedinaAttaccante.toString()) || (pedinaAttaccante.equalsPawn("W") && state.getPawn(riga, z).equalsPawn("K"))) 
				return true;
			else {
				if(state.getPawn(riga, z).equalsPawn("O") //se la casella e' vuota..
						&& !(((MyGameAshtonTablut)super.game).getCitadels().contains(state.getBox(riga, z)) || (riga==state.getBoard().length/2 && pedinaAttaccante.equalsPawn("B")))) //..e non e' una cittadella..
					continue; //continuo ad esplorare
				else break; //altrimenti (trovo un trono o una cittadella o una pedina dell'altro colore rispetto a pedinaattaccante) mi fermo
			}
		}
		
		//direzione est
		for(int z=colonna+1; z<state.getBoard().length; z++) {//colonna
			if (state.getPawn(riga, z).equalsPawn(pedinaAttaccante.toString()) || (pedinaAttaccante.equalsPawn("W") && state.getPawn(riga, z).equalsPawn("K"))) 
				return true;
			else {
				if(state.getPawn(riga, z).equalsPawn("O") //se la casella e' vuota..
						&& !(((MyGameAshtonTablut)super.game).getCitadels().contains(state.getBox(riga, z))  || (riga==state.getBoard().length/2 && pedinaAttaccante.equalsPawn("B")))) //..e non e' una cittadella..
					continue; //continuo ad esplorare
				else break; //altrimenti (trovo un trono o una cittadella o una pedina dell'altro colore rispetto a pedina attaccante) mi fermo
			}
		}
		
		return false;
	}

	@Override
	protected boolean hasSafeWinner(double resultUtility) {
		return super.hasSafeWinner(resultUtility);
	}
	
	//controllo se la riga del re non ha altre pedine o accampamenti
	/****/private boolean kingInEmptyRow(State s, int kingRow, int kingCol) {
		for(int j=0; j<s.getBoard().length; j++) {
			if (j==kingCol)
				continue;
			else if(!s.getPawn(kingRow, j).equalsPawn("O") || 
					((MyGameAshtonTablut)super.game).getCitadels().contains(s.getBox(kingRow, j)))
				return false;
		}
		return true;
	}

	//controllo se la colonna del re non ha altre pedine o accampamenti
	/****/private boolean kingInEmptyCol(State s, int kingRow, int kingCol) {
		for(int i=0; i<s.getBoard().length; i++) {
			if (i==kingRow)
				continue;
			else if(!s.getPawn(i, kingCol).equalsPawn("O") ||
					((MyGameAshtonTablut)super.game).getCitadels().contains(s.getBox(i, kingCol)))
				return false;
		}
		return true;
	}

}
