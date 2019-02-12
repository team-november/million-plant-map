import csv
import re
import numpy as np
import pandas as pd
from operator import itemgetter

"""This code is for collecting the various Bentham Hooker resources I have and condensing them into one"""

# First read in the two table extracts that I found from academic papers online,
# identify the families using regex, and combine into lists

# read in the data from the tables
f_table1 = open('../../books for indexing/bentham and hooker/table1extract.txt', 'r')
f_table2 = open('../../books for indexing/bentham and hooker/table2extract.txt', 'r')

lines_table1 = f_table1.readlines()
lines_table2 = f_table2.readlines()

# Regex to identify family codes and names from the tables, (both have almost identical data and also same patterns)
# example to extract: "122.0 ACANTHACEAE Dicliptera chinensis Nees." Want the first two parts.

regex = "[0-9]+.[0-9]+ [A-Z]+"
pattern = re.compile(regex)

print(pattern.findall("123.4 ARG Other"))

# Find all the matches, by iterating over the lines in each file
table_1_matches = []
table_2_matches = []

for l in lines_table1:
    m = pattern.findall(l)
    if len(m) > 0:
        table_1_matches.append(m[0])

for l in lines_table2:
    m = pattern.findall(l)
    if len(m) > 0:
        table_2_matches.append(m[0])

print(table_1_matches)
print(table_2_matches)

# Remove duplicates from regex matches
uniques = np.unique(table_1_matches + table_2_matches)


# extract number and id:
def extract_family(text):
    name = ''
    index = ''
    flag = False

    for c in text:
        if c.isdigit() or c == '.':
            index += c
        elif c.isalpha():
            name += c.lower()

    return [index, name]


# get all values extracted
pairs_from_extracts = [extract_family(text) for text in uniques]

print(pairs_from_extracts)

# Now read in values from previous internet scrape:
file_scrape = open('resources/BHKewScrape.csv', 'r')

lines_scrape = file_scrape.readlines()
# [0:-1] to remove the newline char
pairs_from_scrape = [l[0:-1].lower().split(', ') for l in lines_scrape][1:len(lines_scrape)]

print(pairs_from_scrape)

# Now read in values from excel spreadsheet originally provided by the client
xls = pd.ExcelFile("../../books for indexing/excel sheets/B&H sequence.xlsx")
sheetX = xls.parse(1) #1 is the sheet number
# Read in the relevant columns
names = sheetX["Unnamed: 1"]
indexes = sheetX["B&H"]

pairs_from_excel = [[str(float(round(indexes[i], 2))), names[i].lower()] for i in range(0, len(names))]
print(pairs_from_excel)


# Build new combined list
new_csv_data = []
for pair in pairs_from_excel+pairs_from_scrape+pairs_from_extracts:
    if pair not in new_csv_data:
        new_csv_data.append(pair)


new_csv_data = [['Index', 'Family']] + sorted(new_csv_data, key=itemgetter(0))

print(new_csv_data)

# Finally, write the new data to a final csv
new_csv_file = open('resources/BenthamHookerFamilyIndex.csv', 'w')
writer = csv.writer(new_csv_file)
writer.writerows(new_csv_data)

print('Done')