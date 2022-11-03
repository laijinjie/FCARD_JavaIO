package Face.Person.Result;

import Door.Access.Command.INCommandResult;
import Face.Data.Person;
import io.netty.buffer.ByteBuf;

/**
 * 查询人员数据详情
 * @author F
 */
public class ReadPersonDetail_Result implements INCommandResult {

    /**
     * 人员是否存在
     */
    public boolean isReady;
    /**
     * 人员详情信息
     */
    public Person person;
    @Override
    public void release() {

    }
}
