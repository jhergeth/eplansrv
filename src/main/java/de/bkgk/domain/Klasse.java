package de.bkgk.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Klasse {
    @Id
    @NonNull
    private String kuerzel;

    @NonNull
    private String langname;

    @NonNull
    private String klassenlehrer;

    @NonNull
    private String bigako;

    @NonNull
    private String abteilung;

    private String raum;

    private String bemerkung;

    private String anlage;

    private String alias;
}