package concurrency.barber;

import java.util.concurrent.Semaphore;

public class BarberShop {
    // semaphores
    private Semaphore barber_chair; // the barber chair
    private Semaphore chairs; 		// the waiting room chairs
    private Semaphore barber;		// the actual barber

    private int waiting_customers;
    private final int number_of_chairs;

    private static final int HAIRCUT_TIME = 1000;

    public BarberShop(int number_of_chairs) {
        barber_chair = new Semaphore(1, true);
        chairs = new Semaphore(0, true);
        barber = new Semaphore(0, true);

        this.number_of_chairs = number_of_chairs;
        waiting_customers = 0;
        barberReady();
    }

    private void barberReady() {
        System.out.println("Barber is ready to cut hair");
        barber.release();

        chairs.release();
        System.out.println("Chairs available permits =" +chairs.availablePermits() +",barber_chair=" +barber_chair.availablePermits());
    }

    public void customerReady(Customer c) {
        System.out.println(c + " wants a haircut");
        if(barber_chair.availablePermits() <= 0)
            customerSitDown(c);

        if(c.wantsHaircut()) {
            try {
                barber_chair.acquire();
                haircut(c);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void customerSitDown(Customer c) {
        if(waiting_customers < number_of_chairs) {
            try {
                waiting_customers++;
                System.out.println(c + " is sitting down in the waiting room. There are " + waiting_customers + " waiting");

                chairs.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(c + " gets angry and leaves because the shop is full");
            c.wantsToLeave();
        }
    }

    public void haircut(Customer c) {
        if(waiting_customers > 0)
            waiting_customers--;

        try {
            barber.acquire();
            System.out.println(c + " is getting a haircut");

            //for(int i=0; i<=HAIRCUT_TIME; i++); // busy wait
            Thread.sleep(HAIRCUT_TIME);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println(c + " is done, checks out his haircut and pays the barber");
        barber_chair.release();
        barberReady();
    }

    public static void main(String[] args) throws InterruptedException {
        int customer_number = 100;
        BarberShop sh = new BarberShop(5);
        Thread[] cust = new Thread[customer_number];

        for(int i=0; i<customer_number; i++)
            cust[i] = new Customer(sh, "" + i);

        for(int i=0; i<customer_number ; i++)
            cust[i].start();
    }
}
