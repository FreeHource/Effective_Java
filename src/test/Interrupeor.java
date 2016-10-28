/**
 * 项目名称：effective_java
 * 类 名 称：Interrupeor
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年10月29日 上午7:36:20
 * 修 改 人：cmcc
 * 修改时间：2016年10月29日 上午7:36:20
 * 修改备注：
 * @version
 * 
*/
package test;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：Interrupeor
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年10月29日上午7:36:20
 * @版本：1.0.0
 * 
 */
 class SleepBlocked implements Runnable{

	@Override
	public void run() {
		try{
			while(true){
				TimeUnit.SECONDS.sleep(10);
			}
		}catch(InterruptedException e){
			System.out.println("InterruptedException");
		}
		System.out.println("existing sleep blocked run()");
	}

}
class IOBlocked implements Runnable{
	private InputStream in;
	public IOBlocked(InputStream is){ in = is; }
	public void run(){
		try{
			System.out.println("wait for read():");
			in.read();
		}catch(IOException e){
			if(Thread.currentThread().isInterrupted()){
				System.out.println("interrupted from blocked IO");
			}else{
				throw new RuntimeException(e);
			}
		}
		System.out.println("exis ioblock run()");
	}
}

class SynchronizedBlocked implements Runnable{
	public synchronized void f(){
		while(true){
			Thread.yield();
		}
	}
	public SynchronizedBlocked(){
		new Thread(){
			public void run(){
				f();
			}
		}.start();
	}
	public void run(){
		System.out.println("try to call f()");
		f();
		System.out.println("exist synchronizedBlocked run()");
	}
}

public class Interrupeor{
	private static ExecutorService exec = Executors.newCachedThreadPool();
	static void test(Runnable r) throws InterruptedException{
		Future<?> f = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("interrupting "+r.getClass().getName());
		f.cancel(true);
		System.out.println("interrupt sent to "+r.getClass().getName());
	}
	public static void main(String[] args) throws InterruptedException{
		test(new SleepBlocked());
		test(new IOBlocked(System.in));
		test(new SynchronizedBlocked());
		TimeUnit.SECONDS.sleep(30);
		System.out.println("aborting with system.exist()");
		System.exit(0);
	}
}