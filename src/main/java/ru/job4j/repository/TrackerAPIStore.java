package ru.job4j.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Passport;

import java.util.Collections;
import java.util.List;

@Repository
public class TrackerAPIStore implements Store {

    @Value("${api.url}")
    private String url;

    private final RestTemplate client;

    @Autowired
    public TrackerAPIStore(RestTemplateBuilder builder) {
        this.client = builder.build();
    }

    @Override
    public List<Passport> findAll() {
        return getList(String.format(
                "%sfind", url
        ));
    }

    @Override
    public List<Passport> findUnavaliabe() {
        return getList(String.format(
                "%sunavaliabe", url
        ));
    }

    @Override
    public List<Passport>  findReplaceable() {
        return getList(String.format(
                "%sfind-replaceable", url
        ));
    }

    @Override
    public List<Passport> findBySeria(String seria) {
        return getList(String.format(
                "%sfind?seria==%s", url, seria
        ));
    }

    @Override
    public Passport save(Passport passport) {
        return client.postForEntity(
                String.format("%s%s", url, "save"), passport, Passport.class
        ).getBody();
    }

    @Override
    public boolean update(int id, Passport passport) {
        return client.exchange(
                String.format("%supdate?id=%s", url, id),
                HttpMethod.PUT,
                new HttpEntity<>(passport),
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    @Override
    public boolean delete(String id) {
        return client.exchange(
                String.format("%sdelete?id=%s", url, id),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    private List<Passport> getList(String url) {
        List<Passport> body = client.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Passport>>() {
                }
        ).getBody();
        return body == null ? Collections.emptyList() : body;
    }
}
