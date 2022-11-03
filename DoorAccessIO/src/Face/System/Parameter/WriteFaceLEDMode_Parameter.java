/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 补光灯模式参数
 *
 * @author F
 */
public class WriteFaceLEDMode_Parameter extends CommandParameter {

    /**
     * 补光灯模式 0--一直关; 1--一直亮;2--检测到人员时开;
     */
    public int LEDMode;

    /**
     * 补光灯模式参数
     *
     * @param detail
     * @param ledMode
     */
    public WriteFaceLEDMode_Parameter(CommandDetail detail, int ledMode) {
        super(detail);
        LEDMode = ledMode;
        CheckPar();
    }

    /**
     * 补光灯模式参数
     *
     * @param ledMode
     */
    public WriteFaceLEDMode_Parameter(int ledMode) {
        this(null, ledMode);
    }

    /**
     * 校验参数
     */
    private void CheckPar() {
        if (LEDMode < 0 || LEDMode > 2) {
            LEDMode = 0;
        }
    }
}
