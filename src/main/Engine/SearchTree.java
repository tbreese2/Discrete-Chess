package Engine;

/**
 *
 * @author tylerbreese
 */

//helping class to facilitate bound and branching of negamax
//relativly limits memory hogging of deep search depth
public class SearchTree {
    //layer metadata
    public byte ply = -1;
    private final int GENERATED = 0;
    private final int USED = 1;
    
    //some future data for multithreading
    //ill get to this, don't you worry
    private int threadNum;
    private static int threadCount = 0;
    private static long nodeCount;
    
    //data, as well as MLA move ordering and HLA heursitics data
    private int[][] layerData = new int[255][2];
    private int[][] moves = new int[255][218];
    private int[] scores = new int[218];
    private int[][] historicalData = new int[2][255];
    private int pv;

    //MODIFIES: this
    //EFFECTS: creates a new search tree
    //also adds it as thread
    public SearchTree() {
        threadCount++;
        threadNum = threadCount;
        nodeCount = 0;
    }
    
    public void setPV(int p) {
        pv = p;
    }
    
    public void sortPV() {
        for(int i = 0; i < layerData[ply][GENERATED]; i++) {
            if(moves[ply][i] == pv) {
                int temp = moves[ply][0];
                moves[ply][0] = pv;
                moves[ply][i] = temp;
                break;
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: creates a new layer in the search tree
    public void newLayer() {
      ply++;
      nodeCount++;
    }
    
    //EFFECTS: returns current node count of search
    public long getNodeCount() {
        return nodeCount;
    }

    //MODIFIES: this
    //EFFECTS: ends layer, moving a layer back up the search tree
    //all data in the previous layer will
    //be supsiquently overwritten and lost
    public void endLayer() {
      layerData[ply][GENERATED] = 0;
      layerData[ply][USED] = 0;
      ply--;
    }
    
    //EFFECTS: returns true if search depth
    //is 1
    public boolean isTop() {
        return ply == 0;
    }
    
    //EFFETS: returns the first non-used move
    public int getFirstMove() {
        return moves[ply][layerData[ply][USED]];
    }

    //EFFECTS: returns the first non-used move
    //then marks it as used
    public int next() {
       return moves[ply][layerData[ply][USED]++];
    }

    //EFFECTS: returns true if a layer is not empty
    public boolean isLayerNotEmpty() {
        return layerData[ply][GENERATED] != layerData[ply][USED];
    }

    //MODIFIES: this
    //EFFECTS: adds a move to the current layer
    public void addNode(final int move) {
        moves[ply][layerData[ply][GENERATED]++] = move;
    }
    
    //MODIFIES: this
    //adds move to historical data set
    public void setHistoricEval(int score, int color, byte ply) {
        historicalData[color][ply] = score;
    }
    
    //RETURNS: historical eval
    public int getHistoricEval(int color, byte ply) {
        return historicalData[color][ply];
    }
    
    //RETURNS: current ply
    public byte getPly() {
        return (byte)(ply + 1);
    }
    
    //TODO: add MLA and HLA move ordering
}
