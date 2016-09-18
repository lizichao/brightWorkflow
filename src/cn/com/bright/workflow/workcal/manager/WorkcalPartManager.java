package cn.com.bright.workflow.workcal.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.workflow.workcal.domain.WorkcalPart;

public class WorkcalPartManager {

    public List<WorkcalPart> getWorkcalParts(String ruleId) {
        ArrayList<Object> paramList = new ArrayList<Object>();

        PlatformDao pDao = new PlatformDao();
        List<WorkcalPart> workcalPartList = new ArrayList<WorkcalPart>();
        try {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("select * from workcal_part where 1=1");
            if (!StringUtils.isEmpty(ruleId)) {
                strSQL.append("and rule_id = ?");
                paramList.add(ruleId);
            }
            pDao.setBindValues(paramList);
            pDao.setSql(strSQL.toString());

            Element resultElement = pDao.executeQuerySql(-1, 1);
            List<Element> partElements = resultElement.getChildren("Record");
            for (Element partElement : partElements) {
                WorkcalPart workcalPart = new WorkcalPart();
                workcalPart.setId(Long.valueOf(partElement.getChildText("id")));
                workcalPart.setShift(Integer.parseInt(partElement.getChildText("id")));
                workcalPart.setStartTime(partElement.getChildText("start_time"));
                workcalPart.setEndTime(partElement.getChildText("end_time"));
                workcalPartList.add(workcalPart);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pDao.releaseConnection();
        }
        return workcalPartList;
    }

    public void saveWorkcalPart(WorkcalPart workcalPart) {
        PlatformDao platformDao = null;
        platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            platformDao.setSql("SELECT max(ID)+1 as id FROM workcal_part");
            Element resultElement = platformDao.executeQuerySql(-1, 1);
            String id = resultElement.getChild("Record").getChildTextTrim("id");

            platformDao.setSql("insert into workcal_part(id,shift,start_time,end_time,rule_id)"
                + "values(?,?,?,?,?)");
            long seq = DBOprProxy.getNextSequenceNumber("workcal_part");

            ArrayList<Object> bvals = new ArrayList<Object>();
            bvals.add(id);
            bvals.add(workcalPart.getShift());
            bvals.add(workcalPart.getStartTime());
            bvals.add(workcalPart.getEndTime());
            bvals.add(workcalPart.getWorkcalRule().getId());
            platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }

    public void deleteWorkcalPart(List ids) {
        PlatformDao platformDao = null;
        platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            // ArrayList<Object> bvals = new ArrayList<Object>();
            // bvals.add( StringUtils
            // .arrayToCommaDelimitedString(StringUtils
            // .toStringArray(convertWorkcalPartIds(ids))));
            String str = StringUtils.arrayToCommaDelimitedString(StringUtils.toStringArray(convertWorkcalPartIds(ids)));
            StringBuffer sql = new StringBuffer("");
            sql.append("delete  from workcal_part where id in(");
            sql.append(str);
            sql.append(")");
            platformDao.setSql(sql.toString());

            // platformDao.setSql("delete * from workcal_part where id = ?");
            // platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }

    private Set<String> convertWorkcalPartIds(List<String> ids) {
        // String[] idArray = ids.split(",");
        Set<String> idSet = new HashSet<String>();
        for (String id : ids) {
            idSet.add("'" + id + "'");
        }
        return idSet;
    }

    public void updateWorkcalPart(WorkcalPart workcalPart) {
        PlatformDao platformDao = new PlatformDao();
        // platformDao.beginTransaction();
        try {
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("update workcal_part");
            sqlBuf.append(" set");
            sqlBuf.append(" shift = ?,");
            sqlBuf.append(" start_time = ?,");
            sqlBuf.append(" end_time = ?,");
            sqlBuf.append(" rule_id = ? ");
            sqlBuf.append(" where id = ?");

            ArrayList bvals = new ArrayList();
            bvals.add(workcalPart.getShift());
            bvals.add(workcalPart.getStartTime());
            bvals.add(workcalPart.getEndTime());
            bvals.add(workcalPart.getWorkcalRule().getId());
            bvals.add(workcalPart.getId());

            platformDao.setSql(sqlBuf.toString());
            platformDao.setBindValues(bvals);

            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            // platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }
}
