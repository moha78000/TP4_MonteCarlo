package PiJava;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Approximates PI using the Monte Carlo method.  Demonstrates
 * use of Callables, Futures, and thread pools.
 */
public class Pi
{
    public static void main(String[] args) throws Exception
    {
        long total=0;
        // 10 workers, 50000 iterations eac
        total = new Master().doRun(1000000000, 8);
        System.out.println("total from Master = " + total);

    }
}


/**
 * Task for running the Monte Carlo simulation.
 */
class Worker implements Callable<Long>
{
    private int numIterations;
    public Worker(int num)
    {
        this.numIterations = num;
    }

    @Override
    public Long call()
    {
        long circleCount = 0;
        Random prng = new Random ();
        for (int j = 0; j < numIterations; j++)
        {
            double x = prng.nextDouble();
            double y = prng.nextDouble();
            if ((x * x + y * y) < 1)  ++circleCount;
        }
        return circleCount; // nombre de points tombés dans la cible
    }



}
