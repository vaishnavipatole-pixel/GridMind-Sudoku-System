package generator;
import java.util.Random;

import model.SudokuBoard;

public class SudokuGenerator {
	private Random random;

    public SudokuGenerator() {
        random = new Random();
    }

    public SudokuBoard generatePuzzle(int emptyCells) {

        SudokuBoard board = new SudokuBoard();

        // First generate a complete valid Sudoku
        fillBoard(board);

        // Remove numbers to create the puzzle
        removeNumbers(board, emptyCells);

        return board;
    }

    private boolean fillBoard(SudokuBoard board) {

        int[] emptyCell = findEmptyCell(board);

        // No empty cell means board is complete
        if (emptyCell == null) {
            return true;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        // Try numbers in random order
        int[] numbers = getShuffledNumbers();

        for (int number : numbers) {

            if (isSafe(board, row, col, number)) {

                board.setValue(row, col, number);

                if (fillBoard(board)) {
                    return true;
                }

                // Backtrack
                board.setValue(row, col, 0);
            }
        }

        return false;
    }

    private void removeNumbers(SudokuBoard board, int emptyCells) {

        int removed = 0;

        while (removed < emptyCells) {

            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (!board.isEmpty(row, col)) {

                board.setValue(row, col, 0);
                removed++;
            }
        }
    }

    private int[] findEmptyCell(SudokuBoard board) {

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                if (board.isEmpty(row, col)) {
                    return new int[]{row, col};
                }
            }
        }

        return null;
    }

    private boolean isSafe(
            SudokuBoard board,
            int row,
            int col,
            int number) {

        // Check row
        for (int j = 0; j < 9; j++) {

            if (board.getValue(row, j) == number) {
                return false;
            }
        }

        // Check column
        for (int i = 0; i < 9; i++) {

            if (board.getValue(i, col) == number) {
                return false;
            }
        }

        // Check 3x3 box
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = startRow; i < startRow + 3; i++) {

            for (int j = startCol; j < startCol + 3; j++) {

                if (board.getValue(i, j) == number) {
                    return false;
                }
            }
        }

        return true;
    }

    private int[] getShuffledNumbers() {

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = numbers.length - 1; i > 0; i--) {

            int j = random.nextInt(i + 1);

            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }

        return numbers;
    }
}
