# FCARD java 通讯库

#### 介绍
{**以下是码云平台说明，您可以替换此简介**
码云是 OSCHINA 推出的基于 Git 的代码托管平台（同时支持 SVN）。专为开发者提供稳定、高效、安全的云端软件开发协作平台
无论是个人、团队、或是企业，都能够用码云实现代码托管、项目管理、协作开发。企业项目请看 [https://gitee.com/enterprises](https://gitee.com/enterprises)}

#### 2022年11月3日
1. Linux下搜索设备的时候，绑定UDP，不能绑定本地ip

   ~~~ java
   CommandDetail dt = new CommandDetail();
   		//linux 下 _LocalIP 要写 null或""
           UDPDetail udp = new UDPDetail("255.255.255.255", _RemotePort, _LocalIP, _LocalPort);
   
   
           //首先打开UDP端口绑定
           _Allocator.UDPBind(_LocalIP, _LocalPort);
   
           dt.Connector = udp;
           dt.Identity = new Door8800Identity("0000000000000000", Door8800Command.NULLPassword, E_ControllerType.Door8900);
   
           Random rnd = new Random();
           int max = 65535;
           int min = 10000;
   
           dt.RestartCount = 0;
           dt.Timeout = 6000;//每隔5秒发送一次，所以这里设定5秒超时
   
           //网络标记就是一个随机数
           SearchNetFlag = rnd.nextInt(max) % (max - min + 1) + min;//网络标记
   
           SearchTimes = 1;//搜索次数;
   
           SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
           SearchEquptOnNetNum cmd = new SearchEquptOnNetNum(par);
           _Allocator.AddCommand(cmd);
   ~~~

   

6. 