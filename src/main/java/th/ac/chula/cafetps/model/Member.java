package th.ac.chula.cafetps.model;

import java.util.Optional;

public class Member {

    private Optional<String> ID;
    private String name;
    private double points;

    public Member(String name, double points){
        this.name = name;
        this.points = points;
    }

    public Member(String ID, String name, double points) {
        this.ID = Optional.of(ID);
        this.name = name;
        this.points = points;
    }

    public Optional<String> getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = Optional.of(ID);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPoints() {
        return points;
    }

    public double getPointsFromTotal(int total){
        return total*0.025;
    }

    public void setPoints(double points) {
        this.points = (double) Math.round(points*100.0)/100.0;
    }


}

