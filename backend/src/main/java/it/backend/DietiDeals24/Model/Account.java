package it.backend.DietiDeals24.Model;

import java.util.ArrayList;

public abstract class  Account {


    private ArrayList<Notification> notifications;

    private ArrayList<SocialLink> socialLinks;

    private String fullName;

    private String imageAccount;

    private String email;

    private String password;

    private String description;

    private Integer telephoneNumber;

    private String country;


    //full constructor
    public Account(String fullName, String imageAccount, String email, String password, String description, Integer telephoneNumber, String country) {
        this.fullName = fullName;
        this.imageAccount = imageAccount;
        this.email = email;
        this.password = password;
        this.description = description;
        this.telephoneNumber = telephoneNumber;
        this.country = country;
    }

    //starting constructor
    public Account(String fullName, Integer telephoneNumber, String email, String password) {
        this.fullName = fullName;
        this.imageAccount = imageAccount;
        this.email = email;
        this.password = password;
    }


    //all get
    public String getFullName() {
        return fullName;
    }

    public String getImageAccount() {
        return imageAccount;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getCountry() {
        return country;
    }

    //all set

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImageAccount(String imageAccount) {
        this.imageAccount = imageAccount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTelephoneNumber(Integer telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    //add social link and notification
    public void addSocialLink(SocialLink socialLink) {
    	this.socialLinks.add(socialLink);
    }

    public void addNotification(Notification notification) {
    	this.notifications.add(notification);
    }

    //remove social link and notification
    public void removeSocialLink(SocialLink socialLink) {
    	this.socialLinks.remove(socialLink);
    }

    public void removeNotification(Notification notification) {
    	this.notifications.remove(notification);
    }

    //get social link and notification

    public ArrayList<SocialLink> getSocialLinks() {
    	return this.socialLinks;
    }

    public ArrayList<Notification> getNotifications() {
    	return this.notifications;
    }

    //set social link and notification
    public void setSocialLinks(ArrayList<SocialLink> socialLinks) {
    	this.socialLinks = socialLinks;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
    	this.notifications = notifications;
    }


}
