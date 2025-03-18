import java.util.concurrent.atomic.AtomicIntegerArray;

public class PodejscieTrzecie extends Thread{
    static AtomicIntegerArray number = new AtomicIntegerArray(new int[]{1, 1});

    public PodejscieTrzecie(String number) {
        super(number);
    }

    @Override
    public void run() {
        int pid = Integer.parseInt(this.getName());
        while (true) {

            // Własne sprawy procesu
            System.out.println("Własne sprawy procesu ... " + pid);
            try{
                sleep((long)(100*Math.random()));
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }

            // Protokół wstępny
            number.set(pid, 0);
            while (number.get(1-pid) != 1) {
                // System.out.println(number + " --> " + czyja_kolej);
                Thread.yield();
            }


            // Sekcja krytyczna
            System.out.println("Wątek "+ pid + " wchodzi do sekcji krytycznej");
            try{
                sleep((long)(1000*Math.random()));
            } catch (InterruptedException ex) {}
            System.out.println("Wątek " + pid + " kończy sekcję krytyczną");

            // Protokół końcowy
            number.set(pid, 1);
        }
    }

    public static void main(String[] args) {

        PodejscieTrzecie p1 = new PodejscieTrzecie("0");
        PodejscieTrzecie p2 = new PodejscieTrzecie("1");

        p1.start();
        p2.start();
    }
}
