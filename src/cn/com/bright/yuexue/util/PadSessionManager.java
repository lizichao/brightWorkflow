package cn.com.bright.yuexue.util;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.Session;
import nl.justobjects.pushlet.core.SessionManager;
import nl.justobjects.pushlet.util.PushletException;

/**
 * <p>Title:消息推送会话管理器</p>
 * <p>Description: 消息推送会话管理</p>
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
