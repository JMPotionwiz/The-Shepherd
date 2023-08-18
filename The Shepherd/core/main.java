package core;

import java.util.*;
import entities.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import assets.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class main {
    public static Window mainWindow;
    public static int Level, recordedHealth = 50, recordedFood = 0;
    public static long score = 0, highScore = 0, highScorePrev = 0;
    public static boolean activateFullscreen = true, paused = true, allowPausing = true, alwaysShowHealthbars = false, freezeFrame = false, close = false;
    public static byte closing = 0, wolfTimer = 0, menu = 64, menuQueue = 64;
    public static ArrayList<WolfBase> wolfQueue = new ArrayList<WolfBase>();
    public static String ERRAH = "", n_009 = (int)(Math.random() * 100) + "";
    
    static DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM:dd");
    public static final String date = formatDate.format(LocalDate.now());
    
    public static SheepAssets sheepA = new SheepAssets();
    public static DerpShepAssets derpA = new DerpShepAssets();
    public static WolfAssets wolfA = new WolfAssets();
    public static RavenousWolfAssets rwolfA = new RavenousWolfAssets();
    public static SheepskinAssets sheepskinA = new SheepskinAssets();
    public static ShepherdAssets shepherdA = new ShepherdAssets();
    public static BlightedWolfAssets bwolfA = new BlightedWolfAssets();
    public static WolfBossAssets bossA = new WolfBossAssets();
    public static DartwolfAssets dwolfA = new DartwolfAssets();
    
    public static Misc miscAssets = new Misc();
    static byte introTime = 80;
    
    public static keybind[] keys = {
        new keybind("W"), //[0] Move up
        new keybind("S"), //[1] Move down
        new keybind("A"), //[2] Move left
        new keybind("D"), //[3] Move right
        new keybind("Enter"), //[4] Attack
        new keybind("Shift"), //[5] Heal
        new keybind("F11"), //[6] Fullscreen
        new keybind("Escape") //[7] Back/Pause/End game
    };
    
    public static ArrayList<ArrayList<Integer>> tileMap = null;
    public static Image mapImage = null, mapLeaves = null, menuBackground = null;
    public static double[] menuWander = {0,0,45 + ((int)(Math.random() * 4) * 90)};
    public static byte Difficulty = 0;
    public static byte MapSize = 0;
    public static int UID = 0;
    public static HashSet<Entity> entities = new HashSet<Entity>(), spawnIn = new HashSet<Entity>();
    public static Camera cam = null;
    public static byte[] fade = {40,-1};
    public static boolean absorbAttackCall = false;
    public static int wolfAttack = 0;
    public static boolean lastAttackBySheepskin = false;
    
    public static ArrayList<Sound> sounds = new ArrayList<Sound>();
    public static Sound bgm;
    public static int delayBgm = 0;
    private static Sound bg_silence;
    private static ArrayList<String> soundsQueue = new ArrayList<String>();
    private static boolean startupMusic = true;
    public static float sfxVolume = 1f, musVolume = 0.5f;
    public static byte changeVolume[] = {0,0}, sfxVolumeCheck = 0, musVolumeCheck = 0;
    
    public static File saves = new File("data");
    
    public static void main(String[] args) {
        try{if (saves.exists()) LoadSaveFile.run();}catch(Exception e){}
        try {
            bg_silence = new Sound("bg_silence");
            bg_silence.Loop();
        } catch (Exception e) {}
        
        
        mainWindow = new Window("The Shepherd");
        mainWindow.addKeyListener(new keyboardInput());
        mainWindow.getCanvas().addKeyListener(new keyboardInput());
        //System.out.println("keybinds set! ");
        //menus.MainMenu(false);
        maintool.createMenuBackground();
        //System.out.println("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.!?:,;'\"-+_()[]{}/\\*%");
        
        try{Thread.sleep(200);}catch(Exception e){};
        while (true) {
            if (fade[0] == 30 && introTime == 80) {
                maintool.playSound("sfx_introjingle");
            } else if (fade[0] == 0 && introTime > 0) {
                introTime--;
            } else if (introTime == 0) {
                menuQueue = -64;
                fade[1] = 1;
                introTime = -1;
            }
            
            if (menu == 1 || menu == 2) {
                menuWander[0] += Math.cos(Math.toRadians(menuWander[2]));
                menuWander[1] += Math.sin(Math.toRadians(menuWander[2]));
                //menuWander[2] += 0.01;
                if (menuWander[0] >= 64*16) menuWander[0] -= 64*16;
                if (menuWander[1] >= 64*16) menuWander[1] -= 64*16;
                if (menuWander[0] < 0) menuWander[0] += 64*16;
                if (menuWander[1] < 0) menuWander[1] += 64*16;
                if (menuWander[2] >= 360) menuWander[2] -= 360;
            }
            
            if (buttons.buttonsInUse) {
                //changeVolume[0] = 0;
                buttons.buttonHandler();
                if (changeVolume[1] <= 0) {
                    if (changeVolume[0] == (byte) 1) {
                        if (keys[2].pressed() && sfxVolume > 0) {
                            sfxVolume = (float)Math.round(sfxVolume * 20 - 1) / 20;
                            if (sfxVolume < 0) sfxVolume = 0;
                            menus.Settings(true);
                            changeVolume[1] = (byte) 4;
                            sfxVolumeCheck = (byte) 30;
                        } else if (keys[3].pressed() && sfxVolume < 1) {
                            sfxVolume = (float)Math.round(sfxVolume * 20 + 1) / 20;
                            if (sfxVolume > 1) sfxVolume = 1;
                            menus.Settings(true);
                            changeVolume[1] = (byte) 4;
                            sfxVolumeCheck = (byte) 30;
                        }
                    } else if (changeVolume[0] == (byte) 2) {
                        if (keys[2].pressed() && musVolume > 0) {
                            if (bgm != null) bgm.Stop();
                            musVolume = (float)Math.round(musVolume * 20 - 1) / 20;
                            if (musVolume < 0) musVolume = 0;
                            menus.Settings(true);
                            changeVolume[1] = (byte) 4;
                            musVolumeCheck = (byte) 30;
                        } else if (keys[3].pressed() && musVolume < 1) {
                            if (bgm != null) bgm.Stop();
                            musVolume = (float)Math.round(musVolume * 20 + 1) / 20;
                            if (musVolume > 1) musVolume = 1;
                            menus.Settings(true);
                            changeVolume[1] = (byte) 4;
                            musVolumeCheck = (byte) 30;
                        }
                    }
                } else changeVolume[1]--;
            }
            if (sfxVolumeCheck > 1) {
                sfxVolumeCheck--;
            } else if (sfxVolumeCheck == 1) {
                sfxVolumeCheck = 0;
                maintool.playSound("sfx_pause");
            }
            if (musVolumeCheck > 1) {
                musVolumeCheck--;
            } else if (musVolumeCheck == 1) {
                musVolumeCheck = 0;
                if (bgm != null) {
                    bgm.setVolume(musVolume);
                    bgm.clip.flush();
                    bgm.Loop();
                }
            }
            
            if (keys[7].pressed()) {
                if (allowPausing) switch (menu) {
                    case -1:
                        menu = 0;
                        paused = true;
                        buttons.activateButtons();
                        ArrayList<Button> tmp = new ArrayList<Button>();
                        tmp.add(new Button(0 - (int)(Window.getTextWidth("Back to Game") * 0.5),16,"Back to Game",() -> {
                            menu = -1;
                            paused = false;
                            buttons.deactivateButtons();
                            maintool.startAllSounds();
                            maintool.playSound("sfx_unpause");
                            if (bgm != null) bgm.Loop();
                        },()->{}));
                        buttons.buttons.add(tmp);
                        tmp = new ArrayList<Button>();
                        tmp.add(new Button(0 - (int)(Window.getTextWidth("Back to Game") * 0.5),28,"Quit",() -> {
                            menuQueue = 1;
                            fade[1] = 1;
                            sounds.clear();
                            maintool.playSound("sfx_unpause");
                            bgm = null;
                            try {
                                bgm = new Sound("mus_menu");
                                bgm.setVolume(musVolume);
                                startupMusic = true;
                            } catch (Exception e) {}
                        },()->{}));
                        buttons.buttons.add(tmp);
                        maintool.stopAllSounds();
                        if (bgm != null) bgm.Stop();
                        maintool.playSound("sfx_pause");
                        break;
                    case 0:
                        menu = -1;
                        paused = false;
                        buttons.deactivateButtons();
                        maintool.startAllSounds();
                        maintool.playSound("sfx_unpause");
                        if (bgm != null) bgm.Loop();
                        break;
                    case 2:
                        menus.MainMenu(false);
                        break;
                }
                allowPausing = false;
            } else allowPausing = true;
            if (keys[6].pressed() && activateFullscreen == true) {
                mainWindow.FULLSCREEN_MODE_AAAHH();
                activateFullscreen = false;
            } else if (!keys[6].pressed() && activateFullscreen == false) activateFullscreen = true;
            
            if (fade[1] != 0) fade[0] += fade[1];
            if (fade[0] >= 40) {
                fade[1] = -1;
                switch (menuQueue) {
                    case -1:
                        menus.StartGame();
                        break;
                    case 1:
                        menus.MainMenu(true);
                        break;
                    case -64:
                        menus.MainMenu(false);
                        try {
                            bgm = new Sound("mus_menu");
                            bgm.setVolume(musVolume);
                            startupMusic = true;
                        } catch (Exception e) {}
                        break;
                    case 3:
                        menus.GameOver();
                        try {
                            bgm = new Sound("mus_menu");
                            bgm.setVolume(musVolume);
                            startupMusic = true;
                        } catch (Exception e) {}
                        break;
                    case 64: 
                        menu = 64;
                        break;
                }
                if (startupMusic && bgm != null) {
                    bgm.Loop();
                    startupMusic = false;
                }
            } else if (fade[0] == 0 && fade[1] != 0) fade[1] = 0;
            
            if (!paused) {
                if (tool.getNumberOfWolves() <= 0 && wolfQueue.size() <= 0 && Difficulty < 3) {
                    if (Level > 0) for (Entity e : entities) {
                        if (e instanceof Sheep || e instanceof Player) e.heal(5);
                        if (e instanceof Sheep && e.getHealth() > 0) score += 5;
                    }
                    Level++;
                    
                    if (Level % 10 == 0 && Level >= 20) {
                        for (int i = Difficulty; i < 3; i++) {
                            maintool.spawnSheep(true);
                        }
                        wolfTimer = -80;
                    } else if (Difficulty < 2 && Level % 5 == 0) {
                        maintool.spawnSheep(true);
                        wolfTimer = -80;
                    }
                    
                    //System.out.println(Level);
                    int ravenousWolves = 1 + Difficulty;
                    int E_001 = (int)Math.ceil(Math.min(Math.ceil(Level * 0.5), 4 + (4 * Difficulty))); //"E" for efficiency!
                    for (int i = 0; i < E_001; i++) {
                        WolfBase woof = null;
                        if ((Level % 10 == 0 && Level >= 20) && i == 0) woof = new WolfBoss(16,16);
                        if (Level >= 8 && ravenousWolves > 0 && woof == null) {
                            ravenousWolves--;
                            int c = 4;
                            if (Level >= 12) c--;
                            if (Level <= 16) c--;
                            if ((int)(Math.random() * c) == 0) {
                                woof = new RavenousWolf(16,16);
                            }
                        }
                        if ((Level >= 18 && (int)(Math.random() * 4) == 0 && !date.equals("10:31")) || ((int)(Math.random() * 2) == 0) && date.equals("10:31")) {
                            if (woof == null) woof = new BlightedWolf(16,16);
                        }
                        if (woof == null) woof = new Wolf(16,16);
                        wolfQueue.add(woof);
                    }
                    if (!maintool.checkForSheepskin() && Level >= 5) {
                        double sheepskinChance = Math.min(Level,15) * 0.05;
                        if (Math.random() <= sheepskinChance) wolfQueue.add(new Sheepskin(16,16));
                    }
                }
                if (wolfQueue.size() > 0) {
                    if (++wolfTimer > 40) {
                        boolean redo = false;
                        do {
                            redo = false;
                            int r = (int)(Math.random() * wolfQueue.size());
                            if (wolfQueue.get(r) instanceof Sheepskin && wolfQueue.size() > 1) redo = true;
                            if (wolfQueue.get(r) instanceof WolfBoss) {
                                maintool.playSound("sfx_howl");
                                if (bgm != null) bgm.Stop();
                                bgm = null;
                                try {
                                    bgm = new Sound("mus_boss");
                                    bgm.setVolume(musVolume);
                                } catch (Exception e) {}
                                delayBgm = 100;
                            }
                            wolfQueue.get(r).relocateRestrike();
                            entities.add(wolfQueue.get(r));
                            wolfQueue.remove(r);
                            wolfTimer = 0;
                        } while (redo);
                    }
                }
                
                if (delayBgm > 1) {
                    delayBgm--;
                } else if (delayBgm == 1) {
                    if (bgm != null) bgm.Loop();
                    delayBgm = 0;
                }
                
                for (Entity i : entities) i.tick();
                if (spawnIn.size() > 0) {
                    for (Entity i : spawnIn) entities.add(i);
                    spawnIn.clear();
                }
                if (cam != null) cam.move();
                if (absorbAttackCall && !keys[4].pressed()) absorbAttackCall = false;
                if (Difficulty >= 3) {
                    if (Math.random() < 0.125 && tool.getNumberOfSheep() < 24) maintool.spawnSheep(false);
                    if (tool.getNumberOfPlayers() > 0) score++;
                }
                if (recordedHealth <= 0 || tool.getNumberOfSheep() <= 0) {
                    menuQueue = 3;
                    fade[1] = 1;
                    delayBgm = 0;
                    if (bgm != null) bgm.Stop();
                }
            }
            if (score > highScore) highScore = score;
            
            mainWindow.render(entities, buttons.buttons);
            Iterator<Entity> IT = entities.iterator();
            if (!paused) while(IT.hasNext()) if (IT.next().deathAnimFinished()) IT.remove();
            
            Iterator<Sound> IT2 = sounds.iterator();
            while(IT2.hasNext()) if (IT2.next().canRemove()) IT2.remove();
            soundsQueue.clear();
            
            //System.out.println("Lvl " + Level + ", score: " + score);
            
            if (keys[7].pressed()) {
                closing++;
            } else closing = 0;
            if (!mainWindow.isDisplayable()) {
                close = true;
                fade[0] = 40;
            }
            
            if (closing >= 30 || (close && fade[0] >= 40)) {
                maintool.SAVE();
                mainWindow.dispose();
                break;
            }
            if (freezeFrame) {
                freezeFrame = false;
                try{Thread.sleep(25);}catch(Exception e){};
            }
            try{Thread.sleep(25);}catch(Exception e){};
        }
        System.exit(0);
    }
    public static class maintool {
        static void generateMap() {
            int wdht = 32 + (32 * main.MapSize);
            int brdr = 16;
            tileMap = new ArrayList<ArrayList<Integer>>();
            for (int i = 1; i <= wdht + (brdr * 2); i++) {
                ArrayList<Integer> tiles = new ArrayList<Integer>();
                for (int j = 1; j <= wdht + (brdr * 2); j++) {
                    if ((i <= brdr || j <= brdr) || (i > brdr + wdht || j > brdr + wdht)) {
                        tiles.add(0);
                    } else {
                        tiles.add(1);
                    }
                }
                tileMap.add(tiles);
            }
        }
        static void createTilemapRender() {
            GrassAssets art = new GrassAssets();
            int ts = 16;
            
            BufferedImage map = new BufferedImage(tileMap.get(0).size() * ts, tileMap.size() * ts, 1);
            Graphics2D g = map.createGraphics();
            
            int E_001 = tileMap.size();
            for (int i = 0; i < E_001; i++) {
                int E_002 = tileMap.get(i).size();
                for (int j = 0; j < E_002; j++) {
                    String n = "grass";
                    if ((int)(Math.random() * 2) == 0) {
                        if ((int)(Math.random() * 8) == 0) {
                            n += "_flowers" + (int)(Math.random() * 4);
                        } else n += "_rough";
                    }
                    if ((int)(Math.random() * 2) == 0) n += "_flipped";
                    g.drawImage(art.get(n), j * ts, i * ts, null);
                }
            }
            g.dispose();
            
            mapImage = (Image) map;
        }
        static void createLeafmapRender() {
            LeavesAssets art = new LeavesAssets();
            int ts = 16;
            
            BufferedImage map = new BufferedImage(tileMap.get(0).size() * ts, tileMap.size() * ts, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = map.createGraphics();
            
            int E_001 = tileMap.size();
            for (int i = 0; i < E_001; i++) {
                int E_002 = tileMap.get(i).size();
                for (int j = 0; j < E_002; j++) {
                    if (tileMap.get(i).get(j) == 0) {
                        String n = "leaves";
                        if ((int)(Math.random() * 20) == 0) {
                            n += "_thin_" + (int)(Math.random() * 4);
                        }
                        g.drawImage(art.get(n), j * ts, i * ts, null);
                    } else if (tileMap.get(i).get(j) != 0) {
                        String n = "leaves_edge";
                        
                        try{
                            if (tileMap.get(i - 1).get(j) == 0) {n += "N";} else if (tileMap.get(i + 1).get(j) == 0) {n += "S";}
                            if (tileMap.get(i).get(j - 1) == 0) {n += "W";} else if (tileMap.get(i).get(j + 1) == 0) {n += "E";}
                        }catch(Exception e){}
                        
                        g.drawImage(art.get(n), j * ts, i * ts, null);
                    }
                }
            }
            g.dispose();
            
            mapLeaves = (Image) map;
        }
        static void createMenuBackground() {
            GrassAssets art = new GrassAssets();
            LeavesAssets art2 = new LeavesAssets();
            int ts = 16;
            
            ArrayList<ArrayList<Integer>> tmpMap =  new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i < 64; i++) {
                ArrayList<Integer> tiles = new ArrayList<Integer>();
                for (int j = 0; j < 64; j++) {
                    tiles.add(0);
                }
                tmpMap.add(tiles);
            }
            for (int i = (int)(Math.random() * 9) + 16; i > 0; i--) {
                int w = (int)(Math.random() * 3) + 2;
                int h = (int)(Math.random() * 3) + 2;
                int x = (int)(Math.random() * (63 - w)) + 1;
                int y = (int)(Math.random() * (63 - h)) + 1;
                boolean skip = false;
                for (int i1 = -1; i1 < h + 1; i1++) {
                    if (skip) break;
                    for (int j1 = -1; j1 < w + 1; j1++) {
                        try{
                            if (skip) break;
                            if (tmpMap.get(i1 + y).get(j1 + x) == 1) skip = true;
                        }catch(Exception e){
                            skip = true;
                        }
                    }
                }
                if (skip) continue;
                for (int i1 = 0; i1 < h; i1++) {
                    for (int j1 = 0; j1 < w; j1++) {
                        try{
                            tmpMap.get(i1 + y).set(j1 + x, 1);
                        }catch(Exception e){}
                    }
                }
            }
            BufferedImage map = new BufferedImage(tmpMap.get(0).size() * ts, tmpMap.size() * ts, 1);
            Graphics2D g = map.createGraphics();
            
            int E_001 = tmpMap.size();
            for (int i = 0; i < E_001; i++) {
                int E_002 = tmpMap.get(i).size();
                for (int j = 0; j < E_002; j++) {
                    String n = "grass";
                    if ((int)(Math.random() * 2) == 0) {
                        if ((int)(Math.random() * 8) == 0) {
                            n += "_flowers" + (int)(Math.random() * 4);
                        } else n += "_rough";
                    }
                    if ((int)(Math.random() * 2) == 0) n += "_flipped";
                    g.drawImage(art.get(n), j * ts, i * ts, null);
                }
            }
            for (int i = 0; i < E_001; i++) {
                int E_002 = tmpMap.get(i).size();
                for (int j = 0; j < E_002; j++) {
                    if (tmpMap.get(i).get(j) == 0) {
                        String n = "leaves";
                        if ((int)(Math.random() * 20) == 0) {
                            n += "_thin_" + (int)(Math.random() * 4);
                        }
                        g.drawImage(art2.get(n), j * ts, i * ts, null);
                    } else if (tmpMap.get(i).get(j) != 0) {
                        String n = "leaves_edge";
                        
                        try{
                            if (tmpMap.get(i - 1).get(j) == 0) {n += "N";} else if (tmpMap.get(i + 1).get(j) == 0) {n += "S";}
                            if (tmpMap.get(i).get(j - 1) == 0) {n += "W";} else if (tmpMap.get(i).get(j + 1) == 0) {n += "E";}
                        }catch(Exception e){}
                        
                        g.drawImage(art2.get(n), j * ts, i * ts, null);
                    }
                }
            }
            g.dispose();
            
            menuBackground = (Image) map;
        }
        static int mapCenterX() {
            if (main.tileMap != null) return main.tileMap.get(0).size() * 8 + 1;
            return 0;
        }
        static int mapCenterY() {
            if (main.tileMap != null) return main.tileMap.size() * 8 + 1;
            return 0;
        }
        public static boolean wolfSpawnZone(double x, double y) {
            if (x < 64 || y < 64 || x > (main.tileMap.get(0).size() * 16) - 64 || y > (main.tileMap.size() * 16) - 64) return true;
            return false;
        }
        public static void spawnSheep(boolean falling) {
            double x = Math.floor(Math.random() * 129) - 64 + mapCenterX();
            double y = Math.floor(Math.random() * 129) - 64 + mapCenterY();
            if ((int)(Math.random() * 20) == 0) {
                main.entities.add(new DerpShep(x,y,falling));
            } else main.entities.add(new Sheep(x,y,falling));
        }
        public static boolean checkForSheepskin() {
            for (Entity i : entities) if (i instanceof Sheepskin) return true;
            return false;
        }
        public static void playSound(String sound) {
            for (String i : soundsQueue) if (sound.equals(i)) return;
            try {
                Sound tmp = new Sound(sound);
                tmp.setVolume(sfxVolume);
                tmp.Start();
                sounds.add(tmp);
                soundsQueue.add(sound);
            } catch (Exception e) {}
        }
        public static void playSound(String sound, float volume, float panning) {
            for (String i : soundsQueue) if (sound.equals(i)) return;
            try {
                Sound tmp = new Sound(sound);
                tmp.setVolume(volume * sfxVolume);
                tmp.setPanning(panning);
                tmp.Start();
                sounds.add(tmp);
                soundsQueue.add(sound);
            } catch (Exception e) {}
        }
        public static void stopAllSounds() {
            for (Sound i : sounds) i.Stop();
        }
        public static void startAllSounds() {
            for (Sound i : sounds) if (i.clip.getMicrosecondPosition() < i.clip.getMicrosecondLength()) i.Start();
        }
        public static void SAVE() {
            try{
                if (!saves.exists()) {
                    saves.createNewFile();
                }
                BufferedWriter w = new BufferedWriter(new FileWriter(saves));
                w.write("save-version: 1");
                w.newLine();
                w.write(Difficulty + "");
                w.newLine();
                w.write(MapSize + "");
                w.newLine();
                int E_001 = keys.length;
                for (int i = 0; i < E_001; i++) {
                    w.write(keys[i].getKeybind());
                    w.newLine();
                }
                w.write(highScore + "");
                w.newLine();
                w.write(alwaysShowHealthbars + "");
                w.newLine();
                w.write(sfxVolume + "");
                w.newLine();
                w.write(musVolume + "");
                w.newLine();
                w.write(n_009);
                //w.newLine();
                w.close();
                
            }catch(IOException e){}
        }
    }
    public static class menus {
        public static void MainMenu(Boolean Ending) { //menu = 1
            if (Ending) {
                tileMap.clear();
                mapImage = null;
                mapLeaves = null;
                entities.clear();
                cam = null;
                wolfQueue.clear();
                wolfTimer = 0;
                UID = 0;
                recordedHealth = 50;
                recordedFood = 0;
                paused = true;
                menuWander[0] = (int)(Math.random() * 1024);
                menuWander[1] = (int)(Math.random() * 1024);
                menuWander[2] += 90 * (int)(Math.random() * 4);
            }
            menu = 1;
            buttons.activateButtons();
            ArrayList<Button> tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Start Game") * 0.5),0,"Start Game",() -> {
                menuQueue = -1;
                fade[1] = 1;
                maintool.playSound("sfx_start");
                if (bgm != null) bgm.Stop();
                bgm = null;
                try {
                    bgm = new Sound("mus_game");
                    bgm.setVolume(musVolume);
                    startupMusic = true;
                } catch (Exception e) {}
            },()->{}));
            buttons.buttons.add(tmp);
            tmp = new ArrayList<Button>();
            String[] d1 = {"Easy","Medium","Hard","NIGHTMARE"};
            String d2 = d1[Math.max(Math.min(Difficulty, 3), 0)];
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Difficulty: " + d2) * 0.5),16,"Difficulty: " + d2,() -> {
                Difficulty++;
                if (n_009.equals("9999")) {if (Difficulty >= 4) Difficulty = 0;} else if (Difficulty >= 3) Difficulty = 0;
                int i = buttons.selectedButton[0];
                int j = buttons.selectedButton[1];
                buttons.deactivateButtons();
                MainMenu(false);
                buttons.selectedButton[0] = i;
                buttons.selectedButton[1] = j;
                buttons.buttons.get(i).get(j).hover = true;
                //buttons.buttonHandler();
            },()->{}));
            buttons.buttons.add(tmp);
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Settings") * 0.5),32,"Settings",() -> {
                Settings(false);
            },()->{}));
            buttons.buttons.add(tmp);
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Quit Game") * 0.5),48,"Quit Game",() -> {
                close = true;
                if (bgm != null) bgm.Stop();
                menuQueue = -2;
                fade[1] = 1;
                maintool.playSound("sfx_unpause");
            },()->{}));
            buttons.buttons.add(tmp);
            
        }
        public static void StartGame() { //menu = -1
            menu = -1;
            paused = false;
            buttons.deactivateButtons();
            maintool.generateMap();
            maintool.createTilemapRender();
            maintool.createLeafmapRender();
            
            Level = 0;
            score = 0;
            highScorePrev = highScore;
            
            entities.add(new Player(maintool.mapCenterX(),maintool.mapCenterY()));
            for (int i = 0; i < 4; i++) maintool.spawnSheep(false);
            
            if (Difficulty >= 3) entities.add(new Entity(16, 16));
            
            cam = new Camera(maintool.mapCenterX(), maintool.mapCenterY());
            if (Difficulty == 2) {wolfAttack = 1;} else wolfAttack = 0;
        }
        public static void Settings(boolean reload) { //menu = 2
            int i = 0, j = 0;
            if (reload) {
                i = buttons.selectedButton[0];
                j = buttons.selectedButton[1];
            }
            menu = 2;
            buttons.activateButtons();
            if (reload) {
                buttons.selectedButton[0] = i;
                buttons.selectedButton[1] = j;
            }
            int b = -80;
            ArrayList<Button> tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Up: " + keys[0].getKeybind(),() -> {
                keyboardInput.changingKeybind = 0;
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Down: " + keys[1].getKeybind(),() -> {
                keyboardInput.changingKeybind = 1;
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Left: " + keys[2].getKeybind(),() -> {
                keyboardInput.changingKeybind = 2;
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Right: " + keys[3].getKeybind(),() -> {
                keyboardInput.changingKeybind = 3;
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Select/Attack: " + keys[4].getKeybind(),() -> {
                keyboardInput.changingKeybind = 4;
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Heal: " + keys[5].getKeybind(),() -> {
                keyboardInput.changingKeybind = 5;
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Fullscreen: " + keys[6].getKeybind(),() -> {
                keyboardInput.changingKeybind = 6;
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Back/Close: " + keys[7].getKeybind(),() -> {
                keyboardInput.changingKeybind = 7;
            },()->{}));
            buttons.buttons.add(tmp);
            
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Reset Keybinds",() -> {
                keys[0].changeKeybind("W");
                keys[1].changeKeybind("S");
                keys[2].changeKeybind("A");
                keys[3].changeKeybind("D");
                keys[4].changeKeybind("Enter");
                keys[5].changeKeybind("Shift");
                keys[6].changeKeybind("F11");
                keys[7].changeKeybind("Escape");
                Settings(true);
            },()->{}));
            buttons.buttons.add(tmp);
            
            b += 12;
            tmp = new ArrayList<Button>();
            String tmp2 = (alwaysShowHealthbars) ? "Yes":"No";
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Always Show Healthbars: " + tmp2,() -> {
                alwaysShowHealthbars = !alwaysShowHealthbars;
                Settings(true);
            },()->{}));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            String tmp3 = (int)(sfxVolume * 100) + "%";
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Sound Volume: " + tmp3,() -> {},()->{
                changeVolume[0] = (byte) 1;
            }));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            String tmp4 = (int)(musVolume * 100) + "%";
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Music Volume: " + tmp4,() -> {},()->{
                changeVolume[0] = (byte) 2;
            }));
            buttons.buttons.add(tmp);
            b += 12;
            tmp = new ArrayList<Button>();
            tmp.add(new Button(0 - (int)(Window.getTextWidth("Always Show Healthbars") * 0.5),b,"Back",() -> {
                MainMenu(false);
            },()->{}));
            buttons.buttons.add(tmp);
            
            if (reload) buttons.buttons.get(i).get(j).hover = true;
        }
        public static void GameOver() { //menu = 3
            buttons.activateButtons();
            menu = 3;
            paused = true;
            ArrayList<Button> tmp = new ArrayList<Button>();
            String tmp2 = "Press " + keys[4].getKeybind() + " to continue";
            tmp.add(new Button(0 - (int)(Window.getTextWidth(tmp2) * 0.5),40,tmp2,() -> {
                menuQueue = 1;
                fade[1] = 1;
            },()->{},false));
            buttons.buttons.add(tmp);
        }
    }
}













