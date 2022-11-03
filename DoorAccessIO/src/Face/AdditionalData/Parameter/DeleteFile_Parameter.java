/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.AdditionalData.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 *
 * @author FCARD
 */
public class DeleteFile_Parameter extends CommandParameter {

    /**
     * 用户号
     */
    public int UserCode;
    /**
     * 人脸序号 长度为10的boolean集合 索引号为人脸文件序号
     *
     */
    public ArrayList<Boolean> FaceNums;
    /**
     * 指纹序号 长度为10的boolean集合 索引号为指纹序号
     */
    public ArrayList<Boolean> FingerprintNums;
    /**
     * 红外/动态人脸特征码的存储情况 true表示删除，false表示跳过
     */
    public Boolean FeatureNum;

    public DeleteFile_Parameter(CommandDetail detail, int UserCode, ArrayList<Boolean> FaceNums, ArrayList<Boolean> FingerprintNums, boolean FeatureNum) {
        super(detail);
        this.UserCode = UserCode;
        this.FaceNums = FaceNums;
        this.FingerprintNums = FingerprintNums;
        this.FeatureNum = FeatureNum;
        CheckParameter();
    }

    public void CheckParameter() {

        if (FaceNums != null) {
            if (FaceNums.size() > 5) {
                new Exception("人脸序号列表超出范围");
            }
        }
        if (FingerprintNums != null) {
            if (FingerprintNums.size() > 10) {
                new Exception("指纹序号列表超出范围");
            }
        }
        if (UserCode == 0) {
            new Exception("用户号错误");
        }
    }

    public void getBytes(ByteBuf buf) {
        byte[] bFaceNums = new byte[5];
        if (FaceNums != null) {
            int size = FaceNums.size();
            for (int i = 0; i < size; i++) {
                if (FaceNums.get(i)) {
                    bFaceNums[i] = 1;
                }
            }
        }
        byte[] bFingerprintNums = new byte[10];
        if (FingerprintNums != null) {
            int size = FingerprintNums.size();
            for (int i = 0; i < size; i++) {
                if (FingerprintNums.get(i)) {
                    bFingerprintNums[i] = 1;
                }
            }
        }
        buf.writeInt(UserCode);
        buf.writeBytes(bFaceNums);
        buf.writeBytes(bFingerprintNums);
        buf.writeBoolean(FeatureNum);
    }

    public int GetDataLen() {
        return 20;
    }
}
