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
 * Represents an instance of an agony minimization problem parameterized by: n =
 * Number of nodes in each graph m = Number of graphs k = Number of clusters
 */
public class Problem {

    public final int n;
    public final int m;
    public final int k;

    public List<Graph> graphs = new ArrayList<Graph>();
    public List<Graph> clusterCenters = new ArrayList<Graph>();

    private Problem(int n, int m, int k) {
        this.n = n;
        this.m = m;
        this.k = k;

        generateGraphs();
    }

    private static final Random RAND = new Random();

    private void generateGraphs() {
        // Generate k cluster centers
        for(int i = 0; i < k; i++) {
            Graph c = Graph.randomDAG(n);
            clusterCenters.add(c);
            // TODO: Ensure that the agony between these is sufficiently large
        }

        // Choose how many samples will be in each partition
        int[] partitionCounts = new int[k];
        // The sum of all of the partition counts must equal m
        // Try three different distributions:
        // - Uniform
        // - Fractal distribution
        // - Poisson distribution

        // Uniform
        for(int i = 0; i < m; i++) {
            partitionCounts[RAND.nextInt(k)]++;
        }

        // Fractal

        // Poisson

        // For each cluster center, generate a sufficient number of modified
        // samples within a maximum agony
        for(int i = 0; i < k; i++) {
            Graph c = clusterCenters.get(i);
            for(int j = 0; j < partitionCounts[i]; j++) {
                Graph mod = modGraph(c, 5);
                // modify this graph to induce some maximum agony
                graphs.add(mod);
            }
        }
    }

    /**
     * Given a DAG, swap some edges and return a new graph that has at most
     * maxAgony induced between it and the original graph The result is still a
     * DAG
     * 
     * @param g
     * @param maxAgony
     * @return
     */
    private static Graph modGraph(Graph g, int maxAgony) {
        // Swap random edges
        Graph mod = g.clone();
        // construct two permutations to iterate through the edge set in
        List<Integer> p1 = orderedList(1, mod.nodes);

        List<Integer> p2 = orderedList(1, mod.nodes);

        maxAgony = RAND.nextInt(maxAgony) + 1; // ensure it is not zero agony
        // Find some edges and remove them
        int edgesToAdd = maxAgony;
        // System.out.println(String.format("Removing %d edges", maxAgony));
        while (maxAgony > 0) {
            permute(p1);
            permute(p2);
            rLoop: for(int i : p1) {
                for(int j : p2) {
                    if(i != j && mod.edges[i][j]) {
                        mod.edges[i][j] = false;
                        maxAgony--;
                        break rLoop;
                    }
                }
            }
        }

        // Add some different edges back in until it is connected as long as
        // it does not cause a cycle

        while (edgesToAdd > 0) {
            if(!(mod.isAcyclic())) {
                System.out.println("Badness 2");
            }
            permute(p1);
            permute(p2);
            rLoop: for(int i : p1) {
                for(int j : p2) {
                    // Don't generate self loops or add edges that already exist
                    if(i != j && !mod.edges[i][j]) {
                        mod.edges[i][j] = true;
                        if(!mod.isAcyclic()) {
                            mod.edges[i][j] = false;
                        } else {
//                            System.out.println(String.format(
//                                    "Added edge: %d -> %d", i, j));
                            edgesToAdd--;
                            break rLoop;
                        }
                    }
                }
            }
        }
        return mod;
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
        for(int i = 0; i < graphs.size(); i++) {
            Graph g = graphs.get(i);
            g.write(bw);
        }

        bw.close();
    }

    public void writeFile(String filePath) throws IOException {
        write(new FileOutputStream(new File(filePath)));
    }

    // Write the cluster centers and the original partition map
    public void writeKey(OutputStream output) throws IOException {

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
        // Write out all of the cluster centers
        for(int i = 0; i < clusterCenters.size(); i++) {
            Graph g = clusterCenters.get(i);
            g.write(bw);
        }
        bw.close();
    }

    public void writeKeyFile(String filePath) throws IOException {
        writeKey(new FileOutputStream(new File(filePath)));
    }

    private static List<Integer> orderedList(int min, int max) {
        List<Integer> list = new ArrayList<Integer>();
        for(int i = min; i <= max; i++) {
            list.add(i);
        }
        return list;
    }

    // Implements Fisher-Yates:
    // http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
    private static <T> void permute(List<T> input) {
        for(int i = input.size() - 1; i > 1; i--) {
            int swapIndex = RAND.nextInt(i + 1);
            T swapValue = input.get(swapIndex);
            input.set(swapIndex, input.get(i));
            input.set(i, swapValue);
        }
    }

    /**
     * Generate the sample and test problems
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {

        System.out.println("Problem 1");
        Problem p1 = new Problem(10, 15, 3);
        p1.writeFile("data/problem_1.in");
        p1.writeKeyFile("data/problem_1.key");

        System.out.println("Problem 2");
        Problem p2 = new Problem(50, 64, 4);
        p2.writeFile("data/problem_2.in");
        p2.writeKeyFile("data/problem_2.key");

        System.out.println("Problem 3");
        Problem p3 = new Problem(100, 50, 4);
        p3.writeFile("data/problem_3.in");
        p3.writeKeyFile("data/problem_3.key");

        System.out.println("Problem 4");
        Problem p4 = new Problem(100, 100, 6);
        p4.writeFile("data/problem_4.in");
        p4.writeKeyFile("data/problem_4.key");

        System.out.println("Problem 5");
        Problem p5 = new Problem(150, 200, 8);
        p5.writeFile("data/problem_5.in");
        p5.writeKeyFile("data/problem_5.key");
    }
}