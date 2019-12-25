package com.will.bluetoothprinterdemo.vo;

public class Product {
    private int id; //自增长
    private String orderId;
    private String name;
    private String color;
    private int numbers;
    private double price;

    public Product() {
    }

    public Product(int id, String orderId, String name, String color, int numbers, double price) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.color = color;
        this.numbers = numbers;
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", numbers=" + numbers +
                ", price=" + price +
                '}';
    }
}
