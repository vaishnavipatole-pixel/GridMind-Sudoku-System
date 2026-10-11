package analysis;
import model.SudokuBoard;
import solver.SolverStatistics;
public class DifficultyAnalyzer {
	public String analyzeDifficulty(
            SudokuBoard board,
            SolverStatistics statistics) {

        int emptyCells = countEmptyCells(board);
        int recursiveCalls = statistics.getRecursiveCalls();
        int backtracks = statistics.getBacktracks();

        if (backtracks < 100 && emptyCells < 35) {
            return "EASY";
        } 
        else if (backtracks < 500 && emptyCells < 45) {
            return "MEDIUM";
        } 
        else if (backtracks < 2000 && emptyCells < 55) {
            return "HARD";
        } 
        else {
            return "EXPERT";
        }
    }

    public int countEmptyCells(SudokuBoard board) {

        int count = 0;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (board.isEmpty(row, col)) {
                    count++;
                }
            }
        }

        return count;
    }

    public void displayAnalysis(
            SudokuBoard board,
            SolverStatistics statistics) {

        String difficulty =
                analyzeDifficulty(board, statistics);

        int emptyCells = countEmptyCells(board);

        System.out.println();
        System.out.println("================================");
        System.out.println("       PUZZLE ANALYSIS");
        System.out.println("================================");
        System.out.println("Difficulty      : " + difficulty);
        System.out.println("Empty Cells     : " + emptyCells);
        System.out.println("Recursive Calls : "
                + statistics.getRecursiveCalls());
        System.out.println("Backtracks      : "
                + statistics.getBacktracks());
        System.out.println("Cells Filled    : "
                + statistics.getCellsFilled());
        System.out.println("Solving Time    : "
                + statistics.getExecutionTime() + " ms");
        System.out.println("================================");
    }
}
