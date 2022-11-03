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
 *
 * @author F
 */
public class DeletePassword_Parameter extends CommandParameter {

   public ArrayList<PasswordDetail> passwordDetails;

    public DeletePassword_Parameter(CommandDetail detail, ArrayList<PasswordDetail> list) {
        super(detail);
        passwordDetails = list;
    }

    public DeletePassword_Parameter(CommandDetail detail,PasswordDetail pdetail) {
        super(detail);
        ArrayList<PasswordDetail> list = new ArrayList<>();
        list.add(pdetail);
        passwordDetails = list;
    }
}
