package com.example.seaker.database.DTOs;

public class ZoneDTO {
    private long id;
    private String name;
    private String fromOrTo;

    public ZoneDTO(long id, String name, String fromOrTo) {
        this.id = id;
        this.name = name;
        this.fromOrTo = fromOrTo;
    }
    public ZoneDTO(String name, String fromOrTo) {
        this.fromOrTo = fromOrTo;
        this.id = -1;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromOrTo() {
        return fromOrTo;
    }

    public void setFromOrTo(String fromOrTo) {
        this.fromOrTo = fromOrTo;
    }
}
