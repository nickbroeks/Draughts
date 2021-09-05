import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import Exceptions.InvalidMoveException;
import Exceptions.NoValidMovesException;
import Exceptions.WrongInputException;

public class Checkers {
    Board board;
    Scanner sc;
    Random random;
    boolean singlePlayer;

    Checkers() {
        sc = new Scanner(System.in);
        random = new Random();
    }

    void setupGame() {
        System.out.print("Do you want to start a multiplayer game? (y/n): ");
        String input = sc.nextLine().toLowerCase();
        while (!input.matches("[yn]")) { //Input must be either 'y' or 'n'
            System.out.println("Please respond with 'y' or 'n'");
            System.out.print("Do you want to start a multiplayer game? (y/n): ");
            input = sc.nextLine().toLowerCase();
        }
        singlePlayer = (input.charAt(0) == 'n');

        board = new Board(10, 10);
        board.setupGame();
        System.out.println("\n\n");
    }
    
    int selectPiece(boolean currPlayer, Set<Piece> possiblePieces) {

        //Keep infinitely looping until we get a valid input and return the index
        while(true) {
            board.show();
            System.out.println("");
            if (currPlayer) {System.out.print((char)0x25CF);} else {System.out.print((char)0x25CB);} //Display current player icon
            System.out.print("  Select the piece you want to move (Ex. D1):");
            String input = sc.nextLine();
            System.out.println("");
            int index = 0;
            try {
                index = board.getIndexFromPosition(input); //Try to parse input
            }
            catch( WrongInputException e) { //Catch wrong input exception
                System.out.println(e.getMessage());
                continue;   //Start this loop again and ask again for input
            } 

            System.out.println("\n\n");
            Piece piece = board.getPiece(index);
            if (piece == null) {
                System.out.println("There is no piece here");
                continue;
            } else if (board.getPiece(index).team != currPlayer) {
                System.out.println("This is not your piece");
                continue;
            }
            if(possiblePieces.contains(piece)) {System.out.println("\n\n Selected piece: " + input);return index;}
            System.out.println("This piece is unable to move");
        }
    }

    int selectPosition(Move[] possiblePosition) throws InvalidMoveException{
        board.show();
        int index = -1; //Index of chosen position, use -1 as "not found yet"
        while (index == -1) {
            System.out.print("\n\nSelect the position you want to move to (Ex. D1):");
            String input = sc.nextLine();
            System.out.println("");
            try {
                index = board.getIndexFromPosition(input);//Try to parse input
            } catch ( WrongInputException e) {//Catch wrong input exception
                System.out.println(e.getMessage());
                continue; //Start this loop again and ask again for input
            }
        }
        Piece piece = board.getPiece(index);
        if (piece != null) {
            throw new InvalidMoveException("There is already a piece here");
        }
        for (int i = 0; i < possiblePosition.length; i++) { //loop over all possible positions for this piece
            if (possiblePosition[i].indexPosition == index){ // if the chosen index is the current index
                return i;                                       //return index
            }
        }
        throw new InvalidMoveException("This is not a valid move"); //If this code is reached, no valid move was found
    }

    void computerTurn(HashMap<Piece,Move[]> possibleMoves, boolean player1) {
        boolean turnEnded = false;
        while (!turnEnded) {
            board.show();
            System.out.println("\n");
            try{TimeUnit.MILLISECONDS.sleep(200);}catch(InterruptedException ex){} //Wait 2 seconds
            Piece[] possiblePieces = possibleMoves.keySet().toArray(new Piece[0]); //Get all pieces that can be moved
            Piece selectedPiece = possiblePieces[random.nextInt(possiblePieces.length)]; //Choose a random piece to move
            Move[] possiblePositions = possibleMoves.get(selectedPiece); //Get all the positions this piece can go to
            Move selectedMove = possiblePositions[random.nextInt(possiblePositions.length)]; //Choose a random position for this piece to go to
            board.move(board.getIndex(selectedPiece.y, selectedPiece.x), selectedMove);
            turnEnded = true;
            while (selectedMove instanceof Capture) { //While we capture we can keep going
                possibleMoves = board.getPossibleMoves(player1); //Get a new list of all moves that can be done
                selectedPiece = board.getPiece(selectedMove.indexPosition); //Get the piece used too capture last piece
                Move[] nextMoves = possibleMoves.get(selectedPiece); //Choose a random position for the  piece to go to
                if(nextMoves != null && nextMoves[0] instanceof Capture) { //If this piece can move and that move is a capture
                    selectedMove = possiblePositions[random.nextInt(possiblePositions.length)]; //Choose a random position for this piece to go to
                    board.move(board.getIndex(selectedPiece.y, selectedPiece.x), selectedMove);
                }
            }
        }
    }
    void turn(boolean player1) throws NoValidMovesException {
        HashMap<Piece,Move[]> possibleMoves = board.getPossibleMoves(player1);
        if (possibleMoves.size() == 0) {throw new NoValidMovesException();}

        if (singlePlayer && !player1) {computerTurn(possibleMoves, player1);} else {
            Move selectedMove = null;
            while(selectedMove == null) { //While we haven't made a move yet
                int selectedPieceIndex = 0;
                int selectedIndex = 0;
                Set<Piece> possiblePieces = possibleMoves.keySet();
                selectedPieceIndex = selectPiece(player1, possiblePieces); //Select a piece from the possible pieces
                Move[] possiblePositions = possibleMoves.get(board.getPiece(selectedPieceIndex));
                try {
                    selectedIndex = selectPosition(possiblePositions);
                } catch (InvalidMoveException e) {//When an incorrect move is made
                    System.out.println(e.getMessage());
                    continue;
                }
    
                selectedMove = possiblePositions[selectedIndex]; //Save the move
                board.move(selectedPieceIndex, selectedMove);

                while (selectedMove instanceof Capture) { //While we capture we can keep going
                    possibleMoves = board.getPossibleMoves(player1); //Get a new list of all moves that can be done
                    Piece selectedPiece = board.getPiece(selectedMove.indexPosition); //Get the piece used too capture last piece
                    Move[] nextMoves = possibleMoves.get(selectedPiece); //Choose a random position for the  piece to go to
                    if(nextMoves != null && nextMoves[0] instanceof Capture) { //If this piece can move and that move is a capture
                        selectedMove = possiblePositions[random.nextInt(possiblePositions.length)]; //Choose a random position for this piece to go to
                        board.move(board.getIndex(selectedPiece.y, selectedPiece.x), selectedMove);
                    } else {
                        selectedMove = null;
                    }
                }
            }
        }

        board.checkNewKings(player1);
    }

    void run() {
        setupGame();
        boolean player1 = true;
        try {
            while (true) { //As long as someone can move, keep playing
                turn(player1);
                System.out.println("\n\n");
                player1 = !player1;
            }
        } catch (NoValidMovesException e) {}
        
        board.show();
        if (player1) {
            System.out.print("Player 1 has won");
        } else {
            System.out.print("Player 2 has won");
        }
    }
    public static void main(String[] args) {
        (new Checkers()).run();
    }
}
