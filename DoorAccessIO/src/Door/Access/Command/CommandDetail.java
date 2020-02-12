package Door.Access.Command;

import Door.Access.Connector.ConnectorDetail;
import java.util.Calendar;
import Door.Access.Connector.INConnectorEvent;

/**
 * 包含命令的执行时的一些必要信息，命令执行的连接器通道，命令身份验证信息，用户附加数据，超时重试参数
 *
 * @author 赖金杰
 */
public class CommandDetail {

    /**
     * 用来存储命令将要使用的连接器信息
     */
    public ConnectorDetail Connector;

    /**
     * 指示命令接收者的身份信息
     */
    public INIdentity Identity;

    /**
     * 作为命令的附加信息，供上层调用
     */
    public String Desc;

    /**
     * 作为命令的附加信息，供上层调用
     */
    public int ID;

    /**
     * 作为命令的附加信息，供上层调用
     */
    public String Name;

    /**
     * 作为命令的附加信息，供上层调用
     */
    public Object UserPar;

    /**
     * 命令执行完毕时间
     */
    public Calendar EndTime;

    /**
     * 起始时间
     */
    public Calendar BeginTime;

    /**
     * 监听此命令的事件回调，如果为Null 则需要监听 Allocator 类的事件回调
     */
    public INConnectorEvent Event;

    /**
     * 命令在通道中发送后的最大等待应答事件，单位毫秒
     */
    public int Timeout;

    /**
     * 当命令发生超时后，最大重试次数。
     */
    public int RestartCount;

    public CommandDetail() {
        Timeout = 500;//默认值是5秒
        RestartCount = 3;//默认重试3次
        init();
    }

    private void init() {
        Identity = null;
        Connector = null;
        Event = null;
        EndTime = null;
        BeginTime = null;
        UserPar = null;
        Desc = null;
        Name = null;
    }

    /**
     * 用来释放资源，防止互相引用导致无法回收
     *
     */
    public void release() {
        init();
    }

}
