/**
 * 项目名称：effective_java
 * 类 名 称：TestBlockingQueues
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月7日 下午10:14:31
 * 修 改 人：cmcc
 * 修改时间：2016年11月7日 下午10:14:31
 * 修改备注：
 * @version
 * 
*/
package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：TestBlockingQueues
 * @描述：测试BlockingQueue
 * @作者：cmcc
 * @时间：2016年11月7日下午10:14:31
 * @版本：1.0.0
 * 
 */
public class CopyOfTestBlockingQueues {
	static void getKey(){
		try{
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	static void getKey(String message){
		System.out.println(message);
		getKey();
	}
	static void test(String msg, BlockingQueue<Lift>queue) throws InterruptedException{
		System.out.println(msg);
		
		LiftRunner runner = new LiftRunner(queue);
		Thread t = new Thread(runner);
		t.start();//读取队列
		for(int i=0; i<5; i++){
			runner.getQueue().add(new Lift(i));//网队列里添加
		}
		
		//getKey("press enter("+msg+")");
		TimeUnit.SECONDS.sleep(5);
		for(int i=0; i<5; i++){
			runner.getQueue().add(new Lift(i+5));//网队列里添加
		}
		TimeUnit.SECONDS.sleep(5);
		t.interrupt();
		System.out.println("finished"+msg+" test");
	}
	public static void main(String[] args) throws InterruptedException{
		test("LinkedBlockingQueue",new LinkedBlockingQueue<Lift>());
		//test("ArrayBlockingQueue",new ArrayBlockingQueue<Lift>(2));
		//test("SynchronousQueue",new SynchronousQueue<Lift>());
	}
}



class LiftRunner implements Runnable{
	private BlockingQueue<Lift> rockets;
	public LiftRunner(BlockingQueue<Lift> queue){
		rockets = queue;
	}
	public BlockingQueue<Lift> getQueue(){
		return this.rockets;
	}
	/*
	public void add(Lift lo){
		try{
			rockets.put(lo);//将数据放入队列
		}catch(InterruptedException e){
			System.out.println("interrupted during put()");
		}
	}
	*/
	public void run(){
		try{
			while(!Thread.interrupted()){
				Lift rocket = rockets.take();//从队列中去数据，当没有数据时，会被阻塞
				rocket.print();;//取出数据后执行
			}
		}catch(InterruptedException e){
			System.out.println("waking from take()");
		}
		System.out.println("exiting LiftOffRunner");
	}
}

class Lift {//底层线程对象
	private int tag;
	public Lift(int i){
		this.tag = i;
	}
	public void print() {
		System.out.println(this.tag+" -->");
	}
	
}
