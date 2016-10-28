/**
 * 项目名称：effective_java
 * 类 名 称：PriorityBlockingQueueDemo
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月15日 下午11:06:17
 * 修 改 人：cmcc
 * 修改时间：2016年11月15日 下午11:06:17
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：PriorityBlockingQueueDemo
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年11月15日下午11:06:17
 * @版本：1.0.0
 * 
 */
class PriorityedTask implements Runnable,Comparable<PriorityedTask>{
	private Random rand = new Random(47);
	private static int counter = 0;
	private final int id = counter++;
	private final int priority;
	protected static List<PriorityedTask> sequeue = new ArrayList<PriorityedTask>();//用于记录任务生成的顺序
	public PriorityedTask(int priority){
		this.priority = priority;
		sequeue.add(this);
	}
	public int compareTo(PriorityedTask arg){
		return priority<arg.priority?1:(priority>arg.priority?-1:0);
	}
	public void run(){
		try{
			TimeUnit.MILLISECONDS.sleep(rand.nextInt(250));
		}catch(InterruptedException e){}
		System.out.println(this);
	}
	public String toString(){
		return String.format("%1$-3d",priority)+" Task "+id;
	}
	public String summary(){
		return "("+id+";"+priority+")";
	}
	public static class EndSentinel extends PriorityedTask{
		private ExecutorService exec ;
		public EndSentinel(ExecutorService e){
			super(-1);
			exec = e;
		}
		public void run(){
			int count = 0;
			for(PriorityedTask pt : sequeue){
				System.out.println(pt.summary());
				if(++count % 5 ==0 ){
					System.out.println();
				}
			}
			System.out.println();
			System.out.println(this+"calling shutdown()");
			exec.shutdownNow();
		}
	}
}
//‘任务创建’线程
class PriorityedTaskProducer implements Runnable{
	private Random rand = new Random(47);
	private Queue<Runnable> queue;
	private ExecutorService exec;
	public PriorityedTaskProducer(Queue<Runnable> q, ExecutorService e){
		queue = q;
		exec = e;
	}
	public void run(){
		for(int i=0; i<20; i++){
			queue.add(new PriorityedTask(rand.nextInt(10)));
			Thread.yield();
		}
		try{
			for(int i=0; i<10; i++){
				TimeUnit.MILLISECONDS.sleep(250);
				queue.add(new PriorityedTask(10));
			}
			for(int i=0; i<10; i++){
				queue.add(new PriorityedTask(i));
			}
			queue.add(new PriorityedTask.EndSentinel(exec));//添加末尾的哨兵线程
		}catch(InterruptedException e){}
		System.out.println("finished producer");
	}
}
//‘任务消费’线程
class PriorityedTaskConsumer implements Runnable {
	private PriorityBlockingQueue<Runnable> q;
	public PriorityedTaskConsumer(PriorityBlockingQueue<Runnable> q){
		this.q = q;
	}
	public void run(){
		try{
			while(!Thread.interrupted()){
				q.take().run();//如果队列里没有任务了，会进入阻塞状态，直到新任务到达队列
			}
		}catch(InterruptedException e){}
		System.out.println("finished consumer");
	}
}

public class PriorityBlockingQueueDemo{
	public static void main(String[] arg){
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();//初始化一个优先级队列
		exec.execute(new PriorityedTaskProducer(queue,exec));//启动创建任务的线程，放到指定的优先级队列中
		exec.execute(new PriorityedTaskConsumer(queue));//启动执行任务的线程，从指定优先级队列中取任务
	}
}
