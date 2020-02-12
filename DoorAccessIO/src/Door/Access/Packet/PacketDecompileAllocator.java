/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Packet;

import Door.Access.Command.INWatchResponse;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Door8800.Command.Door8800CommandWatchResponse;
import Door.Access.Door8800.Packet.Door8800Decompile;
import Door.Access.Door89H.Command.Door89HCommandWatchResponse;
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
        DecompileMap.put(E_ControllerType.Door8800.getClass().getCanonicalName(), Door8800CommandWatchResponse.class);
        DecompileMap.put(E_ControllerType.Door8900.getClass().getCanonicalName(), Door89HCommandWatchResponse.class);
        DecompileMap.put(E_ControllerType.Door5800.getClass().getCanonicalName(), Door8800CommandWatchResponse.class);
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
