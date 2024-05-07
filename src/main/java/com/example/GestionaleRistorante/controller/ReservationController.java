package com.example.GestionaleRistorante.controller;

import com.example.GestionaleRistorante.controller.interfaces.ReservationApi;
import com.example.GestionaleRistorante.dto.ReservationDto;
import com.example.GestionaleRistorante.model.IdReservation;
import com.example.GestionaleRistorante.entity.Reservation;
import com.example.GestionaleRistorante.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ReservationController implements ReservationApi {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //CRUDS

    @Override
    public ResponseEntity<Reservation> getReservationById(Integer idTable, String date, String time) {
        IdReservation idReservation = new IdReservation.Builder()
                                                        .setIdTable(idTable)
                                                        .setDate(LocalDate.parse(date))
                                                        .setTime(LocalTime.parse(time))
                                                        .build();
        Optional<Reservation> reservationOptional = reservationService.getReservation(idReservation);
        return reservationOptional.map(reservation -> new ResponseEntity<>(reservation, HttpStatus.OK)).orElse(null);
    }


    @Override
    public ResponseEntity<List<Reservation>> getAllReservations() {
       try{
           List<Reservation> reservations  = reservationService.getReservations();
           if(reservations.isEmpty()) {
               throw new Exception("Empty list");
           }
           return new ResponseEntity<>(reservations, HttpStatus.OK);
       } catch(Exception e){
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }

    @Override
    public ResponseEntity<List<Reservation>> getAllReservationsByCustomer(String name, String surname, String seats) {
        try{
            List<Reservation> reservationsByCustomer = reservationService.getReservationsByCustomer(name, surname, Integer.getInteger(seats));
            if(reservationsByCustomer.isEmpty()) {
                throw new Exception("Empty list");
            }
            return new ResponseEntity<>(reservationsByCustomer, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Reservation> getReservationByData(String surname, String date, String time, String seats) {
        try{
            Reservation reservationByData = reservationService.getReservationByData(surname, LocalDate.parse(date), LocalTime.parse(time), Integer.getInteger(seats));
            if(reservationByData==null) {
                throw new Exception("Empty list");
            }
            return new ResponseEntity<>(reservationByData, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }    }

    @Override
    public ResponseEntity<Void> deleteReservation(IdReservation idReservation) {
        if(reservationService.deleteReservation(idReservation)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ReservationDto> updateReservation(ReservationDto reservationDto) {
        ReservationDto reservationDtoOutput = reservationService.updateReservation(reservationDto);
        if(reservationDtoOutput!=null){
            return new ResponseEntity<>(reservationDtoOutput, HttpStatus.OK);
        }   else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @Override
    public ResponseEntity<ReservationDto> addReservation(ReservationDto reservationDto) {
        reservationService.addReservation()
        return null;
    }


}
