package com.seohauniv.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.seohauniv.entity.Course;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Log
public class PdfService {
    @Value("${pdfLocation}")
    private String pdfLocation;

    private final TemplateEngine templateEngine;

    // 강의계획서 PDF 만들기
    public String makePdf(Course course) throws Exception {

        // 중복되지 않는 파일이름 생성
        String savedFileName = UUID.randomUUID().toString() + ".pdf";

        // 파일 경로 + 파일 이름
        String fileUploadFullUrl = pdfLocation + "/" + savedFileName;

        // 작성되어 있는 템플릿으로 HTML 생성
        String htmlContent1 = generateHtml("forPDF/syllabus-template1", course);
        String htmlContent2 = generateHtml("forPDF/syllabus-template2", course);

        // 각 HTML을 개별 PDF로 변환
        String pdf1 = pdfLocation + "/temp1.pdf";
        String pdf2 = pdfLocation + "/temp2.pdf";

        convertHtmlToPdf(htmlContent1, pdf1);
        convertHtmlToPdf(htmlContent2, pdf2);

        // 두 개의 PDF를 합치기
        mergePdfs(fileUploadFullUrl, pdf1, pdf2);

        return savedFileName;
    }

    // HTML 템플릿 생성
    private String generateHtml(String template, Course course) {
        Context context = new Context();
        context.setVariable("course", course);

        // Thymeleaf 템플릿 엔진을 사용하여 HTML 생성
        return templateEngine.process(template, context);
    }

    private void convertHtmlToPdf(String htmlContent, String outputPath) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new FontProvider();

        // 폰트 파일 경로 설정
        ClassPathResource fontResource = new ClassPathResource("static/fonts/notoSansKR/NotoSansKR-Regular.ttf");
        if (!fontResource.exists()) {
            throw new IOException("Font file not found: " + fontResource.getPath());
        }

        byte[] fontBytes = Files.readAllBytes(Paths.get(fontResource.getURI()));
        fontProvider.addFont(fontBytes, "Identity-H");
        converterProperties.setFontProvider(fontProvider);

        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties); // PDF로 변환
        }
    }

    private void mergePdfs(String outputFile, String pdf1, String pdf2) throws IOException {
        try {
            // 첫 번째 PDF 파일 열기
            PdfReader reader1 = new PdfReader(pdf1);
            PdfDocument pdfDoc1 = new PdfDocument(reader1);

            // 두 번째 PDF 파일 열기
            PdfReader reader2 = new PdfReader(pdf2);
            PdfDocument pdfDoc2 = new PdfDocument(reader2);

            // 새로운 PDF 생성
            PdfWriter writer = new PdfWriter(outputFile);
            PdfDocument mergedPdf = new PdfDocument(writer);

            // 첫 번째 PDF의 페이지 수 얻기
            int pages1 = pdfDoc1.getNumberOfPages();

            // 첫 번째 PDF의 페이지를 새로운 PDF에 추가
            for (int i = 1; i <= pages1; i++) {
                pdfDoc1.copyPagesTo(i, i, mergedPdf);
            }

            // 두 번째 PDF의 페이지를 새로운 PDF에 추가
            int pages2 = pdfDoc2.getNumberOfPages();
            for (int i = 1; i <= pages2; i++) {
                pdfDoc2.copyPagesTo(i, i, mergedPdf);
            }

            // 문서 닫기
            pdfDoc1.close();
            pdfDoc2.close();
            mergedPdf.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("PDF 합치기 중 오류가 발생했습니다.");
        }
    }
}
