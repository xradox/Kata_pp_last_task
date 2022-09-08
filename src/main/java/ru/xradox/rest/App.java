package ru.xradox.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.xradox.rest.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class App 
{
    public static void main( String[] args ) {

        RestTemplate restTemplate = new RestTemplate();

        String URL = "http://94.198.50.185:7081/api/users";

        //первый запрос с листом, из которого берем сесшнИд
        ResponseEntity<List<User>> listUsers = restTemplate.exchange(URL, HttpMethod.GET
                , null, new ParameterizedTypeReference<List<User>>() {});

        HttpHeaders headers = listUsers.getHeaders();
        String sessionId = Objects.requireNonNull(headers.get("set-cookie")).get(0);
        HttpHeaders headerWithId = new HttpHeaders();
        headerWithId.setContentType(MediaType.APPLICATION_JSON);
        headerWithId.add("Cookie", sessionId);

        //второй запрос на пост человека
        User user = new User();
        user.setId(3L);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte)18);

        HttpEntity<User> request = new HttpEntity<>(user, headerWithId);

        String response = restTemplate.exchange(URL, HttpMethod.POST ,request, String.class).getBody();

        //третий запрос на апдейт человека
        user.setName("Thomas");
        user.setLastName("Shelby");

        HttpEntity<User> request1 = new HttpEntity<>(user, headerWithId);

        String response1 = restTemplate.exchange(URL, HttpMethod.PUT ,request1, String.class).getBody();

        //четвертый запрос удаление
        String response2 = restTemplate.exchange(URL + "/" + user.getId(),
                HttpMethod.DELETE, request1, String.class).getBody();


        System.out.println(response + response1 + response2);
    }
}
