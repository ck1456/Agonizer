package hps.nyu.fa14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Agonizer {

    // Given a set of graphs and a graph partition
    public static int calculateAgony(List<Graph> graphs, GraphPartition partition) {
        HashMap<Integer, List<Graph>> partitions = new HashMap<Integer, List<Graph>>();
        for(int i : partition.partitionMap.keySet()) {
            partitions.put(i, new ArrayList<Graph>());
        }
        int totalAgony = 0;
        for(int i = 0; i < graphs.size(); i++) {
            Graph g = graphs.get(i);
            // partition number
            int partNumber = partition.partitionMap.get(i + 1);
            List<Graph> graphsInPartition = partitions.get(partNumber);
            graphsInPartition.add(g);
        }
        for(int partNumber : partitions.keySet()) {
            // System.out.println("Partition number "+partNumber);
            List<Graph> graphsInPartition = partitions.get(partNumber);

            // maxPairwiseAgonyForCluster has the agony of the cluster
            totalAgony += getClusterAgony(graphsInPartition);
        }
        return totalAgony;
    }

    private static int getClusterAgony(List<Graph> graphsInPartition) {
        int maxPairwiseAgonyForCluster = 0;
        for(int i = 0; i < graphsInPartition.size(); i++) {
            for(int j = i + 1; j < graphsInPartition.size(); j++) {
                // agony calculation of union of graph i and graph j
                Graph a = graphsInPartition.get(i);
                Graph b = graphsInPartition.get(j);
                // g is the union of a and b
                Graph g = a.union(b);
                
                int agonyOfPair = AgonyUtil.getAgony(g);
                if(agonyOfPair > maxPairwiseAgonyForCluster) {
                    maxPairwiseAgonyForCluster = agonyOfPair;
                }
            }
        }
        return maxPairwiseAgonyForCluster;
    }

    /**
     * Calculates the cumulative agony in a set of clustered graphs
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {

        // Parse the first argument as the city input file and the second as the
        // tour solution file
        if(args.length != 2) {
            usage();
            System.exit(-1);
        }

        String inputFile = args[0];
        String solutionFile = args[1];

        Problem p = Problem.parseFile(inputFile);
        GraphPartition partition = GraphPartition.parseFile(solutionFile);
        // Ensure that the partition solution is well defined:
        // 1) Must specify a mapping for every graph 1...M
        // 2) must contain partition values in 1...K
        if(!partition.assertContainsNumGraphs(p.m)
                || !partition.assertContainsNumPartitions(p.k)) {
            System.err.println("Invalid graph partition");
            System.exit(-1);
        }

        int agony = calculateAgony(p.graphs, partition);
        System.out.println(agony);
    }

    private static void usage() {
        // How to use it
        System.out.println("java -jar agonizer.jar <input_file> <output_file>");
    }
}
