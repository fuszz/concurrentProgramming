public class Main {
    public static void main(String[] args){
        CustomBuffer b = new CustomBuffer(20);
        Consumer c = new Consumer(b);
        Producer p = new Producer(b);
        p.start();
        c.start();
    }
}
