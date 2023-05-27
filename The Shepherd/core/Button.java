package core;

import core.Window;
import java.awt.Graphics2D;

public class Button {
    private buttons.ButtonFunction buttonUse, buttonHover;
    public boolean hover = false, showHover;
    private int x, y;
    private String text, hP, hA;
    public Button(int x, int y, String t, buttons.ButtonFunction u, buttons.ButtonFunction h, boolean showH) {
        this.buttonUse = u;
        this.buttonHover = h;
        this.x = x;
        this.y = y;
        this.text = t;
        this.showHover = showH;
    }
    public Button(int x, int y, String t, buttons.ButtonFunction u, buttons.ButtonFunction h) {
        this.buttonUse = u;
        this.buttonHover = h;
        this.x = x;
        this.y = y;
        this.text = t;
        this.showHover = true;
    }
    public void onPressed() {this.buttonUse.run();}
    public void onHovered() {
        if (!this.hover) this.buttonHover.run();
        this.hover = true;
    }
    public void render(Graphics2D g) {
        //Window.renderText(g, this.text + ((hover) ? hA:""), this.x, this.y, Window.T_004);
        if (hover && showHover) {
            if (keyboardInput.changingKeybind == -1) {Window.renderText(g, this.text, this.x, this.y, Window.T_004);} else 
            Window.renderText(g, this.text, this.x, this.y, Window.T_005);
        } else
        Window.renderText(g, this.text, this.x, this.y);
        //if (hover) Window.renderText(g, hP, this.x - 1 - Window.getTextWidth(hP), this.y);
    }
}