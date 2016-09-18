package cn.com.bright.workflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.workflow.api.vo.UserVO;

public class UserQueryService {

    public UserVO getUserVO(String userid) {
        ArrayList<Object> paramList = new ArrayList<Object>();
        paramList.add(userid);
        PlatformDao pDao = new PlatformDao();
        UserVO userVO = new UserVO();
        if (StringUtils.isEmpty(userid)) {
            return userVO;
        }
        try {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("SELECT a.*,b.deptid,b.deptcode,b.deptname");
            strSQL.append(" FROM pcmc_user a left join pcmc_user_dept c on a.userid=c.userid ");
            strSQL.append(" left join pcmc_dept b on c.deptid=b.deptid");
            strSQL.append(" WHERE a.state>0 and a.userid =?");

            pDao.setSql(strSQL.toString());
            pDao.setBindValues(paramList);

            Element userResult = pDao.executeQuerySql(-1, 1);

            userVO.setUserId(userResult.getChild("Record").getChildText("userid"));
            userVO.setUserCode(userResult.getChild("Record").getChildText("usercode"));
            userVO.setUserName(userResult.getChild("Record").getChildText("username"));
            userVO.setDeptId(userResult.getChild("Record").getChildText("deptid"));
            userVO.setDeptCode(userResult.getChild("Record").getChildText("deptcode"));
            userVO.setDeptName(userResult.getChild("Record").getChildText("deptname"));
            userVO.setEmail(userResult.getChild("Record").getChildText("email"));
            userVO.setMobile(userResult.getChild("Record").getChildText("mobile"));

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pDao.releaseConnection();
        }
        return userVO;
    }

    public List<UserVO> getMultiUserVO(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.EMPTY_LIST;
        }
        // ArrayList<Object> paramList = new ArrayList<Object>();
        // paramList.add(userid);
        PlatformDao pDao = new PlatformDao();

        // String userIdStr = StringUtils.arrayToCommaDelimitedString
        // (StringUtils.toStringArray(userIds));
        String userIdStr = StringUtils.arrayToCommaDelimitedString(convertUserIds(userIds));
        List<UserVO> resultUserVOs = new ArrayList<UserVO>();
        try {
            StringBuffer strSQL = new StringBuffer();
            strSQL.append("SELECT a.*,b.deptid,b.deptcode,b.deptname");
            strSQL.append(" FROM pcmc_user a left join pcmc_user_dept c on a.userid=c.userid ");
            strSQL.append(" left join pcmc_dept b on c.deptid=b.deptid");
            strSQL.append(" WHERE a.state>0 and a.userid in(");
            strSQL.append(userIdStr);
            strSQL.append(")");
            pDao.setSql(strSQL.toString());
            // pDao.setBindValues(paramList);

            Element userResult = pDao.executeQuerySql(-1, 1);
            List<Element> userList = userResult.getChildren("Record");
            for (int i = 0; i < userList.size(); i++) {
                Element rec = (Element) userList.get(i);
                UserVO userVO = new UserVO();
                userVO.setUserId(rec.getChildText("userid"));
                userVO.setUserCode(rec.getChildText("usercode"));
                userVO.setUserName(rec.getChildText("username"));
                userVO.setDeptId(rec.getChildText("deptid"));
                userVO.setDeptCode(rec.getChildText("deptcode"));
                userVO.setDeptName(rec.getChildText("deptname"));
                userVO.setEmail(rec.getChildText("email"));
                userVO.setMobile(rec.getChildText("mobile"));
                resultUserVOs.add(userVO);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pDao.releaseConnection();
        }
        return resultUserVOs;
    }

    private String[] convertUserIds(List<String> userIds) {
        Map<String, String> tempHandlerMap = new HashMap<String, String>();
        String[] result = new String[userIds.size()];
        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);
            if (!tempHandlerMap.containsKey(userId)) {
                result[i] = "'" + userId + "'";
            }
            tempHandlerMap.put(userId, userId);
        }
        return result;
    }

    /**
     * 查找当前用户部门下其他用户
     */
    public List<UserVO> queryCurrentDeptUsers() {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        PlatformDao dao = new PlatformDao();

        Element resultElement = null;
        List<UserVO> resultUserVOs = new ArrayList<UserVO>();
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT a.* ");
            sql.append(" FROM pcmc_user_dept t, pcmc_user a ");
            sql.append("WHERE     t.deptid IN (SELECT a.deptid ");
            sql.append("      FROM pcmc_user_dept a ");
            sql.append("   WHERE a.userid = ?)");
            sql.append("   AND t.userid = a.userid");
            sql.append("   AND t.userid != ?");
            bvals.add(userid);
            bvals.add(userid);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> userList = resultElement.getChildren("Record");
            for (int i = 0; i < userList.size(); i++) {
                Element rec = (Element) userList.get(i);
                UserVO userVO = new UserVO();
                userVO.setUserId(rec.getChildText("userid"));
                userVO.setUserCode(rec.getChildText("usercode"));
                userVO.setUserName(rec.getChildText("username"));
                userVO.setDeptId(rec.getChildText("deptid"));
                userVO.setDeptCode(rec.getChildText("deptcode"));
                userVO.setDeptName(rec.getChildText("deptname"));
                resultUserVOs.add(userVO);
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
        return resultUserVOs;
    }
}
