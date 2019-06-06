/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Data;

import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 针对FC89H使用，刷卡记录<br/>
 * TransactionCode 事件代码含义表：<br/>
 * <ul>
 * <li>1 &emsp; 合法开门                                                         </li>
 * <li>2 &emsp; 密码开门------------卡号为密码                                   </li>
 * <li>3 &emsp; 卡加密码                                                         </li>
 * <li>4 &emsp; 手动输入卡加密码                                                 </li>
 * <li>5 &emsp; 首卡开门                                                         </li>
 * <li>6 &emsp; 门常开 --- 常开工作方式中，刷卡进入常开状态                   </li>
 * <li>7 &emsp; 多卡开门 -- 多卡验证组合完毕后触发                             </li>
 * <li>8 &emsp; 重复读卡                                                         </li>
 * <li>9 &emsp; 有效期过期                                                       </li>
 * <li>10 &emsp; 开门时段过期                                                    </li>
 * <li>11 &emsp; 节假日无效                                                      </li>
 * <li>12 &emsp; 未注册卡                                                        </li>
 * <li>13 &emsp; 巡更卡 -- 不开门                                              </li>
 * <li>14 &emsp; 探测锁定                                                        </li>
 * <li>15 &emsp; 无有效次数                                                      </li>
 * <li>16 &emsp; 防潜回                                                          </li>
 * <li>17 &emsp; 密码错误------------卡号为错误密码                              </li>
 * <li>18 &emsp; 密码加卡模式密码错误----卡号为卡号。                            </li>
 * <li>19 &emsp; 锁定时(读卡)或(读卡加密码)开门                                  </li>
 * <li>20 &emsp; 锁定时(密码开门)                                                </li>
 * <li>21 &emsp; 首卡未开门                                                      </li>
 * <li>22 &emsp; 挂失卡                                                          </li>
 * <li>23 &emsp; 黑名单卡                                                        </li>
 * <li>24 &emsp; 门内上限已满，禁止入门。                                        </li>
 * <li>25 &emsp; 开启防盗布防状态(设置卡)                                        </li>
 * <li>26 &emsp; 撤销防盗布防状态(设置卡)                                        </li>
 * <li>27 &emsp; 开启防盗布防状态(密码)                                          </li>
 * <li>28 &emsp; 撤销防盗布防状态(密码)                                          </li>
 * <li>29 &emsp; 互锁时(读卡)或(读卡加密码)开门                                  </li>
 * <li>30 &emsp; 互锁时(密码开门)                                                </li>
 * <li>31 &emsp; 全卡开门                                                        </li>
 * <li>32 &emsp; 多卡开门--等待下张卡                                            </li>
 * <li>33 &emsp; 多卡开门--组合错误                                              </li>
 * <li>34 &emsp; 非首卡时段刷卡无效                                              </li>
 * <li>35 &emsp; 非首卡时段密码无效                                              </li>
 * <li>36 &emsp; 禁止刷卡开门 -- 【开门认证方式】验证模式中禁用了刷卡开门时    </li>
 * <li>37 &emsp; 禁止密码开门 -- 【开门认证方式】验证模式中禁用了密码开门时    </li>
 * <li>38 &emsp; 门内已刷卡，等待门外刷卡。（门内外刷卡验证）                    </li>
 * <li>39 &emsp; 门外已刷卡，等待门内刷卡。（门内外刷卡验证）                    </li>
 * <li>40 &emsp; 请刷管理卡(在开启管理卡功能后提示)(电梯板)                      </li>
 * <li>41 &emsp; 请刷普通卡(在开启管理卡功能后提示)(电梯板)                      </li>
 * <li>42 &emsp; 首卡未读卡时禁止密码开门。                                      </li>
 * <li>43 &emsp; 控制器已过期_刷卡                                               </li>
 * <li>44 &emsp; 控制器已过期_密码                                               </li>
 * <li>45 &emsp; 合法卡开门—有效期即将过期                                      </li>
 * <li>46 &emsp; 拒绝开门--区域反潜回失去主机连接。                              </li>
 * <li>47 &emsp; 拒绝开门--区域互锁，失去主机连接                                </li>
 * <li>48 &emsp; 区域防潜回--拒绝开门                                            </li>
 * <li>49 &emsp; 区域互锁--有门未关好，拒绝开门                                  </li>
 * </ul>
 *
 * @author 徐铭康
 */
public class CardTransaction extends Net.PC15.FC8800.Command.Data.CardTransaction{
    
    /**
     * 卡号
     */
    public String CardDataHEX;
    public String CardDataStr;
    @Override
    public void SetBytes(ByteBuf data) {
        try {
            data.readByte();
            /*
            if (data.readUnsignedByte() == 255) {
                _IsNull = true;
                //return;
            }
            
            byte[] btCardData = new byte[9];
            data.readBytes(btCardData, 0, 9);
            byte[] btCardData = new byte[37];
            data.readBytes(btCardData, 0, 37);
            */
            //CardData = data.readUnsignedInt();
            byte[] btCardData = new byte[8];
            data.readBytes(btCardData, 0, 8);
            CardDataHEX = ByteUtil.ByteToHex(btCardData);
            CardDataStr = Net.PC15.Util.StringUtil.LTrim(CardDataHEX,'0');
            //CardData = Integer.parseInt(CardDataHEX,10);
            //CardDataHEX = Net.PC15.Util.StringUtil.HexStr2Str(CardDataHEX,16);
           
            //CardData = Long.valueOf(CardDataHEX);
            
            byte[] btTime = new byte[6];
            data.readBytes(btTime, 0, 6);
            _TransactionDate = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
            Reader = data.readUnsignedByte();
            _TransactionCode = data.readUnsignedByte();
            if (_TransactionCode == 0 || Reader == 0 || Reader > 8 || _TransactionDate == null) {
                _IsNull = true;
            }
        } catch (Exception e) {
        }

        return;
    }

}
