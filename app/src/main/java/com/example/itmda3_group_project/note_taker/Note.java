package com.example.itmda3_group_project.note_taker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "pdf_uri")
    private String pdfUri; // String representation of Uri (can be null)
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    public Note(String title, String content, String pdfUri, long timestamp) {
        this.title = title;
        this.content = content;
        this.pdfUri = pdfUri;
        this.timestamp = timestamp;
    }
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getPdfUri() { return pdfUri; }
    public void setPdfUri(String pdfUri) { this.pdfUri = pdfUri; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
