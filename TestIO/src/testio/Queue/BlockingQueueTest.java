/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author 赖金杰
 */
public class BlockingQueueTest {
    /**
     * 
     * 定义装苹果的篮子
     * 
     */
    public class Basket {
        // 篮子，能够容纳3个苹果
        BlockingQueue<String> basket = new LinkedBlockingQueue<String>(3);

        // 生产苹果，放入篮子
        public void produce() throws InterruptedException {
            // put方法放入一个苹果，若basket满了，等到basket有位置
            basket.put("An apple");
        }

        // 消费苹果，从篮子中取走
        public String consume() throws InterruptedException {
            // take方法取出一个苹果，若basket为空，等到basket有苹果为止(获取并移除此队列的头部)
            return basket.take();
        }
    }

    // 定义苹果生产者
    class Producer implements Runnable {
        private String instance;
        private Basket basket;

        public Producer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }

        public void run() {
            try {
                while (true) {
                    // 生产苹果
                    System.out.println("生产者准备生产苹果：" + instance);
                    basket.produce();
                    System.out.println("!生产者生产苹果完毕：" + instance);
                    // 休眠300ms
                    Thread.sleep(300);
                }
            } catch (InterruptedException ex) {
                System.out.println("Producer Interrupted");
            }
        }
    }

    // 定义苹果消费者
    class Consumer implements Runnable {
        private String instance;
        private Basket basket;

        public Consumer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }

        public void run() {
            try {
                while (true) {
                    // 消费苹果
                    System.out.println("消费者准备消费苹果：" + instance);
                    System.out.println(basket.consume());
                    System.out.println("!消费者消费苹果完毕：" + instance);
                    // 休眠1000ms
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                System.out.println("Consumer Interrupted");
            }
        }
    }
/*
    public static void main(String[] args) {
        BlockingQueueTest test = new BlockingQueueTest();

        // 建立一个装苹果的篮子
        Basket basket = test.new Basket();

        ExecutorService service = Executors.newCachedThreadPool();
        Producer producer = test.new Producer("生产者001", basket);
        Producer producer2 = test.new Producer("生产者002", basket);
        Consumer consumer = test.new Consumer("消费者001", basket);
        service.submit(producer);
        service.submit(producer2);
        service.submit(consumer);
        // 程序运行5s后，所有任务停止
//        try {
//            Thread.sleep(1000 * 5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        service.shutdownNow();
    }*/
    
    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
    private static int count = 3; // 线程个数
    //CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
    private static CountDownLatch latch = new CountDownLatch(count);
    private static boolean offerover;
    public static void main(String[] args) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        offerover=false;
        ExecutorService es = Executors.newFixedThreadPool(10);
        //ConcurrentLinkedQueueTest.offer();

        es.submit(()->{offer();latch.countDown();});
        for (int i = 0; i < count-1; i++) {
            es.submit(new Poll());
        }
        timeStart = System.currentTimeMillis();
        latch.await(); //使得主线程(main)阻塞直到latch.countDown()为零才继续执行
        System.out.println("pool耗时 " + (System.currentTimeMillis() - timeStart) + "ms");
        es.shutdown();
    }
    
    
    /**
     * 生产
     */
    public static void offer() {
        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            queue.offer(i);
        }
        offerover=true;
        System.out.println("offer完毕，加入缓存耗时 " + (System.currentTimeMillis() - timeStart) + "ms");
    }


    /**
     * 消费
     *  
     * @author 林计钦
     * @version 1.0 2013-7-25 下午05:32:56
     */
    static class Poll implements Runnable {
        public void run() {
            int nullcount =0;
            int count =0;
            while (true) {
                try {
                    Integer value=queue.poll();
                    if(value == null)
                    {
                        
                        if(offerover)
                        {
                            break;
                        }
                        nullcount++;
                        
                    }
                    else
                    {
                        count++;
                    }
                    
                    //if(count>100000) break;
                } catch (Exception e) {
                    System.out.println("发生错误：" + e.getMessage());
                    break;
                }
                
                //System.out.println();
            }
            System.out.println("poll次数：" + count + ", null 次数：" + nullcount);
            latch.countDown();
        }
    }
}
