# 에이전트 생성

참고한 레포지토리: https://github.com/SUSTechGameAI/EngagementMetrics

EngagementMetrics 레포지토리에는 Super Mario Bros 게임을 플레이하는 A* 에이전트가 구현되어 있다. 에이전트는 플레이스타일에 따라 Runner, Collector, Killer 의 세 유형으로 구분된다. 본 프로젝트는 해당 레포지토리를 참고하여 새로운 에이전트 newagent를 생성한 후, 에이전트가 보다 다양한 플레이스타일을 가질 수 있도록 에이전트의 비용 함수를 수정하였다.

newagent 에이전트의 비용 함수는 `EngagementMetrics\Mario-AI-Framework\src\agents\newagent\AStarTree.java`의 `pickBestPos` 함수에 구현되어 있다. 에이전트의 비용 함수에서 반영하는 6가지 지표는 다음과 같다. 비용 함수에서 **낮은 가중치** 를 부여할 경우 **해당 지표를 활성화** 한다는 의미이고, **높은 가중치** 를 부여할 경우 **해당 지표를 덜 활성화** 한다는 의미이다.  

| 지표                 | 설명                                                 |
|----------------------|------------------------------------------------------|
| `getKillRate`          | 전체 적 대비 처치한 적의 비율                        |
| `getCollectRate`       | 전체 코인 대비 수집한 코인의 비율                    |
| `getJumpTimeRatio`     | 전체 시간 대비 플레이어가 점프한 시간의 비율         |
| `getRemainingTimeRatio`| 레벨을 완료하고 남은 시간을 전체 시간으로 나눈 값    |
| `ifWin`                | 레벨을 클리어하면 1을, 클리어하지 못하면 0을 반환    |
| `ifLose`               | 레벨을 클리어하지 못하면 1을, 클리어하면 0을 반환    |


