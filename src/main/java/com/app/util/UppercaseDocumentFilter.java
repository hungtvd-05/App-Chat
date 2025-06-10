package com.app.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author LENOVO
 */
public class UppercaseDocumentFilter extends DocumentFilter {
    private int maxCharacters = Integer.MAX_VALUE;
    
    public UppercaseDocumentFilter() {
        
    }
    
    public UppercaseDocumentFilter(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }  
    
    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();
        String newText = doc.getText(0, doc.getLength()) + text;
        
        if ((doc.getLength() + text.length()) <= maxCharacters) {
            super.insertString(fb, offset, text.toUpperCase(), attr);
        }
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        int currentLength = doc.getLength() - length;
        int newTextLength = text.length();
        if (currentLength + newTextLength <= maxCharacters) {
            super.replace(fb, offset, length, text.toUpperCase(), attrs);
        }
    }
}
