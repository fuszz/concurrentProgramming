package lab4;

import java.util.concurrent.Semaphore;

public class Exercise2 extends Thread {
    static Semaphore sCritical = new Semaphore(1);
    static Semaphore sCounter;

    Exercise2 (String i){
        super(i);
    }

    @Override
    public void run() {
        int pid = Integer.parseInt(this.getName());
        System.out.println("Wątek " + pid + " rozpoczął swoją pracę");
        sCounter.release(1);
        sCounter.acquireUninterruptibly(0);

        while(true){

            // Sekcja lokalna
            System.out.println("Wątek " + pid + " sekcja lokalna");
            try{
                sleep((long) (Math.random() * 5000));
            } catch (InterruptedException _){}

            // Protkół wstępny
            sCritical.acquireUninterruptibly();

            // Sekcja krytyczna
            System.out.println("Wątek " + pid + " początek sekcji krytycznej");
            try{
                sleep((long) (Math.random() * 3000));
            } catch (InterruptedException _){}
            System.out.println("Wątek " + pid + " koniec sekcji krytycznej");

            //Protokół końcowy
            sCritical.release();

        }
    }

    public static void main(String[] args) {
        int threadsNumber = 5;
        sCounter = new Semaphore(-threadsNumber);

        Exercise2[] exercises = new Exercise2[threadsNumber];
        for(int i = 0; i < threadsNumber; i++){
            exercises[i] = new Exercise2(Integer.toString(i));
        }
        for(int i = 0; i < threadsNumber; i++){
            exercises[i].start();
        }


    }

}
