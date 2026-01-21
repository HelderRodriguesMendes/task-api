package br.com.curso.tasks.service;

import br.com.curso.tasks.entity.Guest;

import java.util.List;

public interface GuestService {
    public List<Guest> getUserId(Long userId);
}