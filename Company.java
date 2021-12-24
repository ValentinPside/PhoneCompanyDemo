package ru.avalon.javapp.devj120.avalontelecom.models;

public class Company extends ClientInfo{

    private String contactName;
    private String directorName;

    public Company(PhoneNumber phoneNumber, String name, String address, String contactName, String directorName) {
        super(phoneNumber, name, address);
        setContactName(contactName);
        setDirectorName(directorName);
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        if(contactName == null)
            throw new IllegalArgumentException("Contact name can't be null.");
        this.contactName = contactName;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        if(directorName == null)
            throw new IllegalArgumentException("Director's name can't be null.");
        this.directorName = directorName;
    }

    @Override
    public String getExtraInformation() {
        return "Director: " + directorName + ", " + "Contact: " + contactName;
    }
}
