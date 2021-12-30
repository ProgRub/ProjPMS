package com.example.seaker;

public class SpecieSummary {
    private String specie;
    private String totalNrIndividuals;
    private String averageNrIndividualsPerSighting;
    private String mostCommonBehavior;
    private String mostCommonReaction;
    private String averageBeaufort;
    private String averageTrustLvl;
    private String nrPhotos;


    public SpecieSummary(String specie, String totalNrIndividuals, String averageNrIndividualsPerSighting, String mostCommonBehavior, String mostCommonReaction, String averageBeaufort, String averageTrustLvl, String nrPhotos) {
        this.specie = specie;
        this.totalNrIndividuals = totalNrIndividuals;
        this.averageNrIndividualsPerSighting = averageNrIndividualsPerSighting;
        this.mostCommonBehavior = mostCommonBehavior;
        this.mostCommonReaction = mostCommonReaction;
        this.averageBeaufort = averageBeaufort;
        this.averageTrustLvl = averageTrustLvl;
        this.nrPhotos = nrPhotos;
    }


    public String getTotalNrIndividuals() {
        return totalNrIndividuals;
    }

    public void setTotalNrIndividuals(String totalNrIndividuals) {
        this.totalNrIndividuals = totalNrIndividuals;
    }

    public String getAverageNrIndividualsPerSighting() {
        return averageNrIndividualsPerSighting;
    }

    public void setAverageNrIndividualsPerSighting(String averageNrIndividualsPerSighting) {
        this.averageNrIndividualsPerSighting = averageNrIndividualsPerSighting;
    }

    public String getMostCommonBehavior() {
        return mostCommonBehavior;
    }

    public void setMostCommonBehavior(String mostCommonBehavior) {
        this.mostCommonBehavior = mostCommonBehavior;
    }

    public String getMostCommonReaction() {
        return mostCommonReaction;
    }

    public void setMostCommonReaction(String mostCommonReaction) {
        this.mostCommonReaction = mostCommonReaction;
    }

    public String getAverageBeaufort() {
        return averageBeaufort;
    }

    public void setAverageBeaufort(String averageBeaufort) {
        this.averageBeaufort = averageBeaufort;
    }

    public String getAverageTrustLvl() {
        return averageTrustLvl;
    }

    public void setAverageTrustLvl(String averageTrustLvl) {
        this.averageTrustLvl = averageTrustLvl;
    }

    public String getNrPhotos() {
        return nrPhotos;
    }

    public void setNrPhotos(String nrPhotos) {
        this.nrPhotos = nrPhotos;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }
}
