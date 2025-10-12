package com.Unigym.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Unigym.entities.Instrutor;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long>{
	Instrutor findByEmail(String email);
}
