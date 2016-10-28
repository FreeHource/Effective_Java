/**
 * 项目名称：effective_java
 * 类 名 称：Restaurant
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月3日 下午11:28:35
 * 修 改 人：cmcc
 * 修改时间：2016年11月3日 下午11:28:35
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
 * @类名：Restaurant
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年11月3日下午11:28:35
 * @版本：1.0.0
 * 
 */
//开放一个餐厅，开始营业
public class Restaurant {
	public Meal meal;//餐厅的菜
	ExecutorService exec = Executors.newCachedThreadPool();
	WaitPerson waitPerson = new WaitPerson(this);//初始化餐厅的服务员
	Chef chef = new Chef(this);//初始化餐厅的厨师
	public Restaurant(){//启动厨师和服务员线程
		exec.execute(chef);
		exec.execute(waitPerson);
	}
	public static void main(String[] args){
		new Restaurant();
	}
}
class Meal{
	private final int orderNum;
	public Meal(int orderNum){
		this.orderNum = orderNum;
	}
	public String toString(){
		return "Meal" + orderNum;
	}
}
class WaitPerson implements Runnable{
	private Restaurant restaurant;
	public WaitPerson(Restaurant r){//初始化服务员
		restaurant = r;
	}
	public void run(){
		try{
			while(!Thread.interrupted()){
				synchronized(this){
					while(restaurant.meal == null){//没有菜品时，服务员线程挂起
						wait();
					}
				}
				System.out.println("waitperson got "+ restaurant.meal);
				synchronized(restaurant.chef){
					restaurant.meal = null;
					restaurant.chef.notifyAll();//菜品端出，激活厨师线程
				}
			}
		}catch(Exception e){
				System.out.println("waitperson interrupted");
		}
	}
}
class Chef implements Runnable{
	private Restaurant restaurant;
	private int count = 0;
	public Chef(Restaurant r){//初始化厨师
		restaurant = r;
	}
	public void run(){//启动厨师线程
		try{
			while(!Thread.interrupted()){
				synchronized(this){
					while(restaurant.meal!=null){//菜品准备好后，厨师线程挂起
						wait();
					}
				}
				if(++count == 10){
					System.out.println("out of food,closing");
					restaurant.exec.shutdownNow();//生产10份菜品后，整个活动停止
				}
				System.out.println("order up");
				synchronized(restaurant.waitPerson){//
					restaurant.meal = new Meal(count);//生产菜品
					restaurant.waitPerson.notifyAll();//菜品准备好，激活服务员线程
				}
				TimeUnit.SECONDS.sleep(3);
			}
		}catch(Exception e){
			System.out.println("chef interrupted");
		}
	}
}
