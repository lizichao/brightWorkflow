package cn.com.bright.workflow.workcal.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.workflow.util.DateUtil;
import cn.com.bright.workflow.workcal.domain.WorkcalPart;
import cn.com.bright.workflow.workcal.domain.WorkcalRule;
import cn.com.bright.workflow.workcal.domain.WorkcalType;

public class WorkcalRuleManager {

    public List<WorkcalRule> getWorkcalRules(Integer status, Integer year, boolean statusWay) {
        ArrayList<String> paramList = new ArrayList<String>();

        PlatformDao dao = null;
        List<WorkcalRule> workcalRuleList = new ArrayList<WorkcalRule>();
        try {
            dao = new PlatformDao();
            StringBuffer strSQL = new StringBuffer();

            // strSQL.append("select * from workcal_rule where status = "+status);

            if (statusWay) {
                strSQL.append("select * from workcal_rule where status = " + status);
                if (null != year) {
                    strSQL.append(" and year = " + year);
                }
            } else {
                strSQL.append("select * from workcal_rule where status <> " + status);
                if (null != year) {
                    strSQL.append(" and year = " + year);
                }
                // strSQL.append("select * from workcal_rule where status <>'0' and year ='2015'");
            }

            /*
             * if(null != status){ if(statusWay){
             * strSQL.append(" and status = ?"); }else{
             * strSQL.append(" and status <> ?"); } paramList.add(status); }
             * if(null != year){ strSQL.append(" and year = ?");
             * paramList.add(year); }
             */
            // paramList.add(String.valueOf(status));
            // paramList.add(String.valueOf(year));
            // dao.setBindValues(paramList);
            dao.setSql(strSQL.toString());
            Element resultElement = dao.executeQuerySql(-1, 1);
            List<Element> ruleElements = resultElement.getChildren("Record");

            for (Element ruleElement : ruleElements) {
                WorkcalRule workcalRule = new WorkcalRule();
                long ruleId = Long.valueOf(ruleElement.getChildTextTrim("id"));
                workcalRule.setId(ruleId);
                workcalRule.setName(ruleElement.getChildTextTrim("name"));
                workcalRule.setStatus(Integer.parseInt(ruleElement.getChildTextTrim("status")));
                String week = ruleElement.getChildTextTrim("week");
                int weekInt = 0;
                if (!StringUtils.isEmpty(week)) {
                    weekInt = Integer.parseInt(week);
                }

                workcalRule.setWeek(weekInt);
                workcalRule.setWorkDate(DateUtil.stringToDate(ruleElement.getChildTextTrim("work_date")));
                workcalRule.setYear(Integer.parseInt(ruleElement.getChildTextTrim("year")));

                Set<WorkcalPart> workcalPartSet = getWorkcalParts(ruleId);
                workcalRule.setWorkcalParts(workcalPartSet);

                WorkcalType workcalType = getWorkcalType(ruleElement.getChildTextTrim("type_id"));
                workcalRule.setWorkcalType(workcalType);
                workcalRuleList.add(workcalRule);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return workcalRuleList;
    }

    private WorkcalType getWorkcalType(String typeId) {
        ArrayList<Object> paramList = new ArrayList<Object>();
        paramList.add(typeId);
        PlatformDao pDao = new PlatformDao();
        WorkcalType workcalType = new WorkcalType();
        try {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("SELECT * from workcal_type where id = ?");

            pDao.setSql(strSQL.toString());
            pDao.setBindValues(paramList);

            Element workcalTypeResult = pDao.executeQuerySql(-1, 1);

            workcalType.setId(Long.valueOf(workcalTypeResult.getChild("Record").getChildText("id")));
            workcalType.setName(workcalTypeResult.getChild("Record").getChildText("name"));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pDao.releaseConnection();
        }
        return workcalType;
    }

    private Set<WorkcalPart> getWorkcalParts(long ruleId) {
        PlatformDao dao = new PlatformDao();

        Element resultElement = null;
        Set<WorkcalPart> workcalPartSet = new HashSet<WorkcalPart>();
        try {
            ArrayList<Long> bvals = new ArrayList<Long>();
            StringBuffer sql = new StringBuffer("");
            sql.append("select * from workcal_part where rule_id=?");
            bvals.add(ruleId);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List parts = resultElement.getChildren("Record");
            if (parts.size() == 0) {
                return Collections.EMPTY_SET;
            }

            for (int i = 0; i < parts.size(); i++) {
                Element rRec = (Element) parts.get(i);
                String id = rRec.getChildText("id");
                String shift = rRec.getChildText("shift");
                String start_time = rRec.getChildText("start_time");
                String end_time = rRec.getChildText("end_time");
                WorkcalPart workcalPart = new WorkcalPart();
                workcalPart.setId(Long.valueOf(id));
                workcalPart.setShift(Integer.parseInt(shift));
                workcalPart.setStartTime(start_time);
                workcalPart.setEndTime(end_time);
                workcalPartSet.add(workcalPart);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // log4j.logError("[更新用户发生异常.]"+e.getMessage());
            // log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(),
            // "GBK", true));
            // xmlDocUtil.writeErrorMsg("10614","修改密码失败");
        } finally {
            dao.releaseConnection();
        }
        return workcalPartSet;
    }

    public void saveWorkcalRule(WorkcalRule workcalRule) {
        PlatformDao platformDao = null;
        platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            platformDao.setSql("SELECT max(ID)+1 as id FROM workcal_rule");
            Element resultElement = platformDao.executeQuerySql(-1, 1);
            String id = resultElement.getChild("Record").getChildTextTrim("id");
            long seq = DBOprProxy.getNextSequenceNumber("workcal_rule");
            id = StringUtils.isEmpty(id)? String.valueOf(seq) : id;

            platformDao.setSql("insert into workcal_rule(id,year,week,name,work_date,status,type_id)"
                + "values(?,?,?,?,?,?,?)");
            // platformDao.setSql("insert into workcal_rule(year,week,name,status,type_id)"
            // + "values(?,?,?,?,?)");
      
            ArrayList<Object> bvals = new ArrayList<Object>();
            bvals.add(id);
            bvals.add(workcalRule.getYear());
            bvals.add(workcalRule.getWeek());
            bvals.add(workcalRule.getName());
            bvals.add(workcalRule.getWorkDate());
            bvals.add(workcalRule.getStatus());
            bvals.add(workcalRule.getWorkcalType().getId());
            platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }

    public void deleteWorkcalRule(List ids) {
        PlatformDao platformDao = null;
        platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            /*
             * ArrayList<Object> bvals = new ArrayList<Object>(); bvals.add(
             * StringUtils .arrayToCommaDelimitedString(StringUtils
             * .toStringArray(convertWorkcalRuleIds(ids))));
             */
            String str = StringUtils.arrayToCommaDelimitedString(StringUtils
                .toStringArray(convertWorkcalRuleIds(ids)));

            StringBuffer sql = new StringBuffer("");

            sql.append("DELETE FROM workcal_part");
            sql.append(" WHERE RULE_ID in(");
            sql.append(str);
            sql.append(")");
            platformDao.setSql(sql.toString());
            platformDao.executeTransactionSql();

            sql.setLength(0);
            sql.append("delete  from workcal_rule where id in(");
            sql.append(str);
            sql.append(")");
            platformDao.setSql(sql.toString());

            // platformDao.setSql("delete * from workcal_rule where id = ?");
            // platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }

    private Set<String> convertWorkcalRuleIds(List<String> ids) {
        // String[] idArray = ids.split(",");
        Set<String> idSet = new HashSet<String>();
        for (String id : ids) {
            idSet.add("'" + id + "'");
        }
        return idSet;
    }

    public void updateWorkcalRule(WorkcalRule workcalRule) {
        PlatformDao platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("UPDATE workcal_rule");
            sqlBuf.append(" SET");
            sqlBuf.append("  YEAR =?,");
            sqlBuf.append(" WEEK = ?,");
            sqlBuf.append(" NAME = ?,");
            sqlBuf.append(" WORK_DATE = ?,");
            sqlBuf.append(" STATUS = ?,");
            sqlBuf.append(" TYPE_ID = ?");
            sqlBuf.append(" WHERE ID = ?");

            ArrayList<Object> bvals = new ArrayList<Object>();
            bvals.add(workcalRule.getYear());
            bvals.add(workcalRule.getWeek());
            bvals.add(workcalRule.getName());
            bvals.add(workcalRule.getWorkDate());
            bvals.add(workcalRule.getStatus());
            bvals.add(workcalRule.getWorkcalType().getId());
            bvals.add(workcalRule.getId());

            platformDao.setSql(sqlBuf.toString());
            platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }

    public List<WorkcalRule> queryWorkcalRule() {
        PlatformDao dao = null;
        List<WorkcalRule> workcalRuleList = new ArrayList<WorkcalRule>();
        try {
            dao = new PlatformDao();
            // ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT wrule.ID,");
            sqlBuf.append(" YEAR,");
            sqlBuf.append(" WEEK,");
            sqlBuf.append(" wrule.NAME,");
            sqlBuf.append(" WORK_DATE,");
            sqlBuf.append(" STATUS,");
            sqlBuf.append(" TYPE_ID,");
            sqlBuf.append("wtype.NAME AS workcalTypeName");
            sqlBuf.append("  FROM workcal_rule wrule");
            sqlBuf.append(" LEFT JOIN workcal_type wtype ON wrule.TYPE_ID = wtype.ID");

            dao.setSql(sqlBuf.toString());
            Element resultElement = dao.executeQuerySql(-1, 1);
            List<Element> ruleElements = resultElement.getChildren("Record");

            for (Element ruleElement : ruleElements) {
                WorkcalRule workcalRule = new WorkcalRule();
                workcalRule.setId(Long.valueOf(ruleElement.getChildText("id")));
                workcalRule.setName(ruleElement.getChildText("name"));
                workcalRule.setStatus(Integer.parseInt(ruleElement.getChildText("status")));
                String week = ruleElement.getChildText("week");
                int weekInt = 0;
                if (!StringUtils.isEmpty(week)) {
                    weekInt = Integer.parseInt(week);
                }
                workcalRule.setWeek(weekInt);
                workcalRule.setYear(Integer.parseInt(ruleElement.getChildText("year")));
                workcalRule.setWorkDate(DateUtil.stringToDate(ruleElement.getChildText("work_date")));
                WorkcalType workcalType = new WorkcalType();
                workcalType.setId(Long.parseLong(ruleElement.getChildText("type_id")));

                workcalRule.setWorkcalType(workcalType);
                workcalRuleList.add(workcalRule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return workcalRuleList;
    }

    public boolean isWorkDay(String taskCreateTimeStr) {
        ArrayList<String> paramList = new ArrayList<String>();
        paramList.add(taskCreateTimeStr);
        PlatformDao pDao = new PlatformDao();
        try {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("SELECT id, year, week, name, work_date, status, type_id ");
            strSQL.append("FROM workcal_rule where UNIX_TIMESTAMP(WORK_DATE) =UNIX_TIMESTAMP(?)");
            // strSQL.append("FROM workcal_rule where WORK_DATE =?");
            pDao.setSql(strSQL.toString());
            pDao.setBindValues(paramList);

            Element resultElement = pDao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() > 0) {
                String status = resultElement.getChild("Record").getChildTextTrim("status");
                String name = resultElement.getChild("Record").getChildTextTrim("name");
                if ("2".equals(status)) {
                    return true;
                }
                if ((!"1".equals(status)) && (!"3".equals(status))) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pDao.releaseConnection();
        }
        return false;
    }
}
