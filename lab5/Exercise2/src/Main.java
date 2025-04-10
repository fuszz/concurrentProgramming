// Way 1 - Writers locked

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


class Moderator {
    private int readersCount = 0;
    private boolean someoneWriting = false;


    public synchronized void readingStart() {
        while(someoneWriting){
            try{
                wait();
            } catch(InterruptedException e){}
        }
        readersCount++;
    }

    public synchronized void writingStart() {
        while(someoneWriting || readersCount != 0){
            try{
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

class CustomReader extends Thread{
    private Moderator mod;
    String filePath;

    public CustomReader(int num, Moderator mod, String filePath){
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
                System.out.println(br.readLine());
                br.close();
            } catch (Exception e) {}
            System.out.println("Reader " + this.getName() + " stop");
            mod.readingStop();
        }
    }

}

class CustomWriter extends Thread{
    private Moderator mod;
    String filePath;
    static int  number = 0;

    public CustomWriter(int num, Moderator mod, String filePath){
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

public class Main {
    public static void main(String[] args) {
        String filePath = "file.txt";
        CustomReader [] rs = new CustomReader[5];
        CustomWriter [] ws = new CustomWriter[5];
        Moderator mod = new Moderator();
        for(int i = 0; i< 5; i++){

            ws[i] = new CustomWriter(2*i, mod, filePath);
            ws[i].start();

            rs[i] = new CustomReader(i, mod,  filePath);
            rs[i].start();
        }
    }
}