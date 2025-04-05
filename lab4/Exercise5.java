package lab4;

import java.util.concurrent.Semaphore;

class Buffer3 extends Thread{
    private final int size;
    private int[] table;

    private Semaphore canRead = new Semaphore(1);
    private Semaphore canWrite = new Semaphore(1);

    private Semaphore emptyPlaces;
    private Semaphore fullPlaces = new Semaphore(0);

    private int writeIndex;
    private int readIndex;

    Buffer3(int size){
        this.size = size;
        this.table = new int[size];
        this.emptyPlaces = new Semaphore(size);

        this.writeIndex = 0;
        this.readIndex = 0;
    }

    public void setValue(int value){
        canWrite.acquireUninterruptibly();
        emptyPlaces.acquireUninterruptibly();

        table[this.writeIndex % this.size] = value;
        this.writeIndex = (this.writeIndex + 1) % this.size;

        fullPlaces.release();
        canWrite.release();
    }

    public int getValue() {
        canRead.acquireUninterruptibly();
        fullPlaces.acquireUninterruptibly();

        int v = table[this.readIndex];
        this.readIndex = (this.readIndex + 1) % this.size;

        emptyPlaces.release();
        canRead.release();
        return v;
    }

}

class Consumer1 extends Thread {
    Buffer3 b;

    Consumer1(Buffer3 b){
        this.b = b;
    }

    void consume(int value){
        System.out.println("Otrzymałem wartość " + value);
    }

    @Override
    public void run() {
        while(true){
            try{sleep(500);} catch (InterruptedException e){};
            int value = b.getValue();
            consume(value);
        }
    }
}


class Producer1 extends Thread {
    Buffer3 buffer3;

    Producer1(Buffer3 buffer3){
        this.buffer3 = buffer3;
    }

    int produce(){
        return (int) Math.floor(Math.random()*100);
    }

    @Override
    public void run(){
        while(true){
            try{sleep(500);} catch (InterruptedException e) {};
            int value = produce();
            System.out.println("Wyprodukowałem " + value);
            buffer3.setValue(value);
        }
    }

}



public class Exercise5 {
    public static void main(String[] args) {
        Buffer3 b = new Buffer3(2);
        Consumer1 c = new Consumer1(b);
        Producer1 p1 = new Producer1(b);
        Producer1 p2 = new Producer1(b);
        Producer1 p3 = new Producer1(b);

        c.start();
        p1.start();
        p2.start();
        p3.start();
    }
}
