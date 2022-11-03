package com.example.sqlite;

public class Computer {
    private String maMT, ten, maDM;

    public Computer(String maMT, String ten, String maDM) {
        this.maMT = maMT;
        this.ten = ten;
        this.maDM = maDM;
    }

    public String getMaMT() {
        return maMT;
    }

    public void setMaMT(String maMT) {
        this.maMT = maMT;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }
}
