package cn.com.bright.workflow.util;

import java.util.HashMap;
import java.util.Map;

import cn.com.bright.workflow.api.vo.ComponentVO;

public class ComponentHelper {
    // private static Logger logger =
    // LoggerFactory.getLogger(ButtonHelper.class);
    // private Log logger = new Log(this.getClass().toString());
    private Map<String, ComponentVO> map = new HashMap<String, ComponentVO>();

    public ComponentHelper() {
        // this.addButton("saveDraft", "保存草稿");
        // this.addButton("taskConf", "配置任务");
        // this.addButton("confirmStartProcess", "提交数据");
        // this.addButton("startProcess", "发起流程");
        // this.addButton("completeTask", "完成任务");

        // this.addButton("claimTask", "认领任务");
        // this.addComponent("releaseTask", "释放任务");
        this.addComponent("copyto", "抄送", ComponentVO.TEXT_COMPONENT_TYPE);
        this.addComponent("transfer", "转审", ComponentVO.BUTTON_COMPONENT_TYPE);
        // this.addComponent("rollback", "退回");
        this.addComponent("rollbackPrevious", "退回（上一步）", ComponentVO.BUTTON_COMPONENT_TYPE);
        // this.addComponent("counterSignOperate", "加减签");
        this.addComponent("addSubTask", "交办", ComponentVO.BUTTON_COMPONENT_TYPE);
        this.addComponent("addTaskHandler", "新增处理人", ComponentVO.BUTTON_COMPONENT_TYPE);
        // this.addComponent("revokeTask",
        // "收回任务",ComponentVO.BUTTON_COMPONENT_TYPE);

        // this.addButton("rollbackAssignee", "退回（指定负责人）");
        // this.addButton("rollbackActivity", "退回（指定步骤）");
        // this.addButton("rollbackActivityAssignee", "退回（指定步骤，指定负责人）");
        // this.addButton("delegateTask", "协办");
        // this.addButton("resolveTask", "还回");
        // this.addButton("endProcess", "终止流程");
        // this.addButton("suspendProcess", "暂停流程");
        // this.addButton("resumeProcess", "恢复流程");
        // this.addButton("viewHistory", "查看流程状态");
        // this.addButton("addCounterSign", "加签");
        // this.addButton("jump", "自由跳转");
        // this.addButton("reminder", "催办");
        // this.addButton("withdraw", "撤销");
    }

    public void addComponent(String name, String label, int componentCategory) {
        this.addComponent(new ComponentVO(name, label, componentCategory));
    }

    public void addComponent(ComponentVO componentVO) {
        this.map.put(componentVO.getComponentType(), componentVO);
    }

    public ComponentVO findComponent(String componentId, String name) {
        ComponentVO componentVO = map.get(name);

        if (componentVO == null) {
            // logger.logInfo("button "+name+" not exists");
        }
        componentVO.setComponentId(componentId);
        return componentVO;
    }

    public Map<String, ComponentVO> getMap() {
        return this.map;
    }
}
