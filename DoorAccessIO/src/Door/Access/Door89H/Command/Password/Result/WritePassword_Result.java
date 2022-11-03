/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Password.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door89H.Command.Data.PasswordDetail;
import java.util.ArrayList;

/**
 *写入开门密码返回结果
 * @author F
 */
public class WritePassword_Result implements INCommandResult {

    public int ErrorCount;
    
    public ArrayList<PasswordDetail> ErrorDetails;
    
    @Override
    public void release() {
        
    }
    
}