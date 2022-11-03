package Face.Person.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Face.Data.Person;

import java.util.ArrayList;

/**
 * 添加人员-参数
 *
 * @author F
 */
public class Person_Parameter extends CommandParameter {

    /**
     * 人员详情列表
     */
    public ArrayList<Person> PersonList;

    /**
     * 添加人员参数
     *
     * @param detail 命令详情
     * @param personList 人员详情列表
     */
    public Person_Parameter(CommandDetail detail, ArrayList<Person> personList) {
        super(detail);
        PersonList = personList;
    }
}
