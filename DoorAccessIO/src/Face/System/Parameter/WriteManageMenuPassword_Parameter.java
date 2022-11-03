package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 写入管理密码
 */
public class WriteManageMenuPassword_Parameter extends CommandParameter {

    /**
     * 管理密码
     */
    public String Password;

    /**
     * 初始化
     * @param detail 通讯参数
     * @param password 管理密码
     */
    public WriteManageMenuPassword_Parameter(CommandDetail detail,String password) {
        super(detail);
        if(password==null){
            password="FFFFFFFF";
        }
        if(password.length()>8){
            throw new UnsupportedOperationException("password.length > 8");
        }
        if(password.length()<8){
            password=padRight(password,8,'F');
        }
        Password=  password;
    }
    private  String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }
        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for ( int i=src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }
}
