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
public class SyncOther {
	private Object syncObject = new Object();
	public synchronized void f(){
		for(int i=0; i<5; i++){
			System.out.println("f()");
			Thread.yield();
		}
	}
	
	public void g(){
		synchronized(syncObject){
			for(int i=0; i<5; i++){
				System.out.println("g()");
				Thread.yield();
			}
		}
	}
}

class SyncObject{
	public static void main(String[] args){
		final SyncOther so = new SyncOther();
		new Thread(){
			public void run(){
				so.f();
			}
		}.start();
		so.g();
	}
}
