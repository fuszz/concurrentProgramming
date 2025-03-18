import java.util.Random;

public class PodejsciePierwsze extends Thread{
    volatile static int czyja_kolej = 0;

    public PodejsciePierwsze(String number) {
        super(number);
    }

    @Override
    public void run() {
        Random r = new Random();
        while (true) {
            int number = Integer.parseInt(this.getName());

            // Własne sprawy procesu
            System.out.println("Własne sprawy procesu ... " + number);
            try{
                sleep(r.nextInt(1000));
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }

            // Protokół wstępny
            while (czyja_kolej != number) {
                // System.out.println(number + " --> " + czyja_kolej);
                Thread.yield();
            }

            // Sekcja krytyczna
            System.out.println("Wątek "+number + " wchodzi do sekcji krytycznej");
            try{
                sleep(r.nextInt(3000));
            } catch (InterruptedException ex) {}
            System.out.println("Wątek "+number + " kończy sekcję krytyczną (czas na wątek " + czyja_kolej + ")");

            // Protokół końcowy
            czyja_kolej = 1 - number;
        }
    }

    public static void main(String[] args) {
        PodejsciePierwsze p1 = new PodejsciePierwsze("0");
        PodejsciePierwsze p2 = new PodejsciePierwsze("1");

        p1.start();
        p2.start();
    }
}
