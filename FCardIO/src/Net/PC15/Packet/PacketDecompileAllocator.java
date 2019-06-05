/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Packet;

import Net.PC15.Command.INWatchResponse;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.FC8800.Command.FC8800CommandWatchResponse;
import Net.PC15.FC8800.Packet.FC8800Decompile;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令包解析器分配器
 *
 * @author 赖金杰
 */
public class PacketDecompileAllocator {

    private static final ConcurrentHashMap<String, Class<? extends INWatchResponse>> DecompileMap;

    static {
        DecompileMap = new ConcurrentHashMap<>();
        //初始化类型比较器
        DecompileMap.put(E_ControllerType.FC8800.getClass().getCanonicalName(), FC8800CommandWatchResponse.class);
        DecompileMap.put(E_ControllerType.FC8900.getClass().getCanonicalName(), FC8800CommandWatchResponse.class);
        DecompileMap.put(E_ControllerType.MC5800.getClass().getCanonicalName(), FC8800CommandWatchResponse.class);
    }

    public static INWatchResponse GetDecompile(E_ControllerType type) {
        if (type == null) {
            return null;
        }
        String sKey = type.getClass().getCanonicalName();
        if (DecompileMap.containsKey(sKey)) {
            INWatchResponse watch = null;
            try {
                watch = DecompileMap.get(sKey).newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
            }

            return watch;
        } else {
            return null;
        }

    }
}
