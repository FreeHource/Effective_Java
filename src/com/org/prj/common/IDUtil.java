package com.org.prj.common;

/**
 * ID生成器
 * 
 * @author twitter
 */
public class IDUtil {
	
	private final long workerId;
	private final long workerIdBits = 4L;
	private final long sequenceBits = 10L;
	private final long twepoch = 1361753741828L;
	
	private final long workerIdShift = sequenceBits;
	private final long timestampLeftShift = sequenceBits + workerIdBits;
	private final long sequenceMask = -1L ^ -1L << sequenceBits;
	
	private long sequence = 0L;
	private long lastTimestamp = -1L;
	
	private static IDUtil idUtil;
	
	/**
	 * 单例初始化
	 */
	private IDUtil(final long workerId) {
		this.workerId = workerId;
	}
	
	/**
	 * 生成下一个ID
	 */
	private synchronized long nextId() {
		long timestamp = System.currentTimeMillis();
		if(this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & sequenceMask;
			if(this.sequence == 0) {
				timestamp = System.currentTimeMillis();
				while(timestamp <= lastTimestamp) {
					timestamp = System.currentTimeMillis();
				}
			}
		}
		else {
			this.sequence = 0;
		}
		this.lastTimestamp = timestamp;
		return ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << this.workerIdShift) | (this.sequence);
	}
	
	/**
	 * 生成ID
	 */
	public static long newUUID() {
		if(idUtil == null) {
			idUtil = new IDUtil(2);
		}
		return idUtil.nextId();
	}
	
	public static void main(String[] args) {
		while(true) {
			System.out.println(IDUtil.newUUID());
		}
	}
}
