package overlays;

import org.javagram.dao.Contact;

/**
 * Created by i5 on 22.10.2016.
 */
public class ContactData
{
    private String name;
    private String surname;
    private String number;
    private int id;

    public ContactData() {
        this("", "", "");
    }

    public ContactData(String name, String surname, String number) {
        this(name, surname, number, 0);
    }

    public ContactData(String name, String surname, String number, int id) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.id = id;
    }

    public ContactData(Contact contact) {
        this(contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber(), contact.getId());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClearedNumber() {
        return getNumber().replaceAll("\\D+", "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
