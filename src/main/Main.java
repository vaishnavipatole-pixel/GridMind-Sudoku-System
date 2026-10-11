package main;
import java.util.Scanner;
import java.util.InputMismatchException;
import dao.UserDAO;
import game.SudokuJourney;
import model.User;
import database.DatabaseConnection;
import model.SudokuBoard;
import solver.SolverStatistics;
import solver.SudokuSolver;
import validator.SudokuValidator;
import analysis.CandidateAnalyzer;
import analysis.DifficultyAnalyzer;
import game.SudokuGame;
import game.WordSearchGame;
import generator.SudokuGenerator;
import model.LeaderboardBST;
import dao.UserDAO;
/**
 * Phase 1 testing menu for the GridMind core engine.
 */
public class Main {
	// Sample Sudoku
    private static final int[][] SAMPLE_PUZZLE = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},

        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},

        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        UserDAO userDAO = new UserDAO();

        SudokuBoard sampleBoard =
                new SudokuBoard(SAMPLE_PUZZLE);

        SudokuValidator validator =
                new SudokuValidator();

        SudokuBoard lastGeneratedBoard = null;

        SudokuSolver solver =
                new SudokuSolver();

        SudokuGenerator generator =
                new SudokuGenerator();

        CandidateAnalyzer candidateAnalyzer =
                new CandidateAnalyzer();

        DifficultyAnalyzer difficultyAnalyzer =
                new DifficultyAnalyzer();

        boolean running = true;

        System.out.println();
        System.out.println("=================================");
        System.out.println("          S U D O S O L V E");
        System.out.println("=================================");

        while (running) {

            User currentUser =
                    authenticationMenu(scanner, userDAO);

            if (currentUser == null) {
                running = false;
                break;
            }

            System.out.println();
            System.out.println("Welcome, "
                    + currentUser.getUsername() + "!");
            System.out.println();
            showUserSolver(scanner, currentUser);
            boolean loggedIn = true;

            while (loggedIn && running) {

                printDashboardMenu(
                        currentUser.getUsername()
                );

                int choice =
                        readMenuChoice(scanner);

                switch (choice) {

                case 1:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;

                case 2:
                    System.out.println("Exiting SudoSolve...");
                    System.out.println("Thank you for using SudoSolve!");
                    loggedIn = false;
                    running = false;
                    break;

                default:
                    System.out.println(
                        "Invalid choice. Please enter 1 or 2."
                    );
            }
        }
        }
        scanner.close();
    }

    private static void showUserSolver(
            Scanner scanner,
            User currentUser) {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("          USER SOLVER");
            System.out.println("=================================");
            
            System.out.println("1. Digit Sudoku");
            System.out.println("2. Word Puzzle");
            System.out.println("3. My Profile");
            System.out.println("4. Leaderboard");
            System.out.println("5. Main Menu");
            int choice = readMenuChoice(scanner);

            switch (choice) {

                case 1:
                    showDigitSudokuModeMenu(
                            scanner,
                            currentUser
                    );
                    break;


case 2: {
    WordSearchGame wordGame = new WordSearchGame();

    boolean wordCompleted =
            wordGame.play(scanner, currentUser.getUserId());
    if (wordCompleted) {

        UserDAO userDAO = new UserDAO();

        userDAO.addXP(
            currentUser.getUserId(),
            100
        );

        int coins = 0;

        switch (wordGame.getSelectedDifficulty()) {
            case 1:
                coins = 50;
                break;

            case 2:
                coins = 100;
                break;

            case 3:
                coins = 200;
                break;

            case 4:
                coins = 350;
                break;
        }

        int hintsUsed = wordGame.getHintsUsed();
        int netCoins = coins - (hintsUsed * 5);

        if (netCoins < 0) {
            netCoins = 0;
        }

        userDAO.addCoins(
            currentUser.getUserId(),
            netCoins
        );
        userDAO.updateStreak(
            currentUser.getUserId()
        );

        System.out.println("\nPuzzle completed successfully!");
        System.out.println("XP earned: 100");
        System.out.println("Coins earned: " + netCoins);
    }

    break;
}

                case 3:

                    UserDAO userDAO =
                            new UserDAO();

                    userDAO.displayProfile(
                            currentUser.getUserId()
                    );

                    break;


                case 4: {
                    LeaderboardBST bst = new LeaderboardBST();

                    UserDAO dao = new UserDAO();
                    dao.populateLeaderboard(bst);

                    bst.display();
                    break;
                }

 case 5:
    running = false;
    break;

                

                default:
                    System.out.println(
                        "Invalid choice. Please enter 1 to 4."
                    );
            }
        }
    }

