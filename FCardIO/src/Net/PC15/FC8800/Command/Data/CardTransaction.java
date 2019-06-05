/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 刷卡记录<br/>
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
 * @author 赖金杰
 */
public class CardTransaction extends AbstractTransaction {

    public CardTransaction() {
        _TransactionType = 1;
    }

    /**
     * 卡号
     */
    public long CardData;
    /**
     * 读卡器号
     */
    public short Reader;

    @Override
    public int GetDataLen() {
        return 13;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        try {
            if (data.readUnsignedByte() == 255) {
                _IsNull = true;
                //return;
            }
            CardData = data.readUnsignedInt();
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

    @Override
    public ByteBuf GetBytes() {
        return null;
    }

    /**
     * 获取门号
     *
     * @return 1-4 代表4个门
     */
    public short DoorNum() {
        switch (Reader) {
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
                return 2;
            case 5:
            case 6:
                return 3;
            case 7:
            case 8:
                return 4;
            default:
                return 0;

        }

    }

    /**
     * 是否为进门读卡
     *
     * @return true 进门读卡，false 出门读卡
     */
    public boolean IsEnter() {
        if (Reader == 0 || Reader > 8) {
            return false;
        }
        return Reader % 2 == 1;
    }

    /**
     * 是否为出门读卡
     *
     * @return true 出门读卡，false 进门读卡
     */
    public boolean IsExit() {
        return !IsEnter();
    }
}
