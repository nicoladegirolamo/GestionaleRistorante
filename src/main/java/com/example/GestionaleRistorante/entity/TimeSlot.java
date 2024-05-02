package com.example.GestionaleRistorante.entity;

import java.time.LocalTime;

public enum TimeSlot {
        SLOT1L("12:00-13:00"),
        SLOT2L("13:00-14:00"),
        SLOT3L("14:00-15:00"),
        SLOT1D("20:00-21:00"),
        SLOT2D("21:00-22:00"),
        SLOT3D("22:00-23:00");

        private final String slot;

        TimeSlot(String slot) {
            this.slot = slot;
        }

        public String getTimeSlot() {
            return this.slot;
        }


    public static TimeSlot slotAllocation(LocalTime time){
        if(time.isAfter(LocalTime.parse("12:00")) && time.isBefore(LocalTime.parse("13:00"))){
            return TimeSlot.SLOT1L;
        }
        if(time.isAfter(LocalTime.parse("13:00")) && time.isBefore(LocalTime.parse("14:00"))){
            return TimeSlot.SLOT2L;
        }
        if(time.isAfter(LocalTime.parse("14:00")) && time.isBefore(LocalTime.parse("15:00"))){
            return TimeSlot.SLOT3L;
        }
        if(time.isAfter(LocalTime.parse("20:00")) && time.isBefore(LocalTime.parse("21:00"))){
            return TimeSlot.SLOT1D;
        }
        if(time.isAfter(LocalTime.parse("21:00")) && time.isBefore(LocalTime.parse("22:00"))){
            return TimeSlot.SLOT2D;
        }
        if(time.isAfter(LocalTime.parse("22:00")) && time.isBefore(LocalTime.parse("23:00"))){
            return TimeSlot.SLOT3D;
        }
        return null;
    }
}




