package com.rutaexpress.shipments.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class PackageInfo {

    private double weightKg;
    private double volumeM3;
    private String description;

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public double getVolumeM3() {
        return volumeM3;
    }

    public void setVolumeM3(double volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
