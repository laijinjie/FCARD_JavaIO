/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.TimeGroup.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import java.util.ArrayList;

/**
 * 设置开门时段参数
 * @author 徐铭康
 */
public class AddTimeGroup_Parameter extends CommandParameter {    

    public ArrayList<WeekTimeGroup> List;
    
    public AddTimeGroup_Parameter(CommandDetail detail,ArrayList<WeekTimeGroup> list) {
        super(detail);
        List = list;
    }
}
