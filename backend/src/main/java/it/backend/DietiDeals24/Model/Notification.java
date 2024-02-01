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
}
