package com.rocky.reservationservice.feigns;

import com.rocky.reservationservice.dtos.RoomWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ROOM-SERVICE")
public interface RoomFeign {
    @GetMapping("api/room/{number}")
    public ResponseEntity<RoomWrapper> getRoom(@PathVariable Integer number);
}
