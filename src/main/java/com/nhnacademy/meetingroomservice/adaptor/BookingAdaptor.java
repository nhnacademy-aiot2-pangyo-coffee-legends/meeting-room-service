package com.nhnacademy.meetingroomservice.adaptor;

import com.nhnacademy.meetingroomservice.dto.EntryRequest;
import com.nhnacademy.meetingroomservice.dto.EntryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="booking-service", url="http://localhost:10257", path="/api/v1/bookings")
public interface BookingAdaptor {

    @PostMapping("/verify")
    ResponseEntity<EntryResponse> checkBooking(@RequestHeader("X-USER") String email, @RequestBody EntryRequest entryRequest);
}
