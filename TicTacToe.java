import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(400, 400);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeBoard();

        // Create buttons and add them to the frame
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (board[row][col] == '-' && currentPlayer == 'X') {
                            makeMove(row, col, 'X');
                            if (checkWin('X')) {
                                showMessage("Player Wins!");
                                resetGame();
                            } else if (isBoardFull()) {
                                showMessage("It's a Draw!");
                                resetGame();
                            } else {
                                aiMove(); // AI's turn after player moves
                            }
                        }
                    }
                });
                add(buttons[i][j]);
            }
        }

        setVisible(true);
    }

    // Initialize board with empty cells
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
                if (buttons[i][j] != null) {
                    buttons[i][j].setText("");
                }
            }
        }
        currentPlayer = 'X';
    }

    // Make a move on the board
    private void makeMove(int row, int col, char player) {
        board[row][col] = player;
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setEnabled(false);
    }

    // AI Move using Minimax + Alpha-Beta
    private void aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    board[i][j] = 'O';
                    int score = minimax(false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    board[i][j] = '-';
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        makeMove(bestRow, bestCol, 'O');

        if (checkWin('O')) {
            showMessage("AI Wins!");
            resetGame();
        } else if (isBoardFull()) {
            showMessage("It's a Draw!");
            resetGame();
        }
    }

    // Minimax with Alpha-Beta Pruning
    private int minimax(boolean isMaximizing, int alpha, int beta) {
        if (checkWin('O')) return +10;
        if (checkWin('X')) return -10;
        if (isBoardFull()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '-') {
                        board[i][j] = 'O';
                        int score = minimax(false, alpha, beta);
                        board[i][j] = '-';
                        bestScore = Math.max(bestScore, score);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) break;
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '-') {
                        board[i][j] = 'X';
                        int score = minimax(true, alpha, beta);
                        board[i][j] = '-';
                        bestScore = Math.min(bestScore, score);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) break;
                    }
                }
            }
            return bestScore;
        }
    }

    // Check for a winner
    private boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    // Check if the board is full
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') return false;
            }
        }
        return true;
    }

    // Display a message
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Reset the game
    private void resetGame() {
        initializeBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(true);
            }
        }
    }

    // Main function
    public static void main(String[] args) {
        new TicTacToe();
    }
}
