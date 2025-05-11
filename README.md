**branch master**

에이전트로 레벨을 플레이하고 플레이 로그를 터미널 창에 출력한다. 에이전트의 플레이 화면이 함께 출력된다.
src\PlayLevel.java 의 repeatNewAgent 함수에서 어떤 레벨을 몇 회 반복할지 설정할 수 있다.
src\agents\newagent\AStarTree.java 의 pickBestPos 함수에서 에이전트의 보상 함수를 변경할 수 있다.

구현된 비용 함수 지표는 다음과 같다.
- getkillrate: 전체 적 대비 처치한 적의 비율
- getCollectRate: 전체 코인 대비 수집한 코인의 비율
- getJumpTimeRatio: 전체 시간 대비 플레이어가 점프한 시간의 비율
- getRemainingTimeRatio: 레벨을 완료하고 남은 시간을 전체 시간으로 나눈 값
- ifWin: 레벨을 클리어하면 1을, 클리어하지 못하면 0을 반환
- ifLose: 레벨을 클리어하지 못하면 1을, 클리어하면 0을 반환

터미널에서 src 디렉토리로 이동한 후 아래의 명령어를 입력하여 실행할 수 있다.

```
java PlayLevel.java
```

**branch grid_search**

여러 에이전트를 이용해 테스트를 순차적으로 진행한 후, 가중치 조합과 플레이 로그를 csv 파일로 출력한다. 한 에이전트의 테스트가 끝나면 에이전트의 가중치가 자동으로 갱신된 후 테스트를 반복한다. 모든 에이전트의 테스트가 끝나면 코드가 종료되고, 출력된 csv 파일은 /logs 폴더에서 확인할 수 있다. 에이전트의 플레이 화면은 제공되지 않는다.

플레이할 레벨, 레벨당 반복 횟수를 수정하는 방법은 branch master와 같다.

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