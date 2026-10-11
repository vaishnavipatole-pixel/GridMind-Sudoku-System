package model;

public class SudokuBoard {
	private int[][] board;

    public SudokuBoard() {
        board = new int[9][9];
    }

    public SudokuBoard(int[][] puzzle) {
        board = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = puzzle[i][j];
            }
        }
    }

    public int getValue(int row, int col) {
        return board[row][col];
    }

    public void setValue(int row, int col, int value) {
        board[row][col] = value;
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == 0;
    }

    public void display() {

        System.out.println("-------------------------");

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                if (j % 3 == 0) {
                    System.out.print("| ");
                }

                System.out.print(board[i][j] + " ");
            }

            System.out.println("|");

            if ((i + 1) % 3 == 0) {
                System.out.println("-------------------------");
            }
        }
    }

    public SudokuBoard copy() {

        return new SudokuBoard(board);
    }

    public int[][] getBoard() {
        return board;
    }
}
