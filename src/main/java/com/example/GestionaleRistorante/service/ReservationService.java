package com.example.GestionaleRistorante.service;

import com.example.GestionaleRistorante.dto.CustomerDto;
import com.example.GestionaleRistorante.dto.ReservationDto;
import com.example.GestionaleRistorante.dto.TableRestaurantDto;
import com.example.GestionaleRistorante.entity.*;
import com.example.GestionaleRistorante.repository.ReservationRepository;
import com.example.GestionaleRistorante.repository.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerService customerService;
    private final TableBrookerService tableBrooker;
    private final ModelMapper modelMapper;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, TableRepository tableRepository, CustomerService customerService, ModelMapper modelMapper, TableBrookerService tableBrooker, ModelMapper modelMapper1) {
        this.reservationRepository = reservationRepository;
        this.customerService = customerService;
        this.tableBrooker = tableBrooker;
        this.modelMapper = modelMapper1;
    }


    //CRUD OPERATIONS

//al contorller arriverà una richiesta con un json contenente una reservationDto, questa conterrà a sua volta le strutture TableRestaurant(valorizzato solo il numero di posti) e Customer(valorizzati solo nome, cognome e num telefono)
    public ReservationDto addReservation(ReservationDto reservationDto){
        try{
            TableRestaurantDto tableAvailableDto = tableBrooker.getFirstAvailablebyData(reservationDto.getIdReservation().getDate(), reservationDto.getIdReservation().getTime(), reservationDto.getTableRestaurantDto().getSeats());
            if (tableAvailableDto==null) {
                throw new Exception("Table already booked for this reservationTime slot");
            }
            IdReservation idReservation =  new IdReservation.Builder()
                                                            .setIdTable(tableAvailableDto.getId())
                                                            .setDate(reservationDto.getIdReservation().getDate())
                                                            .setTime(reservationDto.getIdReservation().getTime())
                                                            .build();
            CustomerDto customerDtoResult = customerService.addCustomer(reservationDto.getCustomerDto()); //lo aggiunge solo se non esiste
            TimeSlot slot = TimeSlot.slotAllocation(reservationDto.getIdReservation().getTime());
            reservationDto.setIdReservation(idReservation);
            reservationDto.setTableRestaurantDto(tableAvailableDto);
            reservationDto.setCustomerDto(customerDtoResult);
            reservationDto.setTimestamp(LocalDateTime.now());
            reservationDto.setTimeSlot(slot);
            LocalDateTime key = LocalDateTime.of(reservationDto.getIdReservation().getDate(), reservationDto.getIdReservation().getTime());
            tableAvailableDto.getReservationsDto().put(key, reservationDto);
            tableBrooker.updateTable(tableAvailableDto); //sistemare dentro questo updateTable
            reservationRepository.save(modelMapper.map(reservationDto, Reservation.class));
            return reservationDto;
        }   catch (Exception e){
            log.error("Error during table booking occured.");
            return null;
        }
    }

        public ReservationDto updateReservation (ReservationDto newReservation){
        try {
            Optional<ReservationDto> reservationOptional = getReservation(newReservation.getIdReservation());
            if (reservationOptional.isEmpty()) { //controllo sull'esistenza della vecchia prenotazione
                throw new Exception("Reservation not found");
            }
            ReservationDto reservationDto = reservationOptional.get();
            if (Objects.equals(reservationDto.getTableRestaurantDto().getSeats(), newReservation.getTableRestaurantDto().getSeats())) {
                TableRestaurantDto tableAvailableDto = tableBrooker.getFirstAvailablebyData(newReservation.getIdReservation().getDate(), newReservation.getIdReservation().getTime(), newReservation.getTableRestaurantDto().getSeats());
                IdReservation idReservation = new IdReservation.Builder()
                        .setIdTable(tableAvailableDto.getId())
                        .setDate(newReservation.getIdReservation().getDate())
                        .setTime(newReservation.getIdReservation().getTime())
                        .build();
                reservationDto.setIdReservation(idReservation);
                reservationDto.setTableRestaurantDto(tableAvailableDto);
            }   else {
                    reservationDto.setIdReservation(newReservation.getIdReservation());
                }
            Reservation reservation = modelMapper.map(reservationDto, Reservation.class);
            reservationRepository.save(reservation);
            return reservationDto;
        }   catch(Exception e){
                log.error(e.getMessage());
                return null;
            }
    }

    public Boolean deleteReservation(IdReservation idReservation){
        //Reservation reservation = getReservationByData(surname, reservationDate, reservationTime, seats);
        try{
            reservationRepository.deleteById(idReservation);
        } catch(Exception e){
            log.error("Reservation not found");
            return false;
        }
        return true;
    }

    public List<ReservationDto> getReservations(){
        try {
            List<ReservationDto> reservationDtos = new ArrayList<>();
        List<Reservation> outList = reservationRepository.findAll();
        outList.forEach(reservation -> reservationDtos.add(modelMapper.map(reservation, ReservationDto.class)));
        return reservationDtos;
        }   catch(Exception e){
            log.error("Bookings not found");
            return null;
        }
    }

    public Optional<ReservationDto> getReservation(IdReservation isReservation){
        Optional<Reservation> reservationOptional = reservationRepository.findById(isReservation);
        return reservationOptional.map(reservation -> modelMapper.map(reservation, ReservationDto.class));
    }

    public List<ReservationDto> getReservationsByCustomer(String name, String surname, Integer seats){
        try {
            List<ReservationDto> reservationDtos = new ArrayList<>();
            List<Reservation> outList = reservationRepository.findByCustomer(name, surname, seats);
            outList.forEach(reservation -> reservationDtos.add(modelMapper.map(reservation, ReservationDto.class)));
            return reservationDtos;
        }   catch(Exception e){
            log.error("Bookings not found");
            return null;
            }
    }

       /* public ReservationDto getReservationByData(String surname, LocalDate date, LocalTime time, Integer seats){

        return reservationRepository.findByData(date, time, surname, seats);
    }*/

}
