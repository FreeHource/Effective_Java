/**
 * 项目名称：effective_java
 * 类 名 称：ThreadLocalTest
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年10月20日 下午10:46:15
 * 修 改 人：cmcc
 * 修改时间：2016年10月20日 下午10:46:15
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：ThreadLocalTest
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年10月20日下午10:46:15
 * @版本：1.0.0
 * 
 */
public class ThreadLocalTest implements Runnable{
	private final int id;
	public ThreadLocalTest(int idn){ id = idn; }
	public void run(){
		while(!Thread.currentThread().isInterrupted()){
			//ThreadLocalVariableHolder.increment();
			ThreadLocalVariableHolder.value.set(ThreadLocalVariableHolder.get() + 1);
			System.out.println(this);
			Thread.yield();
		}
	}
	public String toString(){
		return "#"+ id + ":" + ThreadLocalVariableHolder.get();
	}
	
	public static void main(String[] args) throws InterruptedException{
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0; i<5; i++){
			exec.execute(new ThreadLocalTest(i));
		}
		TimeUnit.MILLISECONDS.sleep(15);
		exec.shutdownNow();
	}

}

class ThreadLocalVariableHolder{
	public static ThreadLocal<Integer> value = 
			new ThreadLocal<Integer>(){
		private Random rand = new Random(47);
		protected  Integer initialValue(){
			int intValue = rand.nextInt(1000);
			System.out.println("-->initValue:"+intValue);
			return intValue;
		}
	};
	public static void increment(){
		value.set(value.get() + 1);
	}
	public static int get(){
		return (int) value.get();
	}
}
