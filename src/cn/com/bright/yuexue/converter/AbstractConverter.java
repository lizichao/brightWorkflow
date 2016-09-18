package cn.com.bright.yuexue.converter;

/**
 * <p>Title:文档转换接口</p>
 * <p>Description: 文档转换接口</p>
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
 * <p> zhangxq    2014/08/12       1.0          build this moudle </p>
 *     
 */
public interface AbstractConverter {
    /**
     * 启动转换
     * @param srcPath
     * @param destPath
     */
	public void process(String srcPath,String destPath) throws Exception;		
	
}
