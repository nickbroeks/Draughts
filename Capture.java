public class Capture extends Move {
    
    int indexCaptured;
    Capture(int indexPosition, int indexCaptured) {
        super(indexPosition);
        this.indexCaptured = indexCaptured;
    }
}
