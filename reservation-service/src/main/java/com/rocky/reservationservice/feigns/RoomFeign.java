package com.rocky.reservationservice.feigns;

import com.rocky.reservationservice.dtos.ChooseRoomOldConceptRequestForRoomService;
import com.rocky.reservationservice.dtos.RoomWrapper;
import com.rocky.reservationservice.dtos.SuggestRoomsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("ROOM-SERVICE")
public interface RoomFeign {
    @GetMapping("api/room/{number}")
    public ResponseEntity<RoomWrapper> getRoom(@PathVariable Integer number);

    @PostMapping("api/room/suggest")
    public ResponseEntity<SuggestRoomsResponse> getSuggetsRooms(@RequestBody ChooseRoomOldConceptRequestForRoomService request);
}
