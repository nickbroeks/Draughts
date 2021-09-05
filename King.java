import java.util.ArrayList;
import java.lang.Math;
import Exceptions.PositionOutOfBoundsException;

class King extends Piece {
    
    King(int _x, int _y, boolean _team) {
        super(_x, _y, _team);
    }
    
    @Override
    ArrayList<Move> regularMoves(int rows, int cols) {
        ArrayList<Move> result = new ArrayList<Move>();
        for (int i = 0; i < Math.max(rows, cols); i++) {
            try {result.add(new Move(upLeft(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Move(upRight(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Move(downLeft(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Move(downRight(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
        }
        result.removeIf(m -> (!m.isValid()));
        return result;
    }

    @Override
    ArrayList<Capture> captureMoves(int rows, int cols) {
        ArrayList<Capture> result = new ArrayList<Capture>();
        for (int i = 1; i < Math.max(rows, cols); i++) {
            try {result.add(new Capture(upLeft(rows, cols, i + 1), upLeft(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Capture(upRight(rows, cols, i + 1), upRight(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Capture(downLeft(rows, cols, i + 1), downLeft(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Capture(downRight(rows, cols, i + 1), downRight(rows, cols, i)));} catch (PositionOutOfBoundsException e) {}
        }
        result.removeIf(m -> (!m.isValid()));
        return result;
    }
    
}