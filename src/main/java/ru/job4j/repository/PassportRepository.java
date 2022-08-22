package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Passport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PassportRepository extends CrudRepository<Passport, Integer> {
    Optional<List<Passport>> findBySeria(String seria);

    @Query("SELECT p FROM Passport p WHERE p.expDate <= :date")
    List<Passport> findUnavailable(LocalDate date);

    @Query("SELECT p FROM Passport p WHERE :date <= p.expDate")
    List<Passport> findRepalceable(LocalDate date);
}
