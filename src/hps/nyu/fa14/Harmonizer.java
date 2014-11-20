package hps.nyu.fa14;

import java.io.File;
import java.util.Random;

/**
 * Several very basic implementations for generating solutions, so that we can calculate their agony
 */
public class Harmonizer {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        if(args.length != 2) {
            usage();
            System.exit(-1);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        Problem p = Problem.parseFile(inputFile);
        GraphPartition gp = solveTrivial(p);
        
        // Make directory for the output file if it does not exist
        File outFile = new File(outputFile);
        outFile.getAbsoluteFile().getParentFile().mkdirs();
        
        gp.writeFile(outputFile);
        
    }
    
    private static void usage() {
        // How to use it
        System.out.println("java -jar Harmonizer.jar <input_file> <output_file>");
    }
    
    private static final Random RAND = new Random();
    private static GraphPartition solveRandom(Problem p){
        GraphPartition gp = new GraphPartition();
        for(int i = 1; i <= p.m; i++){
            gp.partitionMap.put(i, RAND.nextInt(p.k) + 1);
        }
        return gp;
    }

    // puts everything in one partition
    private static GraphPartition solveTrivial(Problem p){
        GraphPartition gp = new GraphPartition();
        for(int i = 1; i <= p.m; i++){
            gp.partitionMap.put(i, 1);
        }
        return gp;
    }

}
