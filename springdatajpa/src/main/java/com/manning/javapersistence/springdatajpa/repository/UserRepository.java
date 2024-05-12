package com.manning.javapersistence.springdatajpa.repository;

import com.manning.javapersistence.springdatajpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	List<User> findAllByOrderByUsernameAsc();
	List<User> findByRegistrationDateBetween(LocalDate start, LocalDate end);

	List<User> findByUsernameAndEmail(String username, String email);
}