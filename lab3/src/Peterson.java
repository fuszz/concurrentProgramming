package lab3.src;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class Peterson extends Thread {
    static volatile AtomicIntegerArray wants = new AtomicIntegerArray(new int [] {0, 0});
    static volatile int whosWaiting = 0;

    Peterson(String i) {
        super(i);
    }

    public static void main (String [] args){
      Peterson thread0 = new Peterson("0");
      Peterson thread1 = new Peterson("1");

      thread0.start();
      thread1.start();
    };

    @Override
    public void run(){
        int pid = Integer.parseInt(this.getName());
        while(true){
            // Sekcja lokalna
            System.out.println("Sekcja lokalna wątku " + pid);
            try {
                sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {}


            // Protokół wstępny
            wants.set(pid, 1);
            whosWaiting = pid;
            while(wants.get(1 - pid) == 1 && whosWaiting == pid){}

            // Sekcja lokalna
            System.out.println("Wątek " + pid + " wchodzi do sekcji krytycznej");
            try {
                sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {}
            System.out.println("Wątek " + pid + " wychodzi do sekcji krytycznej");

            // Protokół końcowy
            wants.set(pid, 0);

        }
    }

}
