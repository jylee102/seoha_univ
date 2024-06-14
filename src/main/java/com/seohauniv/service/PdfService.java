package com.seohauniv.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.seohauniv.entity.Course;
import com.seohauniv.entity.Syllabus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileNotFoundException;
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
        UUID uuid = UUID.randomUUID();
        String savedFileName = uuid.toString() + ".pdf";

        // 파일 경로 + 파일 이름
        String fileUploadFullUrl = pdfLocation + "/" + savedFileName;

        // 작성되어 있는 템플릿으로 HTML 생성
        String htmlContent1 = generateHtml("/forPDF/syllabus-template1", course);
        String htmlContent2 = generateHtml("/forPDF/syllabus-template2", course);

        System.out.println(htmlContent1);

        // 각 HTML을 개별 PDF로 변환
        String pdf1 = pdfLocation + "/temp1.pdf";
        String pdf2 = pdfLocation + "/temp2.pdf";

        try (FileOutputStream fos1 = new FileOutputStream(pdf1)) {
            convertHtmlToPdf(htmlContent1, fos1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("PDF 생성 중 오류가 발생했습니다.");
        }

        try (FileOutputStream fos2 = new FileOutputStream(pdf2)) {
            convertHtmlToPdf(htmlContent2, fos2);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("PDF 생성 중 오류가 발생했습니다.");
        }

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

    private void convertHtmlToPdf(String htmlContent, FileOutputStream outputStream) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();

        // 한글 폰트 설정 (기본 폰트 사용)
        FontProvider fontProvider = new FontProvider();

        // 폰트 파일 경로 설정
        ClassPathResource fontResource = new ClassPathResource("static/fonts/notoSansKR/NotoSansKR-Regular.ttf");

        // 폰트 파일이 실제로 존재하는지 확인
        if (!fontResource.exists()) {
            throw new FileNotFoundException("Font file not found: " + fontResource.getPath());
        }

        // 폰트 파일을 byte[]로 읽어들입니다.
        byte[] fontBytes = Files.readAllBytes(Paths.get(fontResource.getURI()));

        // byte[] 배열을 사용하여 폰트를 추가합니다.
        fontProvider.addFont(fontBytes, "Identity-H");

        converterProperties.setFontProvider(fontProvider);

        // PDF로 변환
        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
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
