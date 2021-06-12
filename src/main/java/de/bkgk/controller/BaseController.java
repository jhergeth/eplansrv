package de.bkgk.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.reactivex.Single;
import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import static io.micronaut.http.HttpStatus.CONFLICT;

public class BaseController {
    protected Single<HttpResponse<String>> uploadFileTo(StreamingFileUpload file, Consumer<String> con) {
        File tempFile;
        try {
            tempFile = File.createTempFile(file.getFilename(), "temp");
        } catch (IOException e) {
            return Single.error(e);
        }
        Publisher<Boolean> uploadPublisher = file.transferTo(tempFile);

        return Single.fromPublisher(uploadPublisher)
                .map(success -> {
                    if (success) {
                        con.accept(tempFile.getAbsolutePath());
                        return HttpResponse.ok("Uploaded");
                    } else {
                        return HttpResponse.<String>status(CONFLICT)
                                .body("Upload Failed");
                    }
                });
    }
}
