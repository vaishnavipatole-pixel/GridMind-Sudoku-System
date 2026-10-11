package game;
import java.util.ArrayDeque;
import java.util.Deque;

import model.Move;
public class MoveHistory {
	private Deque<Move> undoStack;
    private Deque<Move> redoStack;

    public MoveHistory() {
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }

    public void addMove(Move move) {
        undoStack.push(move);
        redoStack.clear();
    }

    public Move undo() {
        if (undoStack.isEmpty()) {
            return null;
        }

        Move move = undoStack.pop();
        redoStack.push(move);

        return move;
    }

    public Move redo() {
        if (redoStack.isEmpty()) {
            return null;
        }

        Move move = redoStack.pop();
        undoStack.push(move);

        return move;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public int getUndoCount() {
        return undoStack.size();
    }

    public int getRedoCount() {
        return redoStack.size();
    }
}
