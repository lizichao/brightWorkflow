package cn.com.bright.masterReview.util;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.masterReview.api.GroupVO;

public abstract class GroupUtils {
	
	
    public static List<GroupVO> findRoomClassRoomVOs(String room_pid) {
        List<GroupVO> result = new ArrayList<GroupVO>();
        findRoomEach(room_pid, result);
        return result;
    }
    
    
    private static  List<GroupVO> findRoomEach(String room_pid,List<GroupVO> result) {
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");
            sqlBuf.append("SELECT DISTINCT t.*,");
         
            sqlBuf.append(" CASE WHEN a.room_id IS NOT NULL ");
            sqlBuf.append(" THEN 1 ");
            sqlBuf.append(" ELSE 0 ");
            sqlBuf.append(" END AS isParentRoom");
            sqlBuf.append(" FROM headmaster_group t LEFT JOIN headmaster_group a ON t.group_id = a.group_pid where t.group_pid=? ");

            bvals.add(room_pid);

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            List<Element> rlist = resultElement.getChildren("Record");
            if (resultElement.getChildren("Record").size() > 0) {
                for (int i = 0; i < rlist.size(); i++) {
                    Element rec = (Element) rlist.get(i);
                    String group_id = rec.getChildText("group_id");
                    String group_pid = rec.getChildText("group_pid");
                    String group_name = rec.getChildText("group_name");
                    
                    GroupVO groupVO = new GroupVO();
                    groupVO.setGroup_id(group_id);
                    groupVO.setGroup_pid(group_pid);
                    groupVO.setGroup_name(group_name);
                    
                    result.add(groupVO);
                    findRoomEach(group_pid, result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return result;
    }

    
    public static List<String> findRooms(String room_pid) {
        List<GroupVO> classRoomVOs = findRoomClassRoomVOs(room_pid);

        List<String> rooms = new ArrayList<String>();
        for (GroupVO groupVO : classRoomVOs) {
            rooms.add(groupVO.getGroup_id());
        }
        return rooms;
    }
	
	
}
