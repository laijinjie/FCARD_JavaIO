/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

/**
 * 命令返回值的存储接口
 *
 * @author 赖金杰
 */
public interface INCommandResult {

    /**
     * 用来释放资源，防止互相引用导致无法回收
     *
     */
    public void release();
}
