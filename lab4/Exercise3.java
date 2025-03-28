package lab4;

import java.util.concurrent.Semaphore;

class SharedSemaphores {
    public static Semaphore  = new Semaphore(1);
    public static Semaphore semaphore2 = new Semaphore(1);
}


class Buffor extends Thread{
    float value;
    Semaphore isFree;

    Buffor(Semaphore isFree){
        this.isFree = isFree;
    }

    public void setValue(float value){
        this.value = value;
    }

    public float getValue(float value){
        return this.value;
    }


}

class Producer extends Thread {

}

class Consumer extends Thread {
    Buffor b;


    Consumer(Buffor b){
        this.b = b;
    }

    void consume(float value){
        System.out.println(Math.sqrt(value));
        System.out.println(Math.log(value));
        System.out.println(Math.pow(value, 2.0));
        System.out.println();
    }

    @Override
    public void run() {
        while(true){
            int value = b.getValue();
            consume(value);
        }
    }
}

public class Exercise3 {
    public static void main(String[] args) {

    }
}
