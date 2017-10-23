package lk.ac.mrt.cse.medipal.model;

/**
 * Created by chand on 2017-06-13.
 */

public class Doctor {
    private String registration_id;
    private String speciality;
    private String name;
    private String gender;
    private String email;
    private String mobile;
    private String password;
    private String image;

    public Doctor(String registration_id, String speciality, String name, String gender, String email, String mobile, String password, String image) {
        this.registration_id = registration_id;
        this.speciality = speciality;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.image = image;
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
