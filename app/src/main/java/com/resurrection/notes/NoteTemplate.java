package com.resurrection.notes;

public class NoteTemplate {
    String primary,id,header,content,date;

    public String getId() {
        return id;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NoteTemplate(String primaryKey,String id, String header, String content, String date) {
        this.primary = primaryKey;
        this.id = id;
        this.header = header;
        this.content = content;
        this.date = date;
    }
}
