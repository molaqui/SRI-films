package org.example.srifilms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    //@JsonIgnore // Exclure ce champ des réponses JSON // a ignorer dasn affichage
    private String fileName; // Nom du fichier comme identifiant unique

    private String title;
    private Integer year;

    private String cast;

    private String genres;
    private String video;

    private String extract;

}
