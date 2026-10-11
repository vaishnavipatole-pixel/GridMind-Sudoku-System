package solver;

public class SolverStatistics {
	private int recursiveCalls;
    private int backtracks;
    private int cellsFilled;
    private long executionTime;

    public SolverStatistics() {
        reset();
    }

    public void reset() {
        recursiveCalls = 0;
        backtracks = 0;
        cellsFilled = 0;
        executionTime = 0;
    }

    public void incrementRecursiveCalls() {
        recursiveCalls++;
    }

    public void incrementBacktracks() {
        backtracks++;
    }

    public void incrementCellsFilled() {
        cellsFilled++;
    }

    public int getRecursiveCalls() {
        return recursiveCalls;
    }

    public int getBacktracks() {
        return backtracks;
    }

    public int getCellsFilled() {
        return cellsFilled;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public void display() {

        System.out.println("\n================================");
        System.out.println("       SOLVER STATISTICS");
        System.out.println("================================");
        System.out.println("Recursive Calls : " + recursiveCalls);
        System.out.println("Backtracks      : " + backtracks);
        System.out.println("Cells Filled    : " + cellsFilled);
        System.out.println("Execution Time  : " + executionTime + " ms");
        System.out.println("================================");
    }
    public void setCellsFilled(int count) {
        this.cellsFilled = count;
    }
}
