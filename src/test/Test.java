package test;

import junit.framework.TestCase;

import com.org.prj.cre_dsty_obj.BuilderObj;
import com.org.prj.cre_dsty_obj.CreateObj;

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
	
	public void testNext(){
		
	}
	
	public void testNextNext(){
		System.out.println("hello");
	}

}
