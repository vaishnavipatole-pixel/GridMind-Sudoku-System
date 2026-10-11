package game;

public class ScoreCalculator {
	 public int calculateScore(
	            int mistakes,
	            int hintsUsed,
	            long timeTaken,
	            String difficulty) {

	        int score = 1000;

	        // Mistake penalty
	        score -= mistakes * 50;

	        // Hint penalty
	        score -= hintsUsed * 30;

	        // Time penalty
	        if (timeTaken > 300) {
	            score -= (int) ((timeTaken - 300) / 10);
	        }

	        // Difficulty bonus
	        if (difficulty.equalsIgnoreCase("EASY")) {
	            score += 100;
	        }
	        else if (difficulty.equalsIgnoreCase("MEDIUM")) {
	            score += 200;
	        }
	        else if (difficulty.equalsIgnoreCase("HARD")) {
	            score += 300;
	        }
	        else if (difficulty.equalsIgnoreCase("EXPERT")) {
	            score += 500;
	        }

	        // Score should not be negative
	        if (score < 0) {
	            score = 0;
	        }

	        return score;
	    }
}
