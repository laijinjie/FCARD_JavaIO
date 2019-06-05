/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

/**
 * 门内人数限制
 * @author 赖金杰
 */
public class DoorLimit {
    public long GlobalLimit;
    public long DoorLimit[];
    public long GlobalEnter;
    public long DoorEnter[];
    
    public DoorLimit()
    {
        DoorLimit = new long[4];
        DoorEnter = new long[4];
    }
}
