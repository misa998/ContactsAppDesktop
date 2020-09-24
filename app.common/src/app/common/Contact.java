package app.common;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty firstname;
    private final SimpleStringProperty lastname;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty note;

    public Contact() {
        this.id = new SimpleIntegerProperty();
        this.firstname = new SimpleStringProperty();
        this.lastname = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
    }

    public Contact(SimpleStringProperty firstname, SimpleStringProperty lastname, SimpleStringProperty phone, SimpleStringProperty note) {
        this.id = new SimpleIntegerProperty();
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.note = note;
    }

    public int getID() {
        return id.get();
    }
    public void setID(int id) {
        this.id.set(id);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getLastname() {
        return lastname.get();
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getNote() {
        return note.get();
    }

    public void setNote(String note) {
        this.note.set(note);
    }
}
