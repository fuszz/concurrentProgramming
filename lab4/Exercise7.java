import org.w3c.dom.ls.LSOutput;

import java.util.concurrent.Semaphore;

public class Exercise7 extends Thread {
    private static final int K_SMOKERS = 5;
    private static final int L_TAMPERS = 2;
    private static final int M_MATCHERS = 3;

    private static final Semaphore tampers = new Semaphore(L_TAMPERS, true);
    private static final Semaphore matches = new Semaphore(M_MATCHERS, true);

    public Exercise7(String id) {
        super(id);
    }

    @Override
    public void run() {
        int pid = Integer.parseInt(this.getName());

        while (true) {
            System.out.println("Smoker " + pid + " requests tamper");
            tampers.acquireUninterruptibly();

            System.out.println("Smoker " + pid + " tampers... ");
            simulateWork();

            System.out.println("Smoker " + pid + " returns tamper");
            tampers.release();

            System.out.println("Smoker " + pid + " requests matches");
            matches.acquireUninterruptibly();

            System.out.println("Smoker " + pid + " fire his pipe");
            simulateWork();

            System.out.println("Smoker " + pid + " return matches");
            matches.release();

            System.out.println("Smoker " + pid + " smokes...");
            simulateWork();
        }
    }

    private void simulateWork() {
        try {
            Thread.sleep(500 + (int)(Math.random() * 500)); // 0.5–1 sekundy
        } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) {
        for (int i = 0; i < K_SMOKERS; i++) {
            new Exercise7(Integer.toString(i)).start();
        }
    }
}
