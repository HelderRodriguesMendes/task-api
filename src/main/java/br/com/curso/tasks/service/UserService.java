package br.com.curso.tasks.service;

import br.com.curso.tasks.entity.User;

import java.util.List;


public interface UserService {
    public User save(User user);
    public User findById(Long id);
    public List<User> findAll();
    public List<User> findByEmail(List<String> emails);
    public void validateEmailNotExists(String email);
    public User update(Long id, User user);
    public void delete(Long id);
}