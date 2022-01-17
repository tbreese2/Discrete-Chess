package Engine;

/**
 *
 * @author tylerbreese
 */
public class MoveList {

    private int[] moves;
    private int count;
    private int cur = 0;

    public MoveList(int max) {
        moves = new int[max];
    }

    public MoveList() {
        this(100);
    }

    public void reserved_clear() {
        count = 0;
    }

    public final void reserved_add(int move) {
        moves[count++] = move;
    }

    public final void reserved_removeLast() {
        count--;
    }

    public final int reserved_getCurrentSize() {
        return count;
    }

    public final int[] reserved_getMovesBuffer() {
        return moves;
    }

    public void clear() {
        reserved_clear();
        cur = 0;
    }

    public int next() {
        if (cur < count) {
            return moves[cur++];
        } else {
            return 0;
        }
    }

    public int size() {
        return count;
    }
}

