package adversarialsearch;
public class State {
    private char [ ] [ ] board ; // the board as a 2D character array [ height ] [ width ]
    private int [ ] agentX ; // the x−coordinates of the agents , agentX [ 0 ] = x_agent0
    private int [ ] agentY ; // the y−coordinates of the agents , agentY [ 0 ] = y_agent0
    private int [ ] score ; // the amount of food eaten by each agent
    private int turn ; //who ’ s turn i t i s , agent 0 or agent 1
    private int food ; // the total amount of food s t i l l avai lable

    public State(char[][] board, int[] agentX, int[] agentY, int[] score, int turn, int food) {
        this.board = board;
        this.agentX = agentX;
        this.agentY = agentY;
        this.score = score;
        this.turn = turn;
        this.food = food;
    }

    public void read(String filename) {
        
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        // Append the board to the result string
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                res.append(board[i][j]);
            }
            res.append("\n");
        }
        res.append("Agent Positions:\n");
        for (int i = 0; i < agentX.length; i++) {
            res.append("Agent ").append(i).append(": (")
                .append(agentX[i]).append(", ").append(agentY[i]).append(")\n");
        }
        res.append("Scores:\n");
        for (int i = 0; i < score.length; i++) {
            res.append("Agent ").append(i).append(": ").append(score[i]).append("\n");
        }
        res.append("Turn: ").append(turn).append("\n");
        res.append("Food remaining: ").append(food).append("\n");
        return res.toString();
    }

}
