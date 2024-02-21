package com.rocky.roomservice.controllers;

import com.rocky.roomservice.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roomType")
public class RoomTypeController {
    @Autowired
    private RoomTypeService roomTypeService;

//    This function shall be used one time
    @GetMapping("createRoomType")
    public ResponseEntity<String> createRoomType(){
        roomTypeService.createRoomType();
        return new ResponseEntity<>("Created", HttpStatus.OK);
    }
}
