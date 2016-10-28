/**
 * 项目名称：effective_java
 * 类 名 称：MethodTest
 * 类 描 述：(描述信息)
 * 创 建 人：cmcc
 * 创建时间：2016年11月15日 上午8:57:03
 * 修 改 人：cmcc
 * 修改时间：2016年11月15日 上午8:57:03
 * 修改备注：
 * @version
 * 
*/
package test;

/**
 * @包名：test
 * @类名：MethodTest
 * @描述：(描述这个类的作用)
 * @作者：cmcc
 * @时间：2016年11月15日上午8:57:03
 * @版本：1.0.0
 * 
 */
public class MethodTest {
	public String getvId() {
		return vId;
	}

	public void setvId(String vId) {
		this.vId = vId;
	}

	public String getaName() {
		return aName;
	}

	public void setaName(String aName) {
		this.aName = aName;
	}

	public String getC_Name() {
		return c_Name;
	}

	public void setC_Name(String c_Name) {
		this.c_Name = c_Name;
	}

	private String vId;
	private String aName;
	private String c_Name;
}
