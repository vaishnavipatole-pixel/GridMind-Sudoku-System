package analysis;
import java.util.ArrayList;
import java.util.List;

import model.SudokuBoard;

public class CandidateAnalyzer {
	public List<Integer> getCandidates(
            SudokuBoard board, int row, int col) {

        List<Integer> candidates = new ArrayList<>();

        // Cell already filled hai
        if (!board.isEmpty(row, col)) {
            return candidates;
        }

        for (int number = 1; number <= 9; number++) {

            if (isSafe(board, row, col, number)) {
                candidates.add(number);
            }
        }

        return candidates;
    }

    private boolean isSafe(
            SudokuBoard board,
            int row,
            int col,
            int number) {

        // Row check
        for (int j = 0; j < 9; j++) {
            if (board.getValue(row, j) == number) {
                return false;
            }
        }

        // Column check
        for (int i = 0; i < 9; i++) {
            if (board.getValue(i, col) == number) {
                return false;
            }
        }

        // 3x3 box check
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

    public void displayCandidates(SudokuBoard board) {

        System.out.println();
        System.out.println("================================");
        System.out.println("      CANDIDATE ANALYSIS");
        System.out.println("================================");

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                if (board.isEmpty(row, col)) {

                    List<Integer> candidates =
                            getCandidates(board, row, col);

                    System.out.println(
                        "Cell (" + (row + 1) + "," + (col + 1) + ") : "
                        + candidates
                    );
                }
            }
        }

        System.out.println("================================");
    }
}
