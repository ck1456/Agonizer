package hps.nyu.fa14;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
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
    public void testCheckLoopFinder() {
        int[][] graph = new int[4][4];
        graph[1][2] = -1;
        graph[2][3] = -1;
        graph[3][1] = -1;
        Agonizer.CycleFinder cycleFinder = new Agonizer.CycleFinder();
        List<Integer> cycleNodes = cycleFinder.getNodesOfCycleWithNegativeEdges(graph);
        System.out.println(cycleNodes);
    }

    @Ignore
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
    
    @Ignore
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
    
    @Test
    public void testCalculateAgony_Problem1() throws Exception {
        Problem p = Problem.parseFile("data/problem_1.in");
        GraphPartition partition = GraphPartition.parseFile("data/problem_1.out");
        
        int agony = Agonizer.calculateAgony(p.graphs, partition);
        System.out.println(agony);
        assertEquals(50, agony);
    }
}
