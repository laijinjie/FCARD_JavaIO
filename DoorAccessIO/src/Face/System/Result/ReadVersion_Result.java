/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Result;

import Door.Access.Command.INCommandResult;

/**
 *读取版本号
 * @author F
 */
public class ReadVersion_Result implements INCommandResult {

    /**
     * 版本号
     */
    public String Version;
    /**
     * 指纹版本号
     */
    public String FingerprintVersion;
    /**
     * 人脸版本号
     */
    public String FaceVersion;

    @Override
    public void release() {
        return;
    }

   
}
