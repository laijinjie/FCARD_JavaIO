/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card.Result;
import Net.PC15.Command.INCommandResult;
/**
 * 控制器中的卡片数据库信息
 * @author 赖金杰
 */
public class ReadCardDatabaseDetail_Result implements INCommandResult {


    /**
     * 排序数据区容量上限
     */
    public long SortDataBaseSize;
    
    /**
     * 排序数据区已使用数量
     */
    public long SortCardSize;
    
    
    /**
     * 顺序存储区容量上限
     */
    public long SequenceDataBaseSize;
    
    /**
     *顺序存储区已使用数量
     */
    public long SequenceCardSize;
    

    public ReadCardDatabaseDetail_Result() {
    }

    @Override
    public void release() {
        return;
    }
    
}
