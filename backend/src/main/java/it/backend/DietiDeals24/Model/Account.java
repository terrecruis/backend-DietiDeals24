package it.backend.DietiDeals24.Model;
import java.util.ArrayList;
import java.util.List;

public abstract class  Account {

    private ArrayList<Notification> notifications;

    private ArrayList<SocialLink> socialLinks = new ArrayList<>();

    private String fullName;

    private String imageAccount;

    private String email;

    private String description;

    private String telephoneNumber;

    private String country;


    //full constructor
    protected Account(String fullName, String imageAccount, String email, String description, String telephoneNumber, String country) {
        this.fullName = fullName;
        this.imageAccount = imageAccount;
        this.email = email;
        this.description = description;
        this.telephoneNumber = telephoneNumber;
        this.country = country;
    }

    //starting constructor
    protected Account(String fullName, String telephoneNumber, String email) {
        this.fullName = fullName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }

    protected Account(String email) {
        this.email = email;
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

    public String getDescription() {
        return description;
    }

    public String getTelephoneNumber() {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTelephoneNumber(String telephoneNumber) {
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
    public List<SocialLink> getSocialLinks() {
    	return this.socialLinks;
    }

    public List<Notification> getNotifications() {
    	return this.notifications;
    }

    //set social link and notification
    public void setSocialLinks(List<SocialLink> socialLinks) {
    	this.socialLinks = (ArrayList<SocialLink>) socialLinks;
    }

    public void setNotifications(List<Notification> notifications) {
    	this.notifications = (ArrayList<Notification>) notifications;
    }


}
