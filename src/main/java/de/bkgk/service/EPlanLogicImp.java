package de.bkgk.service;

import de.bkgk.domain.*;
import de.bkgk.dto.EPlanSummen;
import de.bkgk.responses.ListResponse;
import de.bkgk.util.EPLAN;
import de.bkgk.util.Link;
import de.bkgk.util.Node;
import io.micronaut.context.annotation.Property;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;


@Singleton
public class EPlanLogicImp implements EPlanLogic {
    private static final Logger LOG = LoggerFactory.getLogger(EPlanLogicImp.class);
    private final EPlanRepository ePlanRep;
    private final KlasseRepository klasseRep;
    private final KollegeRepository kollegeRep;
    private final AnrechungRepository anrechungRepository;

    @Property(name = "eplan.bereiche")
    String[] bereiche;

    public EPlanLogicImp(EPlanRepository ePlanRepository,
                         KlasseRepository klasseRepository,
                         KollegeRepository kollegeRepository,
                         AnrechungRepository anrechungRepository) {
        this.ePlanRep = ePlanRepository;
        this.klasseRep = klasseRepository;
        this.kollegeRep = kollegeRepository;
        this.anrechungRepository = anrechungRepository;

        LOG.info("Constructing.");
    }

    @PostConstruct
    public void initialize() {
        LOG.info("Finalizing configuration.");
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList(
            "datum", "stunde", "absenznummer", "unterrichtsnummer", "absLehrer", "vertLehrer",
            "absFach", "vertFach", "absRaum", "vertRaum", "absKlassen", "vertKlassen", "absGrund",
            "vertText", "vertArt", "lastChange", "sendMail"
    );

    @Override
    public List <String> getBereiche(){
        if(bereiche.length == 0){
            return List.of("BauH", "ETIT", "JVA", "AV", "AIF", "FOS", "ErnPfl", "SozKi", "GesSoz");
        }
        return List.of(bereiche);
    }

    /*
        EPLAN
     */

    @Override
    public List<EPlan> getEPlan(String bereich){
        return ePlanRep.findBySchuleAndBereich(EPLAN.SCHULE, bereich);
    }

    @Override
    public List<EPlanSummen> getSummen(){
        Map<String,EPlanSummen> epsMap = new HashMap<>();

        Iterable<Kollege> kList = kollegeRep.findAll();
        for(Kollege k : kList){
            final String kuk = k.getKuerzel();
            EPlanSummen eps = epsMap.get(kuk);
            if(eps == null){
//                eps = new EPlanSummen(k.getKuerzel(), k, new HashMap<String,Double>(), 0.0, k.getSoll(), 0.0);
                List<EPlan> kukEPLs = ePlanRep.findBySchuleAndLehrer(EPLAN.SCHULE, kuk);
                Map<String,Double> kukInBer = kukEPLs.stream()
                        .reduce(
                                new HashMap<String,Double>(),
                                (m,epl) ->{
                                    m.merge(epl.getBereich(), epl.getWstd(), (v1,v2) -> v1 + v2);
                                    return m;
                                },
                                (m1,m2) -> {
                                    for(Map.Entry<String,Double> e : m2.entrySet()){
                                        m1.merge(e.getKey(),e.getValue(), (v1,v2) -> v1 + v2);
                                    }
                                    return m1;
                                }
                        );
                Double ist = kukInBer.entrySet().stream().reduce(0.0, (v,e) -> v+e.getValue(),(v1,v2) -> v1+v2);
                Double anr = anrechungRepository.getAnrechnungKuK(kuk);
                Double diff = ist + anr - k.getSoll();
                eps = EPlanSummen.builder()
                        .lehrer(kuk)
                        .kollege(k)
                        .bereichsSummen(kukInBer)
                        .soll(k.getSoll())
                        .gesamt(ist)
                        .diff(diff)
                        .anrechnungen(anr)
                        .build();
                epsMap.put(kuk, eps);
            }
        }
        List<EPlanSummen> epsList = new ArrayList(epsMap.values());
        Collections.sort(epsList, (a,b) -> a.getLehrer().compareToIgnoreCase(b.getLehrer()));
        return epsList;
    }


}
