package br.com.curso.tasks.service.impl;

import br.com.curso.tasks.entity.Guest;
import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.repository.GuestRepository;
import br.com.curso.tasks.service.contract.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GuestServiceImpl implements GuestService {
    private final GuestRepository guestRepository;

    @Override
    public List<Guest> getUserId(Long userId) {
        log.info("Finding guests for user id {}", userId);
        List<Guest> guests = guestRepository.findByUser_Id(userId);
        if (guests.isEmpty()) {
            log.info("Guests not found for user id {}", userId);
            throw new NotFound(
                MessageException.USER_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND);
        }
        log.info("Guests found for user id {}", userId);
        return guests;
    }
}