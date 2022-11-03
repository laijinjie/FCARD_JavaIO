/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 设置体温检测开关及体温格式
 *
 * @author F
 */
public class WriteFaceBodyTemperaturePar_Parameter extends CommandParameter {

    /**
     * 体温检测开关及格式 0--禁止；1--摄氏度（默认值）；2--华氏度
     */
    public int BodyTemperaturePar;

    public WriteFaceBodyTemperaturePar_Parameter(CommandDetail detail, int bodyTemperaturePar) {
        super(detail);
        BodyTemperaturePar = bodyTemperaturePar;
        CheckPar();
    }
    public WriteFaceBodyTemperaturePar_Parameter(int bodyTemperaturePar) {
        this(null, bodyTemperaturePar);
    }
    /**
     * 检测参数
     */
    private void CheckPar() {
        if (BodyTemperaturePar > 2 || BodyTemperaturePar < 0) {
            BodyTemperaturePar = 0;
        }
    }
}
