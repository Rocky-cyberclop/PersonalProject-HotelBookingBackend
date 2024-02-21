package com.rocky.roomservice.services.impl;

import com.rocky.roomservice.models.Picture;
import com.rocky.roomservice.models.Room;
import com.rocky.roomservice.repositories.RoomRepository;
import com.rocky.roomservice.repositories.RoomTypeRepository;
import com.rocky.roomservice.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
//    This function shall be used one time
    public void createRoom() {
        List<Room> rooms = new ArrayList<>();
        Room room;
        List<Picture> pictures;
        Picture picture;
        for (int i = 0; i < 4; i++) {
            room = new Room();
            room.setNumber(((i + 1)*100)+4);
            room.setPrice(700L);
            room.setRoomType(roomTypeRepository.findByName("Extra large room").get(0));
            pictures = new ArrayList<>();
            picture = new Picture();
            picture.setUrl("doubles1.jpg");
            pictures.add(picture);
            picture.setUrl("doubles2.jpg");
            pictures.add(picture);
            picture.setUrl("doubles3.jpg");
            pictures.add(picture);
            room.setPictures(pictures);
            rooms.add(room);

            room = new Room();
            room.setNumber(((i + 1)*100)+5);
            room.setPrice(400L);
            room.setRoomType(roomTypeRepository.findByName("Couple room").get(0));
            pictures = new ArrayList<>();
            picture = new Picture();
            picture.setUrl("double1.jpg");
            pictures.add(picture);
            picture.setUrl("double2.jpg");
            pictures.add(picture);
            picture.setUrl("double3.jpg");
            pictures.add(picture);
            room.setPictures(pictures);
            rooms.add(room);

            room = new Room();
            room.setNumber(((i + 1)*100)+3);
            room.setPrice(200L);
            room.setRoomType(roomTypeRepository.findByName("Classic room").get(0));
            pictures = new ArrayList<>();
            picture = new Picture();
            picture.setUrl("single1.jpg");
            pictures.add(picture);
            picture.setUrl("single2.jpg");
            pictures.add(picture);
            room.setPictures(pictures);
            rooms.add(room);

            room = new Room();
            room.setNumber(((i + 1)*100)+6);
            room.setPrice(200L);
            room.setRoomType(roomTypeRepository.findByName("Classic room").get(0));
            pictures = new ArrayList<>();
            picture = new Picture();
            picture.setUrl("single1.jpg");
            pictures.add(picture);
            picture.setUrl("single2.jpg");
            pictures.add(picture);
            room.setPictures(pictures);
            rooms.add(room);

            room = new Room();
            room.setNumber(((i + 1)*100)+2);
            room.setPrice(250L);
            room.setRoomType(roomTypeRepository.findByName("Classic room").get(0));
            pictures = new ArrayList<>();
            picture = new Picture();
            picture.setUrl("single1.jpg");
            pictures.add(picture);
            picture.setUrl("single2.jpg");
            pictures.add(picture);
            room.setPictures(pictures);
            rooms.add(room);

            room = new Room();
            room.setNumber(((i + 1)*100)+7);
            room.setPrice(180L);
            room.setRoomType(roomTypeRepository.findByName("Classic room").get(0));
            pictures = new ArrayList<>();
            picture = new Picture();
            picture.setUrl("single1.jpg");
            pictures.add(picture);
            picture.setUrl("single2.jpg");
            pictures.add(picture);
            room.setPictures(pictures);
            rooms.add(room);

            room = new Room();
            room.setNumber(((i + 1)*100)+1);
            room.setPrice(500L);
            room.setRoomType(roomTypeRepository.findByName("Couple room").get(0));
            pictures = new ArrayList<>();
            picture = new Picture();
            picture.setUrl("double1.jpg");
            pictures.add(picture);
            picture.setUrl("double2.jpg");
            pictures.add(picture);
            picture.setUrl("double3.jpg");
            pictures.add(picture);
            room.setPictures(pictures);
            rooms.add(room);
        }
        room = new Room();
        room.setNumber(408);
        room.setPrice(300L);
        room.setRoomType(roomTypeRepository.findByName("Small conference room").get(0));
        pictures = new ArrayList<>();
        picture = new Picture();
        picture.setUrl("small-conference.jpg");
        pictures.add(picture);
        room.setPictures(pictures);
        rooms.add(room);

        room = new Room();
        room.setNumber(409);
        room.setPrice(500L);
        room.setRoomType(roomTypeRepository.findByName("Large conference room").get(0));
        pictures = new ArrayList<>();
        picture = new Picture();
        picture.setUrl("large-conference.jpg");
        pictures.add(picture);
        room.setPictures(pictures);
        rooms.add(room);

        roomRepository.saveAll(rooms);
    }
}
