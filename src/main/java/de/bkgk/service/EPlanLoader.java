package de.bkgk.service;


import de.bkgk.domain.EPlan;
import de.bkgk.domain.Klasse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.reactivex.Single;

import java.io.File;
import java.util.List;

public interface EPlanLoader {
    static final String EXCEL_FILE_SUFFIX = ".xlsx";
    static final String[] AUFG_EXCEL_COLUMMS = { "KuK", "Begin", "Ende", "Klasse", "Fach", "Aufgabe", "Bemerkung"};
    static final String[] KLASSE_EXCEL_COLUMMS = { "Ignore (i)", "Hauptklasse", "Name", "Langname", "Klassenlehrer", "Text 2", "Raum", "Abt.", "Text"};

    public List<Klasse> excelKlassenFromFile(File fName);
    public void excelBereichFromFile(String file, Iterable<String> bereich);
    public void excelBereichFromFile(String file, String bereich);
    }
