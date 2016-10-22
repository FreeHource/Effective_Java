/**
 * 项目名称：effective_java
 * 类 名 称：TestThread
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年10月10日 下午11:26:16
 * 修 改 人：cmcc
 * 修改时间：2016年10月10日 下午11:26:16
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

/**
 * @包名：test
 * @类名：TestThread
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年10月10日下午11:26:16
 * @版本：1.0.0
 * 
 */
public class TestThread extends TestCase {
	//Collections cs = null;
	public void testMethod1(){
		int countDown = 10;
		while(--countDown > 0){
			System.out.println("now is : "+countDown);
			innerClass ic = new innerClass(){
				public void run(){
					System.out.println("now run");
				}
			};
			ic.run();
		}
	}
interface innerClass{
	public void run();
}
	
	public void testThreadExecutor(){
		threadService ts = executors.newWorldPool();
		ts.run();
		threadService cs = executors.newCachePool();
		cs.run();
	}
	
	public void testResultTask() throws InterruptedException, ExecutionException{
		new CallableDemo().testmain();
	}
}

interface thread{
	public void run();
}
interface threadService extends thread{}
abstract class abstractthreadService implements threadService{}
class threadServicePool extends abstractthreadService{
	public String a;
	public String b;
	public threadServicePool(String a,String b){
		this.a = a;
		this.b = b;
	}
	@Override
	public void run() {
		System.out.println(this.a+" "+this.b);
	}
}
class cacheServicePool extends abstractthreadService{
	public String a;
	public String b;
	public cacheServicePool(String a,String b){
		this.a = a;
		this.b = b;
	}
	@Override
	public void run() {
		System.out.println(this.a+" "+this.b);
	}
}
class executors{
	public static threadService newWorldPool(){
		return new threadServicePool("hello","world");
	}
	public static threadService newCachePool(){
		return new cacheServicePool("now","cache");
	}
}



class cacheThreadPool{
	public void min(){
		ExecutorService exec = Executors.newCachedThreadPool();
		Executor e;
	}
}

class TaskWithResult implements Callable{
	public String str = "";
	public int sleep=0;
	public TaskWithResult(String str, int sleep){
		this.str = str;
		this.sleep = sleep;
	}
	@Override
	public Object call() throws Exception {
		if(sleep==0){
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}else{
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		}
		System.out.println(Thread.currentThread()+" : "+Thread.currentThread().getPriority());
		//Thread.sleep(sleep);
		return this.str;
	}
}

class CallableDemo{
	
	public Thread t;
	
	public void testmain() throws InterruptedException, ExecutionException{
		
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future> results = new ArrayList<Future>();
		for(int i=0; i<5; i++){
			System.out.println(i%2);
			results.add(exec.submit(new TaskWithResult("i get you:"+i,i%2==0?5000:0)));
		}
		//show what we get
		for(Future f : results){
			System.out.println(f.get());
		}
		exec.shutdown();
	}
}


