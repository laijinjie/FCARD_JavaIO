/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 *
 * @author F
 */
public class WriteFaceMouthmufflePar_Parameter extends CommandParameter {

    /**
     * 口罩识别开关 0--禁止；1--启用
     */
    public int Mouthmuffle;

    public WriteFaceMouthmufflePar_Parameter(CommandDetail detail, int mouthmuffle) {
        super(detail);
        Mouthmuffle = mouthmuffle;
        if (mouthmuffle > 1 || mouthmuffle < 0) {
            mouthmuffle = 1;
        }
        Mouthmuffle = mouthmuffle;
    }

    public WriteFaceMouthmufflePar_Parameter(int mouthmuffle) {
        this(null, mouthmuffle);
    }
}
