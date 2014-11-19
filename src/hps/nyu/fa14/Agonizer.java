package hps.nyu.fa14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Agonizer {

    
    // Given a set of graphs and a graph partition
    public static int calculateAgony(List<Graph> graphs, GraphPartition partition){
    	HashMap<Integer,List<Graph>> partitions = new HashMap<Integer,List<Graph>>();
    	for(int i:partition.partitionMap.keySet()) {
    		partitions.put(i, new ArrayList<Graph>());
    	}
    	int totalAgony = 0;
        for(int i=0;i<graphs.size();i++) {
        	Graph g = graphs.get(i);
        	// partition number
        	int partNumber = partition.partitionMap.get(i+1);
        	List<Graph> graphsInPartition = partitions.get(partNumber);
        	graphsInPartition.add(g);
        	//partitions.put(partNumber, graphsInPartition);
        }
        for(int partNumber: partitions.keySet()) {
        	System.out.println("Partition number "+partNumber);
        	List<Graph> graphsInPartition = partitions.get(partNumber);
        	
        	//maxPairwiseAgonyForCluster has the agony of the cluster
        	totalAgony += getClusterAgony(graphsInPartition);
        }
        return totalAgony;
    }
    
    private static int getClusterAgony(List<Graph> graphsInPartition) {
    	int maxPairwiseAgonyForCluster = 0;
    	for(int i=0;i<graphsInPartition.size();i++) {
    		for(int j=i+1;j<graphsInPartition.size();j++) {
    			//agony calculation of union of graph i and graph j
    			Graph a = graphsInPartition.get(i);
    			Graph b = graphsInPartition.get(j);
                //g is the union of a and b
    			Graph g = a.union(b);    			
    			int [][] w = new int[a.nodes + 1][a.nodes + 1];
    			for(int x=1;x<=a.nodes;x++) {
    				for(int y=1;y<=a.nodes;y++) {
    					if(g.edges[x][y]) {
    						w[x][y]  = -1;
    					}
    				}
    			}
    			//we now calculate the optimal ranking for graph g
    			//optimal ranking is one which minimized agony of the graph
    			List<Integer> cycleNodes = new CycleFinder().getNodesOfCycleWithNegativeEdges(g, w);
    			while(cycleNodes != null && cycleNodes.size() > 0) {
    				for(int m=0;m<cycleNodes.size()-1;m++) {
    					//there is an edge from mth node to m+1th node
    				    int u = cycleNodes.get(m);
    				    int v = cycleNodes.get(m+1);
    				    //and one more edge from the last node to the 0th node
    					w[u][v] = w[u][v] * -1;
    					// reverses the edge
    					g.edges[v][u] = true;
    					g.edges[u][v] = false;
    				}
    				System.out.println("Cycle updated");
    				cycleNodes = new CycleFinder().getNodesOfCycleWithNegativeEdges(g, w);
    			}
    			
    			reverseAllPositiveEdges(g, w);
    			
    			//all edges in g1 labeled -1 form a DAG
    			//rest of the edges form an eulerian subgraph
    			//label all vertices as 0
    			int[] labels = new int[a.nodes + 1];
    			List<Integer> faultyEdge = null;
    			while((faultyEdge = getFaultyEdgeIfExists(w,labels)).size() > 0) {
    				labels[faultyEdge.get(1)] = labels[faultyEdge.get(0)] 
    						- w[faultyEdge.get(0)][faultyEdge.get(1)];
    			}
    			
    			//calculate agony for this graph now.
    			//this is the agony of the pair i and j
    			int agonyOfPair = getAgony(w, labels);
    			if(agonyOfPair > maxPairwiseAgonyForCluster) {
    				maxPairwiseAgonyForCluster = agonyOfPair;
    			}
    		}
    	}
    	return maxPairwiseAgonyForCluster;
    }
    
    private static void reverseAllPositiveEdges(Graph g, int[][] weights) {
    	for(int i=1;i<=g.nodes;i++) {
    		for(int j=1;j<=g.nodes;j++) {
    			if(weights[i][j] == 1) {
    				g.edges[j][i] = true;
    				g.edges[j][i] = false;
    			}
    		}
    	}
    }
    
    public static int getAgony(int[][] graph, int[] labels) {
    	int agony = 0;
		for(int u=1;u<graph.length;u++) {
			for(int v=1;v<graph.length;v++) {
				if(graph[u][v] > 0) {
					agony += Math.max(labels[u] - labels[v] + 1, 0);
				}
			}
		}
		return agony;
    }
    
    /*
    public static int getAgony(Graph graph, int[] labels) {
    	int agony = 0;
		for(int p=1;p<graph.edges.length;p++) {
			for(int q=1;q<graph.edges.length;q++) {
				if(!graph.edges[p][q]) {
					agony += Math.max(labels[p] - labels[q] + 1, 0);
				}
			}
		}
		return agony;
    }*/
    
    private static List<Integer> getFaultyEdgeIfExists(int[][] graph, int[] labels) {
    	List<Integer> nodes = new ArrayList<Integer>();
    	for(int i=1;i<graph.length;i++) {
    		for(int j=1;j<graph.length;j++) {
    			if(!(graph[i][j] != 0) && (labels[j] < labels[i] - graph[i][j])) {
    				nodes.add(i);
    				nodes.add(j);
    				return nodes;
    			}
    		}
    	}
    	return nodes;
    }
    
    /**
     * Calculates the cumulative agony in a set of clustered graphs
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
        // Parse the first argument as the city input file and the second as the
        // tour solution file
        if (args.length != 2) {
            usage();
            System.exit(-1);
        }
        
        String inputFile = args[0];
        String solutionFile = args[1];

        Problem p = Problem.parseFile(inputFile);
        GraphPartition partition = GraphPartition.parseFile(solutionFile);
        
        int agony = calculateAgony(p.graphs, partition);
        System.out.println(agony);
    }
    
    private static void usage() {
        // How to use it
        System.out.println("java -jar agonizer.jar <input_file> <output_file>");
    }
    
    public static class CycleFinder {
        
        private boolean[] marked;
        private boolean[] onStack;
        private Graph graph;
        private int[][] weights;
        int[] edgeTo;
        List<Integer> cycleNodes = null;
        
        public CycleFinder() {}
        
        private void dfs(int v){
            onStack[v] = true;
            marked[v] = true;
            for(int w = 1; w <= graph.nodes; w++){
                if(!graph.edges[v][w]){
                    continue;  // Only proceed if adjacent
                }
                if(cycleNodes != null) {
                	return;
                }
                else if(!marked[w]){
                    // Check that it is a negative weight edge
                	if(graph.edges[v][w] && weights[v][w] == -1) {
                	  edgeTo[w] = v;
                	}
                    dfs(w);
                    //add this to the cycle
                } else if(onStack[w] && graph.edges[v][w] && weights[v][w] == -1){
                    //we know all the nodes in this cycle - they are the ones that have
                    //onstack set to true
                    cycleNodes = new ArrayList<Integer>();
                    for(int m=v; m!=w && m != 0; m=edgeTo[m]) {
                    	cycleNodes.add(m);
                    }
                    cycleNodes.add(w);
                    cycleNodes.add(v);
                    // Need to return the edges in the correct order
                    // (following the direction of edges)
                    Collections.reverse(cycleNodes);
                }
            }
            onStack[v] = false;
        }
        
        public List<Integer> getNodesOfCycleWithNegativeEdges(Graph g, int[][] w){
        	graph = g;
        	weights = w;
        	marked = new boolean[graph.nodes + 1];
        	edgeTo  = new int[graph.nodes + 1];
        	onStack = new boolean[graph.nodes + 1];;
            for(int v = 1; v <= graph.nodes; v++){
                if(!marked[v]){
                    dfs(v);
                    if(cycleNodes != null) {
                    	return cycleNodes;
                    }
                }
            }
            return cycleNodes;
        }
    }
}
