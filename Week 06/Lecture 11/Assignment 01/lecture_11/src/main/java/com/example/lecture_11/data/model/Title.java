package com.example.lecture_11.data.model;

import java.time.LocalDate;

import com.example.lecture_11.data.model.composite.TitleId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "titles")
@NoArgsConstructor
@AllArgsConstructor
public class Title {

    @EmbeddedId
    private TitleId id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate toDate;
}
