import java.util.ArrayList;
import java.util.List;

/**
 * Abstract superclass for AI Player
 */
abstract class AIPlayer {
    protected Seed[][] board;
    protected Seed mySeed;
    protected Seed oppSeed;

    public AIPlayer(Seed[][] board) {
        this.board = board;
    }

    public void setSeed(Seed seed) {
        this.mySeed = seed;
        this.oppSeed = (seed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    public abstract int move(); // Abstract method to get the next move
}

/**
 * AI Player using Minimax algorithm with Alpha-Beta Pruning
 */
class AIPlayerMinimax extends AIPlayer {

    public AIPlayerMinimax(Seed[][] board) {
        super(board);
    }

    @Override
    public int move() {
        int[] result = minimax(5, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return result[1]; // Return the best column
    }

    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        List<Integer> nextMoves = generateMoves();
        int bestScore = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int move : nextMoves) {
                int row = getAvailableRow(move);
                board[row][move] = player;

                int score;
                if (player == mySeed) {
                    score = minimax(depth - 1, oppSeed, alpha, beta)[0];
                    if (score > bestScore) {
                        bestScore = score;
                        bestCol = move;
                    }
                    alpha = Math.max(alpha, bestScore);
                } else {
                    score = minimax(depth - 1, mySeed, alpha, beta)[0];
                    if (score < bestScore) {
                        bestScore = score;
                        bestCol = move;
                    }
                    beta = Math.min(beta, bestScore);
                }

                board[row][move] = Seed.NO_SEED;

                if (alpha >= beta) break;
            }
        }

        return new int[]{bestScore, bestCol};
    }

    private List<Integer> generateMoves() {
        List<Integer> validMoves = new ArrayList<>();
        for (int col = 0; col < ConnectFour.COLS; col++) {
            if (board[0][col] == Seed.NO_SEED) {
                validMoves.add(col);
            }
        }
        return validMoves;
    }

    private int getAvailableRow(int col) {
        for (int row = ConnectFour.ROWS - 1; row >= 0; row--) {
            if (board[row][col] == Seed.NO_SEED) {
                return row;
            }
        }
        return -1;
    }

    private int evaluate() {
        int score = 0;

        // Add scoring logic for rows, columns, and diagonals
        // Example: +100 for 4-in-a-row, +10 for 3-in-a-row, etc.

        return score;
    }
}