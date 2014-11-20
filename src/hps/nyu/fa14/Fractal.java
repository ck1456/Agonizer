package hps.nyu.fa14;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fractal {

    public static List<Integer> generateFractalDistribution(double frac, int N){
        List<Integer> p = orderedList(1, N);
        
        List<Integer> outvec = new ArrayList<Integer>(p);
        while(p.size() > 1){
            p = getSubList(p, 0, Math.min(p.size(),  (int)(frac * p.size())));
            outvec.addAll(p);
        }
        
        permute(outvec);
        return outvec;
    }
    
    private static List<Integer> getSubList(List<Integer> vec, int start, int end){
        List<Integer> subList = new ArrayList<Integer>(end-start);
        for(int i = start; i < end; i++){
            subList.add(vec.get(i));
        }
        return subList;
    }
    
    private static List<Integer> orderedList(int min, int max) {
        List<Integer> list = new ArrayList<Integer>();
        for(int i = min; i <= max; i++) {
            list.add(i);
        }
        return list;
    }

    private static final Random RAND = new Random();
    
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
}
