package ots.hr;


public class Leave {

    private String id;
    private String description;
    private String fromDate;
    private String toDate;
    private String duration;
    private String status;

    public Leave(String id, String description, String fromDate, String toDate, String duration, String status) {
        this.id = id;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.duration = duration;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }


    public String toString(){
        return " leave id: " + id +" Leave Category: "+ description + " Date interval: "+ fromDate +" - " + toDate + "  Duration(Days):" + duration + " Status:" +status;
    }
}

