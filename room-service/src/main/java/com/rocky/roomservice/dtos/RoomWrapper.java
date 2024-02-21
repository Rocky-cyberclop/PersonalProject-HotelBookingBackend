package com.rocky.roomservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomWrapper {
    private String title;
    private Integer number;
    private List<PictureWrapper> pictures;
    private Long price;
    private String description;
    private Boolean booked;
}
