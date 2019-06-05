/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

/**
 * 星期的枚举
 * @author 赖金杰
 */
public enum E_WeekDay {
    /**
     * 星期一
     */
    Monday(0),
    /**
     * 星期二
     */
    Tuesday(1),
    /**
     * 星期三
     */
    Wednesday(2),
    /**
     * 星期四
     */
    Thursday(3),
    /**
     * 星期五
     */
    Friday(4),
    /**
     * 星期六
     */
    Saturday(5),
    /**
     * 星期日
     */
    Sunday(6);

    private final int value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用

    E_WeekDay(int value) {
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
