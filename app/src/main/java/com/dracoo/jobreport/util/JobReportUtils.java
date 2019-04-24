package com.dracoo.jobreport.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
}
