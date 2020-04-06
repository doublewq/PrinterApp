package com.will.bluetoothprinterdemo.vo;

public class ProductSingle {
    private int pID;
    private String pName;
    private String pColor;
    private Double pPrice;
    private String pImage;
    private int pSellNum;
    private int p_CID;

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public int getpSellNum() {
        return pSellNum;
    }

    public void setpSellNum(int pSellNum) {
        this.pSellNum = pSellNum;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpColor() {
        return pColor;
    }

    public void setpColor(String pColor) {
        this.pColor = pColor;
    }

    public Double getpPrice() {
        return pPrice;
    }

    public void setpPrice(Double pPrice) {
        this.pPrice = pPrice;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public int getP_CID() {
        return p_CID;
    }

    public void setP_CID(int p_CID) {
        this.p_CID = p_CID;
    }

    @Override
    public String toString() {
        return "Product{" +
                "pID=" + pID +
                ", pName='" + pName + '\'' +
                ", pColor='" + pColor + '\'' +
                ", pPrice=" + pPrice +
                ", pImage='" + pImage + '\'' +
                ", pSellNum=" + pSellNum +
                ", p_CID=" + p_CID +
                '}';
    }
}
