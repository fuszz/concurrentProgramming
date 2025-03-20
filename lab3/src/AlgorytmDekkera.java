package lab3.src;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AlgorytmDekkera extends Thread {
    static volatile AtomicIntegerArray wants = new AtomicIntegerArray(new int[]{0, 0});
    static volatile int whosWaiting = 0;

    public AlgorytmDekkera(String number) {
        super(number);
    }

    public static void main(String[] args) {

        AlgorytmDekkera p1 = new AlgorytmDekkera("0");
        AlgorytmDekkera p2 = new AlgorytmDekkera("1");

        p1.start();
        p2.start();
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
            wants.set(pid, 1);
            while (wants.get(1-pid) == 1) {
                if(whosWaiting == pid){
                    wants.set(pid, 0);
                    while(whosWaiting == pid) {}
                    wants.set(pid, 1);
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
            wants.set(pid, 0);
        }
    }
}
