import java.util.concurrent.Semaphore;

public class Exercise1 extends Thread {
    static Semaphore sCritical = new Semaphore(1);

    Exercise1 (String i){
        super(i);
    }

    @Override
    public void run() {
        int pid = Integer.parseInt(this.getName());
        System.out.println("Wątek " + pid + " rozpoczął swoją pracę");

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
        Exercise1 thread1 = new Exercise1("1");
        Exercise1 thread2 = new Exercise1("2");

        thread1.start();
        thread2.start();
    }

}