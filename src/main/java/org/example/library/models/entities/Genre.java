package org.example.library.models.entities;

import jakarta.persistence.*;
import org.example.library.models.enums.EGenre;

@Entity
@Table(name = "genres")
public class Genre extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private EGenre genreType;


    public EGenre getGenreType() {
        return genreType;
    }

    public void setGenreType(EGenre genreType) {
        this.genreType = genreType;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreType=" + genreType +
                '}';
    }
}
