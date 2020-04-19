class Consumer implements Runnable {
    Storage storage;
    Consumer(Storage storage) {
        this.storage = storage;
    }

    public void run() {
        for (int i = 0;; i++) {
            try {
                storage.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
} 