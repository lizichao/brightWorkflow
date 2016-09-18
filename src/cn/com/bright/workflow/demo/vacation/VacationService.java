package cn.com.bright.workflow.demo.vacation;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.TaskDelegateException;
import cn.com.bright.workflow.service.ProcessOperateService;
import cn.com.bright.workflow.service.TaskOperateService;

@Service
@Transactional(rollbackFor = Exception.class)
public class VacationService {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    ProcessOperateService processOperateService;

    @Resource
    TaskOperateService taskOperateService;

    public void startVacationProcess(VacationVO vacationVO, ProcessStartVO processStartVO) {
        StringBuffer sql = new StringBuffer("");
        sql.append("insert into workflow_demo_vacation(id,days,reason) values(?,?,?)");

        long seq = DBOprProxy.getNextSequenceNumber("workflow_demo_vacation");
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(),new Object[] { seq, vacationVO.getDays(), vacationVO.getReason() });

        processOperateService.startProcessInstance(processStartVO, String.valueOf(seq));
    }

    public void completeVacation(TaskCompleteVO taskCompleteVO) throws TaskDelegateException {
        taskOperateService.completeTask(taskCompleteVO);
    }
}
