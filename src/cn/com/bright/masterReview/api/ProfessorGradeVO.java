package cn.com.bright.masterReview.api;

/**
 * 专家评分VO
 * @ClassName: ProfessorGradeVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月22日 上午11:06:56
 */
public class ProfessorGradeVO {
    
    private String businessKey;
    private String apply_headmaster;
    private String professor;
    private String report_grade;
    private String sumGrade;

    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public String getApply_headmaster() {
        return apply_headmaster;
    }
    public void setApply_headmaster(String apply_headmaster) {
        this.apply_headmaster = apply_headmaster;
    }
    public String getProfessor() {
        return professor;
    }
    public void setProfessor(String professor) {
        this.professor = professor;
    }
    public String getReport_grade() {
        return report_grade;
    }
    public void setReport_grade(String report_grade) {
        this.report_grade = report_grade;
    }
    public String getSumGrade() {
        return sumGrade;
    }
    public void setSumGrade(String sumGrade) {
        this.sumGrade = sumGrade;
    }
    
    
}
