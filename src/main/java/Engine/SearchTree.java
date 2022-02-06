package Engine;

/**
 *
 * @author tylerbreese
 */
public class SearchTree {
    
    private int depth = -1;
    private int[][] layerData = new int[100][2];
    private int[][] moves = new int[100][218];
    private int[] scores = new int[218];
    
    private final int GENERATED = 0;
    private final int USED = 1;
    private int threadNum;
    
    public static int threadCount = 0;
    
    
    public SearchTree() {
        threadCount++;
        threadNum = threadCount;
    }

    public void newLayer() {
      depth++;
    }

    public void endLayer() {
      layerData[depth][GENERATED] = 0;
      layerData[depth][USED] = 0;
      depth--;
    }
    
    public boolean isTop() {
        return depth == 0;
    }
    
    public int getFirstMove() {
        return moves[depth][layerData[depth][USED]];
    }

    public int next() {
       return moves[depth][layerData[depth][USED]++];
    }

    public boolean isLayerNotEmpty() {
        return layerData[depth][GENERATED] != layerData[depth][USED];
    }

    public void addNode(final int move) {
        moves[depth][layerData[depth][GENERATED]++] = move;
    }
    
    public void sort() {
        
    }

}
