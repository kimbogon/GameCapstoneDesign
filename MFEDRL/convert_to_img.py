# smb.py와 같은 디렉토리에서 실행
from smb import load_batch
from smb import MarioLevel

# 레벨 불러오기
with open("exp_data/agent0/levels/agent0Levels.smblvs", "r") as f:
    content = f.read()
levels = [MarioLevel(c) for c in content.split("\n;\n")]

# 첫 번째 레벨을 이미지로 저장
levels[0].to_img("exp_data/agent0/levels/agent0Levels.png")