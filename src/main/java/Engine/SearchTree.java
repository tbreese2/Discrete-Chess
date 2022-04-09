package Engine;

/**
 *
 * @author tylerbreese
 */

//helping class to facilitate bound and branching of negamax
//relativly limits memory hogging of deep search depth
public class SearchTree {
    //layer metadaa
    private int depth = -1;
    private final int GENERATED = 0;
    private final int USED = 1;
    
    //some future data for multithreading
    //ill get to this, don't you worry
    private int threadNum;
    private static int threadCount = 0;
    
    //data, as well as MLA move ordering and HLA heursitics data
    private int[][] layerData = new int[100][2];
    private int[][] moves = new int[100][218];
    private int[] scores = new int[218];

    //MODIFIES: this
    //EFFECTS: creates a new search tree
    //also adds it as thread
    public SearchTree() {
        threadCount++;
        threadNum = threadCount;
    }

    //MODIFIES: this
    //EFFECTS: creates a new layer in the search tree
    public void newLayer() {
      depth++;
    }

    //MODIFIES: this
    //EFFECTS: ends layer, moving a layer back up the search tree
    //all data in the previous layer will
    //be supsiquently overwritten and lost
    public void endLayer() {
      layerData[depth][GENERATED] = 0;
      layerData[depth][USED] = 0;
      depth--;
    }
    
    //EFFECTS: returns true if search depth
    //is 1
    public boolean isTop() {
        return depth == 0;
    }
    
    //EFFETS: returns the first non-used move
    public int getFirstMove() {
        return moves[depth][layerData[depth][USED]];
    }

    //EFFECTS: returns the first non-used move
    //then marks it as used
    public int next() {
       return moves[depth][layerData[depth][USED]++];
    }

    //EFFECTS: returns true if a layer is not empty
    public boolean isLayerNotEmpty() {
        return layerData[depth][GENERATED] != layerData[depth][USED];
    }

    //MODIFIES: this
    //EFFECTS: adds a move to the current layer
    public void addNode(final int move) {
        moves[depth][layerData[depth][GENERATED]++] = move;
    }
    
    //TODO: add MLA and HLA move ordering
}