### java 파일 수정
특정 java 파일을 수정했을 경우 아래의 명령어로 컴파일한다.
```
// EngagementMetrics\Mario-AI-Framework\src 내의 모든 java 파일들을 컴파일
// EngagementMetrics\Mario-AI-Framework\src 디렉토리에서 실행

javac -cp . -d bin $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

### PlayLevel.java
```
java -cp bin PlayLevel
```

`newagent`로 게임을 플레이한다. 한 레벨 플레이를 마칠 때마다 플레이 로그가 터미널에 출력된다.

- **플레이할 레벨** 은 `EngagementMetrics\Mario-AI-Framework\src\PlayLevel.java` 의 `repeatNewAgent` 함수에서 수정할 수 있다. 레벨 파일은 `EngagementMetrics\Mario-AI-Framework\levels\original`에 저장되어 있고, 1부터 15까지의 레벨이 있다.
```
for(int j=1; j<=15; j++) { // 플레이할 레벨 설정, 현재는 1레벨부터 15레벨까지 플레이함
```

- **레벨당 반복 횟수** 는 `EngagementMetrics\Mario-AI-Framework\src\PlayLevel.java` 의 `main` 함수에서 수정할 수 있다. `times` 변수가 레벨당 반복 횟수를 의미한다.
```
repeatNewAgent(1); // 레벨당 반복 횟수 설정, 현재는 레벨당 1회 플레이함
```

- **에이전트 비용 함수의 가중치** 는 `EngagementMetrics\Mario-AI-Framework\src\agents\newagent\AStarTree.java` 에서 수정할 수 있다.
```
private float killWeight = -50;
private float collectWeight = -40;
private float jumpWeight = -30;
private float timeWeight = -30;
private float winWeight = -100;
private float loseWeight = +100;
```

### AgentTest.java
```
java -cp bin AgentTest
```

가중치 조합이 다른 여러 에이전트를 이용해 테스트를 순차적으로 진행한 후, 가중치 조합과 플레이 로그를 csv 파일로 출력한다. 한 에이전트의 테스트가 끝나면, 에이전트의 가중치를 자동으로 갱신한 후 테스트를 반복한다. 모든 에이전트의 테스트가 끝나면 실행이 종료된다. 출력된 csv 파일은 `EngagementMetrics/Mario-AI-Framework/src/logs/results.csv` 폴더에서 확인할 수 있다.

- **플레이할 레벨** 을 수정하는 방법은 `PlayLevel.java`와 동일하다.

- **레벨당 반복 횟수** 는 `EngagementMetrics\Mario-AI-Framework\src\AgentTest.java` 의 `runAllWeightConfigs` 함수에서 수정할 수 있다.

```
repeatNewAgent(1, writer); // csv 파일에 플레이 로그를 작성, 레벨당 반복 횟수 설정
```

- **에이전트 비용 함수의 가중치 조합** 은 `EngagementMetrics\Mario-AI-Framework\src\AgentTest.java` 의 `runAllWeightConfigs` 함수에서 수정할 수 있다. 아래의 코드에서는 6개 지표에 대해서 각각 5개, 5개, 3개, 3개, 1개, 1개의 가중치가 사용되었으므로, 총 5 * 5 * 3 * 3 = 225 가지의 서로 다른 에이전트로 테스트를 진행한다.
```
public static void runAllWeightConfigs() {
        // AStarTree.java 파일의 비용 함수에 대입할 가중치 조합을 설정
        float[] killWeights = {-3.0f, -1.0f, 0.0f, 1.0f, 3.0f};
        float[] collectWeights = {-3.0f, -1.0f, 0.0f, 1.0f, 3.0f};
        float[] jumpWeights = {-3.0f, -1.0f, 0.0f};
        float[] timeWeights = {-3.0f, -1.0f, 0.0f};
        float[] winWeights = {-10.0f};
        float[] loseWeights = {10.0f};
```

`EngagementMetrics\Mario-AI-Framework\src\logs\results_250512.csv` 파일은 위의 225개 가중치 조합으로 테스트를 진행한 결과이다. 6개 지표의 가중치 조합이 달라짐에 따라 플레이 로그도 다르게 나타남을 확인할 수 있다. 해당 데이터로 [플레이스타일 클러스터링](/AgentClustering/)을 진행했다.

### 플레이 로그

`PlayLevel.java`, `AgentTest.java`를 실행하면 플레이 로그가 출력된다. 플레이 로그에서 기록하는 16개의 지표는 다음과 같다.

| 항목 | 설명 | 관련 함수 |
|-----|-----|-----|
| `completion` | 마리오가 목표 지점까지 도달한 거리 비율 (0~1) | `getCompletionPercentage()` |
| `remaining_time` | 레벨을 플레이하고 남은 시간 | `getRemainingTime()` |
| `mariostate` | 마리오의 상태 (0: small, 1: large, 2: fire) | `getMarioMode()` |
| `total_kill` | 적을 처치한 총 횟수 (스톰프, 파이어볼, 셸, 낙사 포함) | `getKillsTotal()` |
| `stomp_kill` | 점프로 적을 밟아 처치한 횟수 | `getKillsByStomp()` |
| `fire_kill` | 파이어볼로 적을 처치한 횟수 | `getKillsByFire()` |
| `shell_kill` | 셸로 적을 처치한 횟수 | `getKillsByShell()` |
| `hurts` | 적과 충돌한 프레임 수 | `getMarioNumHurts()` |
| `question_blocks` | 물음표 블록을 친 횟수 | `getNumBumpQuestionBlock()` |
| `bump_bricks` | 벽돌 블록을 친 횟수 | `getNumBumpBrick()` |
| `destroyed_bricks` | 깨뜨린 벽돌 블록 수 | `getNumDestroyedBricks()` |
| `coins` | 획득한 코인 수 | `getCurrentCoins()` |
| `tile_coins` | 타일에 있는 코인을 획득한 횟수 | `getNumCollectedTileCoins()` |
| `mushrooms` | 먹은 버섯 수 | `getNumCollectedMushrooms()` |
| `fireflowers` | 먹은 파이어 플라워 수 | `getNumCollectedFireflower()` |
| `jumps` | 점프한 횟수 | `getNumJumps()` |