/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import java.util.ArrayList;

/**
 *写入开门密码
 * @author F
 */
public class WritePassword_Parameter<T extends Door.Access.Door8800.Command.Data.PasswordDetail> extends CommandParameter {
    public ArrayList<T> passwordDetails;

    public WritePassword_Parameter(CommandDetail detail,ArrayList<T> arrayList) {
        super(detail);
        passwordDetails=arrayList;
    }
      public WritePassword_Parameter(CommandDetail detail,T passwordDetail) {
        super(detail);
        passwordDetails=new ArrayList<>();
        passwordDetails.add(passwordDetail);
    }
}
