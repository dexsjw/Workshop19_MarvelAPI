package nus.iss.day19marvel.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.day19marvel.Day19marvelApplication;
import nus.iss.day19marvel.service.MarvelService;

@RestController
public class MarvelController {

    private final Logger logger = Logger.getLogger(Day19marvelApplication.class.getName());

    @Autowired
    public MarvelService marsvc;

    @GetMapping(path="/marvel")
    public ResponseEntity<String> getMarvel(@RequestParam String character) {
        logger.log(Level.INFO, "HIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        ResponseEntity<String> re = marsvc.getMarvel(character);
        return re;
    }
    
}
