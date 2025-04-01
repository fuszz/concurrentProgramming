package lab4;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;

class Buffer {
    Semaphore elementExists;
    Semaphore placeExists;
    int size;
    AtomicIntegerArray value;
    int wy_ind, we_ind;

    Buffer(int size) {
        value = new AtomicIntegerArray(size);
        this.size = size;
        placeExists = new Semaphore(size);
        elementExists = new Semaphore(0);
    }

    int getValue() {
        elementExists.acquireUninterruptibly();
        int result = value.get(wy_ind);
        wy_ind = (wy_ind + 1) % size;
        placeExists.release();
        return result;
    }
    void setValue(int value) {
        placeExists.acquireUninterruptibly();
        this.value.set(we_ind, value);
        we_ind = (we_ind + 1) % size;
        elementExists.release();
    }
}

public class Exercise4 {

    public static void main(String[] args) {

    }
}
