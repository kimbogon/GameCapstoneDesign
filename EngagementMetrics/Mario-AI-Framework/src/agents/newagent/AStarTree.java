package agents.newagent;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.GameStatus;

public class AStarTree {
    public SearchNode bestPosition;
    public SearchNode furthestPosition;
    float currentSearchStartingMarioXPos;
    ArrayList<SearchNode> posPool;
    ArrayList<int[]> visitedStates = new ArrayList<int[]>();
    private boolean requireReplanning = false;

    private ArrayList<boolean[]> currentActionPlan;
    int ticksBeforeReplanning = 0;
    public int SearchedStates = 0;
    public int SearchedLose = 0;

    // 추가
    private float killWeight = -50;
    private float collectWeight = -40;
    private float jumpWeight = -30;
    private float timeWeight = -30;
    private float winWeight = -100;
    private float loseWeight = +100;

    // 추가
    public AStarTree(boolean useWeightsFile) {
        if (useWeightsFile){
            try {
                Properties props = new Properties();
                props.load(new FileReader("config/weights.txt"));

                if (props.getProperty("killWeight") != null)
                    killWeight = Float.parseFloat(props.getProperty("killWeight"));
                if (props.getProperty("collectWeight") != null)
                    collectWeight = Float.parseFloat(props.getProperty("collectWeight"));
                if (props.getProperty("jumpWeight") != null)
                    jumpWeight = Float.parseFloat(props.getProperty("jumpWeight"));
                if (props.getProperty("timeWeight") != null)
                    timeWeight = Float.parseFloat(props.getProperty("timeWeight"));
                if (props.getProperty("winWeight") != null)
                    winWeight = Float.parseFloat(props.getProperty("winWeight"));
                if (props.getProperty("loseWeight") != null)
                    loseWeight = Float.parseFloat(props.getProperty("loseWeight"));
            } catch (IOException e) {
                System.out.println("⚠ weights.txt 읽기 실패, 기본값 사용");
            }
        }
    }

    private MarioForwardModel search(MarioTimer timer) {
        SearchNode current = bestPosition;
        boolean currentGood = false;
        int maxRight = 176;
        while (posPool.size() != 0
                && ((bestPosition.sceneSnapshot.getMarioFloatPos()[0] - currentSearchStartingMarioXPos < maxRight) || !currentGood)
                && timer.getRemainingTime() > 0) {
            current = pickBestPos(posPool);
            if (current == null) {
                return null;
            }
            currentGood = false;
            float realRemainingTime = current.simulatePos();
            if (realRemainingTime < 0) {
                continue;
            } else if (!current.isInVisitedList && isInVisited((int) current.sceneSnapshot.getMarioFloatPos()[0],
                    (int) current.sceneSnapshot.getMarioFloatPos()[1], current.timeElapsed)) {
                realRemainingTime += Helper.visitedListPenalty;
                current.isInVisitedList = true;
                current.remainingTime = realRemainingTime;
                current.remainingTimeEstimated = realRemainingTime;
                posPool.add(current);
            } else if (realRemainingTime - current.remainingTimeEstimated > 0.1) {
                // current item is not as good as anticipated. put it back in pool and look for best again
                current.remainingTimeEstimated = realRemainingTime;
                posPool.add(current);
            } else {
                currentGood = true;
                visited((int) current.sceneSnapshot.getMarioFloatPos()[0], (int) current.sceneSnapshot.getMarioFloatPos()[1], current.timeElapsed);
                posPool.addAll(current.generateChildren());
            }
            if (currentGood) {
                if (bestPosition.getRemainingTime() > current.getRemainingTime())
                    bestPosition = current;
                if (current.sceneSnapshot.getMarioFloatPos()[0] > furthestPosition.sceneSnapshot.getMarioFloatPos()[0])
                    furthestPosition = current;
            }
        }
        if (current.sceneSnapshot.getMarioFloatPos()[0] - currentSearchStartingMarioXPos < maxRight
                && furthestPosition.sceneSnapshot.getMarioFloatPos()[0] > bestPosition.sceneSnapshot.getMarioFloatPos()[0] + 20)
            // Couldnt plan till end of screen, take furthest
            bestPosition = furthestPosition;

        return current.sceneSnapshot;
    }

    private void startSearch(MarioForwardModel model, int repetitions) {
        SearchNode startPos = new SearchNode(null, repetitions, null);
        startPos.initializeRoot(model);

        posPool = new ArrayList<SearchNode>();
        visitedStates.clear();

        ArrayList<SearchNode> tempPool = startPos.generateChildren();

        posPool.addAll(tempPool);
        currentSearchStartingMarioXPos = model.getMarioFloatPos()[0];

        bestPosition = startPos;
        furthestPosition = startPos;
    }

