/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
/**
 * 读取控制器通讯密码的返回值类
 * @author 赖金杰
 */
public class ReadConnectPassword_Result implements INCommandResult{
    /**
     * 控制器的通讯密码
     */
    public final String Password;
    public ReadConnectPassword_Result(String pwd){
        this.Password = pwd;
    }
    @Override
    public void release() {
        return;
    }
    
}
