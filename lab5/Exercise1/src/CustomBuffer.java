public class CustomBuffer {
    private final int BUFFER_SIZE;
    private final char [] buffer;

    private int inputIndex = 0;
    private int outputIndex = 0;
    private int counter = 0;

    CustomBuffer(int size){
        this.BUFFER_SIZE = size;
        this.buffer = new char [size];
    }

    public synchronized void write(char character){
        if(this.counter == BUFFER_SIZE){
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        buffer[inputIndex] = character;
        counter++;
        inputIndex = (inputIndex + 1) % BUFFER_SIZE;
        notifyAll();
    }

    public synchronized char read(){
        if(this.counter == 0){
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        char readCharacter = buffer[outputIndex];
        counter--;
        outputIndex = (outputIndex + 1) % BUFFER_SIZE;
        notifyAll();
        return readCharacter;
    }


}
