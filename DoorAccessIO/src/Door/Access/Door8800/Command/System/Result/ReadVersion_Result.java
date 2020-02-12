/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 获取版本号的返回值
 * @author 赖金杰
 */
public class ReadVersion_Result  implements INCommandResult{
    /**
     * 控制器的版本号
     */
    public final String Version;
    public ReadVersion_Result(String ver){
        this.Version = ver;
    }
    @Override
    public void release() {
        return;
    }
    
}
