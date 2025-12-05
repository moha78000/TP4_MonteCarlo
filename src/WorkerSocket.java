import java.io.*;
import java.net.*;
import java.util.Random;
/**
 * Worker is a server. It computes PI by Monte Carlo method and sends 
 * the result to Master.
 */
public class WorkerSocket {
    static int port = 25545; //default port
    private static boolean isRunning = true;
    
    /**
     * compute PI locally by MC and sends the number of points 
     * inside the disk to Master. 
     */
    public static void main(String[] args) throws Exception {

	if (!("".equals(args[0]))) port=Integer.parseInt(args[0]);
	    System.out.println(port);
        ServerSocket s = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        Socket soc = s.accept();
	
        // BufferedReader bRead for reading message from Master
        BufferedReader bRead = new BufferedReader(new InputStreamReader(soc.getInputStream()));

        // PrintWriter pWrite for writing message to Master
        PrintWriter pWrite = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);
    	String str;
        Random rand = new Random();
        while (isRunning) {
            str = bRead.readLine();

            if (str == null) continue;  

            if (!str.equals("END")) {

                int N = Integer.parseInt(str);
                System.out.println("Worker reçoit N = " + N);

                // ---- Monte Carlo ----
                int nb_cible = 0;
                for (int i = 0; i < N; i++) {
                    double x = rand.nextDouble();
                    double y = rand.nextDouble();
                    if (x*x + y*y <= 1.0) nb_cible++;
                }
                // ----------------------

                System.out.println("Worker dans la cible = " + nb_cible);
                pWrite.println("" + nb_cible);

            } else {
                System.out.println("Worker reçoit END → arrêt.");
                isRunning = false;
            }
        }
        bRead.close();
        pWrite.close();
        soc.close();
   }
}