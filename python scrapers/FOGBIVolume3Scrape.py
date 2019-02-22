from bs4 import BeautifulSoup   # to handle the html data
import csv                      # to output results to csv
import os
import re

""" This code is to extract the genus indexes for the FOGBI indexing scheme, from the html data from the online
e-book reader"""

"""     EXTRACT SOURCE FROM HTML FILE """
# Check the resources folder contains the HTm file extracted.
print(os.listdir("../resources"))

# Open the htm file text
soup = BeautifulSoup(open("../resources/FOGBIV3.htm"), "html.parser")

# print(soup) Check data I want is in the soup

# Find all the td tags, my data is in the longest td tag
indexes = soup.find_all('td', {"class": "line-content"})

# Find the max size tag, where al the genus' are stored
length = 0
longest = None
for index in indexes:
    if len(index) > length:
        length = len(index)
        longest = index

print(longest)

"""     MATCH GENUS AND FAMILIES USING REGEXES  """
# Now extract all the data from the tag, using regular expressions.

# Genus codes of the form:
"""
    13. Ajuga L.
    2. Chamerion (Raf.) Raf
    
    just match the first part, the name up to the . I think, than in java code can just
    do a contains check for the first part of the genus name and essentially ignore all the author parts.
"""

regex_match_family = "([0-9]+A*. [A-Z][A-Z ]+)" # Use two [A-Z] to prevent it matching the first letter of genus names
regex_match_genus = "[0-9]+. [A-Z][a-z]+"

fam_pattern = re.compile(regex_match_family)
genus_pattern = re.compile(regex_match_genus)

families = fam_pattern.findall(str(longest))[1::] # Remove the division/class info in the first item
genuses = genus_pattern.findall(str(longest))

#print(families)
#print(genuses)


""" SPLIT INTO NUMBER/NAME TOKENS"""


def extract(text):
    # iterate through each char
    number = ''
    name = ''
    flag = False
    for c in text:
        if c.isdigit():
            number += c

        if c == '.':
            flag = True

        elif c.isalpha():
            if flag:
                name += c.lower()
            else:
                number += '.1'    # Replace A's in family numbers with .01's

    return number, name

family_numbers = [extract(text) for text in families]
genus_numbers = [extract(text) for text in genuses]

# There is a missing genus from this list at family 116, shifting all the values out of sync
# I will add it now
new_numbers = []
for number_pair in genus_numbers:
    new_numbers.append(number_pair)

    if number_pair[1] == 'pelargonium':
        new_numbers.append(('1', "limnanthes"))

genus_numbers = new_numbers

print(family_numbers)
print(genus_numbers)


genus_family_code_pairs = []

current_genus_no = 0
current_fam_index = 0
family_no = float(family_numbers[0][0])

for i, v in enumerate(genus_numbers):
    genus_no = int(genus_numbers[i][0])

    # if jumped families, get next family number
    if genus_no <= current_genus_no:
        current_fam_index += 1
        family_no = float(family_numbers[current_fam_index][0])

    # update current
    current_genus_no = genus_no

    # create new index
    genus_family_code_pairs.append((str(family_no) + '/' + str(genus_no), genus_numbers[i][1]))


print(genus_family_code_pairs)


"""OUTPUT THE RESULTS TO A CSV"""
# Create a csv file in the resources folder
csv_file = open('../resources/FOGBIvolume3.csv', 'w')
writer = csv.writer(csv_file)

# Create an array of the correct format
csv_contents = [['fam_genus_code', 'genus']] + [[pair[0], pair[1]] for pair in genus_family_code_pairs]

# Write the contents:
writer.writerows(csv_contents)

csv_file.close()

print("Done")

