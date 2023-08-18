package core;

import java.util.Scanner;
import java.util.ArrayList;

public class LoadSaveFile {
    static void run() {
        try {
            Scanner r = new Scanner(main.saves);
            ArrayList<String> in = new ArrayList<String>();
            while (r.hasNextLine()) {
                in.add(r.nextLine());
            }
            switch (in.get(0)) {
                case "save-version: 1" -> loadV1(in);
                default -> loadLegacy(in);
            }
        } catch (Exception e) {}
    }
    private static void loadV1(ArrayList<String> in) {
        int l = 1;
        main.Difficulty = (byte) Integer.parseInt(in.get(l++));
        main.MapSize = (byte) Integer.parseInt(in.get(l++));
        int E_001 = main.keys.length;
        for (int i = 0; i < E_001; i++) {
            main.keys[i].changeKeybind(in.get(l++));
        }
        main.highScore = Long.parseLong(in.get(l++));
        main.alwaysShowHealthbars = Boolean.parseBoolean(in.get(l++));
        main.sfxVolume = Float.parseFloat(in.get(l++));
        main.musVolume = Float.parseFloat(in.get(l++));
        if (in.size() > l) main.n_009 = in.get(l++);
    }
    private static void loadLegacy(ArrayList<String> in) {
        int l = 0;
        main.Difficulty = (byte) Integer.parseInt(in.get(l++));
        main.MapSize = (byte) Integer.parseInt(in.get(l++));
        int E_001 = main.keys.length;
        for (int i = 0; i < E_001; i++) {
            main.keys[i].changeKeybind(in.get(l++));
        }
        main.highScore = Long.parseLong(in.get(l++));
        main.alwaysShowHealthbars = Boolean.parseBoolean(in.get(l++));
        if (in.size() > l) main.n_009 = in.get(l++);
    }
}
