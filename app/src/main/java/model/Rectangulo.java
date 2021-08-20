package model;

import java.io.Serializable;

public class Rectangulo implements Serializable {
    double west, east, north, south, latitude, longitude;


    public Rectangulo() {
    }

    public Rectangulo(double west, double east, double north, double south) {
        this.west = west;
        this.east = east;
        this.north = north;
        this.south = south;
    }

    public Rectangulo(double west, double east, double north, double south, double latitude, double longitude) {
        this.west = west;
        this.east = east;
        this.north = north;
        this.south = south;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
