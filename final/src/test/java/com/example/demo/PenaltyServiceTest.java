package com.example.demo;

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
import com.example.demo.service.PenaltyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PenaltyServiceTest {
    @Mock
    private PenaltyRepository penaltyRepository;

    @Mock
    private PenaltyMapper penaltyMapper;

    @Mock
    private CarRepository carRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PenaltyService penaltyService;

    private Car car;
    private Reservation reservation;
    private User user;
    private PenaltyDto penaltyDto;
    private Currency currency;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setId(1L);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCar(car);

        user = new User();
        user.setId(1L);
        user.setBalance(1000.0);
        user.setCurrency("USD");

        penaltyDto = new PenaltyDto();
        penaltyDto.setAmount(100.0);
        penaltyDto.setReason("Speeding");

        currency = new Currency();
        currency.setCurrencyCode("USD");
        currency.setExchangeRate(1.0);

        car.setReservations(Collections.singletonList(reservation));
        reservation.setUser(user);
    }

    @Test
    void testGetPenalties_Success() {
        when(penaltyRepository.findAll()).thenReturn(Collections.singletonList(new Penalty()));
        when(penaltyMapper.toDtos(anyList())).thenReturn(Collections.singletonList(new PenaltiesDto()));

        List<PenaltiesDto> result = penaltyService.getPenalties();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(penaltyRepository, times(1)).findAll();
    }

    @Test
    void testCreatePenalty_Success() {
        when(carRepository.findCarWithReservationAndUserById(anyLong())).thenReturn(Optional.of(car));
        when(currencyRepository.findByCurrencyCode(anyString())).thenReturn(Optional.of(currency));
        when(penaltyRepository.save(any(Penalty.class))).thenReturn(new Penalty());

        penaltyService.createPenalty(1L, penaltyDto);

        assertEquals(900.0, user.getBalance(), "Баланс пользователя должен быть уменьшен на сумму штрафа");
        verify(penaltyRepository, times(1)).save(any(Penalty.class));
    }

    @Test
    void testCreatePenalty_CarNotFound() {
        when(carRepository.findCarWithReservationAndUserById(anyLong())).thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> {
            penaltyService.createPenalty(1L, penaltyDto);
        });

        assertEquals("Car not found with id: 1", exception.getMessage());
        verify(penaltyRepository, never()).save(any(Penalty.class));
    }

    @Test
    void testCreatePenalty_ReservationNotFound() {
        car.setReservations(Collections.emptyList());
        when(carRepository.findCarWithReservationAndUserById(anyLong())).thenReturn(Optional.of(car));

        ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class, () -> {
            penaltyService.createPenalty(1L, penaltyDto);
        });

        assertEquals("Reservation not found", exception.getMessage());
        verify(penaltyRepository, never()).save(any(Penalty.class));
    }

    @Test
    void testCreatePenalty_CurrencyNotFound() {
        when(carRepository.findCarWithReservationAndUserById(anyLong())).thenReturn(Optional.of(car));
        when(currencyRepository.findByCurrencyCode(anyString())).thenReturn(Optional.empty());

        CredentialNotFoundException exception = assertThrows(CredentialNotFoundException.class, () -> {
            penaltyService.createPenalty(1L, penaltyDto);
        });

        assertEquals("Currency not found", exception.getMessage());
        verify(penaltyRepository, never()).save(any(Penalty.class));
    }
}