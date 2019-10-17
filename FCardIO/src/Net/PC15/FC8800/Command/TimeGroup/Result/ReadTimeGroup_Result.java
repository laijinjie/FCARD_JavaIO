/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.TimeGroup.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
import java.util.ArrayList;

/**
 *
 * @author 英泽电子
 */
public class ReadTimeGroup_Result <T> implements INCommandResult {

    /**
     * 读取到的卡片列表
     */
    public ArrayList<WeekTimeGroup> List;    
    
    /**
     * 读取到的卡片数量
     */
    public int DataBaseSize;
    @Override
    public void release() {
        List = null;
        
    }
    
}
