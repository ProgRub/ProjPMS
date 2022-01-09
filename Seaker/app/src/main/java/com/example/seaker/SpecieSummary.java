package com.example.seaker;

import android.view.View;

public class SpecieSummary {

    private View summary;
    private String specie;
    private String percent;
    private String totalNrIndividuals;
    private String averageNrIndividualsPerSighting;
    private String mostCommonBehavior;
    private String averageTrustLvl;
    private String mostSightedIn;


    public SpecieSummary(View summary, String specie, String percent, String totalNrIndividuals, String averageNrIndividualsPerSighting, String mostCommonBehavior, String averageTrustLvl, String mostSightedIn) {
        this.summary = summary;
        this.specie = specie;
        this.percent = percent;
        this.totalNrIndividuals = totalNrIndividuals;
        this.averageNrIndividualsPerSighting = averageNrIndividualsPerSighting;
        this.mostCommonBehavior = mostCommonBehavior;
        this.averageTrustLvl = averageTrustLvl;
        this.mostSightedIn = mostSightedIn;
    }


    public View getSummary() {
        return summary;
    }

    public void setSummary(View summary) {
        this.summary = summary;
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
}
