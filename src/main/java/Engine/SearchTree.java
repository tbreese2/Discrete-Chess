package Engine;

/**
 *
 * @author tylerbreese
 */
public class SearchTree {

    private int ply = 0;

    public int[] pv;
    public int bestScore;
    public int depth;

    private int threadNum;

    private final int[] nextToGenerate = new int[64 * 2];
    private final int[] nextToMove = new int[64 * 2];

    private final int[] moves = new int[1500];
    private final int[] moveScores = new int[1500];

    public SearchTree(int threadNumber) {
        this.threadNum = threadNumber;
        if (threadNumber == 0) {
            pv = new int[12];
        }
    }

    public void startPly() {
        nextToGenerate[ply + 1] = nextToGenerate[ply];
        nextToMove[ply + 1] = nextToGenerate[ply];
        ply++;
    }

    public void endPly() {
        ply--;
    }

    public int next() {
        return moves[nextToMove[ply]++];
    }

    public int getMoveScore() {
        return moveScores[nextToMove[ply] - 1];
    }

    public int previous() {
        return moves[nextToMove[ply] - 1];
    }

    public boolean hasNext() {
        return nextToGenerate[ply] != nextToMove[ply];
    }

    public void addMove(final int move) {
        moves[nextToGenerate[ply]++] = move;
    }
    
    public void sort() {
        final int left = nextToMove[ply];
        for (int i = left, j = i; i < nextToGenerate[ply] - 1; j = ++i) {
            final int score = moveScores[i + 1];
            final int move = moves[i + 1];
            while (score > moveScores[j]) {
                moveScores[j + 1] = moveScores[j];
                moves[j + 1] = moves[j];
                if (j-- == left) {
                    break;
                }
            }
            moveScores[j + 1] = score;
            moves[j + 1] = move;
        }
    }

}
