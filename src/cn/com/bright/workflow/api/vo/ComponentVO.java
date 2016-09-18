package cn.com.bright.workflow.api.vo;

public class ComponentVO {

	public static final int BUTTON_COMPONENT_TYPE = 0;
	public static final int TEXT_COMPONENT_TYPE = 1;

	private String componentId;

	private String componentType;

	private String componentLabel;

	private int componentCategory;

	public ComponentVO(String componentType, String componentLabel,int componentCategory) {
		this.componentType = componentType;
		this.componentLabel = componentLabel;
		this.componentCategory = componentCategory;
	}

	public ComponentVO(String componentId, String componentType,String componentLabel) {
		this.componentId = componentId;
		this.componentType = componentType;
		this.componentLabel = componentLabel;
	}

	public ComponentVO() {
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getComponentLabel() {
		return componentLabel;
	}

	public void setComponentLabel(String componentLabel) {
		this.componentLabel = componentLabel;
	}

	public int getComponentCategory() {
		return componentCategory;
	}

	public void setComponentCategory(int componentCategory) {
		this.componentCategory = componentCategory;
	}
}
