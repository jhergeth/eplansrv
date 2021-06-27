package de.bkgk.controller;

import com.google.common.collect.Iterables;
import de.bkgk.domain.*;
import de.bkgk.responses.PivotTable;
import de.bkgk.service.UntisGPULoader;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static io.micronaut.http.HttpStatus.CONFLICT;
import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA;
import static io.micronaut.http.MediaType.TEXT_PLAIN;

@RolesAllowed({"EPlan"})
@Validated
@Controller("/api/domain")
public class DomainController extends BaseController{
    private static final Logger LOG = LoggerFactory.getLogger(DomainController.class);

    private final AnrechungRepository anrechungRepository;
    private final KollegeRepository kollegeRepository;
    private final KlasseRepository klasseRepository;
    private final UntisGPULoader untisGPULoader;

    public DomainController(AnrechungRepository anrechungRepository,
                            KollegeRepository kollegeRepository,
                            KlasseRepository klasseRepository,
                            UntisGPULoader untisGPULoader) {
        this.anrechungRepository = anrechungRepository;
        this.kollegeRepository = kollegeRepository;
        this.klasseRepository = klasseRepository;
        this.untisGPULoader = untisGPULoader;
    }

    @Get("/klassen")
    Iterable<Klasse> getKlassen() {
        Iterable<Klasse> kl = klasseRepository.findAll();
        LOG.info("Fetching Klassenliste, size: {}", Iterables.size(kl) );
        return kl;
    }

    @Post("/klassen/{krzl}")
    HttpResponse<String> getKlassen(String krzl, Klasse k) {
        LOG.info("Writing Klasse {}: {}", krzl, k);
        klasseRepository.update(k);
        return HttpResponse.ok();
    }

    @Post(value = "/klassen/upload", consumes = MULTIPART_FORM_DATA, produces = TEXT_PLAIN)
    public Single<HttpResponse<String>> uploadKl(StreamingFileUpload file) {
        return uploadFileTo(file, p -> untisGPULoader.readKlassen(p));
    }

    @Get("/lehrer")
    Iterable<Kollege> getKollegen() {
        Iterable<Kollege> ko = kollegeRepository.findAll();
        LOG.info("Fetching Kollegenliste, size: {}", Iterables.size(ko) );
        return ko;
    }

    @Post(value = "/lehrer/upload", consumes = MULTIPART_FORM_DATA, produces = TEXT_PLAIN)
    public Single<HttpResponse<String>> uploadKo(StreamingFileUpload file) {
        return uploadFileTo(file, p -> untisGPULoader.readKollegen(p));
    }

    @Get("/anrechnungen")
    Iterable<Anrechnung> getAnrechnungen() {
        Iterable<Anrechnung> ko = anrechungRepository.findAll();
        LOG.info("Fetching Anrechnungen, size: {}", Iterables.size(ko) );
        return ko;
    }

    @Get("/anrechnungpivot")
    PivotTable getAnrechnungPivot() {
        PivotTable pt = anrechungRepository.getAnrechnungPivot();
        LOG.info("Fetching Pivot of Anrechnungen: {}|{}", pt.rows.length, pt.cols.length);
        return pt;
    }

    @Post(value = "/anrechnungen/upload", consumes = MULTIPART_FORM_DATA, produces = TEXT_PLAIN)
    public Single<HttpResponse<String>> uploadAn(StreamingFileUpload file) {
        Single<HttpResponse<String>> res = uploadFileTo(file, p -> untisGPULoader.readAnrechnungen(p));
        anrechungRepository.calcAnrechnungPivot();
        return res;
    }

}
