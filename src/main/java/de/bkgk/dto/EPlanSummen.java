package de.bkgk.dto;

import de.bkgk.domain.Kollege;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EPlanSummen {
    @NonNull
    private String lehrer;
    @NonNull
    private Kollege kollege;

    private Map<String,Double> bereichsSummen;

    @NonNull
    @Builder.Default private Double gesamt = 0.0;
    @NonNull
    @Builder.Default private Double soll = 0.0;
    @NonNull
    @Builder.Default private Double diff = 0.0;
    @NonNull
    @Builder.Default private Double anrechnungen = 0.0;
    @NonNull
    @Builder.Default private Double anrAnpassung = 0.0;
}
