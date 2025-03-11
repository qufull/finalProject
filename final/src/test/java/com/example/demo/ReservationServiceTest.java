package com.example.demo;

import com.example.demo.dto.EndReservationDto;
import com.example.demo.enums.CarStatus;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.exception.CarNotAvailableException;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.exception.CurrencyNotFoundException;
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
import com.example.demo.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private User user;
    private Car car;
    private Reservation reservation;
    private Currency currency;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setBalance(1000.0);
        user.setCurrency("USD");

        car = new Car();
        car.setId(1L);
        car.setPricePerMinute(0.5);
        car.setStatus(CarStatus.AVAILABLE);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setCar(car);
        reservation.setStartTime(new Timestamp(System.currentTimeMillis()));
        reservation.setStatus(ReservationStatus.ACTIVE);

        currency = new Currency();
        currency.setCurrencyCode("USD");
        currency.setExchangeRate(1.0);
    }

    @Test
    void testStartReservation_Success() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        when(reservationRepository.findByUserAndStatus(any(User.class), any(ReservationStatus.class)))
                .thenReturn(Optional.empty());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        reservationService.startReservation(user, 1L);

        assertEquals(CarStatus.RENTED, car.getStatus());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testStartReservation_NegativeBalance() {
        user.setBalance(-100.0);

        UserException exception = assertThrows(UserException.class, () -> {
            reservationService.startReservation(user, 1L);
        });

        assertEquals("Balance is negative", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testStartReservation_ActiveReservationExists() {
        when(reservationRepository.findByUserAndStatus(any(User.class), any(ReservationStatus.class)))
                .thenReturn(Optional.of(reservation));

        UserException exception = assertThrows(UserException.class, () -> {
            reservationService.startReservation(user, 1L);
        });

        assertEquals("User already has an active reservation", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testStartReservation_CarNotFound() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> {
            reservationService.startReservation(user, 1L);
        });

        assertEquals("Car not found with id: 1", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testStartReservation_CarNotAvailable() {
        car.setStatus(CarStatus.RENTED);
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));

        CarNotAvailableException exception = assertThrows(CarNotAvailableException.class, () -> {
            reservationService.startReservation(user, 1L);
        });

        assertEquals("Car is not available for reservation", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testEndReservation_Success() {
        car.setStatus(CarStatus.RENTED);

        when(reservationRepository.findFirstByUserIdOrderByIdDesc(anyLong())).thenReturn(Optional.of(reservation));
        when(currencyRepository.findByCurrencyCode(anyString())).thenReturn(Optional.of(currency));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        EndReservationDto result = reservationService.endReservation(user);

        assertNotNull(result);
        assertEquals(CarStatus.AVAILABLE, car.getStatus(), "Статус автомобиля должен быть AVAILABLE");
        assertEquals(ReservationStatus.COMPLETED, reservation.getStatus(), "Статус бронирования должен быть COMPLETED");
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testEndReservation_ReservationNotFound() {
        when(reservationRepository.findFirstByUserIdOrderByIdDesc(anyLong())).thenReturn(Optional.empty());

        ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.endReservation(user);
        });

        assertEquals("Reservation not found", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testEndReservation_ReservationNotActive() {
        reservation.setStatus(ReservationStatus.COMPLETED);
        when(reservationRepository.findFirstByUserIdOrderByIdDesc(anyLong())).thenReturn(Optional.of(reservation));

        ReservationException exception = assertThrows(ReservationException.class, () -> {
            reservationService.endReservation(user);
        });

        assertEquals("Reservation is not active", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testEndReservation_CarNotRented() {
        car.setStatus(CarStatus.AVAILABLE);
        when(reservationRepository.findFirstByUserIdOrderByIdDesc(anyLong())).thenReturn(Optional.of(reservation));

        CarNotAvailableException exception = assertThrows(CarNotAvailableException.class, () -> {
            reservationService.endReservation(user);
        });

        assertEquals("Car is not rented", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testEndReservation_CurrencyNotFound() {
        car.setStatus(CarStatus.RENTED);

        when(reservationRepository.findFirstByUserIdOrderByIdDesc(anyLong())).thenReturn(Optional.of(reservation));
        when(currencyRepository.findByCurrencyCode(anyString())).thenReturn(Optional.empty());

        CredentialNotFoundException exception = assertThrows(CredentialNotFoundException.class, () -> {
            reservationService.endReservation(user);
        });

        assertEquals("Currency not found", exception.getMessage());

        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}

