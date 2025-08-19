# 레벨 내에서 각 문자의 개수를 출력하는 코드
'''
{"tiles" : {"X" : ["solid","ground"],
"S" : ["solid","breakable"],
"-" : ["passable","empty"],
"?" : ["solid","question block", "item question block"],
"Q" : ["solid","question block", "coin question block"],
"E" : ["enemy","damaging","hazard","moving"],
"<" : ["solid","top-left pipe","pipe"],
">" : ["solid","top-right pipe","pipe"],
"[" : ["solid","left pipe","pipe"],
"]" : ["solid","right pipe","pipe"],
"o" : ["coin","collectable","passable"],
"B" : ["solid","bullet bill","hazard","enemy"],
"b" : ["solid","bullet bill"] } }
'''

from collections import Counter

# 레벨 텍스트 파일 경로
file_path = "exp_data/agent0/levels/agent0Levels.smblvs"

# 파일 열기 및 전체 문자 세기
with open(file_path, 'r', encoding='utf-8') as f:
    lines = f.readlines()
    full_text = ''.join(lines)
    counter = Counter(full_text)

# 전체 문자 빈도 출력
for char, count in sorted(counter.items(), key=lambda x: -x[1]):
    print(f"'{repr(char)[1:-1]}' : {count}")

# 14, 29, 44, 59, 74... 번째 줄에서 '-' 개수 세기
target_lines = range(13, len(lines), 15)  # 0-indexed이므로 13부터 시작
total_dashes = 0
for idx in target_lines:
    line = lines[idx].rstrip('\n')
    count = line.count('-')
    total_dashes += count
    # print(f"Line {idx + 1}: {count} dashes")

print(f"\nTotal number of gaps: {total_dashes}")
