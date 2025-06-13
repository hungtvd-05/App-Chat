/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.app.util;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author LENOVO
 */
public class Utils {
    
    
    public static String formatTime(LocalDateTime time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            return time.format(formatter).toLowerCase(); 
        } catch (Exception e) {
            return "null";
        }
    }
    
    public static String formatTime(LocalDateTime time, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return time.format(formatter).toLowerCase(); 
    }
    
    public static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".gif");
    }
    
    public static boolean isImageFileExists(String relativePath) {
        File imageFile = new File(relativePath);
        return imageFile.exists() && imageFile.isFile();
    }
}
