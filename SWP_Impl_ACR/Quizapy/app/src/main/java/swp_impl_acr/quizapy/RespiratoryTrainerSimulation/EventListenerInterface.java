package swp_impl_acr.quizapy.RespiratoryTrainerSimulation;

/**
 * interface for Breathing Actions
 */
public interface EventListenerInterface {
    void onBreathInStart();
    void onBreathInStop();

    void onBreathOutStart();
    void onBreathOutStop();

    void onHoldBreathStart();
    void onHoldBreathStop();
}
