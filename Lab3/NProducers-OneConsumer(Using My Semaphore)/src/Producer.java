
class Producer implements Runnable {
    Storage storage;
    Producer(Storage storage) {
        this.storage = storage;
    }

    public void run() {
        for (int i = 0; ; i++) {
            try {
                storage.put(i);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
} 
  