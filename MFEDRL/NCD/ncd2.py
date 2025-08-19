# NCD 측정 기준: 구멍 유무

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

import os
import zlib
from itertools import combinations

def convert_level_file(filepath):
    with open(filepath, "r") as f:
        lines = [line.rstrip("\n") for line in f if line.strip() != ""]

    max_length = max(len(line) for line in lines)
    padded_lines = [line.ljust(max_length) for line in lines]

    result = []
    for col in range(max_length):
        bottom_char = padded_lines[-1][col]

        if bottom_char == '-':
            result.append(0)
        elif bottom_char == 'X':
            result.append(1)
        else:
            result.append(2)

    return result

def save_sequence_to_txt(sequence, output_path):
    with open(output_path, "w") as f:
        line = ",".join(map(str, sequence))
        f.write(line)

def read_text_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        return f.read()

def compress_size(text):
    compressed = zlib.compress(text.encode('utf-8'))
    return len(compressed)

def ncd(text1, text2):
    c1 = compress_size(text1)
    c2 = compress_size(text2)
    c12 = compress_size(text1 + text2)
    return (c12 - min(c1, c2)) / max(c1, c2)

input_dir = "../exp_analysis/endless_gen/data"
output_dir = "txt2"
os.makedirs(output_dir, exist_ok=True)

# 1. 레벨 텍스트 파일(.smblvs)를 문자열(.txt)로 변환
base_name = "agent6Levels_copy_" # 여기서 수정
file_indices = range(1, 6)
txt_files = []

for i in file_indices:
    smblvs_file = os.path.join(input_dir, f"{base_name}{i}.smblvs")
    txt_file = os.path.join(output_dir, f"{base_name}{i}.txt")
    sequence = convert_level_file(smblvs_file)
    save_sequence_to_txt(sequence, txt_file)
    txt_files.append(txt_file)

print("모든 smblvs 파일이 txt로 변환되었습니다.\n")

# 2. NCD 계산
pairs = list(combinations(txt_files, 2))
for file1, file2 in pairs:
    text1 = read_text_file(file1)
    text2 = read_text_file(file2)
    value = ncd(text1, text2)
    name1 = os.path.basename(file1)
    name2 = os.path.basename(file2)
    print(f"NCD({name1}, {name2}) = {value:.4f}")