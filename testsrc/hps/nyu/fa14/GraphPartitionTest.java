package hps.nyu.fa14;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphPartitionTest {

    @Test
    public void testAssertContainsNumGraphs() throws Exception {
        GraphPartition gp = GraphPartition.parseFile("data/problem_1.out");
        assertTrue(gp.assertContainsNumGraphs(15));
    }
    
    @Test
    public void testAssertContainsNumPartitions() throws Exception {
        GraphPartition gp = GraphPartition.parseFile("data/problem_1.out");
        assertTrue(gp.assertContainsNumPartitions(3));
    }

}
