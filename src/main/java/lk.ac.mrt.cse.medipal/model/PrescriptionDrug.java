package lk.ac.mrt.cse.medipal.model;

public class PrescriptionDrug {
    private Drug drug;
    private String dosage;
    private String frequency;
    private String route;
    private int duration;
    private int prescriptionID;
    private String useTime;
    private String unitSize;
    private String startDate;

    public PrescriptionDrug(Drug drug, String dosage, String frequency, String route, int duration, int prescriptionID, String useTime, String startDate) {
        this.drug = drug;
        this.dosage = dosage;
        this.frequency = frequency;
        this.route = route;
        this.duration = duration;
        this.prescriptionID = prescriptionID;
        this.useTime = useTime;
        this.startDate = startDate;

    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