private static void showDigitSudokuModeMenu(
        Scanner scanner,
        User currentUser) {

    boolean running = true;

    while (running) {
        System.out.println();
        System.out.println("=================================");
        System.out.println("          DIGIT SUDOKU");
        System.out.println("=================================");
        System.out.println("1. Play by Difficulty");
        System.out.println("2. Level Mode");
        System.out.println("3. Back");
        System.out.print("Enter choice: ");

        int choice = readMenuChoice(scanner);

        switch (choice) {
            case 1:
                showPlayDifficultyMenu(scanner, currentUser);
                break;

            case 2:
                SudokuJourney journey =
                        new SudokuJourney(
                                scanner,
                                currentUser.getUserId()
                        );
                journey.startJourney();
                break;

            case 3:
                running = false;
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }
}


    
    private static void startDigitSudokuSystemSolver(
            Scanner scanner) {

        System.out.println();
        System.out.println("=================================");
        System.out.println("      DIGIT SUDOKU SYSTEM SOLVER");
        System.out.println("=================================");

        System.out.println();
        System.out.println("Enter your Sudoku puzzle.");
        System.out.println("Use 0 for empty cells.");
        System.out.println();

        int[][] puzzle = new int[9][9];

        for (int row = 0; row < 9; row++) {

            while (true) {

                System.out.print(
                    "Enter row " + (row + 1)
                    + " (9 numbers): "
                );

                String input = scanner.next();

                if (input.length() != 9) {
                    System.out.println(
                        "Invalid row. Enter exactly 9 digits."
                    );
                    continue;
                }

                boolean valid = true;

                for (int col = 0; col < 9; col++) {

                    char ch = input.charAt(col);

                    if (ch < '0' || ch > '9') {
                        valid = false;
                        break;
                    }

                    puzzle[row][col] =
                            ch - '0';
                }

                if (!valid) {
                    System.out.println(
                        "Invalid input. Use digits from 0 to 9."
                    );
                    continue;
                }

                break;
            }
        }

        SudokuBoard board =
                new SudokuBoard(puzzle);

        System.out.println();
        System.out.println("===== YOUR PUZZLE =====");
        board.display();
        SudokuValidator validator =
                new SudokuValidator();

        if (!validator.isValidBoard(board)) {

            System.out.println();
            System.out.println(
                "Invalid Sudoku puzzle."
            );

            System.out.println(
                "Please check duplicate or invalid values."
            );

            return;
        }

        SudokuSolver solver =
                new SudokuSolver();

        SudokuBoard solution =
                board.copy();

        boolean solved =
                solver.solve(solution);

        if (solved) {

            System.out.println();
            System.out.println("===== SOLUTION =====");
            solution.display();

            System.out.println();
            System.out.println("===== SOLVER STATISTICS =====");

            SolverStatistics statistics =
                    solver.getStatistics();

            statistics.display();

            System.out.println();
            System.out.println("===== PUZZLE ANALYSIS =====");

            CandidateAnalyzer candidateAnalyzer =
                    new CandidateAnalyzer();

            DifficultyAnalyzer difficultyAnalyzer =
                    new DifficultyAnalyzer();

            candidateAnalyzer.displayCandidates(board);

            difficultyAnalyzer.displayAnalysis(
                    board,
                    solver.getStatistics()
            );
        } else {

            System.out.println();
            System.out.println(
                "This Sudoku has no solution."
            );
        }
        scanner.nextLine();
    }
    private static void startDigitSudokuWithDifficulty(
            Scanner scanner,
            User currentUser) {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("        SELECT DIFFICULTY");
            System.out.println("=================================");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            System.out.println("4. Expert");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = readMenuChoice(scanner);

            String difficulty;
            int emptyCells;

            switch (choice) {

            case 1:
                difficulty = "EASY";
                emptyCells = 15;
                break;

            case 2:
                difficulty = "MEDIUM";
                emptyCells = 30;
                break;

            case 3:
                difficulty = "HARD";
                emptyCells = 45;
                break;

            case 4:
                difficulty = "EXPERT";
                emptyCells = 55;
                break;

                case 5:
                    return;

                default:
                    System.out.println(
                        "Invalid choice. Please enter 1 to 5."
                    );
                    continue;
            }

            SudokuGenerator generator =
                    new SudokuGenerator();

            SudokuSolver solver =
                    new SudokuSolver();

            SudokuBoard puzzle =
                    generator.generatePuzzle(emptyCells);

            SudokuBoard solution =
                    puzzle.copy();

            if (!solver.solve(solution)) {

                System.out.println(
                    "Unable to generate Sudoku."
                );

                continue;
            }

            SudokuGame game =
                    new SudokuGame(
                        puzzle,
                        solution,
                        difficulty
                    );

            boolean completed = game.play(scanner);

            if (completed) {

                UserDAO userDAO = new UserDAO();

                userDAO.addXP(
                        currentUser.getUserId(),
                        100
                );

                int coins = getCoinReward(difficulty);

                userDAO.addCoins(
                        currentUser.getUserId(),
                        coins
                );

                userDAO.updateStreak(
                        currentUser.getUserId()
                );
            }

            running = false;
        }
    }
    private static int getCoinReward(String difficulty) {

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
    private static String selectDifficulty(Scanner scanner) {

        while (true) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("        SELECT DIFFICULTY");
            System.out.println("=================================");
            System.out.println("1. Easy");
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            System.out.println("4. Expert");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            int choice = readMenuChoice(scanner);

            switch (choice) {

                case 1:
                    return "EASY";

                case 2:
                    return "MEDIUM";

                case 3:
                    return "HARD";

                case 4:
                    return "EXPERT";

                case 5:
                    return null;

                default:
                    System.out.println(
                        "Invalid choice. Please enter 1 to 5."
                    );
            }
        }
    }
    private static void startDigitSudokuGame(
            Scanner scanner,
            String difficulty) {

        SudokuGenerator generator =
                new SudokuGenerator();

        SudokuSolver solver =
                new SudokuSolver();

        int emptyCells;

        switch (difficulty) {

            case "EASY":
                emptyCells = 30;
                break;

            case "MEDIUM":
                emptyCells = 40;
                break;

            case "HARD":
                emptyCells = 50;
                break;

            case "EXPERT":
                emptyCells = 55;
                break;

            default:
                emptyCells = 30;
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("        " + difficulty + " SUDOKU");
        System.out.println("=================================");

        SudokuBoard puzzle =
                generator.generatePuzzle(emptyCells);

        SudokuBoard solution =
                puzzle.copy();

        boolean solved =
                solver.solve(solution);

        if (!solved) {
            System.out.println(
                "Unable to generate Sudoku."
            );
            return;
        }

        SudokuGame game =
                new SudokuGame(
                    puzzle,
                    solution,
                    difficulty
                );

        game.play(scanner);
    }
    // ==============================
    // REGISTER / LOGIN MENU
    // ==============================

    private static User authenticationMenu(
            Scanner scanner,
            UserDAO userDAO) {

        while (true) {

            System.out.println();
            System.out.println(
                "================================="
            );
            System.out.println(
                "        SUDOSOLVE ACCOUNT"
            );
            System.out.println(
                "================================="
            );

            System.out.println(
                "1. Register"
            );

            System.out.println(
                "2. Login"
            );

            System.out.println(
                "3. Exit"
            );

            System.out.print(
                "Enter choice: "
            );

            int choice =
                    readMenuChoice(scanner);

            switch (choice) {

                case 1:

                    registerUser(
                        scanner,
                        userDAO
                    );

                    break;


                case 2:

                    User user =
                            loginUser(
                                scanner,
                                userDAO
                            );

                    if (user != null) {
                        return user;
                    }

                    break;


                case 3:

                    System.out.println();
                    System.out.println(
                        "Exiting SudoSolve..."
                    );

                    return null;


                default:

                    System.out.println(
                        "Invalid choice."
                    );
            }
        }
    }


    // ==============================
    // REGISTER
    // ==============================

    private static void registerUser(
            Scanner scanner,
            UserDAO userDAO) {

        System.out.println();
        System.out.println(
            "===== REGISTER ====="
        );

        System.out.print(
            "Enter username: "
        );

        String username =
                scanner.nextLine().trim();

        if (username.isEmpty()) {

            System.out.println(
                "Username cannot be empty."
            );

            return;
        }
        String password;

        while (true) {

            System.out.print(
                "Enter password: "
            );

            password =
                    scanner.nextLine();

            if (password.isEmpty()) {

                System.out.println(
                    "Password cannot be empty."
                );

                continue;
            }

            if (!isStrongPassword(password)) {

                System.out.println();

                System.out.println(
                    "Password is not strong enough."
                );

                System.out.println(
                    "Password must contain:"
                );

                System.out.println(
                    "- At least 8 characters"
                );

                System.out.println(
                    "- At least 1 uppercase letter"
                );

                System.out.println(
                    "- At least 1 lowercase letter"
                );

                System.out.println(
                    "- At least 1 digit"
                );

                System.out.println(
                    "- At least 1 special character"
                );

                System.out.println();

                continue;
            }

            break;
        }
        
        	String confirmPassword;

        	while (true) {

        	    System.out.print(
        	        "Confirm password: "
        	    );

        	    confirmPassword =
        	            scanner.nextLine();

        	    if (!password.equals(confirmPassword)) {

        	        System.out.println();

        	        System.out.println(
        	            "Passwords do not match. Please try again."
        	        );

        	        System.out.println();

        	        continue;
        	    }

        	    break;
        	}
        User user =
                new User(
                    username,
                    password
                );

        userDAO.registerUser(user);
    }
    private static boolean isStrongPassword(String password) {

        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {

            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            }
            else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            }
            else if (Character.isDigit(ch)) {
                hasDigit = true;
            }
            else {
                hasSpecial = true;
            }
        }

        return hasUppercase
                && hasLowercase
                && hasDigit
                && hasSpecial;
    }

    // ==============================
    // LOGIN
    // ==============================

