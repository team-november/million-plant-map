from bs4 import BeautifulSoup   # to handle the html data
import csv                      # to output results to csv
import os
import re

# do this manually instead by reading the file
file = open("../../../../../../../../../Downloads/Volume1tags copy.txt", "r")

lines = file.readlines()
print(len(lines))

whole_file = "".join(lines)

regex = ("style='font-size:[89].0pt;font-family:\"TimesNewRomanPS\",serif;[\\n ]*mso-fareast-font-family:"
         + "[\\n ]*\"Times New Roman\";[\\n ]*mso-bidi-font-family:[\\n ]*\"Times New Roman\"'>"
         + "[0-9]+. [A-Z][A-Za-z]+")

reg_8s = ("style='font-size:8.0pt;font-family:\"TimesNewRomanPS\",serif;[\\n ]*mso-fareast-font-family:"
         + "[\\n ]*\"Times New Roman\";[\\n ]*mso-bidi-font-family:[\\n ]*\"Times New Roman\"'>"
         + "[0-9]+. [A-Z][a-z]+")

reg_9s = ("style='font-size:9.0pt;font-family:\"TimesNewRomanPS\",serif;[\\n ]*mso-fareast-font-family:"
         + "[\\n ]*\"Times New Roman\";[\\n ]*mso-bidi-font-family:[\\n ]*\"Times New Roman\"'>"
         + "[0-9]+. [A-Z][a-z]+")

"""font-size:9.0pt;font-family:"TimesNewRomanPS",serif; mso-fareast-font-family:"Times New Roman";mso-bidi-font-family:"Times New Roman
"""

pattern = re.compile(regex)
patt_8s = re.compile(reg_8s)
patt_9s = re.compile(reg_9s)

matches = pattern.findall(whole_file)
matches_8s = patt_8s.findall(whole_file)
matches_9s = patt_9s.findall(whole_file)

reg_num_name = "[0-9]+. [A-Z][A-Za-z]+"

pattern_num_name = re.compile(reg_num_name)

match_pairs = []
for match in matches:
    match_pairs.append(pattern_num_name.findall(match)[0])

print(match_pairs)

""""" EXTRACTING from raw txt"""

file_txt = open("../../../../../../../../../Downloads/TXT1.txt", "r")

lines = file_txt.readlines()
file_matches = []

def get_first_two(text):
    result = ''
    flag = 0
    for c in text:
        if c == ' ':
            flag += 1
            if flag == 2:
                return result
            else:
                result += c
        else:
            result += c

    return result

for line in lines:
    match = pattern_num_name.match(line)
    if match is not None:
        file_matches.append(get_first_two(match.string))

print(file_matches)

"""Find intersection of the two matches"""

intersection = []
for match in match_pairs:
    if match in file_matches:
        intersection.append(match)

print(intersection)

"""EXTRACTING GENUS NUMBERS"""

regex_number = "[0-9]+"
regex_text = "[A-Z][A-Za-z. ]+"
pn = re.compile(regex_number)
pt = re.compile(regex_text)

current_fam_number = 0
current_genus_number = 0
genus_numbers = []

for line in intersection:
    num = float(pn.findall(line)[0])
    text = pt.findall(line)[0]

    if num == 1:
        current_fam_number += 1         # Got to next family, so update family number
        current_genus_number = 1        # reset genus numbers
        genus_numbers.append(
            ["/".join(  # Put fam/genus together
                [str(current_fam_number), str(current_genus_number)]
            ),
                text])  # Along with the name

    elif num == current_genus_number + 1:
        current_genus_number += 1
        genus_numbers.append(
            ["/".join(                                                # Put fam/genus together
                [str(current_fam_number), str(current_genus_number)]
            ),
                text])                                                # Along with the name

    # Else the number doesn't match our required pattern, so we ignore it.

print(genus_numbers)
print(current_fam_number)