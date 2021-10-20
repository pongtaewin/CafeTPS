package th.ac.chula.cafetps;

public class Member {

    private String memberID,memberName;
    private double point;

    public Member(String memberID, String memberName, double point) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.point = point;
    }

    public Member(){

    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public double getPoint() {
        return point;
    }

    private void convertPrecision(){
        point = (double) Math.round(point*100.0)/100.0;
    }


    public double getPointFromTotal(int total){
        return  total*0.025;
    }

    public void setPoint(double point) {
        this.point = point;
        convertPrecision();
    }


}

