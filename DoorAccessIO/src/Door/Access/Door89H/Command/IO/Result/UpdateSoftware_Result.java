/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.IO.Result;

import Door.Access.Command.INCommandResult;

/**
 *
 * @author FCARD
 */
public class UpdateSoftware_Result implements INCommandResult {

    /**
     * 返回结果 1--校验成功 0--校验失败 255--写入失败
     */
    public byte Success;

    public UpdateSoftware_Result(byte success) {
        Success = success;
    }

    @Override
    public void release() {

    }

}
