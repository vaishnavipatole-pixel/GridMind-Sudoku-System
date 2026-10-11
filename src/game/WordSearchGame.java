package game;
import java.util.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import dao.UserDAO;
import java.util.Scanner;
public class WordSearchGame {

    private int size;
    private String[] words;
    private String[] clues;
    private int coins = 100;
    private int hintsUsed = 0;
private int selectedDifficulty;
    private char[][] matrix;
    private boolean[] completed;
    private int userId;
    private UserDAO userDAO = new UserDAO();


    private final Deque<GameState> undoStack = new ArrayDeque<>();
    private final Deque<GameState> redoStack = new ArrayDeque<>();

    private static class GameState {
        char[][] matrix;
        boolean[] completed;

        GameState(char[][] matrix, boolean[] completed) {
            this.matrix = copyMatrix(matrix);
            this.completed = completed.clone();
        }
    }

    public boolean play(Scanner scanner, int userId) {
        this.userId = userId;

        System.out.println("\n==================================");
        System.out.println("       WORD MATRIX PUZZLE");
        System.out.println("==================================");

        System.out.println("Select Difficulty:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.println("4. Expert");
        System.out.println("5. Back");

        System.out.print("Enter choice: ");

        int choice = readInt(scanner);

        if (choice == 5) {
            return false;
        }

        if (choice < 1 || choice > 4) {
            System.out.println("Invalid difficulty!");
            return false;
        }

selectedDifficulty = choice;
        setupPuzzle(choice);

        while (true) {

            System.out.println("\n========== WORD MATRIX ==========");
            displayMatrix();

            System.out.println("\n--------------- MENU ---------------");
            System.out.println("1. Play Word");
            System.out.println("2. Hint");
            System.out.println("3. Check Progress");
            System.out.println("4. Undo");
            System.out.println("5. Redo");
            System.out.println("6. View Solution");
            System.out.println("7. Help");
            System.out.println("8. Exit Game");

            System.out.print("Enter choice: ");
            int option = readInt(scanner);

            switch (option) {

                case 1:
                    playWord(scanner);
                    break;

                case 2:
                    revealHint();
                    break;

                case 3:
                    checkProgress();
                    break;

                case 4:
                    undo();
                    break;

                case 5:
                    redo();
                    break;

                case 6:
                    displaySolution();
                    break;

                case 7:
                    showHelp();
                    break;

                case 8:
                    System.out.println("Exiting Word Matrix Puzzle...");
                    return false;

                default:
                    System.out.println("Invalid choice!");
            }

            if (isGameComplete()) {
                System.out.println("\n**********************************");
                System.out.println("CONGRATULATIONS! PUZZLE COMPLETED!");
                System.out.println("**********************************");

                displayMatrix();
                return true;
            }
        }
    }

    private void setupPuzzle(int difficulty) {

        switch (difficulty) {

            case 1:
                size = 7;
                words = new String[] {
                    "APPLE", "PLANT", "HOUSE", "BRAVE"
                };
                clues = new String[] {
                    "A fruit that keeps the doctor away.",
                    "A living thing that grows in soil.",
                    "A place where people live.",
                    "To act with courage."
                };
                break;

            case 2:
                size = 9;
                words = new String[] {
                    "GARDEN", "PLANET", "SCHOOL",
                    "FRIEND", "BRIDGE", "STREAM"
                };
                clues = new String[] {
                    "A place where flowers and plants grow.",
                    "A large object that revolves around a star.",
                    "A place where students learn.",
                    "A person you know and trust.",
                    "A structure built over a river or road.",
                    "A small, flowing body of water."
                };
                break;

            case 3:
                size = 11;
                words = new String[] {
                    "COMPUTER", "KEYBOARD", "MOUNTAIN", "NOTEBOOK",
                    "ELEPHANT", "LANGUAGE", "HOSPITAL", "SUNSHINE"
                };
                clues = new String[] {
                    "An electronic machine used to process data.",
                    "A device used to type on a computer.",
                    "A very high natural landform.",
                    "A book used for writing notes.",
                    "A very large land animal with a trunk.",
                    "A system of words used to communicate.",
                    "A place where sick people receive treatment.",
                    "Light and warmth received from the sun."
                };
                break;

            case 4:
                size = 13;
                words = new String[] {
                    "PROGRAMMING", "ENVIRONMENT", "TECHNOLOGY",
                    "KNOWLEDGE", "EDUCATION", "DISCOVERY",
                    "ADVENTURE", "CREATIVITY", "CHALLENGE", "CONFIDENCE"
                };
                clues = new String[] {
                    "The process of writing instructions for a computer.",
                    "The natural world around us.",
                    "The use of scientific knowledge for practical purposes.",
                    "Information and understanding gained through learning.",
                    "The process of teaching and learning.",
                    "Finding or learning something new.",
                    "An exciting or unusual experience.",
                    "The ability to produce original ideas.",
                    "A difficult task that tests your ability.",
                    "Belief in your own abilities."
                };
                break;
        }

        matrix = new char[size][size];
        completed = new boolean[words.length];

        for (int row = 0; row < size; row++) {
            Arrays.fill(matrix[row], '.');
        }

        // Every word is placed horizontally in its own row.
        for (int i = 0; i < words.length; i++) {
            int row = i;
            int startCol = 1;

            for (int j = 0; j < words[i].length(); j++) {
                matrix[row][startCol + j] = '_';
            }

            // Reveal the first letter as a starting clue.
            matrix[row][startCol] = words[i].charAt(0);
        }

        undoStack.clear();
        redoStack.clear();

        System.out.println("\nDifficulty selected successfully!");
        System.out.println("Words to solve: " + words.length);
    }


private void displayMatrix() {

    int displayRows = words.length;
    int displayCols = 0;

    // Find the longest word
    for (String word : words) {
        if (word.length() > displayCols) {
            displayCols = word.length();
        }
    }

    // Column numbering
    System.out.print("    ");
    for (int col = 1; col <= displayCols; col++) {
        System.out.printf("%2d ", col);
    }
    System.out.println();

    // Separator line
    System.out.print("    ");
    for (int col = 0; col < displayCols; col++) {
        System.out.print("---");
    }
    System.out.println();

    // Display rows with only the required cells
    for (int row = 0; row < displayRows; row++) {

        System.out.printf("%2d |", row + 1);

        for (int col = 1; col <= words[row].length(); col++) {
            System.out.print(" " + matrix[row][col] + " ");
        }

        System.out.println();
    }

    System.out.println("\n_ = Empty letter");
}

    private void showClues() {

        System.out.println("\n-------------- CLUES --------------");

        for (int i = 0; i < words.length; i++) {
            String status = completed[i] ? " [COMPLETED]" : "";

            System.out.println((i + 1) + ". " + clues[i]
                    + " (" + words[i].length() + " letters)"
                    + status);
        }
    }

    private void playWord(Scanner scanner) {

        showClues();

        System.out.print("\nEnter clue number: ");
        int number = readInt(scanner);

        if (number < 1 || number > words.length) {
            System.out.println("Invalid clue number!");
            return;
        }

        int index = number - 1;

        if (completed[index]) {
            System.out.println("This word is already completed!");
            return;
        }

        System.out.print("Enter your answer: ");
        String answer = scanner.nextLine().trim().toUpperCase();

        if (answer.equals(words[index])) {

            saveState();

            int row = index;
            int startCol = 1;

            for (int j = 0; j < words[index].length(); j++) {
                matrix[row][startCol + j] = words[index].charAt(j);
            }

            completed[index] = true;

            System.out.println("Correct answer! Word filled successfully.");

        } else {
            System.out.println("Incorrect answer. Try again!");
        }
    }

private void revealHint() {

    int index = -1;

    for (int i = 0; i < words.length; i++) {
        if (!completed[i]) {
            index = i;
            break;
        }
    }

    if (index == -1) {
        System.out.println("All words are already completed!");
        return;
    }

    int row = index;
    int startCol = 1;

    for (int j = 0; j < words[index].length(); j++) {

        if (matrix[row][startCol + j] == '_') {

            // Deduct 5 coins from the user's database balance
            if (!userDAO.deductCoins(userId, 5)) {
                return;
            }

            hintsUsed++;

            saveState();

            matrix[row][startCol + j] =
                    words[index].charAt(j);

            System.out.println("Hint revealed!");
            System.out.println("Clue: " + clues[index]);
            System.out.println("Letter '" + words[index].charAt(j)
                    + "' revealed at Row " + (row + 1)
                    + ", Column " + (startCol + j));

            return;
        }
    }

    System.out.println("All letters of this word are already visible.");
    System.out.println("Enter the answer using Play Word.");
}

    
    private void checkProgress() {

        int count = 0;

        for (boolean value : completed) {
            if (value) {
                count++;
            }
        }

        double percentage = (count * 100.0) / words.length;

        System.out.println("\n========== PROGRESS ==========");
        System.out.println("Completed words: " + count + "/" + words.length);
        System.out.printf("Progress: %.2f%%%n", percentage);

        if (count == words.length) {
            System.out.println("Status: Completed!");
        } else {
            System.out.println("Status: In Progress");
        }
    }

    private void undo() {

        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo!");
            return;
        }

        redoStack.push(new GameState(matrix, completed));

        GameState previous = undoStack.pop();
        matrix = copyMatrix(previous.matrix);
        completed = previous.completed.clone();

        System.out.println("Last move undone.");
    }