    private ArrayList<boolean[]> extractPlan() {
        ArrayList<boolean[]> actions = new ArrayList<boolean[]>();

        // just move forward if no best position exists
        if (bestPosition == null) {

            //actions.add(Helper.createAction(false, true, false, false, true));
            actions.add(Helper.createAction(false, true, false, true, true));

            return actions;
        }

        SearchNode current = bestPosition;
        while (current.parentPos != null) {
            for (int i = 0; i < current.repetitions; i++)
                actions.add(0, current.action);
            if (current.hasBeenHurt) {
                requireReplanning = true;
            }
            current = current.parentPos;
        }
        return actions;
    }

    private SearchNode pickBestPos(ArrayList<SearchNode> posPool) {
        SearchNode bestPos = null;
        float bestPosCost = 10000000;
        for (SearchNode current : posPool) {
            //float currentCost = current.getRemainingTime() * 0.1f + current.timeElapsed * 0.10f - 10000 * current.getkilled(); // slightly bias towards furthest positions
            float[] Enemies = current.getEnemiesFloatPos();
            float distance = 0;
            float mario_x = current.getMarioX();
            float mario_y = current.getMarioY();
//            if(Enemies != null && Enemies.length != 0){
//                for(int i=0; i<Enemies.length; i += 3){
//                    distance += (mario_x - Enemies[i+1])*(mario_x - Enemies[i+1]);
//                    distance += (mario_y - Enemies[i+2])*(mario_y - Enemies[i+2]);
////                    System.out.println("x " + mario_x + " mon "+ Enemies[i+1] + " dis: " + (mario_x - Enemies[i+1])*(mario_x - Enemies[i+1]));
////                    System.out.println("y " + mario_y + " mon "+ Enemies[i+2] + " dis: " + (mario_y - Enemies[i+2])*(mario_y - Enemies[i+2]));
//
//                }
//                distance = distance/Enemies.length ;
//                System.out.println("distance: " + distance);
//
//            }
            if(!current.check){
                current.check = true;
                SearchedStates++;
                if(current.ifLose() == 1){
                    SearchedLose++;
                }
            }
            //float currentCost = - 2 * current.getkillrate() -  current.ifWin() + 20 * current.ifLose();
            //System.out.println("Mario killed: " + current.getkilled() + " CurrentCost: "+ currentCost);
            float currentCost 
            = killWeight * current.getkillrate() 
            + collectWeight * current.getCollectRate() 
            + jumpWeight * current.getJumpTimeRatio() 
            + timeWeight * current.getRemainingTimeRatio() 
            + winWeight * current.ifWin() 
            + loseWeight * current.ifLose();
            if (currentCost < bestPosCost) {
                bestPos = current;
                bestPosCost = currentCost;
            }
        }
        posPool.remove(bestPos);
        return bestPos;
    }

    public boolean[] optimise(MarioForwardModel model, MarioTimer timer) {
        int planAhead = 2;
        int stepsPerSearch = 4;

        MarioForwardModel originalModel = model.clone();
        ticksBeforeReplanning--;
        requireReplanning = false;
        if (ticksBeforeReplanning <= 0 || currentActionPlan.size() == 0 || requireReplanning) {
            currentActionPlan = extractPlan();
            if (currentActionPlan.size() < planAhead) {
                planAhead = currentActionPlan.size();
            }

            // simulate ahead to predicted future state, and then plan for this future state
            for (int i = 0; i < planAhead; i++) {
                model.advance(currentActionPlan.get(i));
            }
            startSearch(model, stepsPerSearch);
            ticksBeforeReplanning = planAhead;
        }
        if (model.getGameStatus() == GameStatus.LOSE) {
            startSearch(originalModel, stepsPerSearch);
        }
        search(timer);

        boolean[] action = new boolean[5];
        if (currentActionPlan.size() > 0)
            action = currentActionPlan.remove(0);
        return action;
    }

    private void visited(int x, int y, int t) {
        visitedStates.add(new int[]{x, y, t});
    }

    private boolean isInVisited(int x, int y, int t) {
        int timeDiff = 5;
        int xDiff = 2;
        int yDiff = 2;
        for (int[] v : visitedStates) {
            if (Math.abs(v[0] - x) < xDiff && Math.abs(v[1] - y) < yDiff && Math.abs(v[2] - t) < timeDiff
                    && t >= v[2]) {
                return true;
            }
        }
        return false;
    }

}
