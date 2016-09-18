package cn.com.bright.workflow.workcal.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.workflow.workcal.domain.WorkcalType;

public class WorkcalTypeManager {
    public void saveWorkcalType(WorkcalType workcalType) {
        PlatformDao platformDao = null;
        platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            platformDao.setSql("insert into workcal_type(id,name)" + "values(?,?)");
            long seq = DBOprProxy.getNextSequenceNumber("workcal_type");
            ArrayList<Object> bvals = new ArrayList<Object>();
            // bvals.add(seq);
            bvals.add(seq);
            bvals.add(workcalType.getName());
            platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }

    public void updateWorkcalType(WorkcalType workcalType) {
        PlatformDao platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("UPDATE workcal_type");
            sqlBuf.append(" SET NAME = ? WHERE ID =?");

            ArrayList<Object> bvals = new ArrayList<Object>();
            bvals.add(workcalType.getName());
            bvals.add(workcalType.getId());

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

    public void deleteWorkcalType(List ids) {
        PlatformDao platformDao = new PlatformDao();
        platformDao.beginTransaction();
        try {
            // ArrayList<Object> bvals = new ArrayList<Object>();
            // bvals.add( StringUtils
            // .arrayToCommaDelimitedString(StringUtils
            // .toStringArray(convertWorkcalTypeIds(ids))));
            String str = StringUtils.arrayToCommaDelimitedString(StringUtils
                .toStringArray(convertWorkcalTypeIds(ids)));
            StringBuffer sql = new StringBuffer("");
            sql.append("delete  from workcal_type where id in(");
            sql.append(str);
            sql.append(")");
            platformDao.setSql(sql.toString());
            // platformDao.setBindValues(bvals);
            platformDao.executeTransactionSql();
        } catch (Exception e) {
            e.printStackTrace();
            platformDao.rollBack();
        } finally {
            platformDao.releaseConnection();
        }
    }

    private Set<String> convertWorkcalTypeIds(List<String> ids) {
        Set<String> idSet = new HashSet<String>();
        for (String id : ids) {
            idSet.add("'" + id + "'");
        }
        return idSet;
    }

    public Element getWorkcalType(String workcalTypeId) {
        PlatformDao dao = null;

        Element masterData = null;
        try {
            dao = new PlatformDao();
            // 取主表结果
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ID, NAME  FROM workcal_type where id =" + workcalTypeId);

            dao.setSql(sql.toString());
            masterData = dao.executeQuerySql(-1, 1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return masterData;
    }

    public List<WorkcalType> queryWorkcalType() {
        PlatformDao dao = null;
        List<WorkcalType> workcalTypeList = new ArrayList<WorkcalType>();
        try {
            dao = new PlatformDao();
            StringBuffer sqlBuf = new StringBuffer();
            sqlBuf.append("SELECT ID, NAME ");
            sqlBuf.append("FROM workcal_type");

            dao.setSql(sqlBuf.toString());
            Element resultElement = dao.executeQuerySql(-1, 1);
            List<Element> typeElements = resultElement.getChildren("Record");

            for (Element typeElement : typeElements) {
                WorkcalType workcalType = new WorkcalType();
                workcalType.setId(Long.valueOf(typeElement.getChildText("id")));
                workcalType.setName(typeElement.getChildText("name"));
                workcalTypeList.add(workcalType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return workcalTypeList;
    }
}
