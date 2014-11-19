package hps.nyu.fa14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the provided solution which needs to be evaluated
 */
public class GraphPartition {

    // Maps graphs (as indexed above) to partitions (also 1-indexed) 
    public final Map<Integer, Integer> partitionMap = new HashMap<Integer, Integer>();
    
    // Need to supply the graphs here
    public static GraphPartition parse(InputStream input) throws IOException{
        GraphPartition part = new GraphPartition();
        // copy the graphs to the partition
        
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = null;
        int graphID = 1;
        while((line = br.readLine()) != null){
            // parse into the map
            //String[] parts = line.trim().split("\\s");
            //int g = Integer.parseInt(parts[0]); // graph
            int p = Integer.parseInt(line); // partition
            part.partitionMap.put(graphID, p);
            graphID++;
        }        
        return part;
    }
    
    public static GraphPartition parseFile(String filePath) throws IOException {
        return parse(new FileInputStream(new File(filePath)));
    }
    
}
