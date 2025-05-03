import engine.core.MarioGame;
import engine.core.MarioResult;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PlayLevel {
    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
        }
        return content;
    }

    public static void repeatKiller(int times) {
       String part_filepath = "../levels/original/lvl-";
       for(int j=1; j<=15; j++) {
           String full_filepath = part_filepath + j + ".txt";
           float completion = 0;
           float total_kill = 0;
           float fall_kill = 0;
           float coins = 0;
           for (int i = 0; i < times; i++) {
               MarioGame game = new MarioGame();
               // printResults(game.playGame(getLevel("../levels/original/lvl-1.txt"), 200, 0));
               MarioResult result = game.runGame(new agents.killer.Agent(), getLevel(full_filepath), 50, 0, false);
               completion += result.getCompletionPercentage();
               total_kill += result.getKillsTotal();
               fall_kill += result.getKillsByFall();
               coins += result.getCurrentCoins();
           }
           completion /= times;
           total_kill /= times;
           fall_kill /= times;
           coins /= times;
           System.out.println("========level " + j +"=======");
           System.out.println("completion : " + completion);
           System.out.println("total_kill : " + total_kill);
           System.out.println("kill : " + (total_kill - fall_kill));
           System.out.println("coins : " + coins);
       }
    }

    public static void repeatCollector(int times) {
        String part_filepath = "../levels/original/lvl-";
        for(int j=1; j<=15; j++) {
            String full_filepath = part_filepath + j + ".txt";
            float completion = 0;
            float total_kill = 0;
            float fall_kill = 0;
            float coins = 0;
            for (int i = 0; i < times; i++) {
                MarioGame game = new MarioGame();
                // printResults(game.playGame(getLevel("../levels/original/lvl-1.txt"), 200, 0));
                MarioResult result = game.runGame(new agents.collector.Agent(), getLevel(full_filepath), 50, 0, true);
                completion += result.getCompletionPercentage();
                total_kill += result.getKillsTotal();
                fall_kill += result.getKillsByFall();
                coins += result.getCurrentCoins();
            }
            completion /= times;
            total_kill /= times;
            fall_kill /= times;
            coins /= times;
            System.out.println("========level " + j +"=======");
            System.out.println("completion : " + completion);
            System.out.println("total_kill : " + total_kill);
            System.out.println("kill : " + (total_kill - fall_kill));
            System.out.println("coins : " + coins);
        }
    }

    public static void repeatRobin(int times) {
        String part_filepath = "../levels/original/lvl-";
        for(int j=1; j<=15; j++) {
            String full_filepath = part_filepath + j + ".txt";
            float completion = 0;
            float total_kill = 0;
            float fall_kill = 0;
            float coins = 0;
            for (int i = 0; i < times; i++) {
                MarioGame game = new MarioGame();
                // printResults(game.playGame(getLevel("../levels/original/lvl-1.txt"), 200, 0));
                MarioResult result = game.runGame(new agents.runner.Agent(), getLevel(full_filepath), 50, 0, false);
                completion += result.getCompletionPercentage();
                total_kill += result.getKillsTotal();
                fall_kill += result.getKillsByFall();
                coins += result.getCurrentCoins();
            }
            completion /= times;
            total_kill /= times;
            fall_kill /= times;
            coins /= times;
            System.out.println("========level " + j +"=======");
            System.out.println("completion : " + completion);
            System.out.println("total_kill : " + total_kill);
            System.out.println("kill : " + (total_kill - fall_kill));
            System.out.println("coins : " + coins);
        }
    }

    /*
    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }
     */
    public static void repeatNewAgent(int times) {
        String part_filepath = "../levels/original/lvl-";
        for(int j=1; j<=1; j++) {
            String full_filepath = part_filepath + j + ".txt";
            float completion = 0;
            float total_kill = 0;
            float fall_kill = 0;
            float coins = 0;

            float lives = 0;
            float remaining_time = 0;
            float mariostate = 0;
            float mushrooms = 0;
            float fireflowers = 0;
            float stomp_kill = 0;
            float fire_kill = 0;
            float shell_kill = 0;
            float bricks = 0;
            float jumps = 0;

            for (int i = 0; i < times; i++) {
                MarioGame game = new MarioGame();
                // printResults(game.playGame(getLevel("../levels/original/lvl-1.txt"), 200, 0));
                MarioResult result = game.runGame(new agents.newagent.Agent(), getLevel(full_filepath), 50, 1, true);
                completion += result.getCompletionPercentage();
                total_kill += result.getKillsTotal();
                fall_kill += result.getKillsByFall();
                coins += result.getCurrentCoins();

                lives += result.getCurrentLives();
                remaining_time += (int) Math.ceil(result.getRemainingTime() / 1000f);
                mariostate += result.getMarioMode();
                mushrooms += result.getNumCollectedMushrooms();
                fireflowers += result.getNumCollectedFireflower();
                stomp_kill += result.getKillsByStomp();
                fire_kill += result.getKillsByFire();
                shell_kill += result.getKillsByShell();
                bricks += result.getNumDestroyedBricks();
                jumps += result.getNumJumps();
            }
            completion /= times;
            total_kill /= times;
            fall_kill /= times;
            coins /= times;

            lives /= times;
            remaining_time /= times;
            mariostate /= times;
            mushrooms /= times;
            fireflowers /= times;
            stomp_kill /= times;
            fire_kill /= times;
            shell_kill /= times;
            bricks /= times;
            jumps /= times;

            System.out.println("========level " + j +"=======");
            System.out.println("completion : " + completion);
            System.out.println("total_kill : " + total_kill);
            System.out.println("kill : " + (total_kill - fall_kill));
            System.out.println("coins : " + coins);

            System.out.println("lives : " + lives);
            System.out.println("remaining_time : " + remaining_time);
            System.out.println("mariostate : " + mariostate);
            System.out.println("mushrooms : " + mushrooms);
            System.out.println("fireflowers : " + fireflowers);
            System.out.println("stomp_kill : " + stomp_kill);
            System.out.println("fire_kill : " + fire_kill);
            System.out.println("shell_kill : " + shell_kill);
            System.out.println("bricks : " + bricks);
            System.out.println("jumps : " + jumps);
        }
    }


    public static void main(String[] args) {
        MarioGame game = new MarioGame();
        // printResults(game.runGame(new agents.collector.Agent(), getLevel("../levels/original/lvl-1.txt"), 50, 0, true));
        // repeatCollector(5);
        // repeatKiller(5);
        // repeatRobin(5);
        repeatNewAgent(5);
    }


