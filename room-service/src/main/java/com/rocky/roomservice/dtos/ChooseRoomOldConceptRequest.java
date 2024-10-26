package com.rocky.roomservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChooseRoomOldConceptRequest {
    private Integer[] excepts;
    private Integer page;
    private List<Integer> filter;
    private Integer adults;
    private Integer children;
    private Integer numberOfRoom;
    private Integer price;
}
