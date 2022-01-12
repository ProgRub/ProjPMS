package com.example.seaker.jsonwriter;

public class SummaryJson {

    private String specie;
    private String percent;
    private String totalNrIndividuals;
    private String averageNrIndividualsPerSighting;
    private String mostCommonBehavior;
    private String mostCommonReaction;
    private String averageTrustLvl;
    private String mostSightedIn;


    public SummaryJson(String specie, String percent, String totalNrIndividuals, String averageNrIndividualsPerSighting, String mostCommonBehavior, String mostCommonReaction, String averageTrustLvl, String mostSightedIn) {
        this.specie = specie;
        this.percent = percent;
        this.totalNrIndividuals = totalNrIndividuals;
        this.averageNrIndividualsPerSighting = averageNrIndividualsPerSighting;
        this.mostCommonBehavior = mostCommonBehavior;
        this.mostCommonReaction = mostCommonReaction;
        this.averageTrustLvl = averageTrustLvl;
        this.mostSightedIn = mostSightedIn;
    }


    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
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

    public String getAverageTrustLvl() {
        return averageTrustLvl;
    }

    public void setAverageTrustLvl(String averageTrustLvl) {
        this.averageTrustLvl = averageTrustLvl;
    }

    public String getMostSightedIn() {
        return mostSightedIn;
    }

    public void setMostSightedIn(String mostSightedIn) {
        this.mostSightedIn = mostSightedIn;
    }

    public String getMostCommonReaction() {
        return mostCommonReaction;
    }

    public void setMostCommonReaction(String mostCommonReaction) {
        this.mostCommonReaction = mostCommonReaction;
    }
}
