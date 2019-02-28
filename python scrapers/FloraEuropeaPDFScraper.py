import os   # For looking at the current working directory
import re   # For matching regular expressions in text documents
import csv  # For writing out results to a CSV file

"""
This is for reading in a txt file of the text of the OCRd copy of the consolidated index of flora Europaea
From this I will get the family and genus codes for the index.
"""

# Check that the text file is in the resources folder.
print(os.listdir("../resources"))
assert 'floraEuropaeaConsolidatedIndex_OCRr.txt' in os.listdir("../resources")

# Open the file and read in the lines
f = open('../resources/floraEuropaeaConsolidatedIndex_OCRr.txt', 'r')
lines = f.readlines()

# Check that the first line is "Abelmoschus Medicus, 2:256 (106-13)"
print(lines[0])
assert lines[0] == "Abelmoschus Medicus, 2:256 (106-13)\n"


x = "ZANNICHELLIACEAE, 5:12 (181)"
y = "Caralluma R. Br., 3:73 (143-8)"

"""REGEXs for different forms in the document, the regexes they are describing are seen as examples above in x, y"""
regex_family = "[A-Z1]+, [0-5]\:[0-9]+ *\([0-9]+\)"
regex_fam_genus = "([A-Z][a-z]*( [A-Z][a-z]*.)*, [0-5]\:[0-9]+ \([0-9]+-[0-9]+\))"

for line in lines:
    if("AIZOACEAE") in line:
        print("FOUND IT")
        print(line)

#AMARANTHACEAE, 1:108 (49)

pattern_family = re.compile(regex_family)
pattern_fam_genus = re.compile(regex_fam_genus)

""" testing
m = pattern_family.findall(x)
print(m)
r = "([A-Z][a-z]*( [A-Z][a-z]*.)*, [0-5]\:[0-9]+ \([0-9]+-[0-9]+\))"
m1 = pattern_fam_genus.search(y)
print(m1[0])
"""


# Functions to extract names and values from family index pairs and genus index index tuples
def extract_family(text):
    name = ''
    index = ''
    flag = False
    flag_end_name = False

    for c in text:
        if c.isalpha():
            name += c.lower()

        elif c == '1' and not flag_end_name:
            name += 'i'

        elif c == ',':
            flag_end_name = True
        elif c == '(':
            flag = True
        elif c == ')':
            return [name, index]

        elif c.isdigit() and flag:
            index += c


def extract_fam_genus(text):
    name = ''
    family_index = ''
    genus_index = ''
    flag_name = True
    flag_fam = False
    flag_genus = False

    for c in text:
        if c.isalpha() or ((c == ' ' or c == '.') and flag_name):
            name += c.lower()

        elif c == ',':
            flag_name = False
        elif c == '(':
            flag_fam = True
        elif c == '-':
            flag_fam = False
            flag_genus = True
        elif c == ')':
            return [name, family_index, genus_index]

        elif c.isdigit():
            if flag_fam:
                family_index += c
            if flag_genus:
                genus_index += c


csv_fam_data = [['Family Name', 'Index']]
csv_fam_genus_data = [['Genus Name', 'Family', 'Genus']]

mFamily = []
mFamGenus = []
total = len(lines)
counter = 0

for text_line in lines:

    if (counter/total*100) % 10 == 0:
        print("next 10th done")
        counter += 1

    # Must check family first as fam genus also matches family
    mFamily = pattern_family.findall(text_line)
    if len(mFamily) > 0:
        csv_fam_data.append(extract_family(text_line))
    else:
        mFamGenus = pattern_fam_genus.findall(text_line)
        if len(mFamGenus) > 0:
            csv_fam_genus_data.append(extract_fam_genus(text_line))


# File for family indexes only
family_file = open("../resources/FloraEuopeaFamilyIndex2.csv", 'w')
writer_fam = csv.writer(family_file)

# File for family and genus indexes, not species
family_genus_file = open("../resources/FloraEuopeaGenusIndex2.csv", 'w')
writer_fam_genus = csv.writer(family_genus_file)


writer = csv.writer(family_file)
writer_fam.writerows(csv_fam_data)
writer_fam_genus.writerows(csv_fam_genus_data)

family_file.close()
family_genus_file.close()

print("Done")

