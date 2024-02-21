package com.rocky.roomservice.services.impl;

import com.rocky.roomservice.models.RoomType;
import com.rocky.roomservice.repositories.RoomTypeRepository;
import com.rocky.roomservice.services.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

//    This function shall be used one time
    @Override
    public void createRoomType(){
        List<RoomType> roomTypeList = new ArrayList<>();

        RoomType roomType1 = new RoomType();
        roomType1.setName("Classic room");
        roomType1.setCapacity(1);
        List<String> utilities1 = new ArrayList<>();
        utilities1.add("Television");
        utilities1.add("Free-wifi");
        utilities1.add("Fridge");
        roomType1.setUtilities(utilities1);
        roomTypeList.add(roomType1);

        RoomType roomType2 = new RoomType();
        roomType2.setName("Couple room");
        roomType2.setCapacity(2);
        List<String> utilities2 = new ArrayList<>();
        utilities2.add("Television");
        utilities2.add("Free-wifi");
        utilities2.add("Fridge");
        utilities2.add("A bathtub");
        roomType2.setUtilities(utilities2);
        roomTypeList.add(roomType2);

        RoomType roomType3 = new RoomType();
        roomType3.setName("Extra large room");
        roomType3.setCapacity(3);
        List<String> utilities3 = new ArrayList<>();
        utilities3.add("Television");
        utilities3.add("Free-wifi");
        utilities3.add("Fridge");
        utilities3.add("A bathtub");
        utilities3.add("An extra single bed beside double bed");
        roomType3.setUtilities(utilities3);
        roomTypeList.add(roomType3);

        RoomType roomType4 = new RoomType();
        roomType4.setName("Small conference room");
        roomType4.setCapacity(1);
        List<String> utilities4 = new ArrayList<>();
        utilities4.add("Television");
        utilities4.add("Free-wifi");
        utilities4.add("A small conference board");
        utilities4.add("A small round table");
        roomType4.setUtilities(utilities4);
        roomTypeList.add(roomType4);

        RoomType roomType5 = new RoomType();
        roomType5.setName("Large conference room");
        roomType5.setCapacity(2);
        List<String> utilities5 = new ArrayList<>();
        utilities5.add("Television");
        utilities5.add("Free-wifi");
        utilities5.add("A projector");
        utilities5.add("A big square table");
        roomType5.setUtilities(utilities5);
        roomTypeList.add(roomType5);

        roomTypeRepository.saveAll(roomTypeList);
    }
}
