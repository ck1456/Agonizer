package hps.nyu.fa14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Agonizer {

    
    // Given a set of graphs and a graph partition
    public static int calculateAgony(List<Graph> graphs, GraphPartition partition){
    	HashMap<Integer,List<Graph>> partitions = new HashMap<Integer,List<Graph>>();
    	for(int i:partition.partitionMap.keySet()) {
    		partitions.put(i, new ArrayList<Graph>());
    	}
        for(int i=1;i<graphs.size();i++) {
        	Graph g = graphs.get(i);
        	// partition number
        	int partNumber = partition.partitionMap.get(i);
        	List<Graph> graphsInPartition = partitions.get(partNumber);
        	graphsInPartition.add(g);
        	partitions.put(partNumber, graphsInPartition);
        }
        for(int partNumber: partitions.keySet()) {
        	System.out.println("Partition number "+partNumber);
        	List<Graph> graphsInPartition = partitions.get(partNumber);
        	for(int i=0;i<graphsInPartition.size();i++) {
        		for(int j=i+1;j<graphsInPartition.size();j++) {
        			//agony calculation of union of graph i and graph j
        			Graph a = graphsInPartition.get(i);
        			Graph b = graphsInPartition.get(j);
        			Graph g = new Graph(a.nodes);
        			int [][] g1 = new int[a.nodes + 1][a.nodes + 1];
        			for(int x=1;x<=a.nodes;x++) {
        				for(int y=1;y<=a.nodes;y++) {
        					g.edges[x][y] = a.edges[x][y] | b.edges[x][y];
        					if(g.edges[x][y]) {
        						g1[x][y]  = -1;
        					}
        				}
        			}
        			//g is the union of a and b
        			//we now calculate the optimal ranking for graph g
        			//optimal ranking is one which minimized agony of the graph
        			CycleFinder cycleFinder = new CycleFinder();
        			List<Integer> cycleNodes = null;
        			while((cycleNodes = cycleFinder.hasNegativeCycle(g1)).size() > 0) {
        				for(int m=0;m<cycleNodes.size()-1;m++) {
        					//there is an edge from mth node to m+1th node
        					//and one more edge from the last node to the 0th node
        					g1[cycleNodes.get(m)][cycleNodes.get(m+1)] *= -1;
        				}
        				g1[cycleNodes.get(cycleNodes.size()-1)][cycleNodes.get(0)] *= -1;
        			}
        			//all edges in g1 labeled -1 form a DAG
        			//rest of the edges form an eulerian subgraph
        			//label all vertices as 0
        			int[] labels = new int[a.nodes + 1];
        			List<Integer> faultyEdge = null;
        			while((faultyEdge = faultyEdgeExists(g1,labels)).size() > 0) {
        				labels[faultyEdge.get(1)] = labels[faultyEdge.get(0)] 
        						- g1[faultyEdge.get(0)][faultyEdge.get(1)];
        			}
        		}
        	}
        }
        return 0;
    }
    
    private static List<Integer> faultyEdgeExists(int[][] graph, int[] labels) {
    	List<Integer> nodes = new ArrayList<Integer>();
    	for(int i=1;i<graph.length;i++) {
    		for(int j=1;j<graph.length;j++) {
    			if(labels[j] < labels[i] - graph[i][j]) {
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
    public static void main(String[] args) {
        

    }
    
    private static class CycleFinder {
        
        private boolean[] marked;
        private boolean[] onStack;
        private boolean cycle;
        private int[][] graph;
        
        public CycleFinder() {}
        
        private List<Integer> dfs(int v){
            onStack[v] = true;
            marked[v] = true;
            List<Integer> cycleNodes = new ArrayList<Integer>();
            for(int w = 1; w <= graph.length; w++){
                if(graph[v][w] == 0){
                    continue;  // Only proceed if adjacent
                }
                if(!marked[w]){
                    dfs(w);
                    //add this to the cycle
                } else if(onStack[w] && graph[v][w]  == -1){
                    cycle = true;
                    //we know all the nodes in this cycle - they are the ones that have
                    //onstack set to true
                    for(int m=1;m<=graph.length;m++) {
                    	if(onStack[m]) {
                    		cycleNodes.add(m);
                    	}
                    }
                    return cycleNodes;
                }
            }
            onStack[v] = false;
            return cycleNodes;
        }
        
        public List<Integer> hasNegativeCycle(int[][] g){
        	graph = g;
        	cycle = false;
        	marked = new boolean[graph.length];
        	onStack = new boolean[graph.length];;
        	List<Integer> cycleNodes = new ArrayList<Integer>();
            for(int v = 1; v <= graph.length; v++){
                if(!marked[v]){
                    cycleNodes = dfs(v);
                }
            }
            return cycleNodes;
        }
    }

}
