package com.rocky.roomservice.services.impl;

import com.rocky.roomservice.dtos.ChooseRoomOldConceptRequest;
import com.rocky.roomservice.dtos.RoomWrapper;
import com.rocky.roomservice.dtos.SuggestRoomsResponse;
import com.rocky.roomservice.models.Picture;
import com.rocky.roomservice.models.Room;
import com.rocky.roomservice.repositories.RoomRepository;
import com.rocky.roomservice.repositories.RoomTypeRepository;
import com.rocky.roomservice.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        for (int i = 0; i < 40; i++) {
            room = new Room();
            room.setNumber(100+i+1);
            room.setPrice((long) (6 + Math.round(Math.random() * 12)) * 50);
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
            room.setNumber(200+i+1);
            room.setPrice((long) (5 + Math.round(Math.random() * 10)) * 50);
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
            room.setNumber(300+i+1);
            room.setPrice((long) (5 + Math.round(Math.random() * 9)) * 50);
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
            room.setNumber(400+i+1);
            room.setPrice((long) (2 + Math.round(Math.random() * 9)) * 50);
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

        }

        roomRepository.saveAll(rooms);
    }

    @Override
    public List<RoomWrapper> getRoomsByFloor(Integer floor) {
        List<RoomWrapper> roomWrappers = new ArrayList<>();
        List<Room> rooms = roomRepository.findRoomByNumberBetween(floor*100, (floor*100)+50);
        for(Room room : rooms){
            ConvertRoomToRoomWrapper(roomWrappers, room);
        }
        roomWrappers.sort(Comparator.comparingInt(RoomWrapper::getNumber));
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

    @Override
    public SuggestRoomsResponse getSuggestRooms(ChooseRoomOldConceptRequest filter) {
        List<String> types = new ArrayList<>();
        List<String> utilities = new ArrayList<>();
        List<Integer> capacities = new ArrayList<>();
        SuggestRoomsResponse response = new SuggestRoomsResponse();
        if(filter.getFilter().contains(0)){
            types.add("Classic room");
        }
        if(filter.getFilter().contains(1)){
            types.add("Couple room");
        }
        if(filter.getFilter().contains(2)){
            types.add("Extra large room");
        }
        if(filter.getFilter().contains(3)){
            utilities.add("Television");
        }
        if(filter.getFilter().contains(4)){
            utilities.add("Free-wifi");
        }
        if(filter.getFilter().contains(5)){
            utilities.add("Fridge");
        }
        if(filter.getFilter().contains(6)){
            utilities.add("A bathtub");
        }
        if(filter.getFilter().contains(7)){
            utilities.add("An extra single bed beside double bed");
        }
        if(filter.getFilter().contains(8)){
            capacities.add(1);
        }
        if(filter.getFilter().contains(9)){
            capacities.add(2);
        }
        if(filter.getFilter().contains(10)){
            capacities.add(3);
        }
        if(!filter.getFilter().contains(0)&&
                !filter.getFilter().contains(1)&&
                !filter.getFilter().contains(2)){
            List<String> allType = new ArrayList<>();
            allType.add("Classic room");
            allType.add("Couple room");
            allType.add("Extra large room");
            types.addAll(allType);
        }
        if(!filter.getFilter().contains(3)&&
                !filter.getFilter().contains(4)&&
                !filter.getFilter().contains(5)&&
                !filter.getFilter().contains(6)&&
                !filter.getFilter().contains(7)
        ){
            List<String> allUtility = new ArrayList<>();
            allUtility.add("Television");
            utilities.addAll(allUtility);
        }
        if(!filter.getFilter().contains(8)&&
                !filter.getFilter().contains(9)&&
                !filter.getFilter().contains(10)
        ){
            List<Integer> allCapacity = new ArrayList<>();
            allCapacity.add(1);
            allCapacity.add(2);
            allCapacity.add(3);
            capacities.addAll(allCapacity);
        }
        Pageable page = PageRequest.of(filter.getPage(), filter.getNumberOfRoom());
        Page<Room> res = this.roomRepository.findRoomByRoomType_NameInAndCapacityInAndUtilitiesInAndNumberNotIn(
                types,
                capacities,
                utilities,
                Arrays.stream(filter.getExcepts()).toList(),
                filter.getPrice(),
                page
        );
        List <RoomWrapper> roomWrappers = new ArrayList<>();
        for(Room room : res.getContent()){
            ConvertRoomToRoomWrapper(roomWrappers, room);
        };
        response.setRooms(roomWrappers);
        response.setTotal(res.getTotalElements());
        return response;
    }

    private void ConvertRoomToRoomWrapper(List<RoomWrapper> roomWrappers, Room room) {
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
        StringBuilder description = new StringBuilder("This room contains these utilities:");
        for(String utl : room.getRoomType().getUtilities()){
            description.append(", ").append(utl);
        }
        roomWrapper.setDescription(description.toString());
        roomWrapper.setCapacity(room.getRoomType().getCapacity());
        roomWrappers.add(roomWrapper);
    }
}
