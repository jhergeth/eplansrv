package de.bkgk.domain;

import com.google.common.collect.ImmutableList;
import io.micronaut.data.annotation.Relation;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kollege {

    @Id
    @NotNull
    private String kuerzel;

    private String vorname;

    private String nachname;

    private String mailadresse;

    private String abteilung;

    @Builder.Default private Integer geschlecht = 1;

    @Builder.Default private Double soll = 0.0;

    @Builder.Default private Double anr = 0.0;

    @OneToMany
    @Singular("fach")private Set<Fach> faecher;
}