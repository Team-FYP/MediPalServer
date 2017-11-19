package lk.ac.mrt.cse.medipal.util;

import org.apache.log4j.Logger;

public class MessageUtil {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(MessageUtil.class);

    public static String HistorySharedMessageBuild(String patient_id, String gender){
        String message_part_one = " Shared ";
        String message_part_two = " history with you";
        String gender_variable = null;
        if(gender.equals("Male")){
            gender_variable = "his";
        }else  {
            gender_variable = "her";
        }

        String message = patient_id + message_part_one + gender_variable + message_part_two;

        return message;

    }

    public static String PrescriptionAddedMessageBuild(String doctor_name){
        String message_part_one = " Dr.";
        String message_part_two = " added a prescription for you";
        String message = message_part_one + doctor_name + message_part_two;
        return message;

    }
}
