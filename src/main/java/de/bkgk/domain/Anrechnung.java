package de.bkgk.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

//  GPU020.TXT DIF-Datei Anrechnungen:
//Nr.        Feld
//1        Nummer *1)
//2        Fremdschlüssel
//3        Statistikkennzeichen
//4        Lehrer (Kurzname)
//5        Anrechnungsgrund (Kurzname)
//6        Wochenwert
//7        Beginndatum (JJJJMMTT)
//8        Enddatum (JJJJMMTT)
//9        Text
//10        Jahreswert
//11        Prozent (%)
//12        Prozenzbasis ('U' für Unterricht oder 'S' für Jahressoll)
//*1) muß angegeben sein (<= 0 neu anlegen, > 0 sofern vorhanden überschreiben sonst neu anlegen)

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class  Anrechnung {
    @Id
    private Long id;

    @NotNull
    private String lehrer;

    @NotNull
    private String grund;

    private Double wwert;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 00:00")
    private LocalDate beginn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 00:00")
    private LocalDate ende;

    private String text;

    private Double jwert;
}
