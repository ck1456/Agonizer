package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

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
        assertEquals(3, agony);        
    }
    
    @Test
    public void testCheckLoopFinder() {
        Graph g = new Graph(3);
        g.edges[1][2] = true;
        g.edges[2][3] = true;
        g.edges[3][1] = true;
        
        int[][] w = new int[4][4];
        w[1][2] = -1;
        w[2][3] = -1;
        w[3][1] = -1;
        Agonizer.CycleFinder cycleFinder = new Agonizer.CycleFinder();
        List<Integer> cycleNodes = cycleFinder.getNodesOfCycleWithNegativeEdges(g, w);
        assertEquals(4, cycleNodes.size());
    }
    
    @Test
    public void testCheckNoLoopFinder() {
        Graph g = new Graph(3);
        g.edges[1][2] = true;
        g.edges[2][3] = true;
        g.edges[1][3] = true;
        
        int[][] w = new int[4][4];
        w[1][2] = -1;
        w[2][3] = -1;
        w[1][3] = -1;
        Agonizer.CycleFinder cycleFinder = new Agonizer.CycleFinder();
        List<Integer> cycleNodes = cycleFinder.getNodesOfCycleWithNegativeEdges(g, w);
        assertNull(cycleNodes);
    }
    
    @Test
    public void testCheckNoLoopFinder2() {
        Graph g = new Graph(3);
        g.edges[1][2] = true;
        g.edges[1][3] = true;
        g.edges[3][2] = true;
        
        int[][] w = new int[4][4];
        w[1][2] = -1;
        w[1][3] = -1;
        w[3][2] = -1;
        Agonizer.CycleFinder cycleFinder = new Agonizer.CycleFinder();
        List<Integer> cycleNodes = cycleFinder.getNodesOfCycleWithNegativeEdges(g, w);
        assertNull(cycleNodes);
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
        System.out.println(agony);
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
        
        assertFalse(g1.union(g2).isAcyclic());
        int agony = Agonizer.calculateAgony(Arrays.asList(g1, g2), gp);
        assertEquals(50, agony);
    }
    
    @Test
    public void testCalculateSmallCycleAgony() {
        Graph g1 = new Graph(5);
        for(int i = 1; i < g1.nodes; i++){
            g1.edges[i][i+1] = true;
        }
        assertTrue(g1.isAcyclic());
        
        Graph g2 = new Graph(5);
        g2.edges[g2.nodes][1] = true;
        
        GraphPartition gp = new GraphPartition();
        gp.partitionMap.put(1, 1);
        gp.partitionMap.put(2, 1);
        
        assertFalse(g1.union(g2).isAcyclic());
        int agony = Agonizer.calculateAgony(Arrays.asList(g1, g2), gp);
        assertEquals(5, agony);
    }
    
    @Test
    public void testCalculateAgony_Problem1() throws Exception {
        Problem p = Problem.parseFile("data/problem_1.in");
        GraphPartition partition = GraphPartition.parseFile("data/problem_1.out");
        
        int agony = Agonizer.calculateAgony(p.graphs, partition);
        System.out.println(agony);
        assertEquals(59, agony);
    }
}
