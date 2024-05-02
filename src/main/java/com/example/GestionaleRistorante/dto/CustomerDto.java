package com.example.GestionaleRistorante.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CustomerDto {

    private Long id;
    private String customerSurname;
    private String customerName;
    private String contactNumber;
    private String email;
    private String address;
    private Boolean isPremium;
    private Map<LocalDateTime, ReservationDto> reservationsDto;

    public CustomerDto(CustomerDto.Builder builder) {
        this.id = builder.id;
        this.customerSurname = builder.customerSurname;
        this.customerName = builder.customerName;
        this.contactNumber = builder.contactNumber;
        this.email = builder.email;
        this.address = builder.address;
        this.isPremium = builder.isPremium;
        this.reservationsDto = builder.reservationsDto;
    }

    public static class Builder{
        private Long id;
        private String customerSurname;
        private String customerName;
        private String contactNumber;
        private String email;
        private String address;
        private Boolean isPremium;
        private Map<LocalDateTime, ReservationDto> reservationsDto;

        public CustomerDto.Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public CustomerDto.Builder setCustomerSurname(String customerSurname) {
            this.customerSurname = customerSurname;
            return this;
        }

        public CustomerDto.Builder setCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public CustomerDto.Builder setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public CustomerDto.Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerDto.Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public CustomerDto.Builder setPremium(Boolean premium) {
            isPremium = premium;
            return this;
        }

        public CustomerDto.Builder setReservationsDto(Map<LocalDateTime, ReservationDto> reservationsDto) {
            this.reservationsDto = reservationsDto;
            return this;
        }

        public CustomerDto build(){
            return new CustomerDto(this);
        }

    }
}
