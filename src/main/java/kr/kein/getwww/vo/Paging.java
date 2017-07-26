package kr.kein.getwww.vo;

public class Paging {
    private int pageNo;
    private int pageSize;
    private int startIdx;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIdx() {
        return (pageNo-1) * pageSize;
    }

    /*public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }
    */
}
