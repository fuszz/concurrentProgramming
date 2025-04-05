import java.util.concurrent.Semaphore;

class Buffer1 extends Thread{
    static Semaphore existValue = new Semaphore(0);
    static Semaphore isFree = new Semaphore(1);
    float value;

    public void setValue(float value){
        isFree.acquireUninterruptibly();
        this.value = value;
        existValue.release();
    }

    public float getValue() {
        existValue.acquireUninterruptibly();
        float v = this.value;
        isFree.release();
        return v;
    }

}

class Consumer extends Thread {
    Buffer1 b;


    Consumer(Buffer1 b){
        this.b = b;
    }

    void consume(float value){
        System.out.println("Otrzymałem wartość " + value);
        System.out.println(Math.sqrt(value));
        System.out.println(Math.log(value));
        System.out.println(Math.pow(value, 2));
        System.out.println();
    }

    @Override
    public void run() {
        while(true){
            try{sleep(500);} catch (InterruptedException e){};
            float value = b.getValue();
            consume(value);
        }
    }
}


class Producer extends Thread {
    Buffer1 buffer1;

    Producer(Buffer1 buffer1){
        this.buffer1 = buffer1;
    }

    float produce(){
        float avg_value = 0;
        for(int i = 0; i < 20; i++){
            avg_value += Math.random();
        }
        avg_value /= 20;
        return avg_value;
    }

    @Override
    public void run(){
        while(true){
            try{sleep(500);} catch (InterruptedException e) {};
            float value = produce();
            System.out.println("Wyprodukowałem " + value);
            buffer1.setValue(value);
        }
    }

}



public class Exercise3 {
    public static void main(String[] args) {
        Buffer1 b = new Buffer1();
        Consumer c = new Consumer(b);
        Producer p = new Producer(b);

        p.start();
        c.start();
    }
}
