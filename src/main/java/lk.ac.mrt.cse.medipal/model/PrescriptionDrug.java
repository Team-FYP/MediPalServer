package lk.ac.mrt.cse.medipal.model;

import java.util.Date;

public class PrescriptionDrug {
    private Drug drug;
    private String dosage;
    private String frequency;
    private String route;
    private int duration;
    private int prescriptionID;
    private String useTime;

    public PrescriptionDrug(Drug drug, String dosage, String frequency, String route, int duration, int prescriptionID, String useTime, Date start_date) {
        this.drug = drug;
        this.dosage = dosage;
        this.frequency = frequency;
        this.route = route;
        this.duration = duration;
        this.prescriptionID = prescriptionID;
        this.useTime = useTime;

        this.start_date = start_date;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Drug getDrug() {

        return drug;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getRoute() {
        return route;
    }

    public int getDuration() {
        return duration;
    }

    public Date getStart_date() {
        return start_date;
    }

    private Date start_date;

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public PrescriptionDrug() {
    }


}
