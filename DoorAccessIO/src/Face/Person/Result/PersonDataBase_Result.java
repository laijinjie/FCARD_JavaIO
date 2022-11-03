package Face.Person.Result;

import Door.Access.Command.INCommandResult;
import Face.Data.Person;

import java.util.ArrayList;

/**
 * 从设备中读取所有已注册的人员信息-返回值
 *
 * @author F
 */
public class PersonDataBase_Result implements INCommandResult {

    /**
     * 人员列表
     */
    public ArrayList<Person> PersonList;
    /**
     * 人员数量
     */
    public int DataBaseSize;

    /**
     * 读取所有注册人员信息
     *
     * @param list 人员信息列表
     */
    public PersonDataBase_Result(ArrayList<Person> list) {
        PersonList = list;
        DataBaseSize = list.size();
    }

    @Override
    public void release() {

    }
}
