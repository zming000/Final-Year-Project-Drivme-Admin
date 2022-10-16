package com.example.finalyeardrivmeadmin;

public class ModelReferenceList {
    String driverID, driverName, refCode, status;

    public ModelReferenceList() {/*empty constructor*/
    }

    public ModelReferenceList(String driverID, String driverName, String refCode, String status) {
        this.driverID = driverID;
        this.driverName = driverName;
        this.refCode = refCode;
        this.status = status;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
