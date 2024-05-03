package com.example.GestionaleRistorante.service;

import com.example.GestionaleRistorante.dto.ReservationDto;
import com.example.GestionaleRistorante.dto.TableRestaurantDto;
import com.example.GestionaleRistorante.entity.Reservation;
import com.example.GestionaleRistorante.entity.TableRestaurant;
import com.example.GestionaleRistorante.repository.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class TableBrookerService { //table dispatcher

    private final TableRepository tableRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TableBrookerService(TableRepository tableRepository, ModelMapper modelMapper) {
        this.tableRepository = tableRepository;
        this.modelMapper = modelMapper;
    }

    public TableRestaurantDto getFirstAvailablebyData(LocalDate date, LocalTime time, Integer seats){
        try {
            List<TableRestaurant> tableList = tableRepository.findByData(date, time, seats);
            return modelMapper.map(tableList.get(0), TableRestaurantDto.class);
        }   catch(Exception e){
            log.error("No available tables found");
            return null;
        }
    }

    //CRUD OPERATIONS

    //aggiunta prenotazione, bisogna agganciare la prenotazione all'oggetto tavolo e cliente
    public TableRestaurant addTable (TableRestaurant tableRestaurant){
        if(tableRepository.findById(tableRestaurant.getId()).isPresent()){
            log.info(String.format("Table id: %s already exist", tableRestaurant.getId()));
            return null;
        }
        return tableRepository.save(tableRestaurant);
    }

    public TableRestaurant updateTable(TableRestaurantDto tableInputDto){
        Optional<TableRestaurant> tableOptional =  tableRepository.findById(tableInputDto.getId());
        if(tableOptional.isPresent()){
            TableRestaurant table = tableOptional.get();
            if(reservationDto!=null){
                LocalDateTime key = LocalDateTime.of(reservationDto.getIdReservation().getDate(), reservationDto.getIdReservation().getTime());
                table.getReservations().put(key, modelMapper.map(reservationDto, Reservation.class));
            }
            if(tableInput.getSeats()!=null){
                table.setSeats(tableInput.getSeats());
            }
            tableRepository.save(table);
            return table;
        }   else{
                log.error("Table do not exist");
                return null;
            }
    }

    public void deleteTable(Integer id){
        tableRepository.deleteById(id);
    }

    public List<TableRestaurant> getTables(){
        return tableRepository.findAll();
    }

    public Optional<TableRestaurant> getTable(Integer id){
        return tableRepository.findById(id);
    }
}
