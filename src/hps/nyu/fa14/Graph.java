package hps.nyu.fa14;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Represents a directed graph as a implicitly numbered set of nodes and edges
 * from node i to j that are either present or not
 **/
public class Graph {

    /**
     * Nodes are 1-indexed
     */
    public final int nodes;

    /**
     * Nodes are 1-indexed, so this matrix is 1 larger than it needs to be in each dimension
     */
    public final boolean[][] edges;

    public Graph(int nodeCount) {
        nodes = nodeCount;
        edges = new boolean[nodes + 1][nodes + 1];
    }

    /**
     * Returns true if the graph is weakly connected meaning the undirected
     * version is connected
     * 
     * @return
     */
    public boolean isConneced() {
        // TODO: Implement
        throw new NoSuchMethodError();
    }

    /**
     * Returns true if the graph does not contain any cycles
     * @return
     */
    public boolean isAcyclic() {
        throw new NoSuchMethodError();
    }
    
    /**
     * Deep copy a graph
     */
    public Graph clone() {
        Graph g = new Graph(nodes);
        for(int i = 1; i <= nodes; i++){
            for(int j = 1; j <= nodes; j++){
                g.edges[i][j] = edges[i][j];
            }
        }
        return g;
    }

    /**
     * Write the representation of a graph as a set of edges
     * @param bw
     */
    public void write(BufferedWriter bw) throws IOException {
        for(int i = 1; i <= nodes; i++){
            for(int j = 1; j <= nodes; j++){
                if(edges[i][j]){
                    // There is an edge from i to j
                    bw.write(String.format("%d,%d ", i, j));
                }
            }
        }
        bw.newLine();
    }
    
    private static final Random RAND = new Random();
    
    /**
     * Generates a weakly connected directed acyclic graph with random edges
     * @param nodeCount
     * @return
     */
    public static Graph randomDAG(int nodeCount){
        Graph g = new Graph(nodeCount);

        
        
        return g;
    }
    
    /**
     * Generates an arbitrary directed graph with n-1 edges
     * (possibly connected)
     * @param nodeCount
     * @return
     */
    public static Graph random(int nodeCount){
        Graph g = new Graph(nodeCount);
        
        int eCount = 0;
        while(eCount < nodeCount - 1){
            int i = RAND.nextInt(nodeCount) + 1;
            int j = RAND.nextInt(nodeCount) + 1;
            if(!g.edges[i][j]){
                g.edges[i][j] = true;
                eCount++;
            }
        }
        return g;
    }
}
