package de.bkgk.controller;

import de.bkgk.domain.EPlan;
import de.bkgk.domain.EPlanRepository;
import de.bkgk.dto.EPlanSummen;
import de.bkgk.service.EPlanLogic;
import de.bkgk.service.EPlanLoader;
import de.bkgk.util.EPLAN;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Controller("/api/eplan")
public class EplanController   extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(EplanController.class);

    private final EPlanLogic ePlanLogic;
    private final EPlanLoader eplLoader;
    private final EPlanRepository ePlanRepository;

    public EplanController(EPlanLogic eplanLogic, EPlanLoader eplLoader, EPlanRepository ePlanRepository){
        this.ePlanLogic = eplanLogic;
        this.eplLoader = eplLoader;
        this.ePlanRepository = ePlanRepository;
    }

    @Get("/bereiche")
    @RolesAllowed({"EPlan"})
    List<String> getBereiche(){
        LOG.info("Fetching Bereichsliste");
        return ePlanLogic.getBereiche();
    }

    @Get("/bereich/{ber}")
    @RolesAllowed({"EPlan"})
    List<EPlan> getBereich(@NotNull String ber){
        LOG.info("Fetching EPlan of Bereich {}", ber);
        return ePlanLogic.getEPlan(ber);
    }

    @Post(value="/bereich/{bereich}", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.TEXT_PLAIN)
    @RolesAllowed({"EPlanAdmin"})
    public Single<HttpResponse<String>> uploadBereich(StreamingFileUpload file, String bereich) {
        return uploadFileTo(file, p -> eplLoader.excelBereichFromFile(p, ePlanLogic.getBereiche()));
    }

    @Post(value="/row")
    @RolesAllowed({"EPlan"})
    public HttpResponse<String> uploadRow(@Body EPlan row) {
        LOG.info("Got new data {}", row);
        ePlanRepository.save(row);
        return HttpResponse.ok();
    }

    @Get("/summen")
    List<EPlanSummen> getEplanSummen(){
        return ePlanLogic.getSummen();
    }

    @Get("/lehrer/{val}")
    @RolesAllowed({"EPlan"})
    List<EPlan> getLehrer(@NotNull String val){
        LOG.info("Fetching EPlan for KuK {}", val);
        return ePlanRepository.findBySchuleAndLehrer(EPLAN.SCHULE, val);
    }

    @Get("/klasse/{val}")
    @RolesAllowed({"EPlan"})
    List<EPlan> getKlasse(@NotNull String val){
        LOG.info("Fetching EPlan for Klasse {}", val);
        return ePlanRepository.findBySchuleAndKlasse(EPLAN.SCHULE, val);
    }

    @Get("/fach/{val}")
    @RolesAllowed({"EPlan"})
    List<EPlan> getFach(@NotNull String val){
        LOG.info("Fetching EPlan for Fach {}", val);
        return ePlanRepository.findBySchuleAndFach(EPLAN.SCHULE, val);
    }



}
