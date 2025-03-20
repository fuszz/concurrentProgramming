package lab3.src;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class Lamport extends Thread {
    static int n = 10;
    static AtomicIntegerArray wpisywanie = new AtomicIntegerArray(n+1);
    static AtomicIntegerArray numer = new AtomicIntegerArray(n+1);

    Lamport(String i){
        super(i);
    }

    public static void main(String [] args){
        for(int i=0; i<n; i++){
            wpisywanie.set(i, 0);
            numer.set(i, 0);
        }

        Lamport [] watki = new Lamport[n];
        for(int i=0; i<n; i++){
            watki[i] = new Lamport(Integer.toString(i+1));
        }
        for(int i=0; i<n; i++){
            watki[i].start();
        }
    }

    @Override
    public void run(){
        int pid = Integer.parseInt(this.getName());
        while(true){
            System.out.println("Sekcja lokalna wątku " + pid);
            try{
                sleep((long)Math.random() * 1000);
            } catch (InterruptedException e) {}

            // Protokół wstępny
            blokuj(pid);
            System.out.println("Wątek " + pid + " wchodzi do sekcji krytycznej");
            try{
                sleep((long)Math.random() * 1000);
            } catch (InterruptedException e) {}
            System.out.println("Wątek " + pid + "wychodzi z sekcji krytycznej");

            //Protokół końcowy
            odblokuj(pid);

        }
    }

    void blokuj(int pid){
        wpisywanie.set(pid, 1);
        numer.set(pid, 1 + maxWpisywanie());
        wpisywanie.set(pid, 0);

        for(int j = 1; j <= n; j++) {
            while (wpisywanie.get(j) == 1) {
            }
            while (numer.get(j) != 0 &&
                    (numer.get(j) < numer.get(pid) ||
                            (numer.get(j) == numer.get(pid) && j < pid))) {
            }
        }
    }

    void odblokuj(int pid){
        numer.set(pid, 0);
    }

    int maxWpisywanie(){
        int maxWartosc = Integer.MIN_VALUE;
        for(int i=0; i<n; i++){
            if(wpisywanie.get(i) > maxWartosc) maxWartosc = wpisywanie.get(i);
        }
        return maxWartosc;
    }

}
