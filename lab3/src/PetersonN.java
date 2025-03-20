package lab3.src;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class PetersonN extends Thread {
    static final int n = 5;
    static volatile AtomicIntegerArray wants = new AtomicIntegerArray(n+1);
    static volatile AtomicIntegerArray whosWaiting = new AtomicIntegerArray(n+1);

    PetersonN(String i) {
        super(i);
    }

    public static void main(String[] args) {
        for (int i = 1; i <= n; i++) {
            new PetersonN(String.valueOf(i+1)).start();
        }
    }

    private boolean checkHigherPriority(int pid, int barrier) {
        for (int j = 1; j <= n; j++) {
            if (j != pid && wants.get(j) >= barrier) {
                if (whosWaiting.get(barrier) == pid && whosWaiting.get(barrier) == pid) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        int pid = Integer.parseInt(this.getName()) - 1; // Przekształcenie z 1-based na 0-based indeksowanie

        while (true) {
            // Sekcja lokalna
            System.out.println("Sekcja lokalna wątku " + (pid + 1));
            try {
                sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {}

            // Protokół wstępny
            for (int barrier = 1; barrier < n; barrier++) {
                wants.set(pid, barrier);
                whosWaiting.set(barrier, pid);

                while (checkHigherPriority(pid, barrier)) {}
            }

            // Sekcja krytyczna
            System.out.println("Wątek " + (pid + 1) + " wchodzi do sekcji krytycznej");
            try {
                sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {}
            System.out.println("Wątek " + (pid + 1) + " wychodzi z sekcji krytycznej");

            // Protokół końcowy
            wants.set(pid, 0);
        }
    }
}
