package com.example.demo.service;

import com.example.demo.dto.EndReservationDto;
import com.example.demo.enums.CarStatus;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.exception.CarNotAvailableException;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.exception.ReservationException;
import com.example.demo.exception.ReservationNotFoundException;
import com.example.demo.exception.UserException;
import com.example.demo.mapper.ReservationMapper;
import com.example.demo.model.Car;
import com.example.demo.model.Currency;
import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    public final ReservationRepository reservationRepository;
    public final CarRepository carRepository;
    private final CurrencyRepository currencyRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public void startReservation(User user, Long carId){
        log.info("User {} is attempting to start a reservation for car id: {}", user.getId(), carId);

        if(user.getBalance()<=0){
            log.error("User {} has a negative balance: {}", user.getId(), user.getBalance());
            throw new UserException("Balance is negative");
        }

        Optional<Reservation> activeReservation = reservationRepository.findByUserAndStatus(user, ReservationStatus.ACTIVE);
        if (activeReservation.isPresent()) {
            log.error("User {} already has an active reservation", user.getId());
            throw new UserException("User already has an active reservation");
        }

        Car car = carRepository.findById(carId).orElseThrow(() ->
        {
            log.error("Car not found with id: {}", carId);
            throw new CarNotFoundException("Car not found with id: " + carId);
        });

        if (car.getStatus() != CarStatus.AVAILABLE) {
            log.error("Car with id: {} is not available for reservation", carId);
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
        log.info("Reservation started successfully for user {} with car id: {}", user.getId(), carId);
    }

    @Transactional
    public EndReservationDto endReservation(User user){
        log.info("User {} is attempting to end their reservation", user.getId());

        Reservation reservation = reservationRepository.findFirstByUserIdOrderByIdDesc(user.getId())
                .orElseThrow(() ->
                {
                    log.error("Reservation not found for user {}", user.getId());
                    return new ReservationNotFoundException("Reservation not found");
                });

        User rentUser = reservation.getUser();

        if(!reservation.getStatus().equals(ReservationStatus.ACTIVE)) {
            log.error("Reservation for user {} is not active", user.getId());
            throw new ReservationException("Reservation is not active");
        }

        Car car = reservation.getCar();

        if(!car.getStatus().equals(CarStatus.RENTED)) {
            log.error("Car with id {} is not rented", car.getId());
            throw new CarNotAvailableException("Car is not rented");
        }

        car.setStatus(CarStatus.AVAILABLE);

        reservation.setEndTime(new Timestamp(System.currentTimeMillis()));
        reservation.setStatus(ReservationStatus.COMPLETED);

        Instant startInstant = reservation.getStartTime().toInstant();
        Instant endInstant = reservation.getEndTime().toInstant();

        Duration duration = Duration.between(startInstant, endInstant);
        long minutes = duration.toMinutes();

        double amount = minutes * car.getPricePerMinute();

        reservation.setTotalCost(amount);

        Currency currency = currencyRepository.findByCurrencyCode(rentUser.getCurrency())
                .orElseThrow(() ->
                {
                    log.error("Currency not found for user currency code: {}", user.getCurrency());
                    return new CredentialNotFoundException("Currency not found");
                });

        double newBalance = user.getBalance() - currency.getExchangeRate() * amount;

        rentUser.setBalance(newBalance);

        reservationRepository.save(reservation);

        log.info("Reservation completed successfully for user {}. Total cost: {}", user.getId(), amount);

        return new EndReservationDto(currency.getExchangeRate() * amount);
    }
}
