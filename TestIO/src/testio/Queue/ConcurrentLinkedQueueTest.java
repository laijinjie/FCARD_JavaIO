/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentLinkedQueueTest {
    private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
    private static int count = 3; // 线程个数
    //CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
    private static CountDownLatch latch = new CountDownLatch(count);
    private static boolean offerover;
    public static void main(String[] args) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        offerover=false;
        ExecutorService es = Executors.newFixedThreadPool(10);
        ConcurrentLinkedQueueTest.offer();
        //Integer[] items = (Integer[])queue.toArray();
        //es.submit(()->{offer();latch.countDown();});
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
        for (int i = 0; i < 10000; i++) {
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