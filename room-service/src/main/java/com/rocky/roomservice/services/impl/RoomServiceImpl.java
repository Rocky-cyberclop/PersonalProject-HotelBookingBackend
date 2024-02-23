package com.rocky.roomservice.services.impl;

import com.rocky.roomservice.dtos.RoomWrapper;
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
            picture = new Picture();
            picture.setUrl("doubles2.jpg");
            pictures.add(picture);
            picture = new Picture();
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
            picture = new Picture();
            picture.setUrl("double2.jpg");
            pictures.add(picture);
            picture = new Picture();
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
            picture = new Picture();
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
            picture = new Picture();
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
            picture = new Picture();
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
            picture = new Picture();
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
            picture = new Picture();
            picture.setUrl("double2.jpg");
            pictures.add(picture);
            picture = new Picture();
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

    @Override
    public List<RoomWrapper> getRoomsByFloor(Integer floor) {
        List<RoomWrapper> roomWrappers = new ArrayList<>();
        List<Room> rooms = roomRepository.findRoomByNumberBetween(floor*100, (floor*100)+50);
        for(Room room : rooms){
            RoomWrapper roomWrapper = new RoomWrapper();
            roomWrapper.setTitle("");
            roomWrapper.setNumber(room.getNumber());
            List<String> images = new ArrayList<>();
            for(Picture picture : room.getPictures()){
                images.add(picture.getUrl());
            }
            roomWrapper.setImages(images);
            roomWrapper.setBooked(false);
            roomWrapper.setPrice(room.getPrice());
            StringBuilder description = new StringBuilder("This room contain:\n");
            for(String utl : room.getRoomType().getUtilities()){
                description.append(utl).append("\n");
            }
            roomWrapper.setDescription(description.toString());
            roomWrapper.setCapacity(room.getRoomType().getCapacity());
            roomWrappers.add(roomWrapper);
        }
        return roomWrappers;
    }

    @Override
    public RoomWrapper getByNumber(Integer number) {
        RoomWrapper roomWrapper = new RoomWrapper();
        if(!roomRepository.findRoomByNumber(number).isEmpty()) {
            Room room = roomRepository.findRoomByNumber(number).get(0);
            roomWrapper.setNumber(room.getNumber());
            roomWrapper.setPrice(room.getPrice());
            roomWrapper.setCapacity(room.getRoomType().getCapacity());
        }
        return roomWrapper;
    }
}
