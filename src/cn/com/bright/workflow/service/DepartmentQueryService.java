package cn.com.bright.workflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.workflow.api.vo.DepartmentVO;

public class DepartmentQueryService {

    public DepartmentVO getDepartmentVO(String deptId) {
        ArrayList<Object> paramList = new ArrayList<Object>();
        paramList.add(deptId);
        PlatformDao pDao = new PlatformDao();
        DepartmentVO departmentVO = new DepartmentVO();
        try {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("select * from pcmc_dept where deptid = ?");
            pDao.setSql(strSQL.toString());
            pDao.setBindValues(paramList);

            Element userResult = pDao.executeQuerySql(-1, 1);

            departmentVO.setDeptId(userResult.getChild("Record").getChildText("deptid"));
            departmentVO.setDeptCode(userResult.getChild("Record").getChildText("deptcode"));
            departmentVO.setDeptName(userResult.getChild("Record").getChildText("deptname"));

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pDao.releaseConnection();
        }
        return departmentVO;
    }

    public List<DepartmentVO> getMultiDepartmentVO(List<String> deptIds) {
        if (CollectionUtils.isEmpty(deptIds)) {
            return Collections.EMPTY_LIST;
        }
        PlatformDao pDao = new PlatformDao();

        String deptIdStr = StringUtils.arrayToCommaDelimitedString(convertiDepartmentIds(deptIds));
        List<DepartmentVO> resultDepartmentVOs = new ArrayList<DepartmentVO>();
        try {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("select * from pcmc_dept where deptid in (");
            strSQL.append(deptIdStr);
            strSQL.append(")");
            pDao.setSql(strSQL.toString());

            Element deptResult = pDao.executeQuerySql(-1, 1);
            List<Element> deptList = deptResult.getChildren("Record");
            for (int i = 0; i < deptList.size(); i++) {
                Element rec = (Element) deptList.get(i);
                DepartmentVO departmentVO = new DepartmentVO();
                departmentVO.setDeptId(rec.getChildText("deptid"));
                departmentVO.setDeptCode(rec.getChildText("deptcode"));
                departmentVO.setDeptName(rec.getChildText("deptname"));
                resultDepartmentVOs.add(departmentVO);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pDao.releaseConnection();
        }
        return resultDepartmentVOs;
    }

    private String[] convertiDepartmentIds(List<String> deptIds) {
        Map<String, String> tempHandlerMap = new HashMap<String, String>();
        String[] result = new String[deptIds.size()];
        for (int i = 0; i < deptIds.size(); i++) {
            String deptId = deptIds.get(i);
            if (!tempHandlerMap.containsKey(deptId)) {
                result[i] = "'" + deptId + "'";
            }
            tempHandlerMap.put(deptId, deptId);
        }
        return result;
    }
}
