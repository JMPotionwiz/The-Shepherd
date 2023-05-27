package core;

import java.awt.event.*;

public class keyboardInput implements KeyListener {
    public static int changingKeybind = -1;
    public static boolean allowKeybindChange = false;
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyText(e.getKeyCode()));
        if (changingKeybind == -1) for (keybind i : main.keys) {
            if (i instanceof keybind) {i.updatePressed(e.getKeyText(e.getKeyCode()));}
        }
        if (changingKeybind != -1) {
            for (keybind i : main.keys) if (i instanceof keybind && i.getKeybind().equals(e.getKeyText(e.getKeyCode())))
            i.changeKeybind(main.keys[changingKeybind].getKeybind());
            main.keys[changingKeybind].changeKeybind(e.getKeyText(e.getKeyCode()));
            changingKeybind = -1;
            main.menus.Settings(true);
        }
    }
    public void keyReleased(KeyEvent e) {
        //System.out.println(e.getKeyText(e.getKeyCode()) instanceof String);
        for (keybind i : main.keys) {
            if (i instanceof keybind) i.updateUnpressed(e.getKeyText(e.getKeyCode()));
        }
        allowKeybindChange = true;
        for (keybind i : main.keys) if (i.pressed()) allowKeybindChange = false;
    }
}
