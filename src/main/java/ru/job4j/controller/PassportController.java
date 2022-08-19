package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Passport;
import ru.job4j.repository.PassportRepository;
import java.util.List;

@RestController
@RequestMapping("/passport")
public class PassportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PassportController.class.getSimpleName());

    private final ObjectMapper objectMapper;

    private BCryptPasswordEncoder encoder;

    private final PassportRepository pr;

    public PassportController(ObjectMapper objectMapper, final PassportRepository pr, BCryptPasswordEncoder encoder) {
        this.objectMapper = objectMapper;
        this.pr = pr;
        this.encoder = encoder;
    }

    public PassportController(ObjectMapper objectMapper, PassportRepository pr) {
        this.objectMapper = objectMapper;
        this.pr = pr;
    }

    @GetMapping("/")
    public ResponseEntity<List<Passport>> findAll() {
        List<Passport> rsl = (List<Passport>) this.pr.findAll();
        return new ResponseEntity<>(
                rsl,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passport> findById(@PathVariable int id) {
        if (id > 5) {
            throw new IllegalArgumentException(
                    "Invalid id. Id must be < 5.");
        }
        var person = this.pr.findById(id);
        return new ResponseEntity<>(
                person.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Person is not found. Please, check id.")),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Passport p = new Passport();
        p.setId(id);
        this.pr.delete(p);
        return ResponseEntity.ok().build();
    }
}
