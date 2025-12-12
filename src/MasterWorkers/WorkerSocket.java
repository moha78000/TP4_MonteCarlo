package MasterWorkers;
import java.io.*;
import java.net.*;
import java.util.Random;

import PiJava.Master;

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
                
                // Utilisation de Master.doRun pour le calcul
                Master master = new Master();
                long total = master.doRun(N, 1);

              
                
                System.out.println("Worker total calculé par Master = " + total);
                pWrite.println("" + total);  // <- envoie le résultat au Master socket

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