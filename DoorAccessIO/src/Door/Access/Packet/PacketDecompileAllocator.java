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

    private static  ConcurrentHashMap<E_ControllerType, Class<? extends INWatchResponse>> DecompileMap;

    static {
        DecompileMap = new ConcurrentHashMap<>();
        //初始化类型比较器
        DecompileMap.put(E_ControllerType.Door8800, Door8800CommandWatchResponse.class);
        DecompileMap.put(E_ControllerType.Door8900, Door89HCommandWatchResponse.class);
        DecompileMap.put(E_ControllerType.Door5800, Door8800CommandWatchResponse.class);
        
    }

    public static INWatchResponse GetDecompile(E_ControllerType type) {
        if (type == null) {
            return null;
        }
        if (DecompileMap.containsKey(type)) {
            INWatchResponse watch = null;
            try {
                watch = DecompileMap.get(type).newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
            }
            return watch;
        } else {
            return null;
        }

    }
    public static void AddDecompile(E_ControllerType type, Class<? extends INWatchResponse> decompile){
     if (!DecompileMap.containsKey(type)) {
        DecompileMap.put(type, decompile);
     }
    }
}
