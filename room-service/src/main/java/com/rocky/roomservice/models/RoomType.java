package com.rocky.roomservice.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("room_type")
public class RoomType {
    @Id
    private String _id;
    @NotBlank(message = "Name can not be blank")
    private String name;
    private List<String> utilities;
    private Integer capacity;
}
