package swp_impl_acr.quizapy;

import android.app.Application;

public class AvailablePoints extends Application{
    private int points;

    public int getPoints(){
        return points;
    }

    public void setPoints(int points){
        this.points=points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void subPoints(int points) {
        this.points -= points;
    }
}
