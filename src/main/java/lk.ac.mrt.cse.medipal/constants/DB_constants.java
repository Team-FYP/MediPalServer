/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ac.mrt.cse.medipal.constants;

/**
 *
 * @author ndine
 */
public class DB_constants {
    public static  final String DB_URL= "jdbc:mysql://localhost:3306/";
    public static  final String DRIVER = "com.mysql.jdbc.Driver";
    public static  final String DB_NAME = "medipal_db";
    public static  final String USERNAME = "root";
    public static  final String PASSWORD = "P@L#cse2K17";
    
    public static class Patient{
        public static String NIC = "NIC";
        public static String NAME = "PATIENT_NAME";
        public static String GENDER = "GENDER";
        public static String EMAIL = "EMAIL";
        public static String BIRTHDAY = "BIRTHDAY";
        public static String CONTACT_NUMBER = "CONTACT_NUMBER";
        public static String EMERGENCY_CONTACT_NUMBER = "EMERGENCY_CONTACT_NUMBER";
        public static String PASSWORD = "PASSWORD";
        public static String PROFILE_PICTURE = "PROFILE_PICTURE";
    }

    public static class Doctor{
        public static String REGISTRATION_NO = "REGISTRATION_NO";
        public static String SPECIALITY = "SPECIALITY";
        public static String NAME = "NAME";
        public static String GENDER = "GENDER";
        public static String EMAIL = "EMAIL";
        public static String CONTACT_NUMBER = "CONTACT_NUMBER";
        public static String PASSWORD = "PASSWORD";
        public static String PROFILE_PICTURE = "PROFILE_PICTURE";
        public static String PRESCRIPTION_PRESCRIPTION_ID = "PRESCRIPTION_PRESCRIPTION_ID";
        public static String PRESCRIPTION_PATIENT_NIC = "PRESCRIPTION_PATIENT_NIC";
    }
}
