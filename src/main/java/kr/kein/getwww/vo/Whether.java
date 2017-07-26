package kr.kein.getwww.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.sql.Date;

public class Whether extends Paging{
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    private int idx;
    private String addr1;
    private String addr2;
    private String code1;
    private String tag_date;
    private float s02_val;
    private float co_val;
    private float o3_val;
    private float no2_val;
    private int pm10_val;
    private String addr;
    private float lat;
    private float lng;
    private Date regdate;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getTag_date() {
        return tag_date;
    }

    public void setTag_date(String tag_date) {
        this.tag_date = tag_date;
    }

    public float getS02_val() {
        return s02_val;
    }

    public void setS02_val(float s02_val) {
        this.s02_val = s02_val;
    }

    public float getCo_val() {
        return co_val;
    }

    public void setCo_val(float co_val) {
        this.co_val = co_val;
    }

    public float getO3_val() {
        return o3_val;
    }

    public void setO3_val(float o3_val) {
        this.o3_val = o3_val;
    }

    public float getNo2_val() {
        return no2_val;
    }

    public void setNo2_val(float no2_val) {
        this.no2_val = no2_val;
    }

    public int getPm10_val() {
        return pm10_val;
    }

    public void setPm10_val(int pm10_val) {
        this.pm10_val = pm10_val;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }
}
