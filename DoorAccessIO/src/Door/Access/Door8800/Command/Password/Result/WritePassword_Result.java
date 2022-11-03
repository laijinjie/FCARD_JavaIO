/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import java.util.ArrayList;

/**
 *
 * @author F
 */
public class WritePassword_Result implements INCommandResult {

    public int ErrorCount;
    
    public ArrayList<PasswordDetail> ErrorDetails;
    /**
     * 密码溢出
     */
    public int  OverflowCount;
    @Override
    public void release() {
        
    }
    
}
