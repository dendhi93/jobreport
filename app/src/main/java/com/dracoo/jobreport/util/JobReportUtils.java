package com.dracoo.jobreport.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;


public class JobReportUtils {

    //hide keyboard
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static PdfPCell createCell(String title, Font fontType){
        PdfPCell cell = new PdfPCell(new Phrase(new Paragraph(title, fontType)));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setRowspan(1);

        return cell;
    }

    public static PdfPCell titleCell(String title, Font fontType){
        PdfPCell cell = new PdfPCell(new Phrase(new Paragraph(title, fontType)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);

        return cell;
    }

    public static PdfPCell headTitleCell(String title, Font fontType){
        PdfPCell cell = new PdfPCell(new Phrase(new Paragraph(title, fontType)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(2);

        return cell;
    }

    public static  PdfPCell bottomLineCell(String title, Font fonType){
        PdfPCell cell = new PdfPCell(new Phrase(new Paragraph(title, fonType)));
        cell.setBorder(Rectangle.BOTTOM);

        return cell;
    }

    public static  PdfPCell bottomLineCenterTextCell(String title, Font fonType){
        Paragraph preface = new Paragraph(title, fonType);
        preface.setAlignment(Element.ALIGN_CENTER);

        PdfPCell cell = new PdfPCell(new Phrase(preface));
        cell.setBorder(Rectangle.BOTTOM);

        return cell;
    }

    public static  PdfPCell borderlessCell(String title, Font fonType){
        PdfPCell cell = new PdfPCell(new Phrase(new Paragraph(title, fonType)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_LEFT);

        return cell;
    }

    public static PdfPCell createBorderLessCellRow(String title, Font fontType){
        PdfPCell cell = new PdfPCell(new Phrase(new Paragraph(title, fontType)));
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setRowspan(1);

        return cell;
    }

    public static Paragraph singleSpace(Font font){
        Paragraph paragraph = new Paragraph("\n", font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setSpacingAfter(10);

        return paragraph;
    }

}
