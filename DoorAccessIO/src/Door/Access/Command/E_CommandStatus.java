/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

/**
 * 指示命令的操作状态 等待发送、等待回应、处理完毕
 * @author 赖金杰
 */
public enum E_CommandStatus {
    /**
     * 命令准备就绪，等待发送
     */
    OnReady(0),
    /**
     * 命令已发送，等待回应
     */
    OnWaitResponse(1),
    /**
     * 命令已处理完毕
     */
    OnOver(2);

    private final int value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用

    E_CommandStatus(int value) {
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