private static void showPlayYourselfMenu(
        Scanner scanner,
        User currentUser) {

    boolean running = true;

    while (running) {
        System.out.println();
        System.out.println("=================================");
        System.out.println("          PLAY YOURSELF");
        System.out.println("=================================");
        System.out.println("1. Play by Difficulty");
        System.out.println("2. Play by Level");
        System.out.println("3. Back");
        System.out.print("Enter choice: ");

        int choice = readMenuChoice(scanner);

        switch (choice) {
            case 1:
                startDigitSudokuWithDifficulty(
                        scanner,
                        currentUser
                );
                break;

            case 2:
                SudokuJourney journey =
                        new SudokuJourney(
                                scanner,
                                currentUser.getUserId()
                        );
                journey.startJourney();
                break;

            case 3:
                running = false;
                break;

            default:
                System.out.println(
                    "Invalid choice. Please enter 1 to 3."
                );
        }
    }
}
    private static User loginUser(
            Scanner scanner,
            UserDAO userDAO) {

        System.out.println();
        System.out.println(
            "===== LOGIN ====="
        );

        System.out.print(
            "Enter username: "
        );

        String username =
                scanner.nextLine().trim();

        System.out.print(
            "Enter password: "
        );

        String password =
                scanner.nextLine();

        User user =
                userDAO.loginUser(
                    username,
                    password
                );

        if (user != null) {

            System.out.println();
            System.out.println(
                "Login successful!"
            );

            return user;
        }

        System.out.println();
        System.out.println(
            "Invalid username or password."
        );

        return null;
    }


    // ==============================
    // DASHBOARD MENU
    // ==============================

    private static void printDashboardMenu(String username) {
        System.out.println();
        System.out.println("=================================");
        System.out.println("       SUDOSOLVE DASHBOARD");
        System.out.println("       Player: " + username);
        System.out.println("=================================");
        System.out.println("1. Logout");
        System.out.println("2. Exit");
        System.out.print("Enter choice: ");
    }

   

