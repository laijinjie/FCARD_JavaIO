/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

/**
 * 命令参数
 * @author 赖金杰
 */
public interface INCommandParameter {
    /**
     * 获取命令通讯连接指示内容
     *
     * @return 表示命令所使用的连接器和命令身份信息
     */
    public CommandDetail getCommandDetail();
    
    /**
     * 设置连接器信息和身份信息
     * @param detail 包含连接器信息和身份信息
     */
    public void setCommandDetail(CommandDetail detail);
}
