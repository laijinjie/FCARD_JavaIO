package Face.Person.Result;

import Door.Access.Command.INCommandResult;

import java.util.ArrayList;

/**
 * 添加人员返回值
 *
 * @author F
 */
public class Person_Result implements INCommandResult {

    /**
     * 上传失败数量
     */
    public int FailTotal;
    /**
     * 上传失败用户号列表
     */
    public ArrayList<Integer> UserCodeList;

    /**
     * 添加人员返回结果
     *
     * @param list 上传失败的用户号
     */
    public Person_Result(ArrayList<Integer> list) {
        UserCodeList = list;
        FailTotal = list.size();
    }

    @Override
    public void release() {

    }
}
