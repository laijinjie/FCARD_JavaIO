/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.AdditionalData.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 写入头像照片\指纹\人脸特征码参数
 *
 * @author F
 */
public class WriteFeatureCode_Parameter extends CommandParameter {

    /**
     * 用户号或者记录序号
     */
    private long _UserCode;
    /**
     * 文件类型
     */
    private int _Type;
    /**
     * 序号
     */
    private int _SerialNumber;
    /**
     * 待写入数据
     */
    private byte[] _Datas;
    /**
     * 等待校验的时间，单位毫秒
     */
    private int _WaitVerifyTime;

    /**
     *
     * @param detail 通讯参数
     * @param userCode 用户号或者记录序号
     * @param type 文件类型
     * @param serialNumber 序号
     * @param data 待写入数据
     */
    public WriteFeatureCode_Parameter(CommandDetail detail, long userCode, int type, int serialNumber, byte[] data) {
        super(detail);
        _UserCode = userCode;
        _Type = type;
        _SerialNumber = serialNumber;
        _Datas = data;
    }

    /**
     * 获取用户号
     *
     * @return 用户号
     */
    public long getUserCode() {
        return _UserCode;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    public int getType() {
        return _Type;
    }

    /**
     * 获取序号
     *
     * @return 序号
     */
    public int getSerialNumber() {
        return _SerialNumber;
    }

    /**
     * 获取文件内容
     *
     * @return 文件内容
     */
    public byte[] getDatas() {
        return _Datas;
    }

    /**
     * 等待校验的时间，单位毫秒
     *
     * @return 校验的时间
     */
    public int getWaitVerifyTime() {
        return _WaitVerifyTime;
    }
}
