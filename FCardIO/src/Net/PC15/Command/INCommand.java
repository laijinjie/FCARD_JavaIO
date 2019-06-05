package Net.PC15.Command;

import Net.PC15.Connector.INConnectorEvent;

/**
 * 所有命令的抽象接口，一个命令包含一个或多个 INPacket
 *
 * @author 赖金杰
 */
public interface INCommand extends INCommandRuntime{

    /**
     * 总步骤数
     *
     * @return
     */
    public int getProcessMax();

    /**
     * 当前指令进度
     *
     * @return
     */
    public int getProcessStep();

    /**
     * 获取命令发送次数
     *
     * @return 每发一次就+1
     */
    public int getSendCount();



    /**
     * 获取此命令是否已超时
     *
     * @return true表示已超时
     */
    public boolean getIsTimeout();

    /**
     * 包含此命令的参数
     *
     * @return 命令参数
     */
    public INCommandParameter getCommandParameter();

    /**
     * 设置此命令相关参数
     *
     * @param par 命令参数
     */
    public void setCommandParameter(INCommandParameter par);

    /**
     * 命令返回结果
     *
     * @return 包含结果结构体
     */
    public INCommandResult getCommandResult();
    


}
