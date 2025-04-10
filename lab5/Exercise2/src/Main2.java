// Way 2 - Readers locked

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


class Moderator2 {
    private int readersCount = 0;
    private boolean someoneWriting = false;

    private int waitingWriters = 0;

    public synchronized void readingStart() {
        while(someoneWriting || waitingWriters > 0){
            try{
                wait();
            } catch(InterruptedException e){}
        }
        readersCount++;
    }

    public synchronized void writingStart() {
        while(someoneWriting || readersCount != 0){
            try{
                waitingWriters++;
                wait();
            } catch(InterruptedException e){}
        }
        someoneWriting = true;

    }

    public synchronized void writingStop() {
        someoneWriting = false;
        notifyAll();
    }

    public synchronized void readingStop() {
        readersCount--;
        notifyAll();
    }
}

class CustomReader2 extends Thread{
    private Moderator2 mod;
    String filePath;

    public CustomReader2(int num, Moderator2 mod, String filePath){
        super(Integer.toString(num));
        this.mod = mod;
        this.filePath = filePath;
    }

    @Override
    public void run(){
        while(true){
            mod.readingStart();
            System.out.println("Reader " + this.getName() + " start");

            try {
                BufferedReader br = new BufferedReader(new FileReader(this.filePath));
                System.out.println("Reader " + this.getName() + " read " + br.readLine());
                br.close();
            } catch (Exception e) {}
            System.out.println("Reader " + this.getName() + " stop");
            mod.readingStop();
        }
    }

}

class CustomWriter2 extends Thread{
    private Moderator2 mod;
    String filePath;
    static int  number = 0;

    public CustomWriter2(int num, Moderator2 mod, String filePath){
        super(Integer.toString(num));
        this.mod = mod;
        this.filePath = filePath;
    }

    @Override
    public void run(){
        while(true){
            mod.writingStart();
            System.out.println("Writer " + this.getName() + " start");

            try {
                BufferedWriter br = new BufferedWriter(new FileWriter(this.filePath));
                br.write(Integer.toString(number));
                br.close();
                System.out.println("Writer " + this.getName() + ": wrote " + this.number);
                this.number++;

            } catch (Exception e) {}
            System.out.println("Writer " + this.getName() + " stop");
            mod.writingStop();
        }
    }

}

public class Main2 {
    public static void main(String[] args) {
        String filePath = "file.txt";
        CustomReader2 [] rs = new CustomReader2[5];
        CustomWriter2 [] ws = new CustomWriter2[5];
        Moderator2 mod = new Moderator2();
        for(int i = 0; i< 5; i++){

            ws[i] = new CustomWriter2(2*i, mod, filePath);
            ws[i].start();

            rs[i] = new CustomReader2(i, mod,  filePath);
            rs[i].start();
        }
    }
}