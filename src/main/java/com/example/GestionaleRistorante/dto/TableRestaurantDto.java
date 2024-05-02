package com.example.GestionaleRistorante.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TableRestaurantDto {

    private Integer id; //table number
    private Map<LocalDateTime, ReservationDto> reservationsDto;
    private Integer seats;

    public TableRestaurantDto(Builder builder) {
        this.id = builder.id;
        this.reservationsDto = builder.reservationsDto;
        this.seats = builder.seats;
    }

    public static class Builder{
        private Integer id; //table number
        private Map<LocalDateTime, ReservationDto> reservationsDto;
        private Integer seats;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setReservations(Map<LocalDateTime, ReservationDto> reservationsDto) {
            this.reservationsDto = reservationsDto;
            return this;
        }

        public Builder setSeats(Integer seats) {
            this.seats = seats;
            return this;
        }

        public TableRestaurantDto build(){
            return new TableRestaurantDto(this);
        }

    }
}