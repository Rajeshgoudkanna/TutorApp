package com.example.tutorapp.model;

public class Course {

    private String Cname;
    private String pid;
    private String Cprice;
    private String Cdescription;
    private String Ctype;
    private String Cstatus;
    private String Ccategory;
    private String CstartDate;
    private String CendDate;
    private String Cimage;

    public String getImage() {
        return Cimage;
    }

    public void setImage(String image) {
        this.Cimage = image;
    }

    public Course() {
    }

    public Course(String cname, String pid, String cprice, String cdescription, String ctype, String cstatus, String ccategory, String cstartDate, String cendDate, String cimage) {
        this.Cname = cname;
        this.pid = pid;
        this.Cprice = cprice;
        this.Cdescription = cdescription;
        this.Ctype = ctype;
        this.Cstatus = cstatus;
        this.Ccategory = ccategory;
        this.CstartDate = cstartDate;
        this.CendDate = cendDate;
        this.Cimage = cimage;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCimage() {
        return Cimage;
    }

    public void setCimage(String cimage) {
        Cimage = cimage;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getCprice() {
        return Cprice;
    }

    public void setCprice(String cprice) {
        Cprice = cprice;
    }

    public String getCdescription() {
        return Cdescription;
    }

    public void setCdescription(String cdescription) {
        Cdescription = cdescription;
    }

    public String getCtype() {
        return Ctype;
    }

    public void setCtype(String ctype) {
        Ctype = ctype;
    }

    public String getCstatus() {
        return Cstatus;
    }

    public void setCstatus(String cstatus) {
        Cstatus = cstatus;
    }

    public String getCcategory() {
        return Ccategory;
    }

    public void setCcategory(String ccategory) {
        Ccategory = ccategory;
    }

    public String getCstartDate() {
        return CstartDate;
    }

    public void setCstartDate(String cstartDate) {
        CstartDate = cstartDate;
    }

    public String getCendDate() {
        return CendDate;
    }

    public void setCendDate(String cendDate) {
        CendDate = cendDate;
    }
}
