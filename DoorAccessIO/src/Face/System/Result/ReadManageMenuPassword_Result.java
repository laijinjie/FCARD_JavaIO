package Face.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 读取管理密码
 */
public class ReadManageMenuPassword_Result  implements INCommandResult {
    /**
     * 管理密码
     */
   public String Password;

    /**
     * 初始化
     * @param password
     */
   public  ReadManageMenuPassword_Result(String password){
       Password=password;
   }
    @Override
    public void release() {

    }
}
