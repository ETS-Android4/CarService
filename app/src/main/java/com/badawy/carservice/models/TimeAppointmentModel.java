package com.badawy.carservice.models;

public class TimeAppointmentModel {
    private byte id;
    private String time;
    private String timeOfDay;
    private String booked;

    public TimeAppointmentModel(byte id, String time, String timeOfDay, String booked) {
        this.id = id;
        this.time = time;
        this.timeOfDay = timeOfDay;
        this.booked = booked;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }
}
