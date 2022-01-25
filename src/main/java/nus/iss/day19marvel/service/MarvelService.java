package nus.iss.day19marvel.service;

import static nus.iss.day19marvel.Constants.API_PUB_KEY;
import static nus.iss.day19marvel.Constants.API_PVT_KEY;
import static nus.iss.day19marvel.Constants.URL_MARVEL_DOMAIN;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nus.iss.day19marvel.Day19marvelApplication;
import nus.iss.day19marvel.model.Marvel;

@Service
public class MarvelService {

    private final Logger logger = Logger.getLogger(Day19marvelApplication.class.getName());
    private String pvtKey;
    private String pubKey;
    private Long ts;
    private String hash;

    public MarvelService() {
        String k1 = System.getenv(API_PVT_KEY);
        String k2 = System.getenv(API_PUB_KEY);
        if (null != k1 && null != k2 && k1.trim().length() > 0 && k2.trim().length() > 0) {
            pvtKey = k1;
            pubKey = k2;
            ts = System.currentTimeMillis();

            String toHash = ts.toString() + pvtKey + pubKey;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(toHash.getBytes());
                byte[] digest = md.digest();
                hash = bytesToHex(digest).toLowerCase();
                logger.log(Level.INFO, "%s", hash);
            } catch (NoSuchAlgorithmException nsae) {
                logger.log(Level.WARNING, "%s", nsae);
            }

        } else {
            pvtKey = "";
            pubKey = "";
            ts = System.currentTimeMillis();
            logger.log(Level.WARNING, "Private and Public keys not set!");
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public ResponseEntity<String> getMarvel(String character) {
        
        final String url = UriComponentsBuilder
            .fromUriString(URL_MARVEL_DOMAIN)
            .queryParam("nameStartsWith", character)
            .queryParam("ts", ts)
            .queryParam("apikey", pubKey)
            .queryParam("hash", hash)
            .toUriString();
        
        final RequestEntity<Void> req = RequestEntity
        .get(url)
        .accept(MediaType.APPLICATION_JSON)
        .build();

        final RestTemplate template = new RestTemplate();
        final ResponseEntity<String> resp = template.exchange(req, String.class);

        return resp;
    }

}
