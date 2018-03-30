package com.example.keke.notebook;

/**
 * Created by keke on 12/10/17.
 */

public class NoteDAO {
    private String NOTE_ID = "";
    private String NOTE_TEXT = "";
    private String NOTE_CREATED = "";
    private String NOTE_LOC_LONGITUDE = "";
    private String NOTE_LOC_LATITUDE = "";

    public NoteDAO(String NOTE_ID, String NOTE_TEXT, String NOTE_CREATED, String NOTE_LOC_LONGITUDE, String NOTE_LOC_LATITUDE) {
        this.NOTE_ID = NOTE_ID;
        this.NOTE_TEXT = NOTE_TEXT;
        this.NOTE_CREATED = NOTE_CREATED;
        this.NOTE_LOC_LONGITUDE = NOTE_LOC_LONGITUDE;
        this.NOTE_LOC_LATITUDE = NOTE_LOC_LATITUDE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteDAO)) return false;

        NoteDAO noteDAO = (NoteDAO) o;

        if (!getNOTE_ID().equals(noteDAO.getNOTE_ID())) return false;
        if (!getNOTE_TEXT().equals(noteDAO.getNOTE_TEXT())) return false;
        if (!getNOTE_CREATED().equals(noteDAO.getNOTE_CREATED())) return false;
        if (getNOTE_LOC_LONGITUDE() != null ? !getNOTE_LOC_LONGITUDE().equals(noteDAO.getNOTE_LOC_LONGITUDE()) : noteDAO.getNOTE_LOC_LONGITUDE() != null)
            return false;
        return getNOTE_LOC_LATITUDE() != null ? getNOTE_LOC_LATITUDE().equals(noteDAO.getNOTE_LOC_LATITUDE()) : noteDAO.getNOTE_LOC_LATITUDE() == null;
    }

    @Override
    public int hashCode() {
        int result = getNOTE_ID().hashCode();
        result = 31 * result + getNOTE_TEXT().hashCode();
        result = 31 * result + getNOTE_CREATED().hashCode();
        result = 31 * result + (getNOTE_LOC_LONGITUDE() != null ? getNOTE_LOC_LONGITUDE().hashCode() : 0);
        result = 31 * result + (getNOTE_LOC_LATITUDE() != null ? getNOTE_LOC_LATITUDE().hashCode() : 0);
        return result;
    }

    public String getNOTE_ID() {
        return NOTE_ID;
    }

    public void setNOTE_ID(String NOTE_ID) {
        this.NOTE_ID = NOTE_ID;
    }

    public String getNOTE_TEXT() {
        return NOTE_TEXT;
    }

    public void setNOTE_TEXT(String NOTE_TEXT) {
        this.NOTE_TEXT = NOTE_TEXT;
    }

    public String getNOTE_CREATED() {
        return NOTE_CREATED;
    }

    public void setNOTE_CREATED(String NOTE_CREATED) {
        this.NOTE_CREATED = NOTE_CREATED;
    }

    public String getNOTE_LOC_LONGITUDE() {
        return NOTE_LOC_LONGITUDE;
    }

    public void setNOTE_LOC_LONGITUDE(String NOTE_LOC_LONGITUDE) {
        this.NOTE_LOC_LONGITUDE = NOTE_LOC_LONGITUDE;
    }

    public String getNOTE_LOC_LATITUDE() {
        return NOTE_LOC_LATITUDE;
    }

    public void setNOTE_LOC_LATITUDE(String NOTE_LOC_LATITUDE) {
        this.NOTE_LOC_LATITUDE = NOTE_LOC_LATITUDE;
    }

    @Override
    public String toString() {
        return "NoteDAO{" +
                "NOTE_ID='" + NOTE_ID + '\'' +
                ", NOTE_TEXT='" + NOTE_TEXT + '\'' +
                ", NOTE_CREATED='" + NOTE_CREATED + '\'' +
                ", NOTE_LOC_LONGITUDE='" + NOTE_LOC_LONGITUDE + '\'' +
                ", NOTE_LOC_LATITUDE='" + NOTE_LOC_LATITUDE + '\'' +
                '}';
    }
}