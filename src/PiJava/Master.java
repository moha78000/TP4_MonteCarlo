package PiJava;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * Creates workers to run the Monte Carlo simulation
 * and aggregates the results.
 */

public class Master {
    public long doRun(int totalCount, int numWorkers) throws InterruptedException, ExecutionException {

        long startTime = System.currentTimeMillis();

        // Create a collection of tasks
        List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
        for (int i = 0; i < numWorkers; ++i)
        {
            tasks.add(new Worker(totalCount));
        }

        // Run them and receive a collection of Futures
        ExecutorService exec = Executors.newFixedThreadPool(numWorkers); // creer un ensemble de thread à parti d'un nombre de workers fixés
        List<Future<Long>> results = exec.invokeAll(tasks);
        long total = 0;

        // Assemble the results.
        for (Future<Long> f : results)
        {
            // Call to get() is an implicit barrier.  This will block
            // until result from corresponding worker is ready.
            total += f.get();
        }
        double pi = 4.0 * total / totalCount / numWorkers;

        long stopTime = System.currentTimeMillis();
        long duration_ms = stopTime - startTime;

        System.out.println("\nPi : " + pi );
        System.out.println("Error: " + (Math.abs((pi - Math.PI)) / Math.PI) +"\n");

        System.out.println("Ntot: " + totalCount*numWorkers);
        System.out.println("Available processors: " + numWorkers);
        System.out.println("Time Duration (ms): " + duration_ms + "\n");

        System.out.println( (Math.abs((pi - Math.PI)) / Math.PI) +" "+ totalCount*numWorkers +" "+ numWorkers +" "+ (stopTime - startTime));




        String fileName = "erreurs.csv";
        try (java.io.FileWriter writer = new java.io.FileWriter(fileName, true)) { // true = append
            // Vérifier si le fichier est vide pour écrire l’en-tête
            java.io.File file = new java.io.File(fileName);
            if (file.length() == 0) {
                writer.write("temps_ms,pi_valeur,erreur_avant,error_avant_relative,log10Error,ntotal,n_workers\n");
            }

            double erreur_avant = pi - Math.PI;
            double errorPercent = erreur_avant / Math.PI * 100;
            double erreur_avant_relative = erreur_avant / pi;
            double absError = Math.abs(erreur_avant);
            double log10Error = Math.log10(absError);
            long ntotal = (long) totalCount *(long) numWorkers;

            // Écriture de la ligne de résultats
            writer.write(duration_ms + "," + pi + "," + erreur_avant + "," + erreur_avant_relative + "," + log10Error + "," +
                    ntotal + "," + numWorkers + "\n");

            System.out.println("Résultats ajoutés dans : " + fileName);

        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
        exec.shutdown();
        return total;
    }
}