
package Net.PC15.Command;

import Net.PC15.Connector.E_ControllerType;



/**
 * 用于验证命令身份权限的接口
 * @author 赖金杰
 */
public interface INIdentity {
    /**
     * 获取用于验证的密码
     * @return 密码
     */
    public String GetPassword();

    
    /**
     * 返回用来用于验证的字符串
     * @return 身份
     */
    public String GetIdentity();
    
    /**
     * 返回用于表示身份类型的整形值
     * @return 类型
     */
    public E_ControllerType GetIdentityType();
    
    /**
     * 用来和对方进行对比
     * @param dec
     * @return 
     */
    public boolean equals(INIdentity dec);
}
