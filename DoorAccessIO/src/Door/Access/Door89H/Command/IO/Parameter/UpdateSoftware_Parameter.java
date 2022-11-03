/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.IO.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 *
 * @author FCARD
 */
public class UpdateSoftware_Parameter extends CommandParameter {

    long SoftwareCRC32;
    byte[] File;

    public long getCrc32() {
        return SoftwareCRC32;
    }

    public UpdateSoftware_Parameter(CommandDetail detail, byte[] file) throws Exception {
        super(detail);
        checkParameter(file);
       // File = file;
    }

    private void checkParameter(byte[] file) throws Exception {
        //0-15 是 估计密钥信息，16-17 是固件版本 18-21 固件内容crc32， 最后4字节为整个文件的crc32
        int fileLen = file.length - 26;//实际固件内容长度
        long fileCRC = ByteUtil.ByteToLong(Arrays.copyOfRange(file, file.length - 4, file.length));//获取文件的CRC32
        byte[] softwareData = Arrays.copyOfRange(file, 22, file.length-4);//获取实际的固件内容
        long tmpCRC = ByteUtil.CreateCRC32(softwareData, 0, softwareData.length);
        if (tmpCRC != fileCRC) {
            throw new Exception("无效固件");
        }
        SoftwareCRC32 = ByteUtil.ByteToLong(Arrays.copyOfRange(file, 18, 22));//整个固件的crc32
        File=softwareData;
    }
    
     private void WriteFiel(byte [] buf,int off,int len, boolean isApped) throws IOException {
        BufferedOutputStream fw = new BufferedOutputStream(new FileOutputStream("D:\\E\\应用软件包\\111.RCBin", isApped));
        fw.write(buf,off,len);
        fw.close();
//         File faceFile = new File("D:\\E\\应用软件包\\111.RCBin");
//         byte[] faceData = Files.readAllBytes(faceFile.toPath());
//           long   tmpCRC2 = ByteUtil.CreateCRC32(faceData, 0, faceData.length);
//            System.out.println(File.length);
    }
    /**
     * 获取固件长度
     * @return 
     */
    public  int getSoftwareSize(){
        return File.length;
    }
       public  byte [] getSoftware(){
        return File;
    }
}
