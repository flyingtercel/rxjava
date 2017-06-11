package us.mifeng.beans;

import java.util.List;

/**
 * Created by 黑夜之火 on 2017/4/3.
 */

public class TvInfo {
    private int error_code;
    private String reason;
    /**
     * channelName : CCTV-1 综合
     * pId : 1
     * rel : cctv1
     * url : http://tv.cntv.cn/live/cctv1
     */

    private List<ResultEntity> result;

    public int getError_code() { return error_code;}

    public void setError_code(int error_code) { this.error_code = error_code;}

    public String getReason() { return reason;}

    public void setReason(String reason) { this.reason = reason;}

    public List<ResultEntity> getResult() { return result;}

    public void setResult(List<ResultEntity> result) { this.result = result;}

    public static class ResultEntity {
        private String channelName;
        private int pId;
        private String rel;
        private String url;

        public String getChannelName() { return channelName;}

        public void setChannelName(String channelName) { this.channelName = channelName;}

        public int getPId() { return pId;}

        public void setPId(int pId) { this.pId = pId;}

        public String getRel() { return rel;}

        public void setRel(String rel) { this.rel = rel;}

        public String getUrl() { return url;}

        public void setUrl(String url) { this.url = url;}
    }
}
