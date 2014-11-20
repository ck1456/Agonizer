package hps.nyu.fa14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the provided solution which needs to be evaluated
 */
public class GraphPartition {

    // Maps graphs (as indexed above) to partitions (also 1-indexed) 
    public final Map<Integer, Integer> partitionMap = new HashMap<Integer, Integer>();
    
    
    public boolean assertContainsNumGraphs(int m){
        for(int i = 1; i <= m; i++){
            if(!partitionMap.containsKey(i)){
                System.err.println(String.format("Map does not contain partition for graph %d", i));
                return false;
            }
        }
        if(partitionMap.size() != m){
            System.err.println("Map contains too many graphs");
            return false;
        }
        return true;
    }
    
    public boolean assertContainsNumPartitions(int k){
        // ensure that all partition values are <= k
        for(int i : partitionMap.values()){
            if(i > k){
                System.err.println(String.format("Unexpected partition in map: %d",  i));
                return false;
            }
        }
        return true;
    }
    
    // Need to supply the graphs here
    public static GraphPartition parse(InputStream input) throws IOException{
        GraphPartition part = new GraphPartition();
        // copy the graphs to the partition
        
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = null;
        int graphID = 1;
        while((line = br.readLine()) != null){
            // parse into the map
            int p = Integer.parseInt(line); // partition
            part.partitionMap.put(graphID, p);
            graphID++;
        }        
        return part;
    }
    
    public static GraphPartition parseFile(String filePath) throws IOException {
        return parse(new FileInputStream(new File(filePath)));
    }
    
    // Write the partition map
    public void write(OutputStream output) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
        // Write out all of the cluster centers
        for(int i = 1; i <= partitionMap.size(); i++) {
            bw.write(String.format("%d", partitionMap.get(i)));
            bw.newLine();
        }
        bw.close();
    }

    public void writeFile(String filePath) throws IOException {
        write(new FileOutputStream(new File(filePath)));
    }
}
