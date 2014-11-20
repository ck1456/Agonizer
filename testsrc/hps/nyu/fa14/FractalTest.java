package hps.nyu.fa14;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class FractalTest {

    @Test
    public void testGenerateDistribution() {
        List<Integer> dist = Fractal.generateFractalDistribution(.75, 5);
        assertTrue(dist.size() > 10);
    }

}
