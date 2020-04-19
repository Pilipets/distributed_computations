import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

class StringChanger implements Runnable{
    StringBuilder stringBuilder;
    CyclicBarrier cyclicBarrier;
    AtomicBoolean cond;
    public StringChanger(StringBuilder stringBuilder,CyclicBarrier cyclicBarrier, AtomicBoolean cond){
        this.stringBuilder = stringBuilder;
        this.cyclicBarrier = cyclicBarrier;
        this.cond = cond;
    }

    public void changeLetters(){
        Random random = new Random();
        int letterIndex = random.nextInt(4);
        char letter = stringBuilder.charAt(letterIndex);
        switch (letter){
            case 'A':
                stringBuilder.setCharAt(letterIndex,'C');
                System.out.println(String.format("Thread %s changed letter %c to %c",Thread.currentThread().getName(),'A', 'C'));
                break;
            case 'B':
                stringBuilder.setCharAt(letterIndex,'D');
                System.out.println(String.format("Thread %s changed letter %c to %c",Thread.currentThread().getName(),'B', 'D'));
                break;
            case 'C':
                stringBuilder.setCharAt(letterIndex,'A');
                System.out.println(String.format("Thread %s changed letter %c to %c",Thread.currentThread().getName(),'C', 'A'));
                break;
            case 'D':
                stringBuilder.setCharAt(letterIndex,'B');
                System.out.println(String.format("Thread %s changed letter %c to %c",Thread.currentThread().getName(),'D', 'B'));
                break;
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!cond.get())
            changeLetters();
        System.out.println(stringBuilder);
    }
}

class Checker implements Runnable{

    private StringBuilder[] work_strings;
    private AtomicBoolean res;
    public Checker(StringBuilder[] work_strings, AtomicBoolean res){
        this.work_strings = work_strings;
        this.res = res;
    }

    @Override
    public void run() {
        int[]arr = new int[4];
        for (int i = 0; i < 4; ++i) {
            StringBuilder s = work_strings[i];
            for (int k = 0; k < s.length(); k++) {
                if(s.charAt(k) == 'A' || s.charAt(k) == 'B'){
                    arr[i]++;
                }
            }
        }

        boolean cond1 = (arr[0]==arr[1] && arr[0]==arr[2]);
        boolean cond2 = (arr[0]==arr[2] && arr[0]==arr[3]);
        boolean cond3 = (arr[0]==arr[1] && arr[0]==arr[3]);
        boolean cond4 = (arr[1]==arr[2] && arr[2]==arr[3]);

        if(cond1||cond2||cond3||cond4){
            System.out.println("Finishing");
            res.set(true);
        }
    }
}

class CyclicBarrierDemo{
    private AtomicBoolean condition;
    private CyclicBarrier cyclicBarrier;
    public void runSimulation() throws InterruptedException {
        StringBuilder[] arr = {
                new StringBuilder("ABCDBCAAA"),
                new StringBuilder("CBCCBAAAA"),
                new StringBuilder("ACCDDABCA"),
                new StringBuilder("ACBBDCBAA")
        };
        condition = new AtomicBoolean(false);
        cyclicBarrier = new CyclicBarrier(4, new Checker(arr, condition));

        Thread[] threads = new Thread[4];
        for(int i = 0; i < 4; ++i){
            threads[i] = new Thread(new StringChanger(arr[i], cyclicBarrier, condition));
            threads[i].start();
        }
        for (int i = 0; i < 4; ++i)
            threads[i].join();
    }
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrierDemo demo = new CyclicBarrierDemo();
        demo.runSimulation();
    }
}