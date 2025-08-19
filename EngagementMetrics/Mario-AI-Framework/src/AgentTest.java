import engine.core.MarioGame;
import engine.core.MarioResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AgentTest {
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

    public static void repeatNewAgent(int times, BufferedWriter writer) throws IOException{
        String part_filepath = "../levels/original/lvl-";
        float weights[] = readWeights();
        float completion = 0, remaining_time = 0, mariostate = 0;
        float total_kill = 0, stomp_kill = 0, fire_kill = 0, shell_kill = 0, hurts = 0;
        float question_blocks = 0, bump_bricks = 0, destroyed_bricks = 0;
        float coins = 0, tile_coins = 0, mushrooms = 0, fireflowers = 0, jumps = 0;
        int num_levels = 0;

        for(int j=1; j<=9; j+=2) { // 플레이할 레벨 설정
            String full_filepath = part_filepath + j + ".txt";
            for (int i = 0; i < times; i++) { 
                MarioGame game = new MarioGame();
                // printResults(game.playGame(getLevel("../levels/original/lvl-1.txt"), 200, 0));
                MarioResult result = game.runGame(new agents.newagent.Agent(true), getLevel(full_filepath), 50, 1, true);

                completion += result.getCompletionPercentage(); // completion: 마리오가 목표 지점까지 도달한 거리 비율 (0~1)
                remaining_time += (int) Math.ceil(result.getRemainingTime() / 1000f); // remaining_time: 레벨을 플레이하고 남은 시간
                mariostate += result.getMarioMode(); // mariostate: 마리오의 상태 (0: small, 1: large, 2: fire)

                total_kill += result.getKillsTotal(); // total_kill: 적을 처치한 총 횟수
                stomp_kill += result.getKillsByStomp(); // stomp_kill: 점프로 적을 밟아 처치한 횟수
                fire_kill += result.getKillsByFire(); // fire_kill: 파이어볼로 적을 처치한 횟수
                shell_kill += result.getKillsByShell(); // shell_kill: 셸로 적을 처치한 횟수
                hurts += result.getMarioNumHurts(); // hurts: 적과 충돌한 프레임 수

                question_blocks += result.getNumBumpQuestionBlock(); // question_blocks: 물음표 블록을 두드린 횟수
                bump_bricks += result.getNumBumpBrick(); // bump_bricks: 벽돌 블록을 두드린 횟수
                destroyed_bricks += result.getNumDestroyedBricks(); // destroyed_bricks: 깨뜨린 벽돌 블록 수

                coins += result.getCurrentCoins(); // coins: 획득한 코인 수
                tile_coins += result.getNumCollectedTileCoins(); // tile_coins: 타일에 있는 코인을 수집한 횟수
                mushrooms += result.getNumCollectedMushrooms(); // mushrooms: 먹은 버섯 수
                fireflowers += result.getNumCollectedFireflower(); // fireflowers: 먹은 파이어 플라워 수
                jumps += result.getNumJumps(); // jumps: 점프한 횟수
            }
            num_levels++;
        }
        // 평균 계산
        completion /=times*num_levels; remaining_time /=times*num_levels; mariostate /=times*num_levels;
        total_kill /=times*num_levels; stomp_kill /=times*num_levels; fire_kill /=times*num_levels; shell_kill /=times*num_levels; hurts /=times*num_levels;
        question_blocks /=times*num_levels; bump_bricks /=times*num_levels; destroyed_bricks /=times*num_levels;
        coins /=times*num_levels; tile_coins /=times*num_levels; mushrooms /=times*num_levels; fireflowers /=times*num_levels; jumps /=times*num_levels;

        // CSV 한 줄 출력
        writer.write(weights[0] + "," + weights[1] + "," + weights[2] + "," + weights[3] + "," + weights[4] + "," + weights[5] + ","
                + completion + "," + remaining_time + "," + mariostate + "," + total_kill + ","
                + stomp_kill + "," + fire_kill + "," + shell_kill + "," + hurts + "," + question_blocks + ","
                + bump_bricks + "," + destroyed_bricks + "," + coins + "," + tile_coins + "," + mushrooms + "," + fireflowers + "," + jumps + "\n");
        writer.flush();
    }

    public static void runAllWeightConfigs() {
        // AStarTree.java 파일의 비용 함수에 대입할 가중치 조합을 설정
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
                    + "completion,remaining_time,mariostate,"
                    + "total_kill,stomp_kill,fire_kill,shell_kill,hurts,"
                    + "question_blocks,bump_bricks,destroyed_bricks,"
                    + "coins,tile_coins,mushrooms,fireflowers,jumps\n");

            for (float kw : killWeights) {
                for (float cw : collectWeights) {
                    for (float jw : jumpWeights) {
                        for (float tw : timeWeights) {
                            for (float ww : winWeights) {
                                for (float lw : loseWeights) {
                                    System.out.println("[" + count + "] Running weights: kill=" + kw + ", collect=" + cw + ", jump=" + jw + ", time=" + tw + ", win=" + ww + ", lose=" + lw);
                                    writeWeights(kw, cw, jw, tw, ww, lw); // csv 파일에 가중치 조합을 작성
                                    repeatNewAgent(1, writer); // csv 파일에 플레이 로그를 작성, 레벨당 반복 횟수 설정
                                    count++;
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("⚠ Failed to save csv file");
            e.printStackTrace();
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
            System.out.println("⚠ Failed to read weights.txt");
        }
        return weights;
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
