package cn.com.bright.edu.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;

/**
 * <p>Title:����ӿ���</p>
 * <p>Description: ���б���ʵ�嶼ʵ�ֱ��ӿ�</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 *      
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time          version      desc</p>
 * <p> zxqing    12/1/7       1.0          build this moudle </p>
 *     
 */
public interface ReportInterface {
    /**
     * �������
     * @param paraHashMap
     * @return ��������
     */
	public Map<String,Object> fillData(HashMap<String,Object> paraHashMap);
	/**
	 * ���ɱ���
	 * @param paraHashMap
	 * @return �������ɺ��web����·��
	 * @throws IOException
	 * @throws ParsePropertyException
	 */
	public String makeReport(HashMap<String,Object> paraHashMap) throws IOException,ParsePropertyException;
	
}
