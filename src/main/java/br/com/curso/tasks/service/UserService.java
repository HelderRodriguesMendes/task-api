package br.com.curso.tasks.service;

import br.com.curso.tasks.dto.request.GuestRequestDTO;
import br.com.curso.tasks.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    public User save(User user);
    public User findById(Long id);
    public List<User> findAll();
    public List<User> getUserActive(List<GuestRequestDTO> emails);
    public User findByEmail(String email, boolean validateEmailExists);
    public User update(Long id, User user);
    public void delete(Long id);
}