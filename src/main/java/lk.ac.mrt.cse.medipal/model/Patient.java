package lk.ac.mrt.cse.medipal.model;

import java.util.Date;

/**
 * Created by Lakshan on 2017-06-01.
 */

public class Patient {
    private String nic;
    private String name;
    private String gender;
    private String email;
    private String birthday;
    private String mobile;
    private String emergency_contact;
    private String password;
    private String image;

    public Patient() {
    }

    public Patient(String nic, String name, String gender, String email, String birthday, String mobile, String emergency_contact, String password, String image) {
        this.nic = nic;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.birthday = birthday;
        this.mobile = mobile;
        this.emergency_contact = emergency_contact;
        this.password = password;
        this.image = image;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
