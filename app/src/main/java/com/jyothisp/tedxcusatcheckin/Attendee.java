package com.jyothisp.tedxcusatcheckin;

public class Attendee {
    private String name, phone, seat;

    public Attendee() {
    }

    public Attendee(String name, String phone, String seat) {
        this.name = name;
        this.phone = phone;
        this.seat = seat;
    }

    public String getSeat() {
        return seat;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}