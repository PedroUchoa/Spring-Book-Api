package com.example.book_api.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/google-books")
public class GoogleBookApiController {

    @Value("${google.books.api.key}")
    private String apiKey;

    @Value("${google.books.api.url}")
    private String apiUrl;


    @GetMapping("/id/{id}")
    public String getBooksById (@PathVariable String id){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity(apiUrl+"/"+id, String.class);
        return resp.getBody();
    }


    @GetMapping("/title/{title}")
    public String getBooksByTitle (@PathVariable String title){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity(apiUrl+"?q=intitle:"+title+"&key="+apiKey, String.class);
        return resp.getBody();
    }

    @GetMapping("/author/{author}")
    public String getBooksByAuthor(@PathVariable String author){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity(apiUrl+"?q=inauthor:"+author+"&key="+apiKey, String.class);
        return resp.getBody();
    }

    @GetMapping("/publisher/{publisher}")
    public String getBooksByPublisher(@PathVariable String publisher){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity(apiUrl+"?q=inpublisher:"+publisher+"&key="+apiKey, String.class);
        return resp.getBody();
    }

    @GetMapping("/isbn/{isbn}")
    public String getBooksByIsbn(@PathVariable String isbn){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity(apiUrl+"?q=isbn:"+isbn+"&key="+apiKey, String.class);
        return resp.getBody();
    }

}
