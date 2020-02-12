/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;
import Door.Access.Command.INCommandResult;
/**
 * 有效期即将过期提醒时间
 * @author 赖金杰
 */
public class ReadCardDeadlineTipDay_Result implements INCommandResult{
    /**
     * <p>有效期即将过期提醒时间</p>
     * 取值范围: 1-255。0--表示关闭;默认值是30天
     */
    public short Day;
    
    @Override
    public void release() {
        return;
    }
    
    
}
