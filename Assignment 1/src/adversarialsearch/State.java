package adversarialsearch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

// Assignemnt 1a Question 1
public class State {
    public char [ ] [ ] board ; // the board as a 2D character array [ height ] [ width ]
    public int [ ] agentX ; // the x−coordinates of the agents , agentX [ 0 ] = x_agent0
    public int [ ] agentY ; // the y−coordinates of the agents , agentY [ 0 ] = y_agent0
    public int [ ] score ; // the amount of food eaten by each agent
    public int turn ; //who ’ s turn it is , agent 0 or agent 1
    public int food ; // the total amount of food still available
    public Vector<String> moves; // List of actions executed so far
    private Vector<int[]> foodLocations; // List of food coordinates

    // No-argument constructor
    public State() {
        // Fields will be initialized in the read method
        this.moves = new Vector<>();
        this.foodLocations = new Vector<>();
    }

    private int width;
    private int height;

    // Assignemnt 1a Question 2
    public void read(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] dimensions = br.readLine().split(" ");
            this.height = Integer.parseInt(dimensions[0]);
            this.width = Integer.parseInt(dimensions[1]);

            // Initialize fields
            this.board = new char[height][width];
            this.agentX = new int[2];
            this.agentY = new int[2];
            this.score = new int[2];
            this.turn = 0;
            this.food = 0;

            // Read board configuration
            for (int i = 0; i < height; i++) {
                String line = br.readLine();
                for (int j = 0; j < width; j++) {
                    char c = line.charAt(j);
                    board[i][j] = c;
                    if (c == 'A') {
                        agentX[0] = j;
                        agentY[0] = i;
                    } else if (c == 'B') {
                        agentX[1] = j;
                        agentY[1] = i;
                    } else if (c == '*') {
                        food++;
                        foodLocations.add(new int[]{i, j});
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Assignemnt 1a Question 3
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Append board configuration
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j]);
            }
            sb.append("\n");
        }
        
        // Append other state information
        sb.append("Turn: Agent ").append(turn).append("\n");
        sb.append("Food remaining: ").append(food).append("\n");
        sb.append("Agent A (X, Y): (").append(agentX[0]).append(", ").append(agentY[0]).append(")\n");
        sb.append("Agent B (X, Y): (").append(agentX[1]).append(", ").append(agentY[1]).append(")\n");
        sb.append("Score A: ").append(score[0]).append("\n");
        sb.append("Score B: ").append(score[1]);
        
        return sb.toString();
    }

    // Assignemnt 1a Question 4
    public State copy() {
        State newState = new State();
        
        // Copy the board (deep copy)
        newState.board = new char[this.board.length][];
        for (int i = 0; i < this.board.length; i++) {
            newState.board[i] = this.board[i].clone();
        }
        
        // Copy the agentX and agentY arrays
        newState.agentX = this.agentX.clone();
        newState.agentY = this.agentY.clone();
        
        // Copy the score array
        newState.score = this.score.clone();
        
        // Copy primitive values
        newState.turn = this.turn;
        newState.food = this.food;
        
        return newState;
    }

    // Assignemnt 1a Question 5
    public Vector<String> legalMoves(int agent) {
        Vector<String> moves = new Vector<>();
        int x = agentX[agent];
        int y = agentY[agent];

        // Check movement directions
        if (y > 0 && board[y-1][x] != '#') moves.add("up");
        if (x < board[0].length - 1 && board[y][x+1] != '#') moves.add("right");
        if (y < board.length - 1 && board[y+1][x] != '#') moves.add("down");
        if (x > 0 && board[y][x-1] != '#') moves.add("left");

        // Check if eat is possible
        for (int[] foodLocation : foodLocations) {
            if (foodLocation[0] == y && foodLocation[1] == x) {
                moves.add("eat");
                break;
            }
        }

        // Check if block is possible (always possible on empty spaces or agent positions)
        if (board[y][x] == ' ' || board[y][x] == 'A' || board[y][x] == 'B') moves.add("block");

        return moves;
    }

    public Vector<String> legalMoves() {
        return legalMoves(turn);
    }

    // Assignemnt 1a Question 6
    public void execute(String action) {
        int currentAgent = turn;
        int x = agentX[currentAgent];
        int y = agentY[currentAgent];

        // Execute the action
        switch (action) {
            case "up": agentY[currentAgent]--; break;
            case "right": agentX[currentAgent]++; break;
            case "down": agentY[currentAgent]++; break;
            case "left": agentX[currentAgent]--; break;
            case "eat":
                for (int i = 0; i < foodLocations.size(); i++) {
                    int[] foodLocation = foodLocations.get(i);
                    if (foodLocation[0] == y && foodLocation[1] == x) {
                        score[currentAgent]++;
                        food--;
                        foodLocations.remove(i);
                        break;
                    }
                }
                break;
            case "block": board[y][x] = '#'; break;
        }

        // Remove the agent from its current position
        board[y][x] = ' ';

        // Update the agent's new position on the board
        x = agentX[currentAgent];
        y = agentY[currentAgent];
        board[y][x] = (currentAgent == 0) ? 'A' : 'B';

        // Add the action to the moves list
        moves.add(action);

        // Switch turns
        turn = 1 - turn;
    }

    // Assignemnt 1a Question 7
    public boolean isLeaf() {
        // Check if there's no food left
        if (food == 0) return true;
        
        // Check if either agent has no legal moves left
        for (int i = 0; i < 2; i++) {
            if (legalMoves(i).isEmpty()) return true;
        }
        
        return false;
    }

    // Assignemnt 1a Question 8
    public int value(int agent) {
        // If no food left, compare scores
        if (food == 0) {
            return score[agent] - score[1 - agent];
        }
        
        // Check if either agent has no legal moves left
        for (int i = 0; i < 2; i++) {
            if (legalMoves(i).isEmpty()) {
                // If the current agent has no moves, they lose
                if (i == agent) {
                    return -1;
                } else {
                    // If the other agent has no moves, the current agent wins
                    return 1;
                }
            }
        }
        
        // If it's not a leaf state, return 0
        return 0;
    }
}
