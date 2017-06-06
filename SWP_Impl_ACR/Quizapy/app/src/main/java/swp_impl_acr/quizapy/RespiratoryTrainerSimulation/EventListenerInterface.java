package swp_impl_acr.quizapy.RespiratoryTrainerSimulation;

/**
 * abstract class for Breathing Actions
 */
public interface EventListenerInterface {
    void onBreathInStart();
    void onBreathInStop();

    void onBreathOutStart();
    void onBreathOutStop();

    void onHoldBreathStart();
    void onHoldBreathStop();

    void graduallyBreathInStart();
    void graduallyBreathInStop();

    void graduallyBreathOutStart();
    void graduallyBreathOutStop();

    void  onRepeatedlyBreathINandOUTstart();
    void  onRepeatedlyBreathINandOUTstop();

    void onBreathingRateChange(int progress);



}
