package lab4;


import java.util.concurrent.Semaphore;

public class Exercise6_1 extends Thread {
    static final int N = 5; // Liczba widelców
    static Semaphore diner = new Semaphore(N - 1);

    static Semaphore[] forks = new Semaphore[N];
    static {
        for (int i = 0; i < N; i++) {
            forks[i] = new Semaphore(1);
        }
    }

    Exercise6_1(int number){
        super(Integer.toString(number));
    }

    void thinking(int number){
        System.out.println("Thinking of forks, " + number);
        try { sleep((long) (Math.random() * 3000)); } catch (InterruptedException e) {}
    }

    void eating(int number){

        System.out.println("Start eating, philosopher " + number);
        try { sleep((long) (Math.random() * 2000)); } catch (InterruptedException e) {}
        System.out.println("End eating, philosopher " + number);

    }

    @Override
    public void run() {
        while(true){
        int number = Integer.parseInt(this.getName());
        thinking(number);

        diner.acquireUninterruptibly();

        forks[number].acquireUninterruptibly();
        forks[(number+1)%N].acquireUninterruptibly();
        eating(number);
        forks[number].release();
        forks[(number+1)%N].release();

        diner.release();
    }}

    public static void main(String[] args) {
        Exercise6_1[] philosophers = new Exercise6_1[N];

        for(int i=0; i<N; i++){
            philosophers[i] = new Exercise6_1(i);
            philosophers[i].start();
        }
    }

}
