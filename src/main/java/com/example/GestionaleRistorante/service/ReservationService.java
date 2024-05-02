package com.example.GestionaleRistorante.service;

import com.example.GestionaleRistorante.dto.ReservationDto;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerService customerService;
    private final TableBrookerService tableBrooker;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, TableRepository tableRepository, CustomerService customerService, ModelMapper modelMapper, TableBrookerService tableBrooker) {
        this.reservationRepository = reservationRepository;
        this.customerService = customerService;
        this.tableBrooker = tableBrooker;
    }


    //CRUD OPERATIONS

    public Reservation addReservation(LocalDate reservationDate, LocalTime reservationTime, Integer seats, String name, String surname, String contactNumber){
        try{
            TableRestaurant tableAvailable = tableBrooker.getFirstAvailablebyData(reservationDate, reservationTime, seats);
            if (tableAvailable!=null) {
                IdReservation idReservation =  new IdReservation.Builder()
                    .setIdTable(tableAvailable.getId())
                    .setDate(reservationDate)
                    .setTime(reservationTime)
                    .build();
                Customer customer = customerService.doesCustomerExist(name, surname, contactNumber);
                if(customer==null) {
                    customer = new Customer.Builder().setCustomerName(name).setCustomerSurname(surname).setContactNumber(contactNumber).setPremium(false).build();
                    customerService.addCustomer(customer);
                }
                TimeSlot slot = TimeSlot.slotAllocation(reservationTime);
                Reservation reservation = new Reservation.Builder()
                        .setIdReservation(idReservation)
                        .setTableRestaurant(tableAvailable)
                        .setCustomer(customer)
                        .setTimestamp(LocalDateTime.now())
                        .setTimeSlot(slot)
                        .build();
                tableBrooker.updateTable(tableAvailable, reservation);
                reservationRepository.save(reservation);
                return reservation;
            }   else{
                    log.error("Table already booked for this reservationTime slot");
                    return null;
                }
        }   catch (Exception e){
                log.error("Error during table booking occured.");
                return null;
            }
    }

    //public Reservation updateReservation (LocalDate newReservationDate, LocalTime newReservationTime, Integer newSeats, LocalDate oldReservationDate, LocalTime oldReservationTime, Integer oldSeats, String surname){

        public Reservation updateReservation (ReservationDto oldReservation, ReservationDto newReservation){
        Optional<Reservation> reservationOptional = getReservation(oldReservation.getIdReservation());
        if(reservationOptional.isEmpty()){ //controllo sull'esistenza della vecchia prenotazione
            return null;
        }
        Reservation reservation = reservationOptional.get();
        /*if (oldReservation.getIdReservation().getDate()!=newReservation.getIdReservation().getDate()
            || oldReservation.getIdReservation().getTime()!=newReservation.getIdReservation().getTime()
            && Objects.equals(oldReservation.getTableRestaurantDto().getSeats(), newReservation.getTableRestaurantDto().getSeats())){
        reservation.setIdReservation(newReservation.getIdReservation());
        } */
        if (Objects.equals(oldReservation.getTableRestaurantDto().getSeats(), newReservation.getTableRestaurantDto().getSeats())){
            TableRestaurant tableAvailable = tableBrooker.getFirstAvailablebyData(newReservation.getIdReservation().getDate(), newReservation.getIdReservation().getTime(), newReservation.getTableRestaurantDto().getSeats());
            IdReservation idReservation =  new IdReservation.Builder()
                                                            .setIdTable(tableAvailable.getId())
                                                            .setDate(newReservation.getIdReservation().getDate())
                                                            .setTime(newReservation.getIdReservation().getTime())
                                                            .build();
            reservation.setIdReservation(idReservation);
            reservation.setTableRestaurant(tableAvailable);
        }   else {
            reservation.setIdReservation(newReservation.getIdReservation());
        }
        return reservationRepository.save(reservation);
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

    public List<Reservation> getReservations(){
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservation(IdReservation isReservation){
        return reservationRepository.findById(isReservation);
    }

    public Reservation getReservationByData(String surname, LocalDate date, LocalTime time, Integer seats){
        return reservationRepository.findByData(date, time, surname, seats);
    }

    public List<Reservation> getReservationsByCustomer(String name, String surname, Integer seats){
        List<Reservation> listaPrenotazioni;
        try{
            listaPrenotazioni = reservationRepository.findByCustomer(name, surname, seats);
        } catch(Exception e){
            log.error("Prenotazioni non trovate.");
            return null;
        }
        return listaPrenotazioni;
    }
}