/* 
    public static void runAllWeightConfigs() {
        float[] jumpWeights = {0.5f, -0.5f};
        float[] winWeights = {1.0f, -1.0f};
        float[] loseWeights = {20.0f, -20.0f};

        int count = 1;

        for (float jw : jumpWeights) {
            for (float ww : winWeights) {
                for (float lw : loseWeights) {
                    System.out.println("\n[" + count + "/8] Running weights: jump=" + jw + ", win=" + ww + ", lose=" + lw);
                    writeWeights(jw, ww, lw);

                    // 로그 저장
                    try {
                        PrintStream out = new PrintStream("logs/output_" + count + ".txt");
                        PrintStream originalOut = System.out;
                        System.setOut(out);

                        repeatNewAgent(5);

                        System.setOut(originalOut);
                    } catch (IOException e) {
                        System.out.println("⚠ Failed to write log file.");
                        e.printStackTrace();
                    }

                    count++;
                }
            }
        }
    }

    public static void writeWeights(float jump, float win, float lose) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config/weights.txt"));
            writer.write("jumpWeight=" + jump + "\n");
            writer.write("winWeight=" + win + "\n");
            writer.write("loseWeight=" + lose + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("⚠ Failed to write weights.txt");
            e.printStackTrace();
        }   
    }

    public static void main(String[] args) {
        runAllWeightConfigs();  // 여기에만 작성하세요
    }
*/
}
