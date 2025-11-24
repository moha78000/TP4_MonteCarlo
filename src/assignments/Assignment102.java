// Estimate the value of Pi using Monte-Carlo Method, using parallel program
package assignments;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
class PiMonteCarlo {
	AtomicInteger nAtomSuccess; // ncible voir ligne 17
	int nThrows;
	double value;
    int nb_process;
	class MonteCarlo implements Runnable {
		@Override
		//  la méthode run() = 1 itération de Monte Carlo
        public void run() {
			double x = Math.random();
			double y = Math.random();
			if (x * x + y * y <= 1)
				nAtomSuccess.incrementAndGet(); // ecriture / lecture (get le résultat) donc nAtomSuccess est un moniteur
		}
	}

	public PiMonteCarlo(int i) { // le i représente n tot : taille de la boucle
		this.nAtomSuccess = new AtomicInteger(0);
		this.nThrows = i; // Nombre de lancer
		this.value = 0;
        this.nb_process = 8 ;
	}

    public int getnThrows(){
        return nThrows;
    }
	public double getPi() {
		
		ExecutorService executor = Executors.newWorkStealingPool(nb_process); // Ensemble de process pour du vol de tâche

        // Représente les itérations parallèles
        for (int i = 1; i <= nThrows; i++) {
			Runnable worker = new MonteCarlo();
			executor.execute(worker);
		}
		executor.shutdown(); // on les éteints
		while (!executor.isTerminated()) {
		}
		value = 4.0 * nAtomSuccess.get() / nThrows;
		return value;
	}
}
public class Assignment102 {
	public static void main(String[] args) {
        PiMonteCarlo PiVal = new PiMonteCarlo(8000000);

        long startTime = System.currentTimeMillis();
        double value = PiVal.getPi();
        long stopTime = System.currentTimeMillis();
        long duration = stopTime - startTime;

        double difference = value - Math.PI;
        double errorPercent = difference / Math.PI * 100;

        // Affichage console
        System.out.println("Approx value: " + value);
        System.out.println("Difference to exact pi: " + difference);
        System.out.println("Error: " + errorPercent + " %");
        System.out.println("processors: " + PiVal.nb_process);
        System.out.println("Time Duration: " + duration + "ms\n");

        // Écriture dans le fichier CSV en mode append
        String fileName = "pi_results_for_weak_scaling.csv";
        try (java.io.FileWriter writer = new java.io.FileWriter(fileName, true)) { // true = append
            // Vérifier si le fichier est vide pour écrire l’en-tête
            java.io.File file = new java.io.File(fileName);
            if (file.length() == 0) {
            writer.write("temps_ms,pi_valeur,difference,error_percent,ntotal,nprocess\n");
            }

            // Écriture de la ligne de résultats
            writer.write(duration + "," + value + "," + difference + "," + errorPercent + "," 
                     + PiVal.getnThrows() + "," + PiVal.nb_process + "\n");

            System.out.println("Résultats ajoutés dans : " + fileName);

        }
        catch (java.io.IOException e) {
        e.printStackTrace();
        }

        /*
      
        Expérience menée avec: ProcesseurIntel(R) Core(TM) i7-9700 CPU @ 3.00GHz, 3000 MHz, 8 cœur(s), 8 processeur(s) logique(s)

        */

    }
}