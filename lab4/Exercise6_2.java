package lab4;


import java.util.concurrent.Semaphore;

public class Exercise6_2 extends Thread {
    static final int N = 5; // Liczba widelców

    static Semaphore[] forks = new Semaphore[N];
    static {
        for (int i = 0; i < N; i++) {
            forks[i] = new Semaphore(1);
        }
    }

    Exercise6_2(int number){
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

        if(number == 0){
            forks[(number+1)%N].acquireUninterruptibly();
            forks[number].acquireUninterruptibly();
        } else {
            forks[number].acquireUninterruptibly();
            forks[(number+1)%N].acquireUninterruptibly();
        }
        eating(number);
        forks[number].release();
        forks[(number+1)%N].release();

    }}

    public static void main(String[] args) {
        Exercise6_2[] philosophers = new Exercise6_2[N];

        for(int i=0; i<N; i++){
            philosophers[i] = new Exercise6_2(i);
            philosophers[i].start();
        }
    }

}
