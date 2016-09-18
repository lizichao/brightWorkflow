package cn.com.bright.yuexue.exception;

/**
 * <p>Title:�Զ����쳣</p>
 * <p>Description: �����ݿ���û���ҵ�����,�׳��쳣</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
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
