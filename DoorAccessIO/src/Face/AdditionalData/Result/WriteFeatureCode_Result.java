/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.AdditionalData.Result;

import Door.Access.Command.INCommandResult;

/**
 * 写入人脸指纹特征返回结果
 *
 * @author F
 */
public class WriteFeatureCode_Result implements INCommandResult {

    /**
     * 文件句柄
     */
    public long FileHandle;

    /**
     * /// 写入结果 /// 1--校验成功 //0--校验失败 //2--特征码无法识别 //3--人员照片不可识别 //255-文件未准备就绪
     */
    public int Success;

    @Override
    public void release() {
        return;
    }

}
