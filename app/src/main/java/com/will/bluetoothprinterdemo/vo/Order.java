package com.will.bluetoothprinterdemo.vo;

public class Order {
    private int id;
    private String orderID;
    private String consumerName;
    private String consumerPhone;
    private int productNum;
    private double salary;
    private double hasPay;
    private String time;

    public Order() {
    }

    public Order(int id, String orderID, String consumerName, String consumerPhone, int productNum, double salary, double hasPay, String time) {
        this.id = id;
        this.orderID = orderID;
        this.consumerName = consumerName;
        this.consumerPhone = consumerPhone;
        this.productNum = productNum;
        this.salary = salary;
        this.hasPay = hasPay;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }

    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getHasPay() {
        return hasPay;
    }

    public void setHasPay(double hasPay) {
        this.hasPay = hasPay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderID='" + orderID + '\'' +
                ", consumerName='" + consumerName + '\'' +
                ", consumerPhone='" + consumerPhone + '\'' +
                ", productNum=" + productNum +
                ", salary=" + salary +
                ", hasPay=" + hasPay +
                ", time='" + time + '\'' +
                '}';
    }
}
