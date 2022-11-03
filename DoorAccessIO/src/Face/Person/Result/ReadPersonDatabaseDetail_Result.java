/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.Person.Result;

import Door.Access.Command.INCommandResult;

/**
 *控制器中的人员数据库信息
 * @author F
 */
public class ReadPersonDatabaseDetail_Result implements INCommandResult{
    /**
     * 人员档案最大容量
     */
    public long SortDataBaseSize;
    /**
     * 已存储人员数量
     */
    public long SortPersonSize;
    /**
     * 指纹特征码最大容量
     */
    public long SortFingerprintDataBaseSize;
    /**
     * 已存储指纹数量
     */
    public long SortFingerprintSize;
    /**
     * 人脸特征码最大容量
     */
    public long SortFaceDataBaseSize;
    /**
     * 已存储人脸数量
     */
    public long SortFaceSize;

    @Override
    public void release() {
        
    }
}
