package de.theo.open_rpc.model;

public class InfoType {

    private String title;

    public InfoType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "InfoType{" +
                "title='" + title + '\'' +
                '}';
    }
}
