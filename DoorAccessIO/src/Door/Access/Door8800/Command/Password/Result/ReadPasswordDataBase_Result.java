/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password.Result;

import Door.Access.Command.INCommandResult;
import java.util.ArrayList;

/**
 *
 * @author F
 */
public class ReadPasswordDataBase_Result<T> implements INCommandResult {

    public ArrayList<T> PasswordDetails;
    public int DataBaseSize;

    @Override
    public void release() {

    }
}
