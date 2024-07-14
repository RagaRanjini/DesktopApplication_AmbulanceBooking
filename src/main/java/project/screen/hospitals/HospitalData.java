package project.screen.hospitals;

import project.common.files.Data;

public class HospitalData implements Data {
    private String hospitalName;
    private String hospitalContact;
    private String hospitalLocation;
    public HospitalData(String hospitalName, String hospitalContact, String hospitalLocation){
        this.hospitalName = hospitalName;
        this.hospitalContact = hospitalContact;
        this.hospitalLocation = hospitalLocation;
    }
    public String getName() {
        return hospitalName;
    }

    public String getContact() {
        return hospitalContact;
    }

    public String getLocation(){
        return hospitalLocation;
    }

    public String getDailyDate(){
        return null;
    }
    public String getDailyTripStart(){
        return null;
    }
    public String getDailyTripEnd(){
        return null;
    }
    public String getDailyHospitalId(){
        return null;
    }
    public String getDailyAmbulanceId(){
        return null;
    }
    public String getDailyDriverId(){
        return null;
    }
    public String getDailyAttender(){
        return null;
    }
}
