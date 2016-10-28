/**
 * 项目名称：effective_java
 * 类 名 称：CountDownLatchDemo
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月14日 下午10:20:49
 * 修 改 人：cmcc
 * 修改时间：2016年11月14日 下午10:20:49
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：CountDownLatchDemo
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年11月14日下午10:20:49
 * @版本：1.0.0
 * 
 */
class TaskPortion implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private static Random rand = new Random(47)	;
	private final CountDownLatch latch;
	TaskPortion(CountDownLatch latch){
		this.latch = latch;
	}
	public void run(){
		try{
			System.out.println("going to dowork");
			dowork();
			latch.countDown();
		}catch(InterruptedException ex){
			
		}
	}
	public void dowork() throws InterruptedException{
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
		System.out.println(this+" completed");
	}
	public String toString(){
		return String.format("%1$-3d ", id);
	}
}
class WaitingTask implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private final CountDownLatch latch;
	WaitingTask(CountDownLatch latch){
		this.latch = latch;
	}
	public void run(){
		try{
			System.out.println("goint to await...");
			latch.await();
			System.out.println("latch barrier passed for "+this);
		}catch(InterruptedException ex){
			
		}
	}
	public String toString(){
		return String.format("waiting %1%-3d ",id);
	}
}
public class CountDownLatchDemo {
	static final int SIZE = 100;
	public static void main(String[] args){
		ExecutorService exec = Executors.newCachedThreadPool();
		CountDownLatch latch = new CountDownLatch(SIZE);
		for(int i=0; i<10; i++){
			exec.execute(new WaitingTask(latch));
		}
		for(int i=0; i<SIZE; i++){
			exec.execute(new TaskPortion(latch));
		}
		System.out.println("launched all tasks");
		//exec.shutdown();
	}
}
