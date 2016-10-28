package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

import com.org.prj.cre_dsty_obj.BuilderObj;
import com.org.prj.cre_dsty_obj.CreateObj;
import com.org.prj.cre_dsty_obj.Noninstantiable;

public class Test extends TestCase{
	
	/**测试静态工厂方法*/
	public void testCreateObj(){
		CreateObj obj = CreateObj.getInstance();
		System.out.println(obj.getName());
		System.out.println(obj.getType());
	}
	
	/**测试builder构建器
	 * @throws Exception */
	public void testBuilder() throws Exception{
		BuilderObj obj = new BuilderObj().build().name("BuildObject").type("Object")
				.num(1111).count(2222);
		System.out.println(obj.toString());
		
		BuilderObj obj_1 = new BuilderObj().build().name("BuildObject").type("Object")
				.num(1111).count(-2);
		System.out.println(obj_1.toString());
	}
	
	public void testNoninstantiable(){
		String obj = Noninstantiable.toString("noninstan","object");
		System.out.println(obj);
	}
	
	public void testNextNext(){
		System.out.println("hello");
	}
	
	public static void testFile() throws Exception{
		InputStream ins = new FileInputStream("/Users/cmcc/Documents/Eqi_conf");
		 File file = new File("11");
	        OutputStream os = null;
	        try {
	            os = new FileOutputStream(file);
	            int bytesRead = 0;
	            byte[] buffer = new byte[8192];
	            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
	                os.write(buffer, 0, bytesRead);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new Exception("400");
	        } finally {
	            try {
	                if (os != null)
	                    os.close();
	                if (ins != null)
	                    ins.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	        }
	        System.out.println("game over:");
	        //return file;
	}
	
	
	
	public static void main(String[] args){
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0; i<15; i++){
			exec.execute(new Thread(){
				public void run(){
					try{
						testFile();
						System.out.println(this.currentThread().getName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{}
				}
			});
		}
		exec.shutdown();
		
	}

}
