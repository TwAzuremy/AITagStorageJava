package com.example.aitagstorage.entity;

public class Tags {
    private String table;
    private int id;
    private String title;
    private String front;
    private String reverse;
    private String remark;
    private String preImg;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPreImg() {
        return preImg;
    }

    public void setPreImg(String preImg) {
        this.preImg = preImg;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "table='" + table + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", front='" + front + '\'' +
                ", reverse='" + reverse + '\'' +
                ", remark='" + remark + '\'' +
                ", preImg='" + preImg + '\'' +
                '}';
    }
}
