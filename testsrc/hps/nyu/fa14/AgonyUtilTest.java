package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AgonyUtilTest {

    @Test
    public void testGetAgony() {
        // Edges are 1-indexed
        Graph g1 = new Graph(4);
        g1.edges[1][3] = true;
        g1.edges[2][3] = true;
        g1.edges[4][3] = true;
        
        Graph g2 = new Graph(4);
        g2.edges[1][3] = true;
        g2.edges[3][4] = true;
        g2.edges[4][2] = true;
        
        int agony = AgonyUtil.getAgony(g1.union(g2));
        assertEquals(3, agony);      
    }

}
