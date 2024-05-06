package com.beginner.rest.webservices.restfulwebservices.hibernate;

import com.beginner.rest.webservices.restfulwebservices.hibernate.dao.Driver;
import com.beginner.rest.webservices.restfulwebservices.hibernate.dao.Sponsor;
import com.beginner.rest.webservices.restfulwebservices.hibernate.repository.IDriverRepository;
import com.beginner.rest.webservices.restfulwebservices.hibernate.repository.ISponsorRepository;
import com.beginner.rest.webservices.restfulwebservices.socialmedia.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("moto-gp")
public class MotoGPController {
    @Autowired
    IDriverRepository iDriverRepository;
    @Autowired
    ISponsorRepository iSponsorRepository;

    @GetMapping("/drivers")
    public List<Driver> getDrivers() {
        return iDriverRepository.findAll();
    }

    @GetMapping("/drivers/{id}")
    public Driver getDriverById(@PathVariable Integer id) {
        Optional<Driver> driver = iDriverRepository.findById(id);
        if (driver.isEmpty()) {
            throw new UserNotFoundException("id: " + id);
        }
        return driver.get();
    }

    @PostMapping("/drivers")
    public ResponseEntity<Driver> postDriver(@RequestBody Driver driver) {
        Driver driverSaved = iDriverRepository.save(driver);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}") // adds /{id}
                .buildAndExpand(driverSaved.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/drivers/{id}")
    public void deleteDriverById(@PathVariable Integer id) {
        iDriverRepository.deleteById(id);
    }

    /**
     * for sponsors
     */
    @GetMapping("/drivers/{id}/sponsors")
    public List<Sponsor> getSponsor(@PathVariable Integer id) {
        return iDriverRepository.findById(id).orElseThrow().getSponsorList();
    }

    @PostMapping("/drivers/{id}/sponsors")
    public ResponseEntity<Sponsor> postSponsor(@PathVariable Integer id, @RequestBody Sponsor sponsor) {
        Driver driver = this.getDriverById(id);
        sponsor.setDriver(driver);

        Sponsor savedSponsor = iSponsorRepository.save(sponsor);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}") // adds /{id}
                .buildAndExpand(savedSponsor.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
