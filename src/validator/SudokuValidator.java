package validator;
import model.SudokuBoard;

public class SudokuValidator {
	public boolean isValidBoard(SudokuBoard board) {

        // Check rows
        for (int row = 0; row < 9; row++) {

            boolean[] used = new boolean[10];

            for (int col = 0; col < 9; col++) {

                int value = board.getValue(row, col);

                if (value == 0) {
                    continue;
                }

                if (value < 1 || value > 9) {
                    return false;
                }

                if (used[value]) {
                    return false;
                }

                used[value] = true;
            }
        }

        // Check columns
        for (int col = 0; col < 9; col++) {

            boolean[] used = new boolean[10];

            for (int row = 0; row < 9; row++) {

                int value = board.getValue(row, col);

                if (value == 0) {
                    continue;
                }

                if (used[value]) {
                    return false;
                }

                used[value] = true;
            }
        }

        // Check 3x3 boxes
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {

            for (int boxCol = 0; boxCol < 9; boxCol += 3) {

                boolean[] used = new boolean[10];

                for (int row = boxRow; row < boxRow + 3; row++) {

                    for (int col = boxCol; col < boxCol + 3; col++) {

                        int value = board.getValue(row, col);

                        if (value == 0) {
                            continue;
                        }

                        if (used[value]) {
                            return false;
                        }

                        used[value] = true;
                    }
                }
            }
        }

        return true;
    }
}
