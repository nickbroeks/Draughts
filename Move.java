public class Move {
    int indexPosition;

    Move(int indexPosition){
        this.indexPosition = indexPosition;
    }

    boolean isValid() {
        return indexPosition != -1;
    }
}
