package adversarialsearch;

import java.util.Vector;

public class Game {
	State b;
	public Game() {
		b=new State();
		b.read("data/board.txt");
	}
	public void test() {
		
		//System.out.println(minimax(b, b.turn, 11, 0));
		
		while (!b.isLeaf()){
			System.out.println(b.toString());
			System.out.println("Legal moves for agent with turn:"+b.legalMoves());
			b.execute(b.legalMoves().get((int)(Math.random()*b.legalMoves().size())));
		}
	}
	
}
