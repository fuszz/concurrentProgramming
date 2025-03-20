package lab3.src;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class DoranThomson extends Thread{
    static AtomicIntegerArray chce = new AtomicIntegerArray(new int[] {0, 0});
    static AtomicInteger ktoCzeka = new AtomicInteger(0);

    DoranThomson(String pid){
        super(pid);
    }

    public static void main(String[] args){
        DoranThomson w1 = new DoranThomson("0");
        DoranThomson w2 = new DoranThomson("1");

        w1.start();
        w2.start();
    }

    @Override
    public void run(){
        int pid = Integer.parseInt(this.getName());
        while(true){
            System.out.println("Sekcja lokalna wątku " + pid);
            try {sleep((long)Math.random()*1000);} catch (InterruptedException e) {}

            // Protokół wstępny
            chce.set(pid, 1);
            if(chce.get(1-pid) == 1){
                if(ktoCzeka.get() == pid){
                    chce.set(pid, 0);
                    while(ktoCzeka.get() == pid) {}
                    chce.set(pid, 1);
                }
                while(chce.get(1-pid) == 1) {}
            }
            System.out.println("Początek Sekcji krytycznej wątku " + pid);
            try {sleep((long)Math.random()*1000);} catch (InterruptedException e) {}
            System.out.println("Koniec Sekcji krytycznej wątku " + pid);


            //Protokół końcowy
            chce.set(pid, 0);
            ktoCzeka.set(pid);
        }
    }

}
