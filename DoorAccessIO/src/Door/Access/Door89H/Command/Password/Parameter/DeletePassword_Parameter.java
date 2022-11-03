/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Password.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door89H.Command.Data.PasswordDetail;
import java.util.ArrayList;

/**
 *
 * @author F
 */
public class DeletePassword_Parameter extends CommandParameter {

   public String[] PasswordsStrings;

    public DeletePassword_Parameter(CommandDetail detail, String[] passwordsStrings) {
        super(detail);
        PasswordsStrings = passwordsStrings;
    }

    public DeletePassword_Parameter(CommandDetail detail,String  passwordsString) {
        this(detail, new String[]{passwordsString});
    }
}