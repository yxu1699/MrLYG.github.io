package usc.yuangang.es.model;

public class Venue {
    private String venueName;
    private String venueAddress;
    private String detailCs;
    private String detailContact;
    private String ohText;
    private String grText;
    private String crText;

    public Venue() {
    }

    public Venue(String venueName, String venueAddress, String detailCs, String detailContact, String ohText, String grText, String crText) {
        this.venueName = venueName;
        this.venueAddress = venueAddress;
        this.detailCs = detailCs;
        this.detailContact = detailContact;
        this.ohText = ohText;
        this.grText = grText;
        this.crText = crText;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getDetailCs() {
        return detailCs;
    }

    public void setDetailCs(String detailCs) {
        this.detailCs = detailCs;
    }

    public String getDetailContact() {
        return detailContact;
    }

    public void setDetailContact(String detailContact) {
        this.detailContact = detailContact;
    }

    public String getOhText() {
        return ohText;
    }

    public void setOhText(String ohText) {
        this.ohText = ohText;
    }

    public String getGrText() {
        return grText;
    }

    public void setGrText(String grText) {
        this.grText = grText;
    }

    public String getCrText() {
        return crText;
    }

    public void setCrText(String crText) {
        this.crText = crText;
    }
}
