/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

/**
 * 命令类型
 *
 * @author 赖金杰
 */
public enum E_CommandType {
    /**
     * 写SN
     */
    cmdWriteSN,
    /**
     * 读取SN
     */
    cmdReadSN,
    /**
     * 设置通讯密码
     */
    cmdWriteConnPassword,
    /**
     * 读取通讯密码
     */
    cmdReadConnPassword,
    /**
     * 清空通讯密码
     */
    cmdDeleteConnPassword,
    /**
     * 读取设备有效期
     */
    cmdReadEquptDeadline,
    /**
     * 设置设备有效期
     */
    cmdWriteEquptDeadline,
    /**
     * 读取设备版本号
     */
    cmdReadEquptVer,
    /**
     * 读取TCP信息
     */
    cmdGetTCPInfo,
    /**
     * 设置TCP信息
     */
    cmdSetTCPInfo,
    /**
     * 设置TCP的MAC地址
     */
    cmdSetTCPMAC,
    /**
     * 设置TCP的服务器信息
     */
    cmdSetTCPServer,
    /**
     * 设置消费卡参数
     */
    cmdSetIC,
    /**
     * 获取消费卡参数
     */
    cmdGetICPar,
    /**
     * 设置控卡参数
     */
    cmdSetCardManagePar,
    /**
     * 获取控卡参数
     */
    cmdGetCardManagePar,
    /**
     * 设置记录存储方式
     */
    cmdWriteRecordMode,
    /**
     * 获取记录存储方式
     */
    cmdReadRecordMode,
    /**
     * 设置消费价目表
     */
    cmdSetPrice,
    /**
     * 获取消费价目表
     */
    cmdGetPrice,
    /**
     * 开启监控
     */
    cmdBeginWatch,
    /**
     * 开启监控(广播)
     */
    cmdBeginWatch_Bro,
    /**
     * 关闭监控
     */
    cmdCloseWatch,
    /**
     * 关闭监控(广播)
     */
    cmdCloseWatch_Bro,
    /**
     * 获取监控状态
     */
    cmdReadWatchState,
    /**
     * 初始化
     */
    cmdIniEqupt,
    /**
     * 搜索不同网络标识设备
     */
    cmdSearchEquptOnNetNum,
    /**
     * 设置网络标识
     */
    cmdSetEquptNetNum,
    /**
     * 设置存储区内容
     */
    cmdWriteMemory,
    /**
     * 读取存储区内容
     */
    cmdReadMemory,
    /**
     * 读取时间
     */
    cmdReadTime,
    /**
     * 写入系统时间
     */
    cmdWriteTime,
    /**
     * 写入自定义时间
     */
    cmdWriteTimeDefine,
    /**
     * 读取时钟自修正
     */
    cmdReadAutoTimeRepair,
    /**
     * 写入时钟自修正
     */
    cmdWriteAutoTimeRepair,
    /**
     * 清空消费时段
     */
    cmdClearTimeGroup,
    /**
     * 获取消费时段
     */
    cmdGetTimeGroup,
    /**
     * 设置消费时段
     */
    cmdWriteTimeGroup,
    /**
     * 读取卡片存储信息
     */
    cmdReadCardInfo,
    /**
     * 清空所有名单
     */
    cmdClearCard,
    /**
     * 读取所有卡片
     */
    cmdGetCardList,
    /**
     * 获取单个卡信息
     */
    cmdGetCard,
    /**
     * 上传卡片
     */
    cmdWriteCardList,
    /**
     * 删除名单
     */
    cmdDeleteCardList,
    /**
     * 获取记录信息
     */
    cmdGetRecordInfo,
    /**
     * 清空所有记录
     */
    cmdClearAllRecord,
    /**
     * 清空单类记录
     */
    cmdClearRecordOnly,
    /**
     * 更新上传断点
     */
    cmdUpdateReocrdFlag,
    /**
     * 更新记录尾号
     */
    cmdUpdateReocrdEndFlag,
    /**
     * 读取记录
     */
    cmdReadRecordOnFlay,
    /**
     * 读取记录
     */
    cmdReadRecord,
    /**
     * 获取状态
     */
    cmdGetState,
    /**
     * 获取LED显示器参数
     */
    cmdGetLEDInfo,
    /**
     * 设置LED显示器参数
     */
    cmdSetLEDInfo,
    /**
     * 设置语音播报参数
     */
    cmdSetVoiceInfo,
    /**
     * 获取语音播报参数
     */
    cmdGetVoiceInfo,
    /**
     * 获取黑名单功能参数
     */
    cmdGetBlacklist,
    /**
     * 设置黑名单功能参数
     */
    cmdSetBlacklist,
    /**
     * 获取人员长期驻留报警参数
     */
    cmdGetOccupyAlarm,
    /**
     * 设置人员长期驻留报警参数
     */
    cmdSetOccupyAlarm,
    /**
     * 获取继电器自动化参数
     */
    cmdGetExtendRelayInfo,
    /**
     * 设置继电器自动化参数
     */
    cmdSetExtendRelayInfo,
    /**
     * 关闭报警
     */
    cmdCloseAlarm,
    /**
     * 获取读卡器参数
     */
    cmdGetReaderInfo,
    /**
     * 设置读卡器参数
     */
    cmdSetReaderInfo,
    /**
     * 获取继电器参数参数
     */
    cmdGetRelayInfo,
    /**
     * 设置继电器参数参数
     */
    cmdSetRelayInfo,
    /**
     * 打开继电器（开门）
     */
    cmdOpenRelay,
    /**
     * 关闭继电器(关门)
     */
    cmdCloseRelay,
    /**
     * 保持继电器输出（常开）
     */
    cmdHoldRelay,
    /**
     * 锁定继电器(锁定)
     */
    cmdLockRelay,
    /**
     * 解锁继电器(解锁)
     */
    cmdUnlockRelay,
    /**
     * 获取读卡距离
     */
    cmdGetReaderRange,
    /**
     * 设置读卡距离
     */
    cmdSetReaderRange,
    /**
     * 设置读卡器输出类型
     */
    cmdSetReaderOutput,
    /**
     * 获取读卡器输出类型
     */
    cmdGetReaderOutput,
    /**
     * 获取读卡间隔
     */
    cmdGetReaderInterval,
    /**
     * 设置读卡间隔
     */
    cmdSetReaderInterval,
    /**
     * 6、进出流判断时仅发送结果
     */
    cmdGetSendAdjudgeOnly,
    /**
     * 6、进出流判断时仅发送结果
     */
    cmdSetSendAdjudgeOnly,
    /**
     * 获取继电器输出保持时间
     */
    cmdGetRelayHoldTime,
    /**
     * 设置继电器输出保持时间
     */
    cmdSetRelayHoldTime,
    /**
     * 获取未注册卡报警功能
     */
    cmdGetInvalidCardAlarmOpt,
    /**
     * 设置未注册卡报警功能
     */
    cmdSetInvalidCardAlarmOpt,
    /**
     * 获取黑名单报警功能
     */
    cmdGetBalcklistAlarmOpt,
    /**
     * 设置黑名单报警功能
     */
    cmdSetBalcklistAlarmOpt,
    /**
     * 获取全卡开门
     */
    cmdGetRegFree,
    /**
     * 设置全卡开门
     */
    cmdSetRegFree,
    /**
     * 获取卡片周期参数
     */
    cmdGetCardWorkCycle,
    /**
     * 设置卡片周期参数
     */
    cmdSetCardWorkCycle,
    /**
     * 获取读卡器工作模式
     */
    cmdGetReaderWorkType,
    /**
     * 设置读卡器工作模式
     */
    cmdSetReaderWorkType,
    /**
     * 写排序卡
     */
    cmdWriteSortCardList,
    /**
     * 清空工作计数器
     */
    cmdClearWorkCounter,
    /**
     * tcp 服务端口绑定
     */
    cmdTCPListen,
    /**
     * 读取设备运行信息
     */
    cmdReadSystemState,
    /**
     * 设置物品检测功能开关
     */
    cmdSetReaderStateCheck,
    /**
     * 读取物品检测功能开关
     */
    cmdGetReaderStateCheck,
    /**
     * 读取柜锁单元参数
     */
    cmdGetReaderInfoAll,
    /**
     * 检查柜锁单元状态
     */
    cmdCheckReaderState,
    /**
     * 设置出厂日期
     */
    cmdSetCreateTime,
    /**
     * 获取出厂日期
     */
    cmdGetCreateTime,
    /**
     * CPU温度报警阈值
     */
    cmdGetCPUCriticalValue,
    /**
     * CPU温度报警阈值
     */
    cmdSetCPUCriticalValue,
    /**
     * 环境温度报警阈值
     */
    cmdGetAirCriticalValue,
    /**
     * 环境温度报警阈值
     */
    cmdSetAirCriticalValue,
    /**
     * 电压报警阈值
     */
    cmdGetVoltageCriticalValue,
    /**
     * 电压报警阈值
     */
    cmdSetVoltageCriticalValue,
    /**
     * 工作方式
     */
    cmdSetWorkType,
    /**
     * 营业状态
     */
    cmdSetBusinessType,
    /**
     * 设置IC卡扇区密码
     */
    cmdSetICPassword,
    /**
     * 读取IC卡扇区密码
     */
    cmdGetICPassword,
    /**
     * 设置控卡扇区密码
     */
    cmdSetICCheckPassword,
    /**
     * 读取控卡扇区密码
     */
    cmdGetICCheckPassword,
    /**
     * 设置系统菜单密码
     */
    cmdSetMenuPassword,
    /**
     * 读取系统菜单密码
     */
    cmdGetMenuPassword,
    /**
     * 解除菜单锁定
     */
    cmdMenuUnlock,
    /**
     * 设置系统管理密码
     */
    cmdSetAdminPassword,
    /**
     * 读取系统管理密码
     */
    cmdGetAdminPassword,
    /**
     * 设置控制器名称
     */
    cmdSetControllerName,
    /**
     * 读取控制器名称
     */
    cmdGetControllerName,
    /**
     * 设置显示标题
     */
    cmdSetDisplayTitle,
    /**
     * 读取显示标题
     */
    cmdGetDisplayTitle,
    /**
     * 设置显示短消息
     */
    cmdSetDisplayMessage,
    /**
     * 读取显示短消息
     */
    cmdGetDisplayMessage,
    /**
     * 设置供应商OEM信息
     */
    cmdSetOEMDetails,
    /**
     * 读取供应商OEM信息
     */
    cmdGetOEMDetails,
    /**
     * 设置消费时显示内容
     */
    cmdSetDisplayDetails,
    /**
     * 读取消费时显示内容
     */
    cmdGetDisplayDetails,
    /**
     * 设置消费机屏幕背光灯类型
     */
    cmdSetDisplayType,
    /**
     * 读取消费机屏幕背光灯类型
     */
    cmdGetDisplayType,
    /**
     * 设置消费报表统计时间点
     */
    cmdSetReportTime,
    /**
     * 读取消费报表统计时间点
     */
    cmdGetReportTime,
    /**
     * 设置U盘功能详情
     */
    cmdSetUSBDetails,
    /**
     * 读取U盘功能详情
     */
    cmdGetUSBDetails,
    /**
     * 设置票据打印功能
     */
    cmdSetSalesSlipMode,
    /**
     * 读取票据打印功能
     */
    cmdGetSalesSlipMode,
    /**
     * 设置票据打印内容定义
     */
    cmdSetSalesSlipDefined,
    /**
     * 读取票据打印内容定义
     */
    cmdGetSalesSlipDefined,
    /**
     * 设置消费时语音播报开关
     */
    cmdSetPOSBroadcast,
    /**
     * 读取消费时语音播报开关
     */
    cmdGetPOSBroadcast,
    /**
     * 设置开机语音
     */
    cmdSetStartupTone,
    /**
     * 读取开机语音
     */
    cmdGetStartupTone,
    /**
     * 设置蜂鸣器
     */
    cmdSetBuzzer,
    /**
     * 读取蜂鸣器
     */
    cmdGetBuzzer,
    /**
     * 设置WiFi账号密码
     */
    cmdSetWIFI,
    /**
     * 读取WiFi账号密码
     */
    cmdGetWIFI,
    /**
     * 读取WiFi状态
     */
    cmdGetWIFIStatus,
    /**
     * 设置消费机工作模式
     */
    cmdSetPOSWorkType,
    /**
     * 读取消费机工作模式
     */
    cmdGetPOSWorkType,
    /**
     * 设置定额消费规则
     */
    cmdSetFixedFeeRule,
    /**
     * 读取定额消费规则
     */
    cmdGetFixedFeeRule,
    /**
     * 设置消费限额
     */
    cmdSetPayLimit,
    /**
     * 读取消费限额
     */
    cmdGetPayLimit,
    /**
     * 设置消费密码限制
     */
    cmdSetPayPasswordLimit,
    /**
     * 读取消费密码限制
     */
    cmdGetPayPasswordLimit,
    /**
     * 设置临时变更定额
     */
    cmdSetChangeFixedFee,
    /**
     * 读取临时变更定额
     */
    cmdGetChangeFixedFee,
    /**
     * 设置撤销消费
     */
    cmdSetRefund,
    /**
     * 读取撤销消费
     */
    cmdGetRefund,
    /**
     * 设置IC卡账户
     */
    cmdSetWallet,
    /**
     * 读取IC卡账户
     */
    cmdGetWallet,
    /**
     * 设置补贴
     */
    cmdSetAutoSubsidy,
    /**
     * 读取补贴
     */
    cmdGetAutoSubsidy,
    /**
     * 设置折扣
     */
    cmdSetDiscount,
    /**
     * 读取折扣
     */
    cmdGetDiscount,
    /**
     * 设置积分
     */
    cmdSetIntegral,
    /**
     * 读取积分
     */
    cmdGetIntegral,
    /**
     * 设置计次卡规则
     */
    cmdSetTimesCard,
    /**
     * 读取计次卡规则
     */
    cmdGetTimesCard,
    /**
     * 设置订餐规则
     */
    cmdSetReservation,
    /**
     * 读取订餐规则
     */
    cmdGetReservation,
    /**
     * 读取LED显示内容
     */
    cmdGetLEDText,
    /**
     * 设置LED显示内容
     */
    cmdSetLEDText,
    /**
     * 读取恒温控制开关
     */
    cmdGetConstantTemperature,
    /**
     * 设置恒温控制开关
     */
    cmdSetConstantTemperature,
    /**
     * 读取菜单信息
     */
    cmdReadMenuInfo,
    /**
     * 清空菜单
     */
    cmdClearMenu,
    /**
     * 获取所有菜单
     */
    cmdGetMenuList,
    /**
     * 读取单个菜单
     */
    cmdGetMenu,
    /**
     * 添加菜单
     */
    cmdWriteMenuList,
    /**
     * 删除菜单
     */
    cmdDeleteMenuList,
    /**
     * 读取卡类信息
     */
    cmdReadCardTypeInfo,
    /**
     * 清空卡类
     */
    cmdClearCardType,
    /**
     * 获取所有卡类
     */
    cmdGetCardTypeList,
    /**
     * 获取单个卡类
     */
    cmdGetCardType,
    /**
     * 添加卡类
     */
    cmdWriteCardTypeList,
    /**
     * 删除卡类
     */
    cmdDeleteCardTypeList,
    /**
     * 防晕倒检测开关
     */
    cmdGetCheckTumble,
    /**
     * 防晕倒检测开关
     */
    cmdSetCheckTumble,
    /**
     * 设置TCP的服务器信息
     */
    cmdSetTCPServerEx,
    /**
     * 设置TCP的服务器信息
     */
    cmdGetTCPServerEx,
    /**
     * 读取订餐名单信息
     */
    cmdReadReservationListInfo,
    /**
     * 清空订餐名单
     */
    cmdClearReservationList,
    /**
     * 读取所有订餐名单
     */
    cmdGetReservationList,
    /**
     * 添加订餐名单
     */
    cmdWriteReservationList,
    /**
     * 上传照片
     */
    cmdUploadFile,
    /**
     * 离线时的保活间隔
     */
    cmdSetKeepAliveInterval,
    /**
     * 离线时的保活间隔
     */
    cmdGetKeepAliveInterval,
    /**
     * 无线485
     */
    cmdGetWireless485,
    /**
     * 无线485
     */
    cmdSetWireless485,
    /**
     * 读取Test按键
     */
    cmdGetTestButton,
    /**
     * 设置Test按键
     */
    cmdSetTestButton,
    /**
     * 读取数据转发开关
     */
    cmdGetTransmit,
    /**
     * 设置数据转发开关
     */
    cmdSetTransmit,
    /**
     * 读取内存缓冲池
     */
    cmdGetReceiveTimeout,
    /**
     * 设置内存缓冲池
     */
    cmdSetReceiveTimeout,
    //* Door8900功能函数
    /**
     * 读取所有系统参数
     */
    cmdReadSystemPar,
    /**
     * 键盘参数
     */
    cmdWriteKeyboard,
    /**
     * 键盘参数
     */
    cmdReadKeyboard,
    /**
     * 互锁参数
     */
    cmdWriteLockInteraction,
    /**
     * 互锁参数
     */
    cmdReadLockInteraction,
    /**
     * 消防报警参数
     */
    cmdWriteFireAlarm,
    /**
     * 消防报警参数
     */
    cmdReadFireAlarm,
    /**
     * 匪警报警参数
     */
    cmdWriteOpenAlarm,
    /**
     * 匪警报警参数
     */
    cmdReadOpenAlarm,
    /**
     * 读卡间隔时间参数
     */
    cmdWriteReaderIntervalTime,
    /**
     * 读卡间隔时间参数
     */
    cmdReadReaderIntervalTime,
    /**
     * 设置语音播报开关
     */
    cmdSetBroadcast,
    /**
     * 读取语音播报开关
     */
    cmdGetBroadcast,
    /**
     * 读卡器校验参数
     */
    cmdSetReaderCheckMode,
    /**
     * 读卡器校验参数
     */
    cmdReadReaderCheckMode,
    /**
     * 烟雾报警参数
     */
    cmdWriteSmogAlarmMode,
    /**
     * 烟雾报警参数
     */
    cmdReadSmogAlarmMode,
    /**
     * 门内人数上限参数
     */
    cmdWriteEnterDoorLimit,
    /**
     * 门内人数上限参数
     */
    cmdReadEnterDoorLimit,
    /**
     * 智能防盗主机参数
     */
    cmdWriteTheftAlarmPar,
    /**
     * 智能防盗主机参数
     */
    cmdReadTheftAlarmPar,
    /**
     * 防潜回参数
     */
    cmdWriteCheckInOutPar,
    /**
     * 防潜回参数
     */
    cmdReadCheckInOutPar,
    /**
     * 卡片到期提示参数
     */
    cmdWriteCardPeriodSpeak,
    /**
     * 卡片到期提示参数
     */
    cmdReadCardPeriodSpeak,
    /**
     * 定时读卡播报语音参数
     */
    cmdWriteReadCardSpeak,
    /**
     * 定时读卡播报语音参数
     */
    cmdReadReadCardSpeak,
    /**
     * 消防通知
     */
    cmdSendFireAlarm,
    /**
     * 解除消防报警
     */
    cmdCloseFireAlarm,
    /**
     * 消防报警状态
     */
    cmdReadFireAlarmState,
    /**
     * 烟雾通知
     */
    cmdSendSmogAlarm,
    /**
     * 解除烟雾报警
     */
    cmdCloseSmogAlarm,
    /**
     * 烟雾报警状态
     */
    cmdReadSmogAlarmState,
    /**
     * 防盗主机布防状态
     */
    cmdGetTheftAlarmState,
    /**
     * 防盗报警布防
     */
    cmdSetTheftFortify,
    /**
     * 防盗报警撤防
     */
    cmdSetTheftDisarming,
    /**
     * 防探测功能
     */
    cmdSetExploreLockMode,
    /**
     * 防探测功能
     */
    cmdGetExploreLockMode,
    /**
     * 485线路反接检测开关
     */
    cmdSetCheck485Line,
    /**
     * 485线路反接检测开关
     */
    cmdGetCheck485Line,
    /**
     * 有效期即将过期提醒时间
     */
    cmdSetCardDeadlineTipDay,
    /**
     * 有效期即将过期提醒时间
     */
    cmdGetCardDeadlineTipDay,
    /**
     * 门工作方式
     */
    cmdGetDoorWorkType,
    /**
     * 门工作方式
     */
    cmdSetDoorWorkType,
    /**
     * 定时锁定门参数
     */
    cmdGetAutoLockedPar,
    /**
     * 定时锁定门参数
     */
    cmdSetAutoLockedPar,
    /**
     * 胁迫报警密码
     */
    cmdGetAlarmPassword,
    /**
     * 胁迫报警密码
     */
    cmdSetAlarmPassword,
    /**
     * 门的防潜回参数
     */
    cmdWriteDoorCheckInOutPar,
    /**
     * 门的防潜回参数
     */
    cmdReadDoorCheckInOutPar,
    /**
     * 开门超时提示参数
     */
    cmdGetLongOpenAlarmPar,
    /**
     * 开门超时提示参数
     */
    cmdSetLongOpenAlarmPar,
    /**
     * 出门开关参数
     */
    cmdGetButtonPar,
    /**
     * 出门开关参数
     */
    cmdSetButtonPar,
    /**
     * 门磁报警参数
     */
    cmdGetDoorStateAlarmPar,
    /**
     * 门磁报警参数
     */
    cmdSetDoorStateAlarmPar,
    /**
     * 门内外同时读卡开门
     */
    cmdGetBothWayCheck,
    /**
     * 门内外同时读卡开门
     */
    cmdSetBothWayCheck,
    /**
     * 键盘管理功能（在键盘上发卡、删卡）
     */
    cmdGetKeyboardManage,
    /**
     * 键盘管理功能（在键盘上发卡、删卡）
     */
    cmdSetKeyboardManage,
    /**
     * 键盘管理密码，进入键盘编程模式的密码
     */
    cmdGetKeyboardManagePassword,
    /**
     * 键盘管理密码，进入键盘编程模式的密码
     */
    cmdSetKeyboardManagePassword,
    /**
     * 首卡开门参数
     */
    cmdGetFirstCheckMode,
    /**
     * 首卡开门参数
     */
    cmdSetFirstCheckMode,
    /**
     * 多卡开门检测模式参数
     */
    cmdGetMuchCardCheckMode,
    /**
     * 多卡开门检测模式参数
     */
    cmdSetMuchCardCheckMode,
    /**
     * 多卡开门工作方式
     */
    cmdGetMuchCardWorkType,
    /**
     * 多卡开门工作方式
     */
    cmdSetMuchCardWorkType,
    /**
     * 区域防潜回功能
     */
    cmdGetAreaCheckInOut,
    /**
     * 区域防潜回功能
     */
    cmdSetAreaCheckInOut,
    /**
     * 区域互锁功能
     */
    cmdGetAreaLockInteraction,
    /**
     * 区域互锁功能
     */
    cmdSetAreaLockInteraction,
    /**
     * 多卡开门AB组设置
     */
    cmdGetMuchCardABGroup,
    /**
     * 多卡开门AB组设置
     */
    cmdSetMuchCardABGroup,
    /**
     * 固定多卡开门组
     */
    cmdGetMuchCardFixedGroup,
    /**
     * 固定多卡开门组
     */
    cmdSetMuchCardFixedGroup,
    /**
     * 读取开门密码信息
     */
    cmdReadDoorPasswordInfo,
    /**
     * 清空开门密码
     */
    cmdClearDoorPassword,
    /**
     * 获取所有开门密码
     */
    cmdGetDoorPasswordList,
    /**
     * 上传开门密码
     */
    cmdWriteDoorPasswordList,
    /**
     * 删除开门密码
     */
    cmdDeleteDoorPasswordList,
    // * 2016年12月3日 消费机补贴名单

