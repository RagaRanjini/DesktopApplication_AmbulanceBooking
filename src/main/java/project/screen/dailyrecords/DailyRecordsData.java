package project.screen.dailyrecords;

import project.common.files.Data;

public class DailyRecordsData implements Data {

    private String DailyDate;
    private String DailyPatient;
    private String DailyContact;

    private String DailyTripStart;

    private String DailyTripEnd;
    private String DailyHospitalId;
    private String DailyAmbulanceId;
    private String DailyDriverId;
    private String DailyAttenderId;


    public DailyRecordsData(String Date, String Patient, String Contact, String TripStart, String TripEnd, String Hosp, String Amb, String Driver, String Attender){
        this.DailyDate = Date;
        this.DailyPatient = Patient;
        this.DailyContact = Contact;
        this.DailyTripStart = TripStart;
        this.DailyTripEnd = TripEnd;
        this.DailyHospitalId = Hosp;
        this.DailyAmbulanceId = Amb;
        this.DailyDriverId = Driver;
        this.DailyAttenderId = Attender;
    }



    public String getDailyDate() { return DailyDate;}
    public String getName() { return DailyPatient;}
    public String getContact(){
        return DailyContact;
    }
    public String getDailyTripStart() { return DailyTripStart;}
    public String getDailyTripEnd(){
        return DailyTripEnd;
    }
    public String getDailyHospitalId(){
        return DailyHospitalId;
    }
    public String getDailyAmbulanceId(){
        return DailyAmbulanceId;
    }
    public String getDailyDriverId(){
        return DailyDriverId;
    }
    public String getDailyAttender(){
        return DailyAttenderId;
    }


    public String getLocation(){
        return null;
    }


}
