package com.example.swasthya_2o;

public class PatientModel {
    String userProblem;
    String userChosenDepartment;
    String userChosenSpecialist;
    String userChosenTime;

    public PatientModel(String userProblem,String userChosenSpecialist, String userChosenTime) {
        this.userProblem = userProblem;
//        this.userChosenDepartment = userChosenDepartment;
        this.userChosenSpecialist = userChosenSpecialist;
        this.userChosenTime = userChosenTime;
    }


    public String getUserProblem() {
        return userProblem;
    }

    public void setUserProblem(String userProblem) {
        this.userProblem = userProblem;
    }

    public String getUserChosenDepartment() {
        return userChosenDepartment;
    }

    public void setUserChosenDepartment(String userChosenDepartment) {
        this.userChosenDepartment = userChosenDepartment;
    }

    public String getUserChosenSpecialist() {
        return userChosenSpecialist;
    }

    public void setUserChosenSpecialist(String userChosenSpecialist) {
        this.userChosenSpecialist = userChosenSpecialist;
    }

    public String getUserChosenTime() {
        return userChosenTime;
    }

    public void setUserChosenTime(String userChosenTime) {
        this.userChosenTime = userChosenTime;
    }
}