    /**
     * 获取补贴信息
     */
    cmdReadSubsidyListInfo,
    /**
     * 清空补贴信息
     */
    cmdClearSubsidyList,
    /**
     * 获取补贴名单
     */
    cmdGetSubsidyList,
    /**
     * 上传补贴名单
     */
    cmdWriteSubsidyList,
    /**
     * 2017年3月9日
     */
    /**
     * 将记录起始号，记录尾号，上传断点清零
     */
    cmdClearRecordBeginNum,
    /**
     * 重置记录起始号，上传断点，使其为记录尾号相等（即取记录尾号的值）。
     */
    cmdResetRecordBeginNum,
    /**
     * 更新记录起始号
     */
    cmdUpdateReocrdBeginNum,
    /**
     * 禁止使用飞利浦MF1卡，仅使用CPU卡兼容的MF1卡
     */
    cmdSetDisabledMifare,
    /**
     * 禁止使用飞利浦MF1卡，仅使用CPU卡兼容的MF1卡
     */
    cmdGetDisabledMifare,
    /**
     * 2017年4月28日
     */
    /**
     * 获取SD卡工作状态
     */
    cmdGetSDCardState,
    /**
     * 格式化SD卡
     */
    cmdFormatSDCard,
    /**
     * 获取SD卡容量
     */
    cmdGetSDCardSize,
    /**
     * 2017年6月2日
     */
    /**
     * 生成日期
     */
    cmdWriteCreateDate,
    /**
     * 生成日期
     */
    cmdReadCreateDate,
    /**
     * 自定义卡号
     */
    cmdGetICCardDataRule,
    /**
     * 自定义卡号
     */
    cmdSetICCardDataRule,
    /**
     * TTL输出参数
     */
    cmdSetTTLOutput,
    /**
     * TTL输出参数
     */
    cmdGetTTLOutput,
    /**
     * 更新记录尾号
     */
    cmdUpdateReocrdEndNum,
    /**
     * 2017年6月20日
     */
    /**
     * IC卡寻卡
     */
    cmdIC_SelectCard,
    /**
     * IC卡读扇区
     */
    cmdIC_ReadData,
    /**
     * IC卡写扇区
     */
    cmdIC_WriteData,
    /**
     * 激活触发蜂鸣器
     */
    cmdActiveBuzzer,
    /**
     * 控制LED灯
     */
    cmdActiveLED,
    /**
     * 2017年7月3日 自动测试架命令
     */
    /**
     * 电源打开
     */
    cmdPowerOn,
    /**
     * 电源关闭
     */
    cmdPowerOff,
    /**
     * LCD屏幕显示SN
     */
    cmdLCD_ShowSN,
    /**
     * LCD屏幕显示状态
     */
    cmdLCD_SetStatus,
    /**
     * 开始测试
     */
    cmdStartTest,
    /**
     * 停止测试
     */
    cmdStopTest,
    /**
     * 获取电压数据
     */
    cmdGetVoltage,
    /**
     * 获取温度数据
     */
    cmdGetTemperature,
    /**
     * 开始检测系统LED状态--异步汇报
     */
    cmdStartCheck_SystemLED,
    /**
     * 获取读卡器电源状态
     */
    cmdGetReaderPowr,
    /**
     * 开始检测读卡器Beep状态 -- 异步汇报
     */
    cmdStartCheck_ReaderBeep,
    /**
     * 获取继电器状态
     */
    cmdGetRelayStatus,
    /**
     * 获取LED灯状态
     */
    cmdGetLEDStatus,
    /**
     * 开始检测音源--异步汇报
     */
    cmdStartCheck_SoundStatus,
    /**
     * 发送韦根信号
     */
    cmdSendWGData,
    /**
     * 打开扩展继电器
     */
    cmdOpenRelayExpand,
    /**
     * 发送UDP数据包
     */
    cmdSendUDPPack;

    private static int CodeIndex = 0;
    private final int value;

    private static int GetNextCode() {
        int r = CodeIndex;
        CodeIndex++;
        return r;
    }

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    E_CommandType() {
        value = E_CommandType.GetNextCode();
    }

    /**
     * 获取枚举值
     *
     * @return 类型代码
     */
    public int getValue() {
        return value;
    }
}
