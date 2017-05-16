package swp_impl_acr.quizapy;

import android.app.Application;

/**
 * holds the currently available points
 */
public class AvailablePoints extends Application{
    private int points;

    /**
     * returns the available points
     * @return
     */
    public int getPoints(){
        return points;
    }

    /**
     * sets the available points
     * @param points
     */
    public void setPoints(int points){
        this.points=points;
    }

    /**
     * add points to the available points
     * @param points
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * substracts points from the available points
     * @param points
     */
    public void subPoints(int points) {
        this.points -= points;
    }
}
