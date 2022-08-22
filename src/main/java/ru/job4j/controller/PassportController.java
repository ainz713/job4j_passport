package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Passport;
import ru.job4j.repository.PassportRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class PassportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PassportController.class.getSimpleName());

    private final ObjectMapper objectMapper;

    private final PassportRepository pr;

    public PassportController(ObjectMapper objectMapper, final PassportRepository pr) {
        this.objectMapper = objectMapper;
        this.pr = pr;
    }

    @GetMapping("/find")
    public ResponseEntity<List<Passport>> findAll() {
        List<Passport> rsl = (List<Passport>) this.pr.findAll();
        return new ResponseEntity<>(
                rsl,
                HttpStatus.OK
        );
    }

    @GetMapping("/find?seria={seria}")
    public ResponseEntity<List<Passport>> findBySeria(@PathVariable String seria) {
        Optional<List<Passport>> rsl = this.pr.findBySeria(seria);
        return new ResponseEntity<>(
                rsl.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Passports are not found. Please, check seria.")),
                HttpStatus.OK);
    }

    @GetMapping("/unavaliabe")
    public ResponseEntity<List<Passport>> findUnavaliabe() {
        List<Passport> rsl = this.pr.findUnavailable(LocalDate.now());
        return new ResponseEntity<>(
                rsl,
                HttpStatus.OK
        );
    }

    @GetMapping("/find-replaceable")
    public ResponseEntity<List<Passport>> findReplaceable() {
        List<Passport> rsl = this.pr.findRepalceable(LocalDate.now().plusMonths(3));
        return new ResponseEntity<>(
                rsl,
                HttpStatus.OK
        );
    }

    @PutMapping("/save")
    public ResponseEntity<Passport> save(@Valid @RequestBody Passport passport) {
        return new ResponseEntity<Passport>(
                this.pr.save(passport),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update?id={id}")
    public ResponseEntity<Void> saveById(@PathVariable int id, Passport passport) {
        var em = this.pr.findById(id);
        if (em.isPresent() && passport.getSeria() != null && passport.getNomer() != null
                && passport.getDateOfIssue() != null && passport.getExpDate() != null) {
            Passport e = em.get();
            e.setDateOfIssue(passport.getDateOfIssue());
            e.setExpDate(passport.getExpDate());
            e.setSeria(passport.getSeria());
            e.setNomer(passport.getNomer());
            this.pr.save(e);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Check id and full information about new passport.");
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete?id={id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Passport p = new Passport();
        p.setId(id);
        this.pr.delete(p);
        return ResponseEntity.ok().build();
    }
}
