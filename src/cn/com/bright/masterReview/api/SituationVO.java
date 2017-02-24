package cn.com.bright.masterReview.api;

public class SituationVO {

	private String id;
    private String businessKey;
    
    /** 有无情况 */
    private String hasSituation;
    /** 表名称 */ 
    private String tableName;
    
    public SituationVO() {
    	
    }
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getHasSituation() {
		return hasSituation;
	}
	public void setHasSituation(String hasSituation) {
		this.hasSituation = hasSituation;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
    
}
