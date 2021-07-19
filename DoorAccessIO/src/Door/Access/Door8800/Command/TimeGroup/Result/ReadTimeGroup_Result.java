/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.TimeGroup.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import java.util.ArrayList;

/**
 *
 * @author 英泽电子
 */
public class ReadTimeGroup_Result <T> implements INCommandResult {

    /**
     * 读取到的时段列表
     */
    public ArrayList<WeekTimeGroup> List;    
    
    /**
     * 读取到的时段数量
     */
    public int DataBaseSize;
    @Override
    public void release() {
        List = null;
        
    }
    
}
