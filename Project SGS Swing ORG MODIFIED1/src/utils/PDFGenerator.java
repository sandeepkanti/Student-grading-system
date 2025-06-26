package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import models.Student;
import java.io.FileOutputStream;

public class PDFGenerator {
    public static void generateResultSheet(Student student) {
        Document document = new Document();
        try {
            String filename = "ResultSheet_" + student.getUsn() + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            addBorder(writer);
             // Adding University Header with Logo
             Image logo = Image.getInstance("logo/logo.jpg");
             logo.scaleToFit(500, 100);
             logo.setAlignment(Image.ALIGN_CENTER);
             document.add(logo);
            addHeader(document);
            addStudentDetails(document, student);
            addMarksTable(document, student);
            addSGPATable(document, student);
            addGradeScale(document);
            addFooter(document);

            document.close();
            System.out.println("Result sheet generated: " + filename);
        } catch (Exception e) {
            System.err.println("Error creating PDF: " + e.getMessage());
        }
    }

    private static void addBorder(PdfWriter writer) {
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle border = new Rectangle(30, 30, 565, 800);
        border.setBorder(Rectangle.BOX);
        border.setBorderWidth(2);
        border.setBorderColor(BaseColor.RED);
        canvas.rectangle(border);
           
    }

    private static void addHeader(Document document) throws DocumentException {
        Paragraph title = new Paragraph("PROVISIONAL RESULT SHEET\n",
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.RED));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph courseDetails = new Paragraph("B.Tech - Semester SEE August 2025\n\n",
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        courseDetails.setAlignment(Element.ALIGN_CENTER);
        document.add(courseDetails);
    }

    private static void addStudentDetails(Document document, Student student) throws DocumentException {
        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(100);
        detailsTable.setWidths(new float[]{1, 2});

        addDetailRow(detailsTable, "Name of the Student", student.getName());
        addDetailRow(detailsTable, "Father's / Mother's Name", student.getFatherName());
        addDetailRow(detailsTable, "Faculty Name", Student.getFacultyName());
        addDetailRow(detailsTable, "Programme", Student.getProgramme());
        addDetailRow(detailsTable, "USN", student.getUsn());

        document.add(detailsTable);
        document.add(new Paragraph("\n"));
    }

    private static void addDetailRow(PdfPTable table, String label, String value) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12);
        
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(": " + value, font));
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }

    private static void addMarksTable(Document document, Student student) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 1, 1, 1, 1, 1});

        addTableHeaders(table);
        addSubjectRows(table, student);

        document.add(table);
    }

    private static void addTableHeaders(PdfPTable table) {
        String[] headers = {
            "Course Title", "Credits Assigned", "Max Marks",
            "Marks Obtained", "Credits Earned (C)",
            "Grade Points (G)", "Letter Grade"
        };

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            cell.setMinimumHeight(30);
            table.addCell(cell);
        }
    }

    private static void addSubjectRows(PdfPTable table, Student student) {
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        String[] subjects = student.getSubjects();
        double[] marks = student.getMarks();
        int[] credits = student.getCreditsAssigned();

        for (int i = 0; i < subjects.length; i++) {
            addCell(table, subjects[i], contentFont, Element.ALIGN_LEFT);
            addCell(table, String.valueOf(credits[i]), contentFont, Element.ALIGN_CENTER);
            addCell(table, "100", contentFont, Element.ALIGN_CENTER);
            addCell(table, String.valueOf((int)marks[i]), contentFont, Element.ALIGN_CENTER);
            addCell(table, marks[i] >= 40 ? String.valueOf(credits[i]) : "0", contentFont, Element.ALIGN_CENTER);
            addCell(table, String.valueOf(GradeCalculator.getGradePoints(marks[i])), contentFont, Element.ALIGN_CENTER);
            addCell(table, GradeCalculator.getLetterGrade(marks[i]), contentFont, Element.ALIGN_CENTER);
        }
    }

    private static void addCell(PdfPTable table, String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private static void addSGPATable(Document document, Student student) throws DocumentException {
        PdfPTable sgpaTable = new PdfPTable(1);
        sgpaTable.setWidthPercentage(20);
        sgpaTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        sgpaTable.setSpacingBefore(10f);

        PdfPCell headerCell = new PdfPCell(new Phrase("SGPA", 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        sgpaTable.addCell(headerCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(
            String.format("%.2f", student.getSgpa()),
            FontFactory.getFont(FontFactory.HELVETICA, 10)));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueCell.setPadding(8);
        sgpaTable.addCell(valueCell);

        document.add(sgpaTable);
    }

    private static void addGradeScale(Document document) throws DocumentException {
        Paragraph gradeScaleHeading = new Paragraph("\nGrade Point Scale",
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
        gradeScaleHeading.setAlignment(Element.ALIGN_CENTER);
        gradeScaleHeading.setSpacingBefore(10);
        gradeScaleHeading.setSpacingAfter(5);
        document.add(gradeScaleHeading);

        PdfPTable gradeTable = new PdfPTable(9);
        gradeTable.setWidthPercentage(90);
        gradeTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        gradeTable.setWidths(new float[]{2, 2, 2, 2, 2, 2, 2, 2, 2});

        String[] gradeHeaders = {
            "Outstanding", "Excellent", "Very Good", "Good",
            "Above Average", "Average", "Pass", "Fail", "Absent"
        };
        String[] gradeValues = {"O", "A+", "A", "B+", "B", "C", "P", "F", "Ab"};
        String[] gradePoints = {"10", "9", "8", "7", "6", "5", "4", "00", "00"};

        addGradeScaleRow(gradeTable, gradeHeaders);
        addGradeScaleRow(gradeTable, gradeValues);
        addGradeScaleRow(gradeTable, gradePoints);

        document.add(gradeTable);
    }

    private static void addGradeScaleRow(PdfPTable table, String[] values) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
        for (String value : values) {
            PdfPCell cell = new PdfPCell(new Phrase(value, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(3);
            table.addCell(cell);
        }
    }

    private static void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph("\nMedium of Instruction: English"));
        document.add(new Paragraph("\nNote:  This Card is a duplicate version created for the purpose of the project. \n This Project is done by Sandeep , Kartik , Guruprasad , Jamal. \n Under the Guidance of  PROF : Shravankumar ." 
            ));
          
            
            
            
    }
}


