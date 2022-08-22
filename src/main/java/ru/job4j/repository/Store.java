package ru.job4j.repository;

import ru.job4j.domain.Passport;

import java.util.List;

public interface Store {
    Passport save(Passport passport);

    List<Passport> findAll();

    List<Passport> findUnavaliabe();

    List<Passport>  findReplaceable();

    List<Passport> findBySeria(String seria);

    boolean update(int id, Passport passport);

    boolean delete(String id);
}
