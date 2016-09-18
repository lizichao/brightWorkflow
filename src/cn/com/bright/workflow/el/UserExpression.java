package cn.com.bright.workflow.el;

import java.util.Collection;
import java.util.regex.Pattern;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.JuelExpression;
import org.activiti.engine.impl.javax.el.ValueExpression;

import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class UserExpression extends JuelExpression {

    private static final long serialVersionUID = 8708188853650174857L;

    public UserExpression(ValueExpression valueExpression, String expressionText) {
        super(valueExpression, expressionText);
    }

    public Object getValue(VariableScope variableScope) {
        Object value = super.getValue(variableScope);
        if (value instanceof String) {
            value = getUserOriginalVal((String) value);
        } else if (value instanceof Collection) {
            // Iterator userIdSet = ((Collection) value).iterator();
            // Collection resultCollection = new Collection();
            // while (userIdSet.hasNext()) {
            // String userCollectVal = userIdSet.next();
            // }
        } else {
            throw new ActivitiException("Expression did not resolve to a string or collection of strings");
        }
        return value;
    }

    private String getUserOriginalVal(String value) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select userid from pcmc_user where userid=?");

        String userId = ApplicationContextHelper.getJdbcTemplate().queryForObject(sql.toString(),String.class, value);
        /*
         * PlatformDao dao = new PlatformDao(); StringBuffer sql = new
         * StringBuffer(""); Element resultElement = null; try {
         * ArrayList<String> bvals = new ArrayList<String>(); //if
         * (isNumeric1((String) value)) {
         * sql.append("select * from pcmc_user where userid=?");
         * bvals.add(String.valueOf(value)); dao.setSql(sql.toString());
         * dao.setBindValues(bvals); resultElement = dao.executeQuerySql(-1, 1);
         * } else { sql.append("select * from pcmc_user where usercode=?");
         * bvals.add(String.valueOf(value)); dao.setSql(sql.toString());
         * dao.setBindValues(bvals); resultElement = dao.executeQuerySql(-1, 1);
         * } if (resultElement.getChildren("Record").size() > 0) { value =
         * resultElement.getChild("Record").getChildTextTrim("userid"); } }
         * catch (Exception e) { e.printStackTrace(); //
         * log4j.logError("[更新用户发生异常.]"+e.getMessage()); //
         * log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), // "GBK",
         * true)); // xmlDocUtil.writeErrorMsg("10614","修改密码失败"); } finally {
         * dao.releaseConnection(); }
         */
        return userId;
    }

    private boolean isNumeric1(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
