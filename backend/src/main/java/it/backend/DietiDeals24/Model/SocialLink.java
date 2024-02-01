package it.backend.DietiDeals24.Model;

public class SocialLink {

    private Account accountAssociated;

    private String link;

    public SocialLink(Account accountAssociated, String link) {
    	this.accountAssociated = accountAssociated;
    	this.link = link;
    }


    //all get
    public Account getAccountAssociated() {
    	return this.accountAssociated;
    }

    public String getLink() {
    	return this.link;
    }

    //all set
    public void setAccountAssociated(Account accountAssociated) {
    	this.accountAssociated = accountAssociated;
    }

    public void setLink(String link) {
    	this.link = link;
    }
}
