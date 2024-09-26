package adversarialsearch;

public class Main {
	public static void main(String[] args) {
		// Create a new Game object
		Game g = new Game();
		
		// Print the initial state
		System.out.println("Initial State:");
		System.out.println(g.b.toString());
		
		// Run the test method
		g.test();
	}
}
