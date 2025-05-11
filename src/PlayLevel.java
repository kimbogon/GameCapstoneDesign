import engine.core.MarioGame;
import engine.core.MarioResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


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

    public static float[] readWeights() {
        float[] weights = new float[6]; // kill, collect, jump, time, win, lose
        try {
            Properties props = new Properties();
            props.load(new FileReader("config/weights.txt"));
            weights[0] = Float.parseFloat(props.getProperty("killWeight", "0"));
            weights[1] = Float.parseFloat(props.getProperty("collectWeight", "0"));
            weights[2] = Float.parseFloat(props.getProperty("jumpWeight", "0"));
            weights[3] = Float.parseFloat(props.getProperty("timeWeight", "0"));
            weights[4] = Float.parseFloat(props.getProperty("winWeight", "0"));
            weights[5] = Float.parseFloat(props.getProperty("loseWeight", "0"));
        } catch (IOException e) {
            System.out.println("⚠ weights.txt 읽기 실패");
        }
        return weights;
    }

    public static void repeatNewAgent(int times, BufferedWriter writer) throws IOException {
        String part_filepath = "levels/original/lvl-";
        float[] weights = readWeights();

        float completion = 0, total_kill = 0, fall_kill = 0, coins = 0;
        float lives = 0, remaining_time = 0, mariostate = 0, mushrooms = 0, fireflowers = 0;
        float stomp_kill = 0, fire_kill = 0, shell_kill = 0, bricks = 0, jumps = 0;

        for (int j = 1; j <= 9; j += 2) { // 플레이할 레벨 설정

            for (int i = 0; i < times; i++) { // 레벨당 반복 횟수 설정
                MarioGame game = new MarioGame();
                MarioResult result = game.runGame(new agents.newagent.Agent(), getLevel(part_filepath + j + ".txt"), 50, 1, false);

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
        }
        // 평균 계산
        int test_levels = 5;
        completion /= times*test_levels; total_kill /= times*test_levels; fall_kill /= times*test_levels; coins /= times*test_levels;
        lives /= times*test_levels; remaining_time /= times*test_levels; mariostate /= times*test_levels;
        mushrooms /= times*test_levels; fireflowers /= times*test_levels;
        stomp_kill /= times*test_levels; fire_kill /= times*test_levels; shell_kill /= times*test_levels;
        bricks /= times*test_levels; jumps /= times*test_levels;

        // CSV 한 줄 출력
        writer.write(weights[0] + "," + weights[1] + "," + weights[2] + "," + weights[3] + "," + weights[4] + "," + weights[5] + ","
                + completion + "," + total_kill + "," + (total_kill - fall_kill) + "," + coins + ","
                + lives + "," + remaining_time + "," + mariostate + "," + mushrooms + "," + fireflowers + ","
                + stomp_kill + "," + fire_kill + "," + shell_kill + "," + bricks + "," + jumps + "\n");
        writer.flush();
    }

    public static void runAllWeightConfigs() {
        // 가중치 조합 설정
        float[] killWeights = {-3.0f, -1.0f, 0.0f, 1.0f, 3.0f};
        float[] collectWeights = {-3.0f, -1.0f, 0.0f, 1.0f, 3.0f};
        float[] jumpWeights = {-3.0f, -1.0f, 0.0f};
        float[] timeWeights = {-3.0f, -1.0f, 0.0f};
        float[] winWeights = {-10.0f};
        float[] loseWeights = {10.0f};

        int count = 1;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs/results.csv"))) {
            // CSV 헤더
            writer.write("killWeight,collectWeight,jumpWeight,timeWeight,winWeight,loseWeight,"
                    + "completion,total_kill,kill,coins,"
                    + "lives,remaining_time,mariostate,mushrooms,fireflowers,"
                    + "stomp_kill,fire_kill,shell_kill,bricks,jumps\n");

            for (float kw : killWeights) {
                for (float cw : collectWeights) {
                    for (float jw : jumpWeights) {
                        for (float tw : timeWeights) {
                            for (float ww : winWeights) {
                                for (float lw : loseWeights) {
                                    System.out.println("[" + count + "] Running weights: kill=" + kw + ", collect=" + cw + ", jump=" + jw + ", time=" + tw + ", win=" + ww + ", lose=" + lw);
                                    writeWeights(kw, cw, jw, tw, ww, lw);
                                    repeatNewAgent(1, writer);
                                    count++;
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("⚠ CSV 파일 저장 실패");
            e.printStackTrace();
        }
    }

    public static void writeWeights(float kill, float collect, float jump, float time, float win, float lose) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("config/weights.txt"));
            writer.write("killWeight=" + kill + "\n");
            writer.write("collectWeight=" + collect + "\n");
            writer.write("jumpWeight=" + jump + "\n");
            writer.write("timeWeight=" + time + "\n");
            writer.write("winWeight=" + win + "\n");
            writer.write("loseWeight=" + lose + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("⚠ Failed to write weights.txt");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runAllWeightConfigs();
    }
}
