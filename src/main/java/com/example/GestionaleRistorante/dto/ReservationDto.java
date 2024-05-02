package com.example.GestionaleRistorante.dto;


import com.example.GestionaleRistorante.entity.IdReservation;
import com.example.GestionaleRistorante.entity.ReservationStatus;
import com.example.GestionaleRistorante.entity.TimeSlot;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {

    private IdReservation idReservation;
    private CustomerDto customerDto;
    private TableRestaurantDto tableRestaurantDto;
    private LocalDateTime timestamp;
    private ReservationStatus status = ReservationStatus.CREATED;
    private TimeSlot timeSlot;


    public ReservationDto(Builder builder) {
        this.idReservation = builder.idReservation;
        this.customerDto = builder.customerDto;
        this.tableRestaurantDto = builder.tableRestaurantDto;
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.timeSlot = builder.timeSlot;
    }

    public static class Builder{
        private IdReservation idReservation;
        private CustomerDto customerDto;
        private TableRestaurantDto tableRestaurantDto;
        private LocalDateTime timestamp;
        private ReservationStatus status;
        private TimeSlot timeSlot;


        public Builder setIdReservation(IdReservation idReservation) {
            this.idReservation = idReservation;
            return this;
        }

        public Builder setCustomer(CustomerDto customerDto) {
            this.customerDto = customerDto;
            return this;
        }

        public Builder setTableRestaurant(TableRestaurantDto tableRestaurantDto) {
            this.tableRestaurantDto = tableRestaurantDto;
            return this;
        }

        public Builder setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setStatus(ReservationStatus status) {
            this.status = status;
            return this;
        }

        public Builder setTimeSlot(TimeSlot timeSlot) {
            this.timeSlot = timeSlot;
            return this;
        }

        public ReservationDto build(){
            return new ReservationDto(this);
        }
    }
}