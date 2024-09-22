package com.rocky.reservationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChooseRoomOldConceptRequestForRoomService {
    private Integer[] excepts;
    private Integer page;
    private List<Integer> filter;
    private Integer numberOfRoom;
}
