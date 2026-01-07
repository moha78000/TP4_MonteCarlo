package MasterWorkers;
import java.io.*;
import java.net.*;
/** Master is a client. It makes requests to numWorkers.
 *
 */
public class MasterSocket {
    static int maxServer = 8;
    static final int[] tab_port = {25545, 25546, 25547, 25548, 25549, 25550, 25551, 25552};
    static String[] tab_total_workers = new String[maxServer];
    static final String ip = "127.0.0.1";
    static BufferedReader[] reader = new BufferedReader[maxServer];
    static PrintWriter[] writer = new PrintWriter[maxServer];
    static Socket[] sockets = new Socket[maxServer];


    public static void main(String[] args) throws Exception {

        // MC parameters
        int totalCount = 160000000; // total number of throws on a Worker
        int total = 0; // total number of throws inside quarter of disk
        double pi;

        int numWorkers = maxServer;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String s; // for bufferRead

        System.out.println("#########################################");
        System.out.println("# Computation of PI by MC method        #");
        System.out.println("#########################################");

        System.out.println("\n How many workers for computing PI (< maxServer): ");

        // strong-scaling
        int N_TOTAL = 160000000;
        int baseCount = 0;
        int reste = 0;



        // weakscaling
        //int CHARGE_PAR_WORKER = 160000000; // Chaque worker fera toujours 160M de points
        //long nTotalActuel = 0;

        try {
            s = bufferRead.readLine();
            numWorkers = Integer.parseInt(s);

            // strong-scaling
            baseCount = N_TOTAL / numWorkers;
            reste = N_TOTAL % numWorkers;

            // weak-scaling
            // nTotalActuel = (long) CHARGE_PAR_WORKER * numWorkers;


        } catch (IOException ioE) {
            ioE.printStackTrace();
        }

        for (int i = 0; i < numWorkers; i++) {
            System.out.println("Enter worker" + i + " port : ");
            try {
                s = bufferRead.readLine();
                System.out.println("You select " + s);
            } catch (IOException ioE) {
                ioE.printStackTrace();
            }
        }

        //create worker's socket
        for (int i = 0; i < numWorkers; i++) {
            sockets[i] = new Socket(ip, tab_port[i]);
            System.out.println("SOCKET = " + sockets[i]);

            reader[i] = new BufferedReader(new InputStreamReader(sockets[i].getInputStream()));
            writer[i] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockets[i].getOutputStream())), true);
        }

        //  String message_to_send;
        // message_to_send = String.valueOf(totalCount);

        String message_repeat = "y";

        long stopTime, startTime;

        while (message_repeat.equals("y")) {
            total = 0;
            startTime = System.currentTimeMillis();

            // strong scaling
            for (int i = 0; i < numWorkers; i++) {
            // Si c'est le premier worker, on lui donne la base + le reste
                int countToSend = (i == 0) ? (baseCount + reste) : baseCount;
                writer[i].println(String.valueOf(countToSend));
            }

            // weak scaling

            //for (int i = 0; i < numWorkers; i++) {
              //  writer[i].println(String.valueOf(CHARGE_PAR_WORKER));
            //}

            //listen to workers's message
            for (int i = 0; i < numWorkers; i++) {
                tab_total_workers[i] = reader[i].readLine();      // read message from server
                System.out.println("Client sent: " + tab_total_workers[i]);
            }

            // compute PI with the result of each workers
            for (int i = 0; i < numWorkers; i++) {
                total += Integer.parseInt(tab_total_workers[i]);
            }
            // strong scaling
            pi = 4.0 * (double) total / (double) N_TOTAL;

            // weak scaling
            //pi = 4.0 * (double) total / (double) nTotalActuel;

            stopTime = System.currentTimeMillis();
            long duration_ms = stopTime - startTime;

            System.out.println("\nPi : " + pi);
            System.out.println("Error: " + (Math.abs((pi - Math.PI)) / Math.PI) + "\n");

            System.out.println("Ntot: " + totalCount * numWorkers);
            System.out.println("Available processors: " + numWorkers);
            System.out.println("Time Duration (ms): " + (stopTime - startTime) + "\n");

            System.out.println((Math.abs((pi - Math.PI)) / Math.PI) + " " + totalCount * numWorkers + " " + numWorkers + " " + (stopTime - startTime));

            String fileName = "erreurs_mw_strong.csv";
            //String fileName = "erreurs_mw_weak.csv";
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

                // strong scaling
                long ntotal = (long) N_TOTAL;

                // weak scaling
                //long ntotal = (long) nTotalActuel;

                // Écriture de la ligne de résultats
                writer.write(duration_ms + "," + pi + "," + erreur_avant + "," + erreur_avant_relative + "," + log10Error + "," +
                        ntotal + "," + numWorkers + "\n");

                System.out.println("Résultats ajoutés dans : " + fileName);
                System.out.println("\n Repeat computation (y/N): ");
                try {
                    message_repeat = bufferRead.readLine();
                    System.out.println(message_repeat);
                } catch (IOException ioE) {
                    ioE.printStackTrace();
                }
            }


        }
        for (int i = 0; i < numWorkers; i++) {
            System.out.println("END");     // Send ending message
            writer[i].println("END");
            reader[i].close();
            writer[i].close();
            sockets[i].close();
        }
    }
}