private static void showPlayDifficultyMenu(
        Scanner scanner,
        User currentUser) {

    boolean running = true;

    while (running) {
        System.out.println();
        System.out.println("=================================");
        System.out.println("       PLAY BY DIFFICULTY");
        System.out.println("=================================");
        System.out.println("1. User Plays Sudoku");
        System.out.println("2. System Solver");
        System.out.println("3. Back");
        System.out.print("Enter choice: ");

        

int choice = readMenuChoice(scanner);
        switch (choice) {
            case 1:
                startDigitSudokuWithDifficulty(
                        scanner,
                        currentUser
                );
                break;

            case 2:
                startDigitSudokuSystemSolver(scanner);
                break;

            case 3:
                running = false;
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }
}

    // ==============================
    // SOLVE SAMPLE
    // ==============================

    private static void solveSample(
            SudokuBoard sampleBoard,
            SudokuSolver solver,
            SudokuValidator validator) {

        System.out.println(
            "Original Sudoku:"
        );

        sampleBoard.display();

       
        if (!validator.isValidBoard(sampleBoard)) {
            System.out.println(
                "Sudoku is invalid."
            );

            return;
        }

        SudokuBoard board =
                sampleBoard.copy();

        boolean solved =
                solver.solve(board);

        if (solved) {

            System.out.println();
            System.out.println(
                "Solved Sudoku:"
            );

            board.display();

        } else {

            System.out.println(
                "No solution found."
            );
        }
    }


    // ==============================
    // SHOW STATISTICS
    // ==============================

    private static void showStatistics(
            SudokuSolver solver) {

        SolverStatistics stats =
                solver.getStatistics();

        System.out.println(
            "Recursive Calls : "
            + stats.getRecursiveCalls()
        );

        System.out.println(
            "Backtracks      : "
            + stats.getBacktracks()
        );

        System.out.println(
            "Cells Filled    : "
            + stats.getCellsFilled()
        );

        System.out.println(
            "Execution Time  : "
            + stats.getExecutionTime()
            + " ms"
        );
    }


    // ==============================
    // PUZZLE ANALYSIS
    // ==============================

    private static void analyzePuzzle(
            SudokuBoard board,
            CandidateAnalyzer candidateAnalyzer,
            DifficultyAnalyzer difficultyAnalyzer,
            SudokuSolver solver) {

        System.out.println(
            "Analyzing generated puzzle..."
        );

        board.display();

        int emptyCells = 0;

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                if (board.isEmpty(i, j)) {
                    emptyCells++;
                }
            }
        }

        System.out.println();
        System.out.println(
            "Empty Cells: "
            + emptyCells
        );

        difficultyAnalyzer.displayAnalysis(
                board,
                solver.getStatistics()
        );
        System.out.println();
        System.out.println(
            "Candidate Analysis:"
        );

        boolean foundEmptyCell = false;

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                if (board.isEmpty(i, j)) {

                    System.out.println(
                        "Cell ("
                        + (i + 1)
                        + ","
                        + (j + 1)
                        + ") -> "
                        + candidateAnalyzer.getCandidates(
                            board,
                            i,
                            j
                        )
                    );

                    foundEmptyCell = true;

                    break;
                }
            }

            if (foundEmptyCell) {
                break;
            }
        }
    }


    // ==============================
    // READ MENU CHOICE
    // ==============================
    

private static int readMenuChoice(Scanner scanner) {
    while (true) {
        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.print("Please enter a valid number: ");
        }
    }
}

}


// For System Solver
/* 
530070000
600195000
098000060
800060003
400803001
700020006
060000280
000419005
000080079
 */
