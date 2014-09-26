package com.david.yunfei.entities;

import java.io.Serializable;

/**
 * 返回结果
 */
public class PrdtResult implements Serializable{


    public String wh;
    public String unit;
    public String prd_no;
    public float qty;
    public String  prd_mark;

    public PrdtResult(  ) {

    }

    public String getWh() {
        return wh;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrd_no() {
        return prd_no;
    }

    public float getQty() {
        return qty;
    }

    public String getPrd_mark() {
        return prd_mark;
    }

    public void setWh(String wh) {
        this.wh = wh;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrd_no(String prd_no) {
        this.prd_no = prd_no;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public void setPrd_mark(String prd_mark) {
        this.prd_mark = prd_mark;
    }
}
