package cn.com.bright.yuexue.converter;

/**
 * <p>Title:�ĵ�ת���ӿ�</p>
 * <p>Description: �ĵ�ת���ӿ�</p>
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
 * <p> zhangxq    2014/08/12       1.0          build this moudle </p>
 *     
 */
public interface AbstractConverter {
    /**
     * ����ת��
     * @param srcPath
     * @param destPath
     */
	public void process(String srcPath,String destPath) throws Exception;		
	
}
