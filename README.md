# EDPCG의 플레이스타일 클러스터링과 최적화 레벨 생성

![가중치 1번 에이전트 최적화 레벨 시뮬레이션 결과](MFEDRL/agent1.gif)


-> agent1(적 처치를 선호하는 능숙한 에이전트)의 플레이스타일에 최적화된 레벨 시뮬레이션 결과

아래 feature의 가중치를 반영한 에이전트를 기반으로 강화학습을 진행하여 각 에이전트에 
최적화된 레벨을 생성할 수 있도록 한다.

(가중치가 낮을수록 해당 feature의 특성이 향상됨을 의미한다.)

ex) 아래는 에이전트 행동 특성 가중치 조합이다. 
| feature | 설명 | 가중치 |
|-----------|--------|---------|
| getkillrate | 적 처치 비율  | (-50, 50)  |
| getcollectweight | 코인 수집 비율 | (-40, 40)  |
| getJumpTimeRatio | 점프 시간 비율 | (-30, 30) |
| getRemainingTimeRatio | 클리어 후 남은 시간 비율 | (-30, 30) | 
| ifWin | 레벨 클리어 여부 | (-100, 100) |
| ifLose | 플레이어 사망 여부 | (-100, 100) |


## Features


- Mario-AI-Framework_agent0.jar 폴더 안의 agents/robinBaumgarten/AStarTree.java 파일 내에 가중치 수정 부분을 원하는 가중치 조합으로 변경한 후 재컴파일한다.

  .jar 폴더 압축 해제 명령어
 
  `jar xf ".jar 파일 경로”`

  디렉토리 내의 모든 .java 파일 재컴파일 명령어
 
  `javac -cp . -d . $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })`
 
- 변경한 jar 파일을 재컴파일한 후 다시 jar 파일로 압축하여 내보낸 후 기존 jar 파일과 교체한다.

  폴더 내의 모든 파일들을 jar파일로 압축하는 명령어
 
  `java --create --file Mario-AI-Framework.jar -C ..`

  기존 프레임워크의 jar 파일을 새로 만든걸로 카피 명령어
 
  `Copy-Item -Path "새로 만든 jar 폴더 경로" -Destination "기존 프레임워크 jar 폴더 경로" -Force`

- train.py 코드에서 학습 시 default로 설정하여 학습을 진행하는데 src/designer/train_designer.py 파일 상단 코드에 default는 'Runner' 에이전트로 설정하고 있다. Runner 에이전트는 robinBaumgarten 에이전트이기 때문에 자바 파일 내의 robinBaumgarten 폴더 내의 AStartree.java파일을 변경하면 적용된다. 

- smb.py 파일의 simulate_long() 함수에서 realTimeLimitMs 변수를 수정해주면서 한 스텝의 플레이 타임리밋을 설정해줄 수 있다. (30000으로 설정 시 30초)
 
- python train.py designer --res_path test6 명령어를 입력해서 RL-desiger를 학습시킨 후에 mylog.txt 파일이 test6 폴더 내에 생성되고 250스텝마다의 Funbehaviour, FunContent, Playability 로그를 확인할 수 있   다. 이를 통해 에이전트별로 학습이 진행될수록 레벨의 다양성, 플레이 다양성과 플레이가능성이 개선되는 것을 확인할 수 있다. 
  

## Getting Started

```Anaconda prompt
python train.py designer --res_path test_n
test_n 폴더가 생성되면서 강화학습이 진행된다.

python generate.py
학습된 모델로 레벨을 생성하여 지정한 경로에 .txt 레벨 파일이 생성된다. 

java PlayLevel.java
플레이할 에이전트와 레벨 설정 후 시뮬레이션을 진행할 수 있다.

python smb.py
에이전트에 최적화된 레벨로 플레이 레벨을 변경한 후 직접 레벨을 플레이하며 테스트해볼 수 있다.
```

## Based on

This project is based on [EngagementMetrics](github.com/SUSTechGameAI/EngagementMetrics), [MFEDRL](github.com/SUSTechGameAI/MFEDRL).

Parts of the code in the `EngagementMetrics/`, `MFEDRL-MASTER/` directory were modified from the original repository.
