package de.bkgk.domain;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.bkgk.responses.PivotTable;
import de.bkgk.util.EPLAN;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@JdbcRepository
public abstract class AnrechungRepository implements CrudRepository<Anrechnung, Long> {
    private PivotTable anrPivot = null;
    private String[] kuka = null;
    private String[] anra = null;
    private Double[][] dData = null;

    public PivotTable getAnrechnungPivot(){
        if(dData == null ){
            calcAnrechnungPivot();
        }
        if(anrPivot == null ){
            genStringPivot();
        }
        return anrPivot;
    }

    public Double getAnrechnungKuK(String kuk){
        if(dData == null ){
            calcAnrechnungPivot();
        }

        for(int i = 0; i < kuka.length; i++){
            if(kuka[i].compareToIgnoreCase(kuk) == 0){
                return dData[i][0];
            }
        }
        return 0.0;
    }

    public void calcAnrechnungPivot(){
        List<String> kukl = StreamSupport.stream(findAll().spliterator(),false)
                .map( Anrechnung::getLehrer )
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        kuka = kukl.toArray(new String[0]);

        List<String> anrl = StreamSupport.stream(findAll().spliterator(),false)
                .map( Anrechnung::getGrund )
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        anra = anrl.toArray(new String[0]);

        dData = new Double[kuka.length][anra.length+1];

        for(int r = 0; r < kuka.length; r++){
            for(int c = 0; c < anra.length+1; c++) {
                dData[r][c] = 0.0;
            }
        }

        findAll().forEach(a -> {
            if(!a.getBeginn().isAfter(EPLAN.MINDATE) && !a.getEnde().isBefore(EPLAN.MAXDATE)){
                int r = kukl.indexOf(a.getLehrer());
                int c = anrl.indexOf(a.getGrund())+1;
                dData[r][c] += a.getWwert();
                dData[r][0] += a.getWwert();
            }
        });

        anrPivot = null;
    }

    private void genStringPivot(){
        String[][] sData = new String[kuka.length+1][anra.length+2];
        for(int i = 0; i < kuka.length; i++){
            sData[i+1][0] = kuka[i];
        }

        sData[0][0] = "";
        sData[0][1] = "Sum";
        for(int i = 0; i < anra.length; i++){
            sData[0][i+2] = anra[i];
        }

        for(int r = 0; r < kuka.length; r++){
            for(int c = 0; c < anra.length; c++) {
                sData[r+1][c+1] = dData[r][c] != 0.0 ? Double.toString(dData[r][c]) : "";
            }
        }

        anrPivot = new PivotTable(kuka, anra, sData);
    }
}
