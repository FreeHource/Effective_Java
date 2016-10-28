/**
 * 项目名称：effective_java
 * 类 名 称：NotifyVsNotifyAll
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月3日 下午11:01:28
 * 修 改 人：cmcc
 * 修改时间：2016年11月3日 下午11:01:28
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：NotifyVsNotifyAll
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年11月3日下午11:01:28
 * @版本：1.0.0
 * 
 */
public class NotifyVsNotifyAll {
	public static void main(String[] args) throws InterruptedException{
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0; i<5; i++){
			exec.execute(new Task());
		}
		exec.execute(new Task2());
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			boolean prod = true;
			public void run(){
				if(prod){
					System.out.println("notify() ");
					Task.blocker.prod();
					prod = false;
				}else{
					System.out.println("notifyAll() ");
					Task.blocker.prodAll();
					prod = true;
				}
			}
		}, 500, 500);
		TimeUnit.SECONDS.sleep(5);
		timer.cancel();
		System.out.println("timer cancled");
		TimeUnit.SECONDS.sleep(2);
		System.out.println("Task2.blocker.prodAll()");
		Task2.blocker.prodAll();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("shutting down ");
		exec.shutdownNow();
	}

}
class Blocker{
	synchronized void waitingCall(){
		try{
			while(!Thread.interrupted()){
				wait();
				System.out.println(Thread.currentThread()+" ");
			}
		}catch(Exception e){
		}
	}
	synchronized void prod(){
		notify();
	}
	synchronized void prodAll(){
		notifyAll();
	}
}
class Task implements Runnable{
	static Blocker blocker = new Blocker();
	public void run(){
		blocker.waitingCall();
	}
}
class Task2 implements Runnable{
	static Blocker blocker = new Blocker();
	public void run(){
		blocker.waitingCall();
	}
}