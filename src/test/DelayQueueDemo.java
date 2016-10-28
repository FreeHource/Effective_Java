/**
 * 项目名称：effective_java
 * 类 名 称：DelayQueueDemo
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月14日 下午11:01:12
 * 修 改 人：cmcc
 * 修改时间：2016年11月14日 下午11:01:12
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：DelayQueueDemo
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年11月14日下午11:01:12
 * @版本：1.0.0
 * 
 */
class DelayedTask implements Runnable, Delayed{
	private static int counter = 0;
	private final int id = counter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayedTask> sequeue = new ArrayList<DelayedTask>();
	public DelayedTask(int delayInMilliseconds){
		delta = delayInMilliseconds;
		trigger = System.nanoTime()+ TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);//将毫秒转换成纳秒
		sequeue.add(this);
	}
	public long getDelay(TimeUnit unit){
		return unit.convert(trigger-System.nanoTime(), TimeUnit.NANOSECONDS);//将原来的nanoseconds单位值转换成unit对应单位的值。
	}
	public int compareTo(Delayed arg){//delayed接口继承了该接口
		DelayedTask that = (DelayedTask)arg;
		if(trigger < that.trigger){
			return -1;
		}
		if(trigger > that.trigger){
			return 1;
		}
		return 0;
	}
	public void run(){
		System.out.println(this+" ");
	}
	//格式化输出
	public String toString(){
		return String.format("%1$-3d", delta)+" Task "+id;
	}
	//格式化输出
	public String summary(){
		return "("+id+":"+delta+")";
	}
	//嵌套类，提供一个关闭所有事物的方法。该类对象放到队列的最末尾，执行后关闭资源。
	public static class EndSentinel extends DelayedTask{
		private ExecutorService exec;
		public EndSentinel(int delay, ExecutorService e){
			super(delay);
			exec = e;
		}
		//这个方法会输出队列中的对象
		public void run(){
			for(DelayedTask pt : sequeue){
				System.out.println(pt.summary()+" ");
			}
			System.out.println();
			System.out.println(this+" calling shutdonw");
			exec.shutdownNow();
		}
	}
}

class DelayedTaskConsumer implements Runnable{
	private DelayQueue<DelayedTask> q;
	public DelayedTaskConsumer(DelayQueue<DelayedTask>q){
		this.q = q;
	}
	public void run(){
		try{
			while(!Thread.interrupted()){
				//q.take().run();
				try{
				q.poll().run();
				}catch(Exception e1){}
			}
		}catch(Exception e){
			
		}
		System.out.println("finished DelayedTaskConsumer");
	}
}

public class DelayQueueDemo {
	public static void main(String[] args){
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
		for(int i=0; i<20; i++){
			queue.put(new DelayedTask(rand.nextInt(5000)));//初始化延迟队列，延迟时间随机生成。put()方法内部对每次插入都会进行排序
		}
		queue.add(new DelayedTask.EndSentinel(5000, exec));//该对象放到队列的末尾，用于在最后关闭所有资源。
		exec.execute(new DelayedTaskConsumer(queue));//按延时进行调用
	}
}
