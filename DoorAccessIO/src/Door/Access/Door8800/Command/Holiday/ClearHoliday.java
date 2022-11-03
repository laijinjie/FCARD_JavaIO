/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 *
 * @author FCARD
 */
public class ClearHoliday extends Door8800Command {

    public ClearHoliday(CommandParameter par) {
        _Parameter=par;
        CreatePacket(0x04, 0x02);
    }

    @Override
    protected void Release0() {

    }
}
