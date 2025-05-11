**branch master**

에이전트로 레벨을 플레이하고 플레이 로그를 터미널 창에 출력한다. 에이전트의 플레이 화면이 함께 출력된다.
에이전트가 플레이할 레벨은 ```src\PlayLevel.java``` 의 다음 위치에서 수정할 수 있다.

```
public static void repeatNewAgent(int times) {
        String part_filepath = "../levels/original/lvl-";

        float[] total = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for(int j=1; j<=15; j++) { // 플레이할 레벨 설정
        ...
```
레벨당 반복 횟수는 ```src\PlayLevel.java```의 다음 위치에서 ```times``` 변수 값을 바꾸어 수정할 수 있다.

```
public static void main(String[] args) {
        MarioGame game = new MarioGame();
        // printResults(game.runGame(new agents.collector.Agent(), getLevel("../levels/original/lvl-1.txt"), 50, 0, true));
        // repeatCollector(5);
        // repeatKiller(5);
        // repeatRobin(5);
        repeatNewAgent(1); // 레벨당 반복 횟수 설정
    }
```

에이전트의 비용 함수는 ```src\agents\newagent\AStarTree.java```의 다음 위치에서 수정할 수 있다.

```
private SearchNode pickBestPos(ArrayList<SearchNode> posPool) {
        ...
            float currentCost = -50 * current.getkillrate() -40 * current.getCollectRate() -30 * current.getJumpTimeRatio() -30 * current.getRemainingTimeRatio() -100 * current.ifWin() +100 * current.ifLose();
        ...
    }
```

구현된 비용 함수 지표는 다음과 같다.
- getkillrate: 전체 적 대비 처치한 적의 비율
- getCollectRate: 전체 코인 대비 수집한 코인의 비율
- getJumpTimeRatio: 전체 시간 대비 플레이어가 점프한 시간의 비율
- getRemainingTimeRatio: 레벨을 완료하고 남은 시간을 전체 시간으로 나눈 값
- ifWin: 레벨을 클리어하면 1을, 클리어하지 못하면 0을 반환
- ifLose: 레벨을 클리어하지 못하면 1을, 클리어하면 0을 반환

플레이 로그에서 출력하는 지표는 다음과 같다.

| 항목               | 설명                                                | 관련 함수                         |
| ---------------- | ------------------------------------------------- | ----------------------------- |
| `completion`     | 마리오가 목표지점까지 도달한 거리 비율 (0\~1)                      | `getCompletionPercentage()`   |
| `total_kill`     | 적을 처치한 총 횟수 (스톰프, 파이어볼, 셸, 낙사 포함)                 | `getKillsTotal()`             |
| `fall_kill`      | 적이 낙사하여 처치된 횟수                                    | `getKillsByFall()`            |
| `coins`          | 최종적으로 보유한 코인 수                                    | `getCurrentCoins()`           |
| `lives`          | 최종 생명 수 (기본 생명 + 100코인 또는 1UP 등으로 증가된 수)          | `getCurrentLives()`           |
| `remaining_time` | 남은 시간 (프레임 단위, 1초는 약 24\~30프레임으로 계산됨) → 초 단위로 변환됨 | `getRemainingTime()`          |
| `mariostate`     | 마리오 상태 (0: small, 1: large, 2: fire)              | `getMarioMode()`              |
| `mushrooms`      | 먹은 버섯 수                                           | `getNumCollectedMushrooms()`  |
| `fireflowers`    | 먹은 파이어 플라워 수                                      | `getNumCollectedFireflower()` |
| `stomp_kill`     | 점프로 적을 밟아 처치한 횟수                                  | `getKillsByStomp()`           |
| `fire_kill`      | 파이어볼로 적을 처치한 횟수                                   | `getKillsByFire()`            |
| `shell_kill`     | 셸로 적을 처치한 횟수                                      | `getKillsByShell()`           |
| `bricks`         | 깨트린 벽돌 블록 수 (마리오가 large 또는 fire 상태일 때만 가능)        | `getNumDestroyedBricks()`     |
| `jumps`          | 점프한 횟수                                            | `getNumJumps()`               |


터미널에서 src 디렉토리로 이동한 후 아래의 명령어를 입력하여 실행할 수 있다.

```
java PlayLevel.java
```

**branch grid_search_csv**

여러 에이전트를 이용해 테스트를 순차적으로 진행한 후, 가중치 조합과 플레이 로그를 csv 파일로 출력한다. 한 에이전트의 테스트가 끝나면 에이전트의 가중치가 자동으로 갱신된 후 테스트를 반복한다. 모든 에이전트의 테스트가 끝나면 코드가 종료되고, 출력된 csv 파일은 /logs 폴더에서 확인할 수 있다. 에이전트의 플레이 화면은 제공되지 않는다.

에이전트가 플레이할 레벨, 레벨당 반복 횟수를 수정하는 방법은 branch master와 같다.

비용 함수의 가중치 조합은 ```PlayLevel.java```의 다음 위치에서 수정할 수 있다.

```
public static void runAllWeightConfigs() {
        // 가중치 조합 설정
        float[] killWeights = {-3.0f, -1.0f, 0.0f, 1.0f, 3.0f};
        float[] collectWeights = {-3.0f, -1.0f, 0.0f, 1.0f, 3.0f};
        float[] jumpWeights = {-3.0f, -1.0f, 0.0f};
        float[] timeWeights = {-3.0f, -1.0f, 0.0f};
        float[] winWeights = {-10.0f};
        float[] loseWeights = {10.0f};
        ...
```

특정 java 파일을 수정했을 경우 아래와 같이 파일 경로를 포함한 명령어를 입력하여 컴파일해야 한다.

```
// src/PlayLevel.java를 수정했을 경우
javac -cp src -d bin src/PlayLevel.java
```

컴파일을 진행한 후 아래의 명령어를 입력하여 실행한다.

```
java -cp bin PlayLevel
```