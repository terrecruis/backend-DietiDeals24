package it.backend.DietiDeals24.Model;

import java.util.Date;

public class Notification {

    private Account accountAssociated;

    private String imageNotification;

    private String status;

    private String title;

    private Date date;


    public Notification(Account accountAssociated, String imageNotification, String status, String title, Date date) {
        this.accountAssociated = accountAssociated;
        this.imageNotification = imageNotification;
        this.status = status;
        this.title = title;
        this.date = date;
    }


    //all get
    public Account getAccountAssociated() {
        return accountAssociated;
    }

    public String getImageNotification() {
        return imageNotification;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }


    //all set
    public void setAccountAssociated(Account accountAssociated) {
        this.accountAssociated = accountAssociated;
    }

    public void setImageNotification(String imageNotification) {
        this.imageNotification = imageNotification;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
