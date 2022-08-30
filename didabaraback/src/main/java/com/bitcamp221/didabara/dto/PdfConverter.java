package com.bitcamp221.didabara.dto;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PdfConverter {

    public void convertWordToPdf(String wordFileName, String pdfFileName) throws IOException {
        try (InputStream docxInputStream = new FileInputStream(wordFileName);
             OutputStream pdfOutputStream = new FileOutputStream(pdfFileName)) {

            IConverter converter = LocalConverter.builder().build();

            converter.convert(docxInputStream).as(DocumentType.MS_WORD)
                    .to(pdfOutputStream).as(DocumentType.PDF)
                    .execute();

            converter.shutDown();
        }
    }
}