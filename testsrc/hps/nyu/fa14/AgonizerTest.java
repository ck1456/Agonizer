package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class AgonizerTest {

    @Test
    public void testCalculatePairwiseAgony_4Nodes() {
        Graph g1 = new Graph(4);
        g1.edges[1][3] = true;
        g1.edges[2][3] = true;
        g1.edges[4][3] = true;
        
        Graph g2 = new Graph(4);
        g2.edges[1][3] = true;
        g2.edges[3][4] = true;
        g2.edges[4][2] = true;
        
        GraphPartition gp = new GraphPartition();
        gp.partitionMap.put(1, 1);
        gp.partitionMap.put(2, 1);
        
        int agony = Agonizer.calculateAgony(Arrays.asList(g1, g2), gp);
        assertEquals(2, agony);        
    }

    @Test
    public void testCalculatePairwiseAgony() {
        Graph g1 = new Graph(3);
        g1.edges[1][2] = true;
        g1.edges[3][2] = true;
        
        Graph g2 = new Graph(3);
        g2.edges[1][2] = true;
        g2.edges[2][3] = true;
        g2.edges[1][3] = true;
        
        GraphPartition gp = new GraphPartition();
        gp.partitionMap.put(1, 1);
        gp.partitionMap.put(2, 1);
        
        int agony = Agonizer.calculateAgony(Arrays.asList(g1, g2), gp);
        assertEquals(2, agony);
    }
    
    @Test
    public void testCalculateLargeCycleAgony() {
        Graph g1 = new Graph(50);
        for(int i = 1; i < g1.nodes; i++){
            g1.edges[i][i+1] = true;
        }
        
        Graph g2 = new Graph(50);
        g2.edges[g2.nodes][1] = true;
        
        GraphPartition gp = new GraphPartition();
        gp.partitionMap.put(1, 1);
        gp.partitionMap.put(2, 1);
        
        int agony = Agonizer.calculateAgony(Arrays.asList(g1, g2), gp);
        assertEquals(50, agony);
    }
}
