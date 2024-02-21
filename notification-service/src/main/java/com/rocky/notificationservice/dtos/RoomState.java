package com.rocky.notificationservice.dtos;

import com.rocky.notificationservice.enums.RoomChosen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomState implements Serializable {
    private String from;
    private String to;
    private Integer room;
    private RoomChosen type;
    private String guest;
}
