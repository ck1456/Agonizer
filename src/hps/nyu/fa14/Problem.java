package hps.nyu.fa14;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an instance of an agony minimization problem
 * parameterized by:
 *  n = Number of nodes in each graph
 *  m = Number of graphs
 *  k = Number of clusters
 */
public class Problem {

    public final int n;
    public final int m;
    public final int k;

    public List<Graph> graphs = new ArrayList<Graph>();
    public List<Graph> clusterCenters = new ArrayList<Graph>();
    
    private Problem(int n, int m, int k){
        this.n = n;
        this.m = m;
        this.k = k;
        
        generateGraphs();
    }
    
    private static final Random RAND = new Random();
    
    private void generateGraphs(){
        // Generate k cluster centers
        for(int i = 0; i < k; i++){
            clusterCenters.add(Graph.random(n));
            // TODO: Ensure that the agony between these is sufficiently large
        }
        
        // Choose how many samples will be in each partition
        int[] partitionCounts = new int[k];
        // The sum of all of the partition counts must equal m
        // Try three different distributions:
        //  - Uniform
        //  - Fractal distribution
        //  - Poisson distribution
        
        // Uniform
        for(int i = 0; i < m; i++){
            partitionCounts[RAND.nextInt(k)]++;
        }
        
        // Fractal
        
        
        //Poisson
        
        // For each cluster center, generate a sufficient number of modified samples within a maximum agony
        for(int i = 0; i < k; i++){
            Graph c = clusterCenters.get(i);
            
            for(int j = 0; j < partitionCounts[i]; j++){
                Graph mod = c.clone();
                // modify this graph to induce some maximum agony
                graphs.add(mod);
            }
        }
    }
    
    public void write(OutputStream output) throws IOException {
        
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
     
        // The first line is
        // N M K
        bw.write(String.format("%d %d %d", n, m, k));
        bw.newLine();
        
        // Randomize the graphs in the list
        permute(graphs);
        
        // Write out all of the graphs
        for(int i = 0; i < graphs.size(); i++){
            Graph g = graphs.get(i);
            g.write(bw);
        }
        
        bw.close();
    }
    
    public void writeFile(String filePath) throws IOException {
        write(new FileOutputStream(new File(filePath)));
    }
    
    // Implements Fisher-Yates:
    // http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
    private static <T> void permute(List<T> input) {
        for (int i = input.size() - 1; i > 1; i--) {
            int swapIndex = RAND.nextInt(i + 1);
            T swapValue = input.get(swapIndex);
            input.set(swapIndex, input.get(i));
            input.set(i, swapValue);
        }
    }
    
    /**
     * Generate the sample and test problems
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
        Problem p1 = new Problem(10, 15, 3);
        p1.writeFile("data/problem_1.in");
        
        Problem p2 = new Problem(50, 64, 4);
        p2.writeFile("data/problem_2.in");
        
        Problem p3 = new Problem(100, 50, 4);
        p3.writeFile("data/problem_3.in");
        
        Problem p4 = new Problem(100, 100, 6);
        p4.writeFile("data/problem_4.in");
        
        Problem p5 = new Problem(150, 200, 8);
        p5.writeFile("data/problem_5.in");
    }
}