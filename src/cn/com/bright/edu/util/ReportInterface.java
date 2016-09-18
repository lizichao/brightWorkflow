package cn.com.bright.edu.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;

/**
 * <p>Title:报表接口类</p>
 * <p>Description: 所有报表实体都实现本接口</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 *      
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time          version      desc</p>
 * <p> zxqing    12/1/7       1.0          build this moudle </p>
 *     
 */
public interface ReportInterface {
    /**
     * 填充数据
     * @param paraHashMap
     * @return 参数对象
     */
	public Map<String,Object> fillData(HashMap<String,Object> paraHashMap);
	/**
	 * 生成报表
	 * @param paraHashMap
	 * @return 报表生成后的web访问路径
	 * @throws IOException
	 * @throws ParsePropertyException
	 */
	public String makeReport(HashMap<String,Object> paraHashMap) throws IOException,ParsePropertyException;
	
}
