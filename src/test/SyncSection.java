package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import com.org.prj.cre_dsty_obj.BuilderObj;
import com.org.prj.cre_dsty_obj.CreateObj;
import com.org.prj.cre_dsty_obj.Noninstantiable;

public class SyncSection {
	static void testApproaches(PairManager pman2){
		ExecutorService  exec =  Executors.newCachedThreadPool();
		PairManipulator pm2 = new PairManipulator(pman2);//pman2.increment(),x\y增加
		PairChecker pchecker2 = new PairChecker(pman2);//pman2.checkCounter.incrementAndGet(),then pm.getPair().checkState()
		exec.execute(pm2);
		exec.execute(pchecker2);
		try{
			TimeUnit.MILLISECONDS.sleep(5000);
		}catch(Exception e){
			System.out.println("sleep interrupted");
		}
		System.out.println("pm2:"+pm2);
		System.exit(0);
	}
	
	public static void main(String[] args){
		PairManager pman2 = new PairManager2();//
		testApproaches(pman2);
	}
	
	
}

class Pair{
	private int x,y;
	public Pair(int x,int y){
		this.x=x;
		this.y=y;
	}
	public Pair(){	this(0,0); };
	public int getX(){	return x; }
	public int getY(){	return y; }
	public void incrementX(){	x++; }
	public void incrementY(){	y++; }
	public String toString(){
		return "x:"+ x + ",y:"+ y;
	}
	public class PairValueNotEqualException extends RuntimeException{
		public PairValueNotEqualException(){
			super("not equals:"+Pair.this);
		}
	}
	public void checkState(){
		if(x != y){
			throw new PairValueNotEqualException();
		}
	}
}

abstract class PairManager{
	AtomicInteger checkCounter = new AtomicInteger(0);//
	protected Pair p = new Pair();
	private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());
	public synchronized Pair getPair(){
		return new Pair(p.getX(),p.getY());
	}
	protected void store(Pair p){
		storage.add(p);
		try{
			TimeUnit.MILLISECONDS.sleep(50);
		}catch(Exception e){}
	}
	public abstract void increment();
}

class PairManager2 extends PairManager{
	public void increment(){
		Pair temp;
		synchronized(this){
			p.incrementX();
			p.incrementY();
			temp=getPair();
		}
		store(temp);
	}
}

class PairManipulator implements Runnable{
	private PairManager pm;
	public PairManipulator(PairManager pm){
		this.pm = pm;
	}
	public void run(){
		while(true){
			pm.increment();
		}
	}
	public String toString(){
		return "Pair:"+pm.getPair()+" checkCounter="+pm.checkCounter.get();
	}
}

class PairChecker implements Runnable{
	private PairManager pm;
	public PairChecker(PairManager pm){
		this.pm = pm;
	}
	public void run(){
		while(true){
			pm.checkCounter.incrementAndGet();//原子性的将值加1
			pm.getPair().checkState();
		}
	}
}

