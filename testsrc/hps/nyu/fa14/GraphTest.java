package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class GraphTest {

    @Test
    public void testGenerateRandom() {
        Graph g = Graph.random(30);
        assertEquals(29, g.numEdges());
    }

    @Test
    public void testIsConnected() {
        Graph g = new Graph(50);
        for(int i = 2; i <= g.nodes; i++) {
            g.edges[1][i] = true;
        }
        assertTrue(g.isConneced());
    }

    @Test
    public void testIsAcyclic() {
        Graph g = new Graph(50);
        for(int i = 2; i <= g.nodes; i++) {
            g.edges[1][i] = true;
        }
        assertTrue(g.isAcyclic());
        
        // Add one more edge
        g.edges[g.nodes][1] = true;
        assertFalse(g.isAcyclic());
    }
    
    private static final Random RAND = new Random();
    
    @Test
    public void testGenerateRandomDAG() {
        for(int i = 0; i < 100; i++){
            int nodeCount = RAND.nextInt(70) + 30; 
            Graph g = Graph.randomDAG(nodeCount);
            assertTrue(g.isConneced());
            assertTrue(g.isAcyclic());
            assertEquals(nodeCount - 1, g.numEdges());            
        }
    }

}
