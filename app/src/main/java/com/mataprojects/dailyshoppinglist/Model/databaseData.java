package com.mataprojects.dailyshoppinglist.Model;

public class databaseData {

    private String type,note,date,id;
    int amount;

    public databaseData() {
    }

    public databaseData(String type, String note, String date, String id, int amount) {
        this.type = type;
        this.note = note;
        this.date = date;
        this.id = id;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
