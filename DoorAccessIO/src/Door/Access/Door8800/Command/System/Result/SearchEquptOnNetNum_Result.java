/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
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

        public SearchResult(String SN, byte[] ResultData) {
            this.SN = SN;
            this.ResultData = ResultData;
        }
        /**
         * 控制器SN
         */
        public String SN;

        /**
         * 控制器返回数据
         */
        public byte[] ResultData;
    }

    public SearchEquptOnNetNum_Result() {
        SearchTotal = 0;
    }

    public synchronized void AddSearchResult(String SN, byte[] ResultData) {

        if (ResultList == null) {
            ResultList = new ArrayList<>(10);
        }
        ResultList.add(new SearchResult(SN, ResultData));
        SearchTotal++;
    }

    @Override
    public void release() {
        return;
    }

}
