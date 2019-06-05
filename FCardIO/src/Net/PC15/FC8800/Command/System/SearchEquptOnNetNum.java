/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.SearchEquptOnNetNum_Parameter;
import Net.PC15.FC8800.Command.System.Result.SearchEquptOnNetNum_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 搜索控制器--搜索不是指定网络标记的控制器.<br/>
 * 成功返回结果参考 {@link SearchEquptOnNetNum_Result}<br/>
 * <h3>注意：此命令不会触发事件 {@link INConnectorEvent#CommandCompleteEvent(Net.PC15.Command.INCommand, Net.PC15.Command.INCommandResult)
 * }<br>
 * 此命令在搜索完毕后返回事件为 {@link INConnectorEvent#CommandTimeout(Net.PC15.Command.INCommand)
 * }
 * </h3>
 *
 *
 * @author 赖金杰
 */
public class SearchEquptOnNetNum extends FC8800Command {

    public SearchEquptOnNetNum(SearchEquptOnNetNum_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeShort(par.NetNum);
        CreatePacket(1, 0xFE, 0, 2, dataBuf);
        _Result = new SearchEquptOnNetNum_Result();
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xFE, 0)) {

            //设定返回值
            SearchEquptOnNetNum_Result ret = (SearchEquptOnNetNum_Result) _Result;

            String SN;
            byte[] ResultData=null;
            SN = model.GetSN();
            if (model.GetDataLen() > 0) {
                ByteBuf buf = model.GetDatabuff();
                ResultData = new byte[buf.readableBytes()];
                buf.readBytes(ResultData);
            }

            ret.AddSearchResult(SN, ResultData);
            //RaiseCommandCompleteEvent(oEvent);
            return false;
        } else {
            return false;
        }
    }

}
