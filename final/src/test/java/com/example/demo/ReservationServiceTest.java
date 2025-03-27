package com.example.demo;

import com.example.demo.dto.EndReservationDto;
import com.example.demo.enums.CarStatus;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.exception.CarNotAvailableException;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.exception.CredentialNotFoundException;
import com.example.demo.exception.CurrencyNotFoundException;
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
import com.example.demo.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
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
    private RentalPointRepository rentalPointRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private User user;
    private Car car;
    private RentalPoint rentalPoint;
    private Reservation reservation;
    private Currency currency;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setBalance(100.0);
        user.setCurrency("USD");

        car = new Car();
        car.setId(1L);
        car.setStatus(CarStatus.AVAILABLE);
        car.setPricePerMinute(0.5);

        rentalPoint = new RentalPoint();
        rentalPoint.setId(1L);

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
    void startReservation_ShouldThrowException_WhenUserBalanceIsNegative() {
        user.setBalance(-1.0);

        assertThrows(UserException.class, () -> {
            reservationService.startReservation(user, 1L, 1L);
        });
    }

    @Test
    void startReservation_ShouldThrowException_WhenUserHasActiveReservation() {
        when(reservationRepository.findByUserAndStatus(user, ReservationStatus.ACTIVE))
                .thenReturn(Optional.of(reservation));

        assertThrows(UserException.class, () -> {
            reservationService.startReservation(user, 1L, 1L);
        });
    }

    @Test
    void startReservation_ShouldThrowException_WhenCarNotFound() {
        when(carRepository.findCarByIdAndRentalPointId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> {
            reservationService.startReservation(user, 1L, 1L);
        });
    }

    @Test
    void startReservation_ShouldThrowException_WhenCarNotAvailable() {
        car.setStatus(CarStatus.RENTED);
        when(carRepository.findCarByIdAndRentalPointId(1L, 1L)).thenReturn(Optional.of(car));

        assertThrows(CarNotAvailableException.class, () -> {
            reservationService.startReservation(user, 1L, 1L);
        });
    }

    @Test
    void startReservation_ShouldStartReservation_WhenAllConditionsAreMet() {
        when(carRepository.findCarByIdAndRentalPointId(1L, 1L)).thenReturn(Optional.of(car));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        reservationService.startReservation(user, 1L, 1L);

        verify(carRepository, times(1)).findCarByIdAndRentalPointId(1L, 1L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        assertEquals(CarStatus.RENTED, car.getStatus());
    }
    @Test
    void endReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findFirstByUserIdOrderByIdDesc(1L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.endReservation(user, 1L);
        });
    }

    @Test
    void endReservation_ShouldThrowException_WhenRentalPointNotFound() {
        when(reservationRepository.findFirstByUserIdOrderByIdDesc(1L)).thenReturn(Optional.of(reservation));
        when(rentalPointRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RentalPointNotFoundException.class, () -> {
            reservationService.endReservation(user, 1L);
        });
    }

    @Test
    void endReservation_ShouldThrowException_WhenReservationIsNotActive() {
        reservation.setStatus(ReservationStatus.COMPLETED);
        when(reservationRepository.findFirstByUserIdOrderByIdDesc(1L)).thenReturn(Optional.of(reservation));
        when(rentalPointRepository.findById(1L)).thenReturn(Optional.of(rentalPoint));

        assertThrows(ReservationException.class, () -> {
            reservationService.endReservation(user, 1L);
        });
    }

    @Test
    void endReservation_ShouldThrowException_WhenCarIsNotRented() {
        car.setStatus(CarStatus.AVAILABLE);
        when(reservationRepository.findFirstByUserIdOrderByIdDesc(1L)).thenReturn(Optional.of(reservation));
        when(rentalPointRepository.findById(1L)).thenReturn(Optional.of(rentalPoint));

        assertThrows(CarNotAvailableException.class, () -> {
            reservationService.endReservation(user, 1L);
        });
    }

    @Test
    void endReservation_ShouldEndReservation_WhenAllConditionsAreMet() {
        car.setStatus(CarStatus.RENTED);
        user.setCurrency("USD");

        long currentTimeMillis = System.currentTimeMillis();
        reservation.setStartTime(new Timestamp(currentTimeMillis - Duration.ofMinutes(10).toMillis()));

        when(reservationRepository.findFirstByUserIdOrderByIdDesc(user.getId()))
                .thenReturn(Optional.of(reservation));
        when(rentalPointRepository.findById(rentalPoint.getId()))
                .thenReturn(Optional.of(rentalPoint));
        when(currencyRepository.findByCurrencyCode(user.getCurrency()))
                .thenReturn(Optional.of(currency));
        when(reservationMapper.toEndReservationDto(anyDouble()))
                .thenReturn(new EndReservationDto(5.0));

        EndReservationDto result = reservationService.endReservation(user, rentalPoint.getId());


        verify(reservationRepository).findFirstByUserIdOrderByIdDesc(user.getId());
        verify(rentalPointRepository).findById(rentalPoint.getId());
        verify(currencyRepository).findByCurrencyCode(user.getCurrency());
        verify(reservationRepository).save(reservation);

        assertEquals(CarStatus.AVAILABLE, car.getStatus());
        assertEquals(ReservationStatus.COMPLETED, reservation.getStatus());
        assertNotNull(result);
        assertTrue(result.getTotalCost() > 0);
    }
}
