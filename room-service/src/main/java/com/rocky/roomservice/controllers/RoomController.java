package com.rocky.roomservice.controllers;

import com.rocky.roomservice.dtos.RoomWrapper;
import com.rocky.roomservice.services.RoomService;
import com.rocky.roomservice.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("hi")
    public ResponseEntity<String> sayHi() {
        return new ResponseEntity<>("Hi from room service!", HttpStatus.OK);
    }

//    This function shall be used one time
    @GetMapping("createRoom")
    public ResponseEntity<String> createRoom(){
        roomService.createRoom();
        return new ResponseEntity<>("Created", HttpStatus.OK);
    }

    @GetMapping("assets/images/{name}")
    public ResponseEntity<byte[]> getImage(@PathVariable String name) {
        ClassPathResource imgFile = new ClassPathResource("static/assets/images/" + name);
        byte[] imageData;
        InputStream inputStream;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        try{
            inputStream = imgFile.getInputStream();
            imageData = inputStream.readAllBytes();

        }catch (IOException e){
            System.out.println("No file found!");
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @GetMapping("floor/{number}")
    public ResponseEntity<List<RoomWrapper>> getRoomInFloor(@PathVariable("number") Integer floor){
        return new ResponseEntity<>(roomService.getRoomsByFloor(floor), HttpStatus.OK);
    }

    @GetMapping("{number}")
    public ResponseEntity<RoomWrapper> getRoom(@PathVariable Integer number){
        return new ResponseEntity<>(roomService.getByNumber(number), HttpStatus.OK);
    }
}
