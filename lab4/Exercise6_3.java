import java.util.concurrent.Semaphore;

public class Exercise6_3 extends Thread {
    static final int N = 5; // Liczba widelców

    static Semaphore[] forks = new Semaphore[N];

    static {
        for (int i = 0; i < N; i++) {
            forks[i] = new Semaphore(1);
        }
    }

    Exercise6_3(int number) {
        super(Integer.toString(number));
    }

    void thinking(int number) {
        System.out.println("Thinking of forks, " + number);
        try {
            sleep((long) (Math.random() * 3000));
        } catch (InterruptedException e) {
        }
    }

    void eating(int number) {

        System.out.println("Start eating, philosopher " + number);
        try {
            sleep((long) (Math.random() * 2000));
        } catch (InterruptedException e) {
        }
        System.out.println("End eating, philosopher " + number);

    }

    @Override
    public void run() {
        while (true) {
            int number = Integer.parseInt(this.getName());
            thinking(number);

            int decision = Math.random() < 0.5 ? 0 : 1;
            forks[(number + decision) % N].acquireUninterruptibly();
            boolean second_fork = forks[(number + (1 - decision)) % N].tryAcquire();
            if (!second_fork) {
                forks[(number + decision) % N].release();
                continue;
            }
            eating(number);
            forks[(number + decision) % N].release();
            forks[(number + (1 - decision)) % N].release();
        }
    }

    public static void main(String[] args) {
        Exercise6_3[] philosophers = new Exercise6_3[N];

        for (int i = 0; i < N; i++) {
            philosophers[i] = new Exercise6_3(i);
            philosophers[i].start();
        }
    }

}
