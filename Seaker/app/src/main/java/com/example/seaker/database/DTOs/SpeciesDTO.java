package com.example.seaker.database.DTOs;

public class SpeciesDTO {
    private long id;
    private String name;
    private long typeOfAnimalId;

    public SpeciesDTO(long id, String name, long typeOfAnimalId) {
        this.id = id;
        this.name = name;
        this.typeOfAnimalId = typeOfAnimalId;
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

    public long getTypeOfAnimalId() {
        return typeOfAnimalId;
    }

    public void setTypeOfAnimalId(long typeOfAnimalId) {
        this.typeOfAnimalId = typeOfAnimalId;
    }
}
