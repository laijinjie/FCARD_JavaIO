/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TCPDetail;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * 自动搜索指令的返回值
 *
 * @author 赖金杰
 */
public class SearchEquptOnNetNum_Result implements INCommandResult {

    /**
     * 搜索到的结果集
     */
    public int SearchTotal;
    /**
     * 搜索到的控制器结果集
     */
    public ArrayList<SearchResult> ResultList;

    public class SearchResult {

        public SearchResult(String SN, ByteBuf data) {
            this.SN = SN;
            this.TCP = new TCPDetail();
            this.TCP.SetBytes(data);
        }
        /**
         * 控制器SN
         */
        public String SN;

        /**
         * 控制器返回数据
         */
        public TCPDetail TCP;
    }

    public SearchEquptOnNetNum_Result() {
        SearchTotal = 0;
    }

    public synchronized void AddSearchResult(String SN, ByteBuf data) {

        if (ResultList == null) {
            ResultList = new ArrayList<>();
        }
        if(!ResultList.stream().filter(m -> m.SN.equals(SN)).findAny().isPresent()){
            ResultList.add(new SearchResult(SN, data));
            SearchTotal++;
        }
    }

    @Override
    public void release() {
        return;
    }

}
