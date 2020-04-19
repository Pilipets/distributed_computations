import java.util.LinkedList;
import java.util.Queue;

import Synchronizators.*;

class Storage {
    private Queue<Integer>queue;
    private int size;

    private static CustomSemaphore semCon = new CustomSemaphore(0);
    private static CustomSemaphore semProd = new CustomSemaphore(1);

    public Storage(int size){
        queue = new LinkedList<>();
        this.size = size;
    }
    public void get() throws Exception {
        semCon.acquire();

        synchronized (queue){
            if(queue.isEmpty())
                semProd.release();
            else {
                int item = queue.remove();
                System.out.println("Consumer consumed item : " + item);
                semCon.release();
            }
        }
    }

    void put(int item) throws Exception {
        semProd.acquire();

        synchronized (queue){
            if(queue.size() == size) {
                semCon.release();
            }
            else{
                queue.offer(item);
                System.out.println(Thread.currentThread().getName() + " produced item : " + item);
                semProd.release();
            }
        }
    }
} 