package com.example.demosecurity.models;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private Integer minAge;

    private Integer availableStock;

    private Boolean active;

    @PrePersist
    private void onCreate() {

        if (minAge == null) {
            minAge = 0;
        }

        if (active == null) {
            active = true;
        }

        if (availableStock == null) {
            availableStock = 0;
        }
    }
}
