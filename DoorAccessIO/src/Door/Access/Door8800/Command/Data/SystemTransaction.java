/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 系统记录<br/>
 * TransactionCode 事件代码含义表：<br/>
 * <ul>
 * <li>1 &emsp; 系统加电                         </li>
 * <li>2 &emsp; 系统错误复位（看门狗）           </li>
 * <li>3 &emsp; 设备格式化记录                   </li>
 * <li>4 &emsp; 系统高温记录，温度大于>75        </li>
 * <li>5 &emsp; 系统UPS供电记录                  </li>
 * <li>6 &emsp; 温度传感器损坏，温度大于>100     </li>
 * <li>7 &emsp; 电压过低，小于<09V               </li>
* <
 * li>8 &emsp; 电压过高，大于>14V               </li>
 * <li>9 &emsp; 读卡器接反。                     </li>
 * <li>10 &emsp; 读卡器线路未接好。              </li>
 * <li>11 &emsp; 无法识别的读卡器                </li>
 * <li>12 &emsp; 电压恢复正常，小于14V，大于9V   </li>
 * <li>13 &emsp; 网线已断开                      </li>
 * <li>14 &emsp; 网线已插入                      </li>
 * </ul>
 *
 * @author 赖金杰
 */
public class SystemTransaction extends AbstractTransaction {

    public SystemTransaction() {
        _TransactionType = 6;
    }

    @Override
    public int GetDataLen() {
        return 8;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        try {
            short code = data.readUnsignedByte();
            if (code == 255) {
                _IsNull = true;
                //return;
            }
            _TransactionCode = code;
            byte[] btTime = new byte[6];
            data.readBytes(btTime, 0, 6);
            if (ByteUtil.uByte(btTime[0]) == 255) {
                _IsNull = true;
                //return;
            }
            _TransactionDate = TimeUtil.BCDTimeToDate_ssmmhhddMMyy(btTime);
            data.readUnsignedByte();//占位
            
            if (_TransactionCode == 0) {
                _IsNull = true;
            }
        } catch (Exception e) {
        }

        return;
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }

}
