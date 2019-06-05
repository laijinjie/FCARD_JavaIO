/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector;

/**
 * 文件连接器，将命令发送到一个文件上。
 * @author 赖金杰
 */
public class FileDetail extends ConnectorDetail {
    /**
     * 命令将要输出到的路径和文件名
     */
    public String File;
    
    public FileDetail(String fileString)
    {
        File=fileString;
    }
    
    @Override
    public E_ConnectorType GetConnectorType() {
        return E_ConnectorType.OnFile;
    }
}
