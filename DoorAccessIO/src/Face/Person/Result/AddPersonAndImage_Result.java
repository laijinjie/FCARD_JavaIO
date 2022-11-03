package Face.Person.Result;

import Door.Access.Command.INCommandResult;
import Face.Data.IdentificationData;
import Face.Data.Person;

import java.util.ArrayList;

/**
 * 添加人员识别信息返回结果
 * @author F
 */
public class AddPersonAndImage_Result implements INCommandResult {
    /**
     * 用户号
     */
    public long UserCode;
    /**
     * 用户上传状态
     */
    public boolean UserUploadStatus;
    /**
     * 识别信息上传状态 1--上传完毕；2--特征码无法识别；3--人员照片不可识别；4--人员照片或特征码重复
     */
    public ArrayList<Integer>IdDataUploadStatus;
    /**
     *  识别信息重复的用户号
     */
    public ArrayList<Long> IdDataRepeatUser ;
    /**
     * 添加人员识别信息返回结果
     * @param person
     * @param datas 
     */
    public  AddPersonAndImage_Result(Person person, ArrayList<IdentificationData> datas){
        UserCode = person.UserCode;
        IdDataRepeatUser =new ArrayList<Long>();
        IdDataUploadStatus=new ArrayList<Integer>();
        for (IdentificationData data : datas) {
            IdDataRepeatUser.add((long) 0);
            IdDataUploadStatus.add(0);
        }
    }
    @Override
    public void release() {

    }
}
