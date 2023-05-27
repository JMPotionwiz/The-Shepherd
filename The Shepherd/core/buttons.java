package core;

import java.util.ArrayList;
import core.Button;

public class buttons {
    public static ArrayList<ArrayList<Button>> buttons = new ArrayList<ArrayList<Button>>();
    public static int buttonScroll = 0;
    public static boolean buttonPressed = false, buttonsInUse = false;
    public static int[] selectedButton = {0,0};
    
    public static void buttonHandler() {
        if (buttonsInUse && main.fade[0] <= 0) {
            int E_001 = buttons.size();
            for (int i = 0; i < E_001; i++) {
                int E_002 = buttons.get(i).size();
                for (int j = 0; j < E_002; j++) {
                    if (i == selectedButton[0] && j == selectedButton[1]) {
                        buttons.get(i).get(j).onHovered();
                    } else buttons.get(i).get(j).hover = false;
                }
            }
            if (main.keys[4].pressed()) {
                if (!buttonPressed) buttons.get(selectedButton[0]).get(selectedButton[1]).onPressed();
                buttonPressed = true;
                main.absorbAttackCall = true;
            } else buttonPressed = false;
            if (buttonScroll <= 0 && main.keys[0].pressed() && !main.keys[1].pressed()) {
                buttonScroll = 10;
                int E_002 = selectedButton[0];
                int E_003 = Math.max(selectedButton[0] - 1, 0);
                if (buttons.get(E_002).size() != buttons.get(E_003).size()) selectedButton[1] = 0;
                selectedButton[0] = E_003;
            }
            if (buttonScroll <= 0 && main.keys[1].pressed() && !main.keys[0].pressed()) {
                buttonScroll = 10;
                int E_002 = selectedButton[0];
                int E_003 = Math.min(selectedButton[0] + 1, buttons.size() - 1);
                if (buttons.get(E_002).size() != buttons.get(E_003).size()) selectedButton[1] = 0;
                selectedButton[0] = E_003;
            }
            if (buttonScroll > 0 && !main.keys[0].pressed() && !main.keys[1].pressed()) buttonScroll = 0;
            if (buttonScroll > 0) buttonScroll--;
        }
    }
    public static void activateButtons() {
        deactivateButtons();
        buttonsInUse = true;
        selectedButton[0] = 0;
        selectedButton[1] = 0;
    }
    public static void deactivateButtons() {
        buttonsInUse = false;
        buttons = new ArrayList<ArrayList<Button>>();
    }
    public interface ButtonFunction {
        void run();
    }
}