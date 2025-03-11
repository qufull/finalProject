package com.example.demo.service;

import com.example.demo.dto.PenaltiesDto;
import com.example.demo.dto.PenaltyDto;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.exception.ReservationNotFoundException;
import com.example.demo.mapper.PenaltyMapper;
import com.example.demo.model.Car;
import com.example.demo.model.Currency;
import com.example.demo.model.Penalty;
import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.repository.PenaltyRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final PenaltyMapper penaltyMapper;
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    public List<PenaltiesDto> getPenalties() {
        return penaltyMapper.toDtos(penaltyRepository.findAll());
    }

    @Transactional
    public void createPenalty(Long carId, PenaltyDto dto) {
        log.info("Creating penalty for car with id: {}", carId);

        Car car = carRepository.findCarWithReservationAndUserById(carId)
                .orElseThrow(() ->
                {
                    log.error("Car not found with id: {}", carId);
                    return new CarNotFoundException("Car not found with id: " + carId);
                });

        Reservation reservation = car.getReservations().stream()
                .findFirst().orElseThrow(() ->
                {
                    log.error("Reservation not found for car id: {}", carId);
                    return new ReservationNotFoundException("Reservation not found");
                });

        User user = reservation.getUser();

        Penalty penalty = Penalty.builder()
                .amount(dto.getAmount())
                .car(car)
                .reservation(reservation)
                .reason(dto.getReason())
                .dateIssued(new Timestamp(System.currentTimeMillis()))
                .build();

        Currency currency = currencyRepository.findByCurrencyCode(user.getCurrency())
                .orElseThrow(() ->
                {
                    log.error("Currency not found for user currency code: {}", user.getCurrency());
                    return new CredentialNotFoundException("Currency not found");
                });

        double newBalance = user.getBalance() - currency.getExchangeRate() * dto.getAmount();

        user.setBalance(newBalance);

        penaltyRepository.save(penalty);
        log.info("Penalty created successfully for user: {} with amount: {}", user.getId(), dto.getAmount());
    }
}
