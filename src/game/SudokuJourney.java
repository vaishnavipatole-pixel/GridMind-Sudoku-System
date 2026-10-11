package game;
import java.util.Scanner;
import dao.ProgressDAO;
import dao.UserDAO;
import generator.SudokuGenerator;
import model.SudokuBoard;
import solver.SudokuSolver;
public class SudokuJourney {
	private Scanner scanner;
    private SudokuGenerator generator;
    private SudokuSolver solver;

    private int currentLevel;
    private int userId;
    private ProgressDAO progressDAO;
    private UserDAO userDAO;
    public SudokuJourney(Scanner scanner, int userId) {

        this.scanner = scanner;

        this.userId = userId;

        this.generator = new SudokuGenerator();

        this.solver = new SudokuSolver();

        this.progressDAO = new ProgressDAO();
        this.userDAO = new UserDAO();

        progressDAO.createProgress(userId);

        this.currentLevel =
                progressDAO.getCurrentLevel(userId);
    }

    public void startJourney() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("        SUDOKU JOURNEY");
            System.out.println("=================================");

            System.out.println("Level 1  " + getStatus(1));
            System.out.println("Level 2  " + getStatus(2));
            System.out.println("Level 3  " + getStatus(3));
            System.out.println("Level 4  " + getStatus(4));
            System.out.println("Level 5  " + getStatus(5));

            System.out.println();
            System.out.println("0. Back to Dashboard");

            System.out.print("Enter level: ");

            int choice = scanner.nextInt();

            if (choice == 0) {
                running = false;
            }
            else if (choice < 1 || choice > 5) {
                System.out.println("Invalid level.");
            }
            else if (choice > currentLevel) {
                System.out.println(
                    "Level is locked. Complete previous levels first."
                );
            }
            else {
                playLevel(choice);
            }
        }
    }

    private String getStatus(int level) {

        if (level < currentLevel) {
            return "✓ Completed";
        }

        if (level == currentLevel) {
            return "🔓 Unlocked";
        }

        return "🔒 Locked";
    }

    private void playLevel(int level) {

        String difficulty = getDifficulty(level);

        System.out.println();
        System.out.println("=================================");
        System.out.println("           LEVEL " + level);
        System.out.println("=================================");
        System.out.println("Difficulty: " + difficulty);

        int emptyCells = getEmptyCells(level);

        SudokuBoard puzzle =
                generator.generatePuzzle(emptyCells);

        SudokuBoard solution =
                puzzle.copy();

        if (!solver.solve(solution)) {

            System.out.println(
                "Unable to generate this level."
            );

            return;
        }

        System.out.println();
        System.out.println("Starting Sudoku Game...");

        SudokuGame game =
                new SudokuGame(
                    puzzle,
                    solution,
                    difficulty.toUpperCase()
                );

        boolean completed = game.play(scanner);

        if (completed) {

            completeLevel(level);

        } else {

            System.out.println();
            System.out.println(
                "Level " + level + " was not completed."
            );
        }
    }

    private String getDifficulty(int level) {

        switch (level) {

            case 1:
            case 2:
                return "Easy";

            case 3:
            case 4:
                return "Medium";

            case 5:
                return "Hard";

            default:
                return "Easy";
        }
    }

    private int getEmptyCells(int level) {

        switch (level) {

            case 1:
                return 30;

            case 2:
                return 32;

            case 3:
                return 40;

            case 4:
                return 45;

            case 5:
                return 50;

            default:
                return 30;
        }
    }
    private int getCoinReward(String difficulty) {

        switch (difficulty.toUpperCase()) {

            case "EASY":
                return 50;

            case "MEDIUM":
                return 100;

            case "HARD":
                return 200;

            case "EXPERT":
                return 350;

            default:
                return 0;
        }
    }
    private void completeLevel(int level) {
    	if (level == currentLevel) {

    	    currentLevel++;

    	    progressDAO.saveProgress(
    	        userId,
    	        level
    	    );

    	    // XP reward
    	    userDAO.addXP(
    	        userId,
    	        100
    	    );

    	    // Coin reward based on difficulty
    	    int coins = getCoinReward(
    	        getDifficulty(level)
    	    );

    	    userDAO.addCoins(
    	        userId,
    	        coins
    	    );
    	}
    	

        System.out.println();
        System.out.println(
            "🎉 Level " + level + " completed!"
        );

        if (currentLevel <= 5) {

            System.out.println(
                "🔓 Level " + currentLevel + " unlocked!"
            );

        } else {

            System.out.println(
                "🏆 Congratulations! Journey completed!"
            );
        }
    }
}
