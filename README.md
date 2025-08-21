# EDPCG의 플레이스타일 클러스터링과 최적화 레벨 생성

![가중치 1번 에이전트 최적화 레벨 시뮬레이션 결과](MFEDRL/agent1.gif)


-> agent1 (적 처치를 선호하는 능숙한 에이전트) 의 플레이스타일에 최적화된 레벨 시뮬레이션 결과

**사용자 경험 기반 절차적 콘텐츠 생성(EDPCG)** 이란 사용자가 느끼는 재미, 몰입감, 난이도 등의 경험을 최적화할 수 있는 콘텐츠 생성을 말한다. 본 프로젝트는 EDPCG에 기반한 Super Mario Bros 게임 레벨 생성을 주제로 한다.

다양한 A* 기반 에이전트를 생성하고, 에이전트의 플레이스타일을 군집화한 뒤 이를 레벨 생성기에 도입하였다. 제안된 접근법은 사전 정의 없이 플레이스타일을 자동으로 분류하고, 개개인의 플레이스타일에 최적화된 레벨을 생성할 수 있도록 한다.

프로젝트는 크게 [에이전트 생성](/EngagementMetrics/), [플레이스타일 클러스터링](/AgentClustering/), [레벨 생성기 강화학습](/MFEDRL/)의 세 부분으로 나뉜다.

## Getting Started

```
// 에이전트 생성

java -cp bin PlayLevel
플레이할 에이전트와 레벨 설정 후 시뮬레이션을 진행할 수 있다.

java -cp bin AgentTest
여러 가중치 조합의 에이전트로 시뮬레이션을 진행하고, 플레이 로그를 csv 파일로 저장한다.

// 레벨 생성기 강화학습

python train.py designer --res_path test_n
test_n 폴더가 생성되면서 강화학습이 진행된다.

python generate.py
학습된 모델로 레벨을 생성하여 지정한 경로에 .txt 레벨 파일이 생성된다. 

python smb.py
에이전트에 최적화된 레벨로 플레이 레벨을 변경한 후 직접 레벨을 플레이하며 테스트해볼 수 있다.
```

## Based on

This project is based on [EngagementMetrics](https://github.com/SUSTechGameAI/EngagementMetrics), [MFEDRL](https://github.com/SUSTechGameAI/MFEDRL).

Parts of the code in the `EngagementMetrics/`, `MFEDRL-MASTER/` directory were modified from the original repository.
