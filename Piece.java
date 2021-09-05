import java.util.ArrayList;

import Exceptions.PositionOutOfBoundsException;

abstract class Piece {
    int x;
    int y;
    boolean team;
    Piece(int _x, int _y, boolean _team) {
        this.x = _x;
        this.y = _y;
        this.team = _team;
    }

    void move(int _x, int _y) {
        this.x = _x;
        this.y = _y;        
    }

    int upRight(int rows, int cols, int steps) throws PositionOutOfBoundsException {
        if (x + steps >= cols || y - steps < 0) { //Formula which checks if we go out of bounds
            throw new PositionOutOfBoundsException();
        }
        return (y - steps) * cols + x + steps;
    }
    int upLeft(int rows, int cols, int steps) throws PositionOutOfBoundsException {
        if (x - steps < 0 || y - steps < 0) { //Formula which checks if we go out of bounds
            throw new PositionOutOfBoundsException();
        }
        return (y - steps) * cols + x - steps;
    }
    int downRight(int rows, int cols, int steps) throws PositionOutOfBoundsException {
        if (x + steps >= cols || y + steps >= rows) { //Formula which checks if we go out of bounds
            throw new PositionOutOfBoundsException();
        }
        return (y + steps) * cols + x + steps;
    }
    int downLeft(int rows, int cols, int steps) throws PositionOutOfBoundsException {
        if (x - steps < 0 || y + steps >= rows) { //Formula which checks if we go out of bounds
            throw new PositionOutOfBoundsException();
        }
        return (y + steps) * cols + x - steps;
    }

    abstract ArrayList<Move> regularMoves(int rows, int cols);
    abstract ArrayList<Capture> captureMoves(int rows, int cols);
}
