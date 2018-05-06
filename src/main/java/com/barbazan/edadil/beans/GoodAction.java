package com.barbazan.edadil.beans;

import java.io.Serializable;
import java.util.Date;

public class GoodAction implements Serializable {

    private int id;
    private GoodPrice goodPrice;
    private Date startDate;
    private Date endDate;
    private Float discount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GoodPrice getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(GoodPrice goodPrice) {
        this.goodPrice = goodPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }
}
