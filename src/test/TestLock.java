/**
 * 项目名称：effective_java
 * 类 名 称：TestLock
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年10月15日 上午12:30:33
 * 修 改 人：cmcc
 * 修改时间：2016年10月15日 上午12:30:33
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @包名：test
 * @类名：TestLock
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年10月15日上午12:30:33
 * @版本：1.0.0
 * 
 */
public class TestLock {
	private ReentrantLock lock = new ReentrantLock();
	public void untimed(){
		boolean captured = lock.tryLock();
		try{
			System.out.println("trylock:"+captured);
		}finally{
			if(captured){
				lock.unlock();
			}
		}
	}
	
	public void timed(){
		boolean captured = false;
		try{
			captured = lock.tryLock(6,TimeUnit.SECONDS);//在接下来2s内如果锁没有被其他线程占用，则尝试获取锁，并立刻返回。
		}catch(InterruptedException e){
			throw new RuntimeException(e);
		}
		try{
			System.out.println("trylock2"+captured);
		}finally{
			if(captured){
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args){
		final TestLock al = new TestLock();
		al.untimed();
		al.timed();
		new Thread(){
			{setDaemon(true);}
			public void run(){
				al.lock.lock();
				System.out.println("acquired");
			}
		}.start();
		Thread.yield();
		al.untimed();
		al.timed();
	}
	
	

}
