import engine.core.MarioGame;
import engine.core.MarioResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public static void repeatNewAgent(int times) {
        String part_filepath = "../levels/original/lvl-";
        for(int j=1; j<=15; j++) { // 플레이할 레벨 설정
            String full_filepath = part_filepath + j + ".txt";
            float completion = 0, remaining_time = 0, mariostate = 0;
            float total_kill = 0, stomp_kill = 0, fire_kill = 0, shell_kill = 0, hurts = 0;
            float question_blocks = 0, bump_bricks = 0, destroyed_bricks = 0;
            float coins = 0, tile_coins = 0, mushrooms = 0, fireflowers = 0, jumps = 0;
            
            for (int i = 0; i < times; i++) { 
                MarioGame game = new MarioGame();
                // printResults(game.playGame(getLevel("../levels/original/lvl-1.txt"), 200, 0));
                MarioResult result = game.runGame(new agents.newagent.Agent(false), getLevel(full_filepath), 50, 1, true);

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
            completion /= times;
            remaining_time /= times;
            mariostate /= times;
            total_kill /= times;
            stomp_kill /= times;
            fire_kill /= times;
            shell_kill /= times;
            hurts /= times;
            question_blocks /= times;
            bump_bricks /= times;
            destroyed_bricks /= times;
            coins /= times;
            tile_coins /= times;
            mushrooms /= times;
            fireflowers /= times;
            jumps /= times;

            System.out.println("========level " + j +"=======");
            System.out.println("completion : " + completion);
            System.out.println("remaining_time : " + remaining_time);
            System.out.println("mariostate : " + mariostate);
            System.out.println("total_kill : " + total_kill);
            System.out.println("stomp_kill : " + stomp_kill);
            System.out.println("fire_kill : " + fire_kill);
            System.out.println("shell_kill : " + shell_kill);
            System.out.println("hurts : " + hurts);
            System.out.println("question_blocks : " + question_blocks);
            System.out.println("bump_bricks : " + bump_bricks);
            System.out.println("destroyed_bricks : " + destroyed_bricks);
            System.out.println("coins : " + coins);
            System.out.println("tile_coins : " + tile_coins);
            System.out.println("mushrooms : " + mushrooms);
            System.out.println("fireflowers : " + fireflowers);
            System.out.println("jumps : " + jumps);
        }
    }

    public static void main(String[] args) {
        MarioGame game = new MarioGame();
//        printResults(game.runGame(new agents.collectorWeek.Agent(), getLevel("../levels/original/lvl-1.txt"), 50, 0, true));
//        repeatCollector(5);
//        repeatKiller(5);
//        repeatRobin(5);
        repeatNewAgent(1); // 레벨당 반복 횟수 설정
    }
}
