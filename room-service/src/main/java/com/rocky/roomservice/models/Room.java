package com.rocky.roomservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("room")
public class Room {
    @Id
    private String _id;
    @NotBlank(message = "Room number can not be blank")
    @Positive(message = "Room number can not be negative")
    private Integer number;
    @Positive(message = "Room price can not be blank or negative")
    private Long price;
    private List<Picture> pictures;
    private RoomType roomType;

}
