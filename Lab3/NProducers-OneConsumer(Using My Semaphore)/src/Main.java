public class Main {
    public static void main(String[]args) throws InterruptedException {
        Storage storage = new Storage(10);
        Thread producerThread1 = new Thread(new Producer(storage),"Bee1");
        Thread producerThread2 = new Thread(new Producer(storage),"Bee2");
        Thread producerThread3 = new Thread(new Producer(storage),"Bee3");
        Thread consumerThread = new Thread(new Consumer(storage),"Consumer");
        producerThread1.start();
        producerThread2.start();
        producerThread3.start();
        consumerThread.start();
        producerThread1.join();
        producerThread2.join();
        producerThread3.join();
        consumerThread.join();
    }
}
