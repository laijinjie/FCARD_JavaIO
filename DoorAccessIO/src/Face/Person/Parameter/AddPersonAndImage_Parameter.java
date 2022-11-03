package Face.Person.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Face.Data.IdentificationData;
import Face.Data.Person;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 添加人员及识别信息命令的参数
 *
 * @author F
 */
public class AddPersonAndImage_Parameter extends CommandParameter {

    /**
     * 人员详情
     */
    public Person PersonDetail;
    /**
     * 人员识别信息
     */
    public ArrayList<IdentificationData> IdentificationDatas;
    /**
     * 等待校验的时间，单位毫秒
     */
    public int WaitVerifyTime;
    /**
     * 如果发生照片重复消息时，是否等待重复详情，适用于人脸机固件版本4.24以上版本
     */
    public boolean WaitRepeatMessage;

    /**
     * 添加人员及识别信息命令的参数
     *
     * @param detail 通讯命令
     * @param personDetail 人员信息
     * @param identificationData 人员识别信息
     */
    public AddPersonAndImage_Parameter(CommandDetail detail, Person personDetail, IdentificationData identificationData) {

        this(detail, personDetail, new IdentificationData[]{identificationData});
    }

    /**
     * 添加人员及识别信息命令的参数
     *
     * @param detail 通讯命令
     * @param personDetail 人员信息
     * @param identificationDatas 人员识别信息
     */
    public AddPersonAndImage_Parameter(CommandDetail detail, Person personDetail, IdentificationData[] identificationDatas) {
        this(detail, personDetail, identificationDatas, false);
    }

    /**
     * 添加人员及识别信息命令的参数
     *
     * @param detail 通讯命令
     * @param personDetail 人员信息
     * @param identificationDatas 人员识别信息
     * @param bWaitRepeatMessage 是否等待重复详情回馈
     */
    public AddPersonAndImage_Parameter(CommandDetail detail, Person personDetail, IdentificationData[] identificationDatas, boolean bWaitRepeatMessage) {
        super(detail);
        PersonDetail = personDetail;
        IdentificationDatas = new ArrayList<IdentificationData>(Arrays.asList(identificationDatas));
        WaitVerifyTime = 6000;
        WaitRepeatMessage = bWaitRepeatMessage;
        checkedParameter();
    }

    /**
     * 参数校验
     */
    private void checkedParameter() {
        if (PersonDetail == null) {
            new Exception("PersonDetail is null");
        }
        if (PersonDetail.UserCode == 0 || PersonDetail.UserCode > Integer.MAX_VALUE) {
            new Exception("PersonDetail.EnterStatus Must be between 1 and " + Integer.MAX_VALUE);
        }
        if (PersonDetail.TimeGroup > 64 || PersonDetail.TimeGroup < 1) {
            new Exception("PersonDetail.EnterStatus Must be between 0 and 64");
        }
        if (PersonDetail.EnterStatus > 3 || PersonDetail.EnterStatus < 0) {
            new Exception("PersonDetail.EnterStatus Must be between 0 and 3");
        }
        if (PersonDetail.Expiry.getWeekYear() > 2099 || PersonDetail.Expiry.getWeekYear() < 2000) {
            new Exception("PersonDetail.Expiry year Must be between 2000 and 2099");
        }

        if (PersonDetail.Identity > 1 || PersonDetail.Identity < 0) {
            new Exception("PersonDetail.Identity Must be between 0 and 1");
        };
        if (PersonDetail.CardType > 1 || PersonDetail.CardType < 0) {

            new Exception("PersonDetail.CardType Must be between 0 and 1");
        };
        if (PersonDetail.CardStatus > 3 || PersonDetail.CardStatus < 0) {
            new Exception("IdentificationDatas Must be between 0 and 3");
        }

        if (IdentificationDatas == null) {
            new Exception("IdentificationDatas is null");
        }
    }

    /**
     * 设置数据包
     *
     * @param buf 数据包
     */
    public void setBytes(ByteBuf buf) {
        try {
            PersonDetail.setBytes(buf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据包
     *
     * @param buf 数据包
     */
    public void getBytes(ByteBuf buf) {
        try {
            PersonDetail.getBytes(buf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
