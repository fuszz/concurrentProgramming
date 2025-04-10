public class Consumer extends Thread{
    CustomBuffer buffer;

    Consumer(CustomBuffer buffer){
        this.buffer = buffer;
    }

    @Override
    public void run(){
        while(true){
            StringBuilder outputString = new StringBuilder();
            while(outputString.length() < 80){
                outputString.append(buffer.read());
            }
            System.out.println(outputString.toString());
        }
    }

}
