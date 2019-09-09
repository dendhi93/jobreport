package com.dracoo.jobreport.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.IOException;

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

    public PdfPCell createImageCell(String path, float imgWidth, float imgHeight) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        img.scaleToFit(imgWidth, imgHeight);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(Rectangle.NO_BORDER);
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

    public Paragraph singleSpace(Font font){
        Paragraph paragraph = new Paragraph("\n", font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setSpacingAfter(10);

        return paragraph;
    }

    public String convertCoordinat(double latitude, double longitude) {
        StringBuilder builder = new StringBuilder();

        if (latitude < 0) { builder.append("S ");
        } else { builder.append("N "); }

        String latitudeDegrees = Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS);
        String[] latitudeSplit = latitudeDegrees.split(":");
        builder.append(latitudeSplit[0]);
        builder.append("°");
        builder.append(latitudeSplit[1]);
        builder.append("'");
        builder.append(Math.round(Double.parseDouble(latitudeSplit[2])));
        builder.append("\"");
        builder.append(" ");

        if (longitude < 0) { builder.append("W ");
        } else { builder.append("E "); }

        String longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS);
        String[] longitudeSplit = longitudeDegrees.split(":");
        builder.append(longitudeSplit[0]);
        builder.append("°");
        builder.append(longitudeSplit[1]);
        builder.append("'");
        builder.append(Math.round(Double.parseDouble(longitudeSplit[2])));
        builder.append("\"");

        return builder.toString();
    }

    public String convertCoordinatToDegree(double coordinat, int typeCoordinat) {
        StringBuilder builder = new StringBuilder();
        if (typeCoordinat == ConfigApps.LATITUDE_TYPE){
            if (coordinat < 0) { builder.append("S "); }
            else { builder.append("N "); }

            String latitudeDegrees = Location.convert(Math.abs(coordinat), Location.FORMAT_SECONDS);
            String[] latitudeSplit = latitudeDegrees.split(":");
            builder.append(latitudeSplit[0]);
            builder.append(" ");
        }

        if (typeCoordinat == ConfigApps.LONGITUDE_TYPE){
            if (coordinat < 0) { builder.append("W ");
            } else { builder.append("E "); }

            String longitudeDegrees = Location.convert(Math.abs(coordinat), Location.FORMAT_SECONDS);
            String[] longitudeSplit = longitudeDegrees.split(":");
            builder.append(longitudeSplit[0]);
        }
        return builder.toString();
    }

    public String convertCoordinatToMinutesSecond(double coor, int typeCoordinat) {
        StringBuilder builder = new StringBuilder();

        if (typeCoordinat == ConfigApps.LATITUDE_TYPE){
            String latitudeDegrees = Location.convert(Math.abs(coor), Location.FORMAT_SECONDS);
            String[] latitudeSplit = latitudeDegrees.split(":");
            builder.append(latitudeSplit[1]);
            builder.append("'");
            builder.append(Math.round(Double.parseDouble(latitudeSplit[2])));
        }

        if (typeCoordinat == ConfigApps.LONGITUDE_TYPE){
            String longitudeDegrees = Location.convert(Math.abs(coor), Location.FORMAT_SECONDS);
            String[] longitudeSplit = longitudeDegrees.split(":");
            builder.append(longitudeSplit[1]);
            builder.append("'");
            builder.append(Math.round(Double.parseDouble(longitudeSplit[2])));
        }

        return builder.toString();
    }
}
