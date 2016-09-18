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
        // this.addButton("saveDraft", "����ݸ�");
        // this.addButton("taskConf", "��������");
        // this.addButton("confirmStartProcess", "�ύ����");
        // this.addButton("startProcess", "��������");
        // this.addButton("completeTask", "�������");

        // this.addButton("claimTask", "��������");
        // this.addComponent("releaseTask", "�ͷ�����");
        this.addComponent("copyto", "����", ComponentVO.TEXT_COMPONENT_TYPE);
        this.addComponent("transfer", "ת��", ComponentVO.BUTTON_COMPONENT_TYPE);
        // this.addComponent("rollback", "�˻�");
        this.addComponent("rollbackPrevious", "�˻أ���һ����", ComponentVO.BUTTON_COMPONENT_TYPE);
        // this.addComponent("counterSignOperate", "�Ӽ�ǩ");
        this.addComponent("addSubTask", "����", ComponentVO.BUTTON_COMPONENT_TYPE);
        this.addComponent("addTaskHandler", "����������", ComponentVO.BUTTON_COMPONENT_TYPE);
        // this.addComponent("revokeTask",
        // "�ջ�����",ComponentVO.BUTTON_COMPONENT_TYPE);

        // this.addButton("rollbackAssignee", "�˻أ�ָ�������ˣ�");
        // this.addButton("rollbackActivity", "�˻أ�ָ�����裩");
        // this.addButton("rollbackActivityAssignee", "�˻أ�ָ�����裬ָ�������ˣ�");
        // this.addButton("delegateTask", "Э��");
        // this.addButton("resolveTask", "����");
        // this.addButton("endProcess", "��ֹ����");
        // this.addButton("suspendProcess", "��ͣ����");
        // this.addButton("resumeProcess", "�ָ�����");
        // this.addButton("viewHistory", "�鿴����״̬");
        // this.addButton("addCounterSign", "��ǩ");
        // this.addButton("jump", "������ת");
        // this.addButton("reminder", "�߰�");
        // this.addButton("withdraw", "����");
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
