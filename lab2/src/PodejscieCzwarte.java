import java.util.concurrent.atomic.AtomicIntegerArray;

public class PodejscieCzwarte extends Thread{
    static AtomicIntegerArray number = new AtomicIntegerArray(new int[]{1, 1});

    public PodejscieCzwarte(String number) {
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
                number.set(pid, 1);
                // System.out.println(number + " --> " + czyja_kolej);
                Thread.yield();
                number.set(pid, 0);
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

        PodejscieCzwarte p1 = new PodejscieCzwarte("0");
        PodejscieCzwarte p2 = new PodejscieCzwarte("1");

        p1.start();
        p2.start();
    }
}
