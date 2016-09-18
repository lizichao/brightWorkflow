package cn.com.bright.yuexue.exception;

/**
 * <p>Title:自定义异常</p>
 * <p>Description: 从数据库中没有找到数据,抛出异常</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/11       1.0          build this moudle </p>
 *     
 */
public class NotFoundDataException extends Exception {
	private static final long serialVersionUID = 3121977966245774560L;
	public NotFoundDataException(String message) {
		super(message);
	}
	public NotFoundDataException(String message, Throwable cause) {
		super(message,cause);
	}
}
