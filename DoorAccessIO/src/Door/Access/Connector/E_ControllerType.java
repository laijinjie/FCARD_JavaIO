/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector;

/**
 * 用来指示控制器类型
 * @author 赖金杰
 */
public enum E_ControllerType {
    /**
     * Door8900 和 Door8800
     */
    Door8900(0),
    Door8800(1),
    Door5800(2),
    Face_Fingerprint(3)
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
