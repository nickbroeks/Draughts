import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Exceptions.WrongInputException;   

class Board {

    int rows, cols;
    HashMap<Integer, Piece> pieces;
    int getIndex(int r, int c) {return r * cols + c;}
    int getX(int index) {return index % cols;}
    int getY(int index) {return index / cols;}
    Piece getPiece(int index) {return pieces.get(index);}

    Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        pieces = new HashMap<>();
    }

    void setupGame() {
        for(int r = 0; r < rows * 0.4; r++) { //Loop through the top 40% of the grid
            for (int c = (r + 1) % 2; c < cols; c+=2) { //Loop through every other column
                pieces.put(getIndex(r,c), new Pawn(c, r, false)); //Put a piece down for player2
            }
        }

        for(int r = rows; r > rows * 0.6; r--) {//Loop through the bottom 40% of the grid
            for (int c = r % 2; c < cols; c+=2) { //Loop through every other column
                pieces.put(getIndex(r - 1,c), new Pawn(c, r - 1, true)); //Put a piece down for player1
            }
        }
    }

    void show() {
        for(int r = 0; r < rows; r++) { //For every row
            System.out.print(Character.toChars(r+65)); //Show letter on left side of grid
            System.out.print("  ");
            for (int c = 0; c < cols; c++) {  
                Piece piece = getPiece(getIndex(r,c));
                if (piece == null) {
                    System.out.print("  ");
                } else {
                    if (piece instanceof Pawn) {
                        if(piece.team) {
                            System.out.print(String.valueOf((char)0x25CF) + " ");
                        } else {
                            System.out.print(String.valueOf((char)0x25CB) + " ");
                        }
                    } else {
                        if(piece.team) {
                            System.out.print(String.valueOf((char)0x263B) + " ");
                        } else {
                            System.out.print(String.valueOf((char)0x263A) + " ");
                        }
                    }
                }
            }
            System.out.println("");
        }

        System.out.print("   ");
        for (int i = 0; i < rows; i++) {
            System.out.print(i + " ");
        }
        System.out.println("");
        
    }

    void move(int selectedPieceIndex, Move move) {
        //Do a simple swap
        Piece selectedPiece = getPiece(selectedPieceIndex);
        pieces.remove(selectedPieceIndex);
        selectedPiece.move(getX(move.indexPosition), getY(move.indexPosition));
        pieces.put(move.indexPosition, selectedPiece);
        
        if(move instanceof Capture) {
            pieces.remove(((Capture)move).indexCaptured); //Remove captured piece
        }
    }

    HashMap<Piece,Move[]> getPossibleMoves(boolean team) {
        //Initialize a variable which maps pieces to all possible moves with that piece
        HashMap<Piece, Move[]> possibleMoves = new HashMap<Piece,Move[]>(); 
        for (Integer index : pieces.keySet()) { //Loop through pieces
            Piece piece = getPiece(index);
            if (piece.team == team) { //Only search moves for pieces of current team
                ArrayList<Capture> possibleMovesPiece = piece.captureMoves(rows, cols); //Search for capturemoves

                Iterator<Capture> itr = possibleMovesPiece.iterator(); //Loop through all the capturemoves
                while (itr.hasNext()) {
                    Capture c = itr.next();
                    Piece capturedPiece = getPiece(c.indexCaptured);
                    Piece emptyPiece = getPiece(c.indexPosition);
                    if (capturedPiece == null || capturedPiece.team == team || emptyPiece != null) {
                        itr.remove();            //And remove those who are invalid
                    } 
                }
                if (!possibleMovesPiece.isEmpty()) { //If there are capture moves for this piece, add them to possibleMoves
                    possibleMoves.put(piece, possibleMovesPiece.toArray(new Move[0]));
                }
                
            }
        }
        if (!possibleMoves.isEmpty()) {//If there are capture moves, return them
            System.out.println("You have to capture a piece");
            return possibleMoves;
        }
        //If you reach this code you can't capture and we check for regular moves

        for (Integer index : pieces.keySet()) {//Loop through pieces
            Piece piece = getPiece(index);
            if (piece.team == team) {//Only search moves for pieces of current team
                ArrayList<Move> PossibleMovesPiece = piece.regularMoves(rows, cols); //Search for capturemoves
                Iterator<Move> itr = PossibleMovesPiece.iterator();
                while (itr.hasNext()) {
                    Move c = itr.next();
                    Piece emptyPiece = getPiece(c.indexPosition);
                    if (emptyPiece != null) {itr.remove();} //And remove those who are invalid
                }
                if (!PossibleMovesPiece.isEmpty()) { //If there are capture moves for this piece, add them to possibleMoves
                    possibleMoves.put(piece, PossibleMovesPiece.toArray(new Move[0]));
                }
                
            }
        }
        return possibleMoves;
        
    }

    void checkNewKings(boolean player1) {
        int row = 0;
        if (!player1) { //Select which row is the King Row
            row = rows - 1;
        }
        for (int c = 0; c < cols; c++) {
            int index = getIndex(row, c);
            Piece piece = getPiece(index);
            if(piece != null && piece.team == player1 && piece instanceof Pawn) {
                pieces.replace(index, makeKing(piece)); //Change pawn to King
            }
        }
        
    }

    King makeKing(Piece piece) {
        return new King(piece.x, piece.y, piece.team);
    }

    int getIndexFromPosition(String input) throws WrongInputException{
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (!input.matches("[" + alphabet.subSequence(0, rows) + "]\\d")) {
            throw new WrongInputException("This answer is not in the right form. It should be in the form 'A3'");
        }
        return getIndex(alphabet.indexOf(input.substring(0,1)), Integer.parseInt(input.substring(1,2)));
    }
}
