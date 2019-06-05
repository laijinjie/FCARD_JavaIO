/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector;

/**
 * 用来指示控制器类型
 * @author 赖金杰
 */
public enum E_ControllerType {
    /**
     * FC8900 和 FC8800
     */
    FC8900(0),
    FC8800(1),
    MC5800(2)
    ;
    
    private final int value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    E_ControllerType(int value) {
        this.value = value;
    }
    
    /**
     * 获取枚举值
     * @return 类型代码
     */
    public int getValue() {
        return value;
    }
}
