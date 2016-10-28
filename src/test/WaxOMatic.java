/**
 * 项目名称：effective_java
 * 类 名 称：WaxOMatic
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月2日 下午10:04:06
 * 修 改 人：cmcc
 * 修改时间：2016年11月2日 下午10:04:06
 * 修改备注：
 * @version
 * 
*/
package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @包名：test
 * @类名：WaxOMatic
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年11月2日下午10:04:06
 * @版本：1.0.0
 * 
 */
public class WaxOMatic {
	public static void main(String[] args) throws InterruptedException{
		Car car = new Car();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));//抛光
		exec.execute(new WaxOn(car));//打蜡
		TimeUnit.SECONDS.sleep(60);
		exec.shutdownNow();
	}

}

class Car{
	private boolean waxOn = false;
	public synchronized void waxed(){//打蜡
		waxOn = true;
		notifyAll();
	}
	public synchronized void buffed(){//抛光
		waxOn = false;
		notifyAll();
	}
	public synchronized void waitForWaxing() throws InterruptedException{//等待打蜡完成
		while(waxOn == false){
			System.out.println("waiting for waxing");
			wait();
		}
	}
	public synchronized void waitForBuffing() throws InterruptedException{//等待抛光完成
		while(waxOn == true){
			System.out.println("waiting for buffing");
			wait();
		}
	}
}

class WaxOn implements Runnable{//打蜡
	private Car car;
	public WaxOn(Car c){
		car = c;
	}
	public void run(){
		try{
			while(!Thread.interrupted()){
				System.out.println("Wax on-->");
				TimeUnit.SECONDS.sleep(3);
				car.waxed();
				car.waitForBuffing();
			}
		}catch(InterruptedException e){
			System.out.println("Exiting via interrupt ");
		}
		System.out.println("Ending Wax On task ");
	}
}

class WaxOff implements Runnable{//抛光
	private Car car;
	public WaxOff(Car c){
		car = c;
	}
	public void run(){
		try{
			while(!Thread.interrupted()){
				car.waitForWaxing();
				System.out.println("Wax off");
				TimeUnit.SECONDS.sleep(3);
				car.buffed();
			}
		}catch(InterruptedException e){
			System.out.println("Exiting via interrupt ");
		}
		System.out.println("Ending Wax Off task ");
	}
}
