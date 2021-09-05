import java.util.ArrayList;

import Exceptions.PositionOutOfBoundsException;

class Pawn extends Piece {
    
    Pawn(int _x, int _y, boolean _team) {
        super(_x, _y, _team);
    }
    
    @Override
    ArrayList<Move> regularMoves(int rows, int cols) {
        ArrayList<Move> result = new ArrayList<Move>();
        if(team) {
            try {result.add(new Move(upLeft(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Move(upRight(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
            
            
        } else {
            try {result.add(new Move(downLeft(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Move(downRight(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
        }
        return result;
    }

    @Override
    ArrayList<Capture> captureMoves(int rows, int cols) {
        ArrayList<Capture> result = new ArrayList<Capture>();
        if(team) {
            try {result.add(new Capture(upLeft(rows, cols, 2), upLeft(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Capture(upRight(rows, cols, 2), upRight(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
        } else {
            try {result.add(new Capture(downLeft(rows, cols, 2), downLeft(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
            try {result.add(new Capture(downRight(rows, cols, 2), downRight(rows, cols, 1)));} catch (PositionOutOfBoundsException e) {}
        }
        result.removeIf(m -> (!m.isValid()));
        return result;
    }
    

    
    
}
