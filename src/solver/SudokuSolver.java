package solver;
import model.SudokuBoard;
public class SudokuSolver {
	private SolverStatistics statistics;
	
    public SudokuSolver() {
        statistics = new SolverStatistics();
    }

    public SolverStatistics getStatistics() {
        return statistics;
    }

    public boolean solve(SudokuBoard board) {

        statistics.reset();

        long startTime = System.nanoTime();
        int emptyCells = 0;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getValue(row, col) == 0) {
                    emptyCells++;
                }
            }
        }
        boolean solved = solveRecursive(board);

        if (solved) {
            statistics.setCellsFilled(emptyCells);
        }
        long endTime = System.nanoTime();

        statistics.setExecutionTime(
            (endTime - startTime) / 1_000_000
        );

        return solved;
    }
  
    private boolean solveRecursive(SudokuBoard board) {

        statistics.incrementRecursiveCalls();

        int[] emptyCell = findEmptyCell(board);

        // No empty cell means Sudoku is solved
        if (emptyCell == null) {
            return true;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        // Try numbers 1 to 9
        for (int number = 1; number <= 9; number++) {

            if (isSafe(board, row, col, number)) {

            	board.setValue(row, col, number);

            	// Recursive call
            	if (solveRecursive(board)) {
            	    return true;
            	}

                // Backtrack
                board.setValue(row, col, 0);
                statistics.incrementBacktracks();
            }
        }

        return false;
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
}
