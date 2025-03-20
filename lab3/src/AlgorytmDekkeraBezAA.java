package lab3.src;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AlgorytmDekkeraBezAA extends Thread {
    static volatile int [] wants = {0, 0};
    static volatile int whosWaiting = 0;

    public AlgorytmDekkeraBezAA(String number) {
        super(number);
    }

    public static void main(String[] args) {

        AlgorytmDekkeraBezAA p1 = new AlgorytmDekkeraBezAA("0");
        AlgorytmDekkeraBezAA p2 = new AlgorytmDekkeraBezAA("1");

        p1.start();
        p2.start();
    }

    synchronized void setWants(int i, int v){
        wants[i] = v;
    }

    synchronized int getWants(int i){
        return wants[i];
    }

    @Override
    public void run() {
        int pid = Integer.parseInt(this.getName());
        while (true) {

            // Własne sprawy procesu
            System.out.println("Własne sprawy procesu ... " + pid);
            try {
                sleep((long) (100 * Math.random()));
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }

            // Protokół wstępny
            setWants(pid, 1);
            while (getWants(1-pid) == 1) {
                if(whosWaiting == pid){
                    setWants(pid, 0);
                    while(whosWaiting == pid) {}
                    setWants(pid, 1);
                }
                // System.out.println(number + " --> " + czyja_kolej);
            }


            // Sekcja krytyczna
            System.out.println("Wątek " + pid + " wchodzi do sekcji krytycznej");
            try {
                sleep((long) (1000 * Math.random()));
            } catch (InterruptedException ex) { }
            System.out.println("Wątek " + pid + " kończy sekcję krytyczną");

            // Protokół końcowy
            whosWaiting = pid;
            setWants(pid, 0);
        }
    }
}
