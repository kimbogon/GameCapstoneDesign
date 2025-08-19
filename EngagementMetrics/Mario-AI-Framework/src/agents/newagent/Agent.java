package agents.newagent;

import engine.core.MarioAgent;
import engine.core.MarioEvent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.EventType;
import engine.helper.MarioActions;

import java.util.ArrayList;

/**
 * @author RobinBaumgarten
 */
public class Agent implements MarioAgent {
    private boolean[] action;
    private AStarTree tree;

    //추가
    private boolean useWeightsFile;

    public Agent(boolean useWeightsFile) {
        this.useWeightsFile = useWeightsFile;
        if (useWeightsFile) {
            tree = new AStarTree(true); // weights.txt 읽음
        } else {
            tree = new AStarTree(false); // weights.txt 읽지 않음
        }
    }

    public Agent() {
        this(false); // 기본값은 false
    }


    // 수정
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.action = new boolean[MarioActions.numberOfActions()];
        this.tree = new AStarTree(useWeightsFile);
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer, ArrayList<MarioEvent> gameEvents) {
        //System.out.println(model.getEnemiesFloatPos());
        action = this.tree.optimise(model, timer);
        return action;
    }

    @Override
    public String getAgentName() {
        return "killer";
    }

    @Override
    public void getAgentRecord() {
        System.out.println("============Killer Result===========");
        System.out.println("Searched Status: " + tree.SearchedStates);
        System.out.println("Lose Status: " + tree.SearchedLose);
        System.out.println("Fail rate: " + (int)100*(float)tree.SearchedLose/(float)tree.SearchedStates + "%");
    }

    @Override
    public int  getTotalState(){
        return tree.SearchedStates;
    }

    @Override
    public int getLoseState(){
        return tree.SearchedLose;
    };
}