    private void redo() {

        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo!");
            return;
        }

        undoStack.push(new GameState(matrix, completed));

        GameState next = redoStack.pop();
        matrix = copyMatrix(next.matrix);
        completed = next.completed.clone();

        System.out.println("Move restored successfully.");
    }

    private void saveState() {
        undoStack.push(new GameState(matrix, completed));
        redoStack.clear();
    }



private void displaySolution() {

    System.out.println("\n========== WORD SOLUTIONS ==========");

    for (int i = 0; i < words.length; i++) {
        System.out.println((i + 1) + ". " + clues[i]);
        System.out.println("   Answer: " + words[i]);
        System.out.println("   Position: Row " + (i + 1)
                + ", Columns 1-" + words[i].length());
    }

    System.out.println("\n========== SOLUTION MATRIX ==========");

    int displayRows = words.length;
    int displayCols = 0;

    // Find the longest word
    for (String word : words) {
        if (word.length() > displayCols) {
            displayCols = word.length();
        }
    }

    // Column numbering
    System.out.print("    ");
    for (int col = 0; col < displayCols; col++) {
        System.out.printf("%2d ", col + 1);
    }
    System.out.println();

    // Separator line
    System.out.print("    ");
    for (int col = 0; col < displayCols; col++) {
        System.out.print("---");
    }
    System.out.println();

    // Print complete words only
    for (int row = 0; row < displayRows; row++) {
        System.out.printf("%2d |", row + 1);

        for (int col = 0; col < words[row].length(); col++) {
            System.out.print(" " + words[row].charAt(col) + " ");
        }

        System.out.println();
    }

    System.out.println("\nYour current game progress is unchanged.");
}


    private void showHelp() {

        System.out.println("\n========== HOW TO PLAY ==========");
        System.out.println("1. Read the clues and select a clue number.");
        System.out.println("2. Enter the correct word as your answer.");
        System.out.println("3. Use Hint to reveal the next missing letter.");
        System.out.println("4. Undo reverses your last correct move or hint.");
        System.out.println("5. Redo restores an undone move.");
        System.out.println("6. Check Progress shows your completion percentage.");
        System.out.println("7. View Solution displays all answers.");
        System.out.println("8. Complete all words to finish the game.");
    }

    private boolean isGameComplete() {

        for (boolean value : completed) {
            if (!value) {
                return false;
            }
        }

        return true;
    }

    private int readInt(Scanner scanner) {

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static char[][] copyMatrix(char[][] original) {

        char[][] copy = new char[original.length][];

        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }

        return copy;
    }

public int getSelectedDifficulty() {
    return selectedDifficulty;
}
public int getHintsUsed() {
    return hintsUsed;
}

}

