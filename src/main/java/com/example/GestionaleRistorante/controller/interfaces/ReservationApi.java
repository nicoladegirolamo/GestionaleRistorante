package com.example.GestionaleRistorante.controller.interfaces;

import com.example.GestionaleRistorante.dto.ReservationDto;
import com.example.GestionaleRistorante.entity.IdReservation;
import com.example.GestionaleRistorante.entity.Reservation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/reservation")
public interface ReservationApi {


    @GetMapping("/{idTable}/{date}/{time}")
    ResponseEntity<Reservation> getReservationById(@PathVariable Integer idTable, @PathVariable String date, @PathVariable String time);


    @GetMapping()
    ResponseEntity<List<Reservation>> getAllReservations();

    @GetMapping("/customer")
    ResponseEntity<List<Reservation>> getAllReservationsByCustomer(@RequestParam(name="name", required=true) String name, @RequestParam(name="surname", required=true) String surname, @RequestParam(name="seats", required=true) String seats);

    @GetMapping()
    ResponseEntity<Reservation> getReservationByData(@RequestParam(name="surname", required=true) String surname, @RequestParam(name="date", required=true) String date, @RequestParam(name="time", required=true) String time, @RequestParam(name="seats", required=true) String seats);

    @DeleteMapping()
    ResponseEntity<Void> deleteReservation(@RequestBody IdReservation idReservation);

    @PutMapping()
    ResponseEntity<ReservationDto> updateReservation(@RequestBody ReservationDto reservationDto);

    @PostMapping()
    ResponseEntity<ReservationDto> addReservation(@RequestBody ReservationDto reservationDto);

    }
