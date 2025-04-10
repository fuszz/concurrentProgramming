public class Producer extends Thread {
    CustomBuffer buffer;

    Producer(CustomBuffer buffer){
        this.buffer = buffer;
    }

    @Override
    public void run(){
        try{
            int previousCharacter = -1;

            while(true){
                int character = (int) System.in.read();

                if (previousCharacter == (int) '*' && character == (int) '*'){
                    buffer.write('&');
                    previousCharacter = -1;
                } else {
                    if (previousCharacter != -1) buffer.write((char) previousCharacter);
                    previousCharacter = character;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
