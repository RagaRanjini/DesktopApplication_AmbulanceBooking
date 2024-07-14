package project.screen.ambulance;

public class AmbulanceData{
    private String ambulanceType;
    private String hospitalId;

    private String ambulanceId;
    private static String selectedAmbulanceId;

    public AmbulanceData(String ambulanceType, String hospitalId, String ambulanceId) {
        this.ambulanceType = ambulanceType;
        this.hospitalId = hospitalId;
        this.ambulanceId = ambulanceId;
    }

    public String getAmbulanceType() {
        return ambulanceType;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getAmbulanceId(){
        return ambulanceId;
    }

    public static void setSelectedAmbulanceId(String AmbulanceId){
        selectedAmbulanceId = AmbulanceId;
    }

    public static String getSelectedAmbulanceId(){
        return selectedAmbulanceId;
    }



}
