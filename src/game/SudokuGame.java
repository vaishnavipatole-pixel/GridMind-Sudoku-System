package game;
import java.util.Scanner;
import java.util.InputMismatchException;
import model.Move;
import model.SudokuBoard;
import validator.SudokuValidator;

public class SudokuGame {
	private SudokuBoard board;
    private SudokuBoard solution;
    private SudokuValidator validator;
    private MoveHistory moveHistory;

    private int mistakes;
    private int hintsUsed;
    private String difficulty;
    private ScoreCalculator scoreCalculator;
    private long startTime;
    private long endTime;

    public SudokuGame(
            SudokuBoard board,
            SudokuBoard solution,
            String difficulty) {

        this.board = board;
        this.solution = solution;
        this.difficulty = difficulty;

        this.validator = new SudokuValidator();
        this.moveHistory = new MoveHistory();
        this.scoreCalculator = new ScoreCalculator();

        mistakes = 0;
        hintsUsed = 0;
    }

    public void displayBoard() {
        board.display();
    }

    public boolean makeMove(
            int row,
            int col,
            int value) {

        if (row < 0 || row >= 9 ||
            col < 0 || col >= 9) {

            System.out.println("Invalid row or column.");
            return false;
        }

        if (value < 1 || value > 9) {
            System.out.println("Enter a number from 1 to 9.");
            return false;
        }

        if (!board.isEmpty(row, col)) {
            System.out.println("This cell is already filled.");
            return false;
        }

        if (solution.getValue(row, col) == value) {

            Move move = new Move(
                    row,
                    col,
                    board.getValue(row, col),
                    value
            );

            board.setValue(row, col, value);
            moveHistory.addMove(move);

            System.out.println("Correct move!");
            return true;

        } else {

            mistakes++;

            System.out.println("Incorrect move.");
            System.out.println(
                    "Mistakes: " + mistakes
            );

            return false;
        }
    }

    public void undo() {

        Move move = moveHistory.undo();

        if (move == null) {
            System.out.println("Nothing to undo.");
            return;
        }

        board.setValue(
                move.getRow(),
                move.getCol(),
                move.getOldValue()
        );

        System.out.println("Move undone.");
    }

    public void redo() {

        Move move = moveHistory.redo();

        if (move == null) {
            System.out.println("Nothing to redo.");
            return;
        }

        board.setValue(
                move.getRow(),
                move.getCol(),
                move.getNewValue()
        );

        System.out.println("Move redone.");
    }

    public boolean isCompleted() {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (board.isEmpty(row, col)) {
                    return false;
                }
            }
        }

        return validator.isValidBoard(board);
    }

    public int getMistakes() {
        return mistakes;
    }

    public int getHintsUsed() {
        return hintsUsed;
    }

    public void showHint() {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (board.isEmpty(row, col)) {

                    System.out.println(
                        "Hint: Row " + (row + 1)
                        + ", Column " + (col + 1)
                        + " = "
                        + solution.getValue(row, col)
                    );

                    hintsUsed++;
                    return;
                }
            }
        }

        System.out.println("No hints available.");
    }

    public boolean play(Scanner scanner) {
    	startTime = System.currentTimeMillis();
        boolean playing = true;

        while (playing && !isCompleted()) {

            System.out.println();
            System.out.println("================================");
            System.out.println("          PLAY SUDOKU");
            System.out.println("================================");

            board.display();

            System.out.println("1. Enter Move");
            System.out.println("2. Undo");
            System.out.println("3. Redo");
            System.out.println("4. Hint");
            System.out.println("5. Show Board");
            System.out.println("6. View Solution");
            System.out.println("7. Exit Game");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("Enter a valid number.");
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the leftover newline
            switch (choice) {

            case 1:

                int row;
                int col;
                int value;

                // ROW VALIDATION
                while (true) {
                    try {
                        System.out.print("Enter row (1-9): ");
                        row = scanner.nextInt();

                        if (row < 1 || row > 9) {
                            System.out.println(
                                "Invalid row. Enter a value from 1 to 9."
                            );
                            continue;
                        }

                        break;

                    } catch (InputMismatchException e) {
                        System.out.println(
                            "Invalid input. Enter a number."
                        );
                        scanner.nextLine();
                    }
                }

                // COLUMN VALIDATION
                while (true) {
                    try {
                        System.out.print("Enter column (1-9): ");
                        col = scanner.nextInt();

                        if (col < 1 || col > 9) {
                            System.out.println(
                                "Invalid column. Enter a value from 1 to 9."
                            );
                            continue;
                        }

                        break;

                    } catch (InputMismatchException e) {
                        System.out.println(
                            "Invalid input. Enter a number."
                        );
                        scanner.nextLine();
                    }
                }

                // VALUE VALIDATION
                while (true) {
                    try {
                        System.out.print("Enter value (1-9): ");
                        value = scanner.nextInt();

                        if (value < 1 || value > 9) {
                            System.out.println(
                                "Invalid value. Enter a number from 1 to 9."
                            );
                            continue;
                        }

                        break;

                    } catch (InputMismatchException e) {
                        System.out.println(
                            "Invalid input. Enter a number."
                        );
                        scanner.nextLine();
                    }
                }

                makeMove(row - 1, col - 1, value);
                break;

                case 2:
                    undo();
                    break;

                case 3:
                    redo();
                    break;

                case 4:
                    showHint();
                    break;

                case 5:
                    displayBoard();
                    break;


                case 6:
                    System.out.println();
                    System.out.println("===== FINAL SOLUTION =====");
                    solution.display();
                    System.out.println();
                    System.out.println("Returning to your game...");
                    break;

                case 7:
                    System.out.println("Exiting Sudoku game...");
                    return false;

                default:
                    System.out.println(
                        "Invalid choice. Enter 1 to 7."
                    );
                	
                
            }
        }

        if (isCompleted()) {

            endTime = System.currentTimeMillis();

            long timeTaken =
                    (endTime - startTime) / 1000;
            int score = scoreCalculator.calculateScore(
                    mistakes,
                    hintsUsed,
                    timeTaken,
                    difficulty
            );

            System.out.println();
            System.out.println("================================");
            System.out.println("       SUDOKU COMPLETED!");
            System.out.println("================================");
            System.out.println("Mistakes : " + mistakes);
            System.out.println("Hints    : " + hintsUsed);
            System.out.println("Time     : " + timeTaken + " seconds");
            System.out.println("Score    : " + score);
            System.out.println("================================");
        }
        return isCompleted();
    }
}
