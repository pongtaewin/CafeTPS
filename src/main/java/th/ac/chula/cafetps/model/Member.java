package th.ac.chula.cafetps.model;

public class Member {

    private String ID;
    private String name;
    private double points;

    public Member(String ID, String name, double points) {
        this.ID = ID;
        this.name = name;
        this.points = points;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

