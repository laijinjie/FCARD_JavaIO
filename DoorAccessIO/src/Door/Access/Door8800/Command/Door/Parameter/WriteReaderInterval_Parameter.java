/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 重复读卡间隔
 *
 * @author 赖金杰
 */
public class WriteReaderInterval_Parameter extends CommandParameter {

    /**
     * 门号<br>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否重复读卡间隔功能
     */
    public boolean Use;

    /**
     * 重复读卡时的记录响应模式<br>
     * *<ul>
     * <li>1 &emsp; 记录读卡，不开门，有提示   </li>
     * <li>2 &emsp; 不记录读卡，不开门，有提示 </li>
     * <li>3 &emsp; 不做响应，无提示           </li>
     * </ul>
     */
    public int RecordOption;

    public WriteReaderInterval_Parameter(CommandDetail detail, int door, boolean use, int recordOption) {
        super(detail);
        this.DoorNum = door;
        this.Use = use;
        this.RecordOption = recordOption;
    }

}
