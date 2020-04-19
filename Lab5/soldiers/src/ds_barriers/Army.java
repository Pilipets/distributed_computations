package ds_barriers;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Army implements Runnable{
    private final CyclicBarrier cyclicBarrier;
    private final AtomicBoolean keepTurning;
    private final boolean[]array;
    private final int leftIndex, rightIndex;
    private final Random r = new Random();
    public Army(CyclicBarrier cyclicBarrier, AtomicBoolean keepTurning,
                boolean[]array,  int leftIndex, int rightIndex){
        this.cyclicBarrier = cyclicBarrier;
        this.keepTurning = keepTurning;
        this.array = array;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    public void makeTurn(){
        for(int i = leftIndex; i < rightIndex; i++){
            // 0 - left turn, 1 right turn
            array[i] = r.nextBoolean();
            int shift = 0;
            if(array[i] && i + 1 < rightIndex && array[i]^array[i+1])
                shift = 1;
            else if(!array[i] && i - 1 >= leftIndex && array[i]^array[i-1])
                shift = -1;
            array[i] = !array[i];
            array[i+shift] = !array[i+shift];
        }
    }

    @Override
    public void run() {
        while (keepTurning.get()) {
            makeTurn();
            try {
                cyclicBarrier.await();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Commander implements Runnable{
    private boolean[]array;
    private final AtomicBoolean keepTurning;
    public Commander(boolean[]array, AtomicBoolean keepTurning){
        this.array = array;
        this.keepTurning = keepTurning;
    }

    public boolean checkPositionsOfSoldiers(){
        for (int i = 0; i < array.length-1; i++){
            if(array[i]^array[i+1]){
                System.out.println("Bad turn!");
                return false;
            }
        }
        System.out.println("Good turn!");
        return true;
    }

    @Override
    public void run() {
        keepTurning.set(!checkPositionsOfSoldiers());
    }
}
class Main {
    public static void main(String[]args) throws InterruptedException {
        final int chunk_num = 4;
        final int total_size = 100;
        final int chunk_size = total_size/chunk_num;
        boolean []arr = new boolean[total_size];

        final AtomicBoolean keepTurning = new AtomicBoolean(true);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(chunk_num, new Commander(arr, keepTurning));
        Thread[] raws = new Thread[chunk_num];
        for(int i = 0; i < chunk_num; ++i){
            raws[i] = new Thread(new Army(cyclicBarrier, keepTurning, arr, i*chunk_size, (i+1)*chunk_size));
            raws[i].start();
        }
        for(int i = 0; i < chunk_num; ++i)
            raws[i].join();
    }
}