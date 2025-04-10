class Piece {
    private final char type;
    private final int number;

    Piece(char type, int number) {
        this.type = type;
        this.number = number;
    }

    public char getType() {
        return this.type;
    }

    public int getNumber() {
        return this.number;
    }

}

class Store {
    private final Piece[][] pieces;
    private final int capacity;
    private int collectionIndex = 0;
    private int supplyingIndex = 0;
    private final int[] storedPieces;

    private boolean someoneAccessing = false;

    Store(int capacity) {
        this.pieces = new Piece[3][capacity];
        this.storedPieces = new int[3];
        this.capacity = capacity;
    }

    public synchronized void supplyPiece(Piece p) {
        int pType = (int) p.getType() - 65;
        while (someoneAccessing || storedPieces[pType] >= capacity) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        someoneAccessing = true;
        pieces[pType][supplyingIndex] = p;
        supplyingIndex = (supplyingIndex + 1) % capacity;
        storedPieces[pType]++;
        System.out.println("There are " + this.storedPieces[pType] + " type " + p.getType() + " pieces");
        someoneAccessing = false;
        notifyAll();
    }

    public synchronized Piece collectPiece(char type) {
        int pType = (int) type - 65;
        while (someoneAccessing || storedPieces[pType] == 0) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        someoneAccessing = true;
        Piece p = pieces[pType][collectionIndex];
        collectionIndex = (collectionIndex + 1) % capacity;
        storedPieces[pType]--;
        System.out.println("There are " + this.storedPieces[pType] + " type " + type + " pieces");
        someoneAccessing = false;
        notifyAll();
        return p;
    }
}

class CustomProcess extends Thread {
    private final char type;
    private int counter;
    private final Store store;

    CustomProcess(char type, Store store) {
        this.type = type;
        this.counter = 0;
        this.store = store;
    }

    @Override
    public void run() {
        while (true) {
            Piece p = new Piece(this.type, this.counter);
            this.counter++;
            System.out.println("Process: " + this.getName() + " supplies piece " + this.type + this.counter);
            store.supplyPiece(p);
        }
    }

}

class MountingProcess extends Thread {
    private final Store store;

    MountingProcess(Store s) {
        this.store = s;
    }

    @Override
    public void run() {
        while (true) {
            store.collectPiece('A');
            store.collectPiece('B');
            store.collectPiece('C');
        }
    }

}

public class Main {
    public static void main(String[] args) {
        Store store = new Store(10);
        CustomProcess[] aProcess = new CustomProcess[]{
                new CustomProcess('A', store),
                new CustomProcess('A', store),
                new CustomProcess('A', store)};

        CustomProcess[] bProcess = new CustomProcess[]{
                new CustomProcess('B', store),
                new CustomProcess('B', store),
                new CustomProcess('B', store)};

        CustomProcess[] cProcess = new CustomProcess[]{
                new CustomProcess('C', store),
                new CustomProcess('C', store),
                new CustomProcess('C', store)};

        MountingProcess[] mProcess = new MountingProcess[]{
                new MountingProcess(store),
                new MountingProcess(store)};

        for (MountingProcess m : mProcess) m.start();
        for (CustomProcess a : aProcess) a.start();
        for (CustomProcess b : bProcess) b.start();
        for (CustomProcess c : cProcess) c.start();
    }
}

