/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Data;

import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;

/**
 *
 * @author Administrator
 */
public class BytesData implements INData{

    private ByteBuf _Buf;
    @Override
    public int GetDataLen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetBytes(ByteBuf data) {
        data.markReaderIndex();
        _Buf=ByteUtil.ALLOCATOR.buffer(data.readableBytes());
        _Buf.writeBytes(data);
        data.resetReaderIndex();
    }

    @Override
    public ByteBuf GetBytes() {
        return _Buf;
    }
    
}
