package by.poltavetsav.artifactsguiproject;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class CooldownThread {
    private int secondsRemaining;
    private Label cooldownLabel;
    public CooldownThread(int secondsRemaining, Label cooldownLabel) {
        this.secondsRemaining = secondsRemaining;
        this.cooldownLabel = cooldownLabel;
    }
}
//while (secondsRemaining > 0){
//        secondsRemaining -= 1;
//        cooldownLabel.setText("Colldown: " + secondsRemaining);
//        Thread.sleep(1000);
//                cooldownLabel.setText("Cooldown: Ready!");