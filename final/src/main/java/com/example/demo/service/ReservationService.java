package com.example.demo.service;

import com.example.demo.dto.EndReservationDto;
import com.example.demo.dto.StartReservationDto;
import com.example.demo.model.enums.CarStatus;
import com.example.demo.model.enums.ReservationStatus;
import com.example.demo.exception.CarNotAvailableException;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.exception.RentalPointNotFoundException;
import com.example.demo.exception.ReservationException;
import com.example.demo.exception.ReservationNotFoundException;
import com.example.demo.exception.UserException;
import com.example.demo.mapper.ReservationMapper;
import com.example.demo.model.Car;
import com.example.demo.model.Currency;
import com.example.demo.model.RentalPoint;
import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.repository.RentalPointRepository;
import com.example.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    public final ReservationRepository reservationRepository;
    public final CarRepository carRepository;
    private final CurrencyRepository currencyRepository;
    private final RentalPointRepository rentalPointRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public StartReservationDto startReservation(User user, Long carId, Long rentalPointId){
        if(user.getBalance()<=0){
            throw new UserException("Balance is negative");
        }

        Optional<Reservation> activeReservation = reservationRepository.findByUserAndStatus(user, ReservationStatus.ACTIVE);
        if (activeReservation.isPresent()) {
            throw new UserException("User already has an active reservation");
        }

        Car car = carRepository.findCarByIdAndRentalPointId(carId,rentalPointId).orElseThrow(() ->
        {
            throw new CarNotFoundException("Car not found with id: " + carId);
        });

        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new CarNotAvailableException("Car is not available for reservation");
        }

        car.setStatus(CarStatus.RENTED);

        Reservation reservation = Reservation.builder()
                .user(user)
                .car(car)
                .startTime(new Timestamp(System.currentTimeMillis()))
                .endTime(new Timestamp(System.currentTimeMillis()))
                .status(ReservationStatus.ACTIVE)
                .build();

        reservationRepository.save(reservation);

        return reservationMapper.toStartReservationDto(reservation);
    }

    @Transactional
    public EndReservationDto endReservation(User user,Long rentalPointId){
        Reservation reservation = reservationRepository.findFirstByUserIdOrderByIdDesc(user.getId())
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        RentalPoint rentalPoint = rentalPointRepository.findById(rentalPointId)
                .orElseThrow(() -> new RentalPointNotFoundException("Rental point not found with id: " + rentalPointId));

        User rentUser = reservation.getUser();

        if(!reservation.getStatus().equals(ReservationStatus.ACTIVE)) {
            throw new ReservationException("Reservation is not active");
        }

        Car car = reservation.getCar();

        if(!car.getStatus().equals(CarStatus.RENTED)) {
            throw new CarNotAvailableException("Car is not rented");
        }

        car.setStatus(CarStatus.AVAILABLE);
        car.setRentalPoint(rentalPoint);

        reservation.setEndTime(new Timestamp(System.currentTimeMillis()));
        reservation.setStatus(ReservationStatus.COMPLETED);

        Instant startInstant = reservation.getStartTime().toInstant();
        Instant endInstant = reservation.getEndTime().toInstant();

        Duration duration = Duration.between(startInstant, endInstant);
        long minutes = duration.toMinutes();

        double amount = minutes * car.getPricePerMinute();

        reservation.setTotalCost(amount);

        Currency currency = currencyRepository.findByCurrencyCode(rentUser.getCurrency())
                .orElseThrow(() ->  new CredentialNotFoundException("Currency not found"));

        double totalCost = amount * currency.getExchangeRate();

        double newBalance = user.getBalance() - totalCost;

        rentUser.setBalance(newBalance);

        reservationRepository.save(reservation);

        return reservationMapper.toEndReservationDto(totalCost);
    }
}
