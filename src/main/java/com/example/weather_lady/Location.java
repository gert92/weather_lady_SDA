package com.example.weather_lady;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Location {
        @Id
        @GeneratedValue
        private int id;
        private List<Double> lonLat = new ArrayList<>();
        private String region;
        @Column(nullable = false)
        private String city;
        @Column(nullable = false)
        private String country;


}
