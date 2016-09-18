package cn.com.bright.yuexue.util;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.Session;
import nl.justobjects.pushlet.core.SessionManager;
import nl.justobjects.pushlet.util.PushletException;

/**
 * <p>Title:��Ϣ���ͻỰ������</p>
 * <p>Description: ��Ϣ���ͻỰ����</p>
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
 * <p> zhangxq    2014/11/1       1.0          build this moudle </p>
 *     
 */
public class PadSessionManager extends SessionManager {
	
	@Override 
	public Session createSession(Event anEvent) throws PushletException { 
		String sessionId = anEvent.getField("userid", createSessionId()); 
		return Session.create(sessionId); 
	}
}
