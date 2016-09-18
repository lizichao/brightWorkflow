package cn.com.bright.workflow.api.service;

import org.jdom.Element;

import cn.com.bright.workflow.api.vo.ProcessStartVO;

public interface IProcessOperateService {

	public String startProcessInstance(String businessKey,
			Element processParam, String userid);

	public String startProcessInstance(ProcessStartVO processStartVO,
			String businessKey);
}
