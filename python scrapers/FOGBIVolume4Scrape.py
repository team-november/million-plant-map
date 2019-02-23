from bs4 import BeautifulSoup   # to handle the html data
import csv                      # to output results to csv
import os
import re

# Open the htm file text
soup = BeautifulSoup(open("../../../books for indexing/sell and murell/text-converted-html.htm"), "html.parser")

tags = [tag.findChildren() for tag in soup.find_all(["b", "h1"])]

text_values = ["".join([tag.text for tag in children]) for children in tags]

text_combined = " ".join(text_values).replace("\n", " ").replace("\ax0", "")

#print(text_combined)


def detect_next_line(text):
    flag_in_number = False
    result = []
    current = ""

    for c in text:
        if c.isdigit() and not flag_in_number:
            flag_in_number = True
            result.append(current)
            current = c

        elif c == '.':
            flag_in_number = False
            current += c

        else:
            current += c

    result.append(current)

    return result



print("-------------\n\n--------------")
"""NOTE A LOT OF THE FUNCTIONS SUED ASSUME THE FUNCTIONS WRITTEN ABOVE THEM HAVE BEEN APPLIED AND DON'T NECESSARILY
WORK AS INTENDED IN THEIR OWN RIGHT"""

# Check if the word contains letters
def contains_letters(text):
    for c in text:
        if c.isalpha():
            return True


# Retain only first two words from each list entry
def get_first_two(text):
    result = ''
    flag = 0
    for c in text:
        if c == ' ':
            flag += 1
            if flag == 3:
                return result
            else:
                result += c
        else:
            result += c

    return result


# Split into new lines by number
line_split = detect_next_line(text_combined)
# Remove the \ax0 characters that have appeared again
# Only retain line if it contains a letter
# remove extra spaces
line_split = [get_first_two(                # Get only first two words
              re.sub(" +", " ",             # Remove repeated spaces
              line.replace("\xa0", "")))    # Replace \xa0 with empty string
              for line in line_split        # For each line of bold text found
              if contains_letters(line)]    # For each line that contains actual letters

print(line_split)

"""okay at this point, genus will start with a capital after the dot
and also will not include starting regex [A-Z]."""


# Check the first letter is a capital letter
def first_character_capital(text):
    for c in text:
        if c.isalpha():
            if c.isupper():
                return True
            if c.islower():
                return False

    return False


# Check only digits before dot:
def digits_only_before_dot(text):
    for c in text:
        if c == '.':
            return True
        elif not c.isdigit():
            return False


# Check the first word is not just a single letter
def first_word_not_letter(text):
    flag_start_word = False
    result = ""
    for c in text:
        if c.isalpha():
            result += c
            if not flag_start_word:
                flag_start_word = True

        elif flag_start_word:
            break

    if len(result) > 1:
        return True
    else:
        return False


line_split = [line for line in line_split           # Iterate through lines again
              if digits_only_before_dot(line)       # Select those with only digits before the first dot
              and first_word_not_letter(line)       # Select those that have more than a letter for the first word
              and first_character_capital(line)]    # Select those where the first letter is upper case

line_split = line_split[2:] # remove the first two pieces division info so start at first family number, 142


"""INSERTING MISSING VALUES"""
# After running the script, I found a couple of genus' group missing, where one had dropped out
# and then the whole pattern had stopped, so I insterted a few manually into the text stream here to keep it
# going.
pred = ["24. Leontodon L.", "80. Tripleurospermum "]
missing = ["25. Picris L.", "81. Cotula"]

new_lines = []
for line in line_split:
    new_lines.append(line)

    if line in pred:
        new_lines.append(missing[pred.index(line)])

line_split = new_lines


print(line_split)
print("data formatting DONE!")

"""Finally can generate a list of genus
The family numbers start at 142, so will keep a count of family number, if a new family number detected,
The genus number can start again at 0, if a number doesnt match the next genus number expected (e.g its a synonym
or some other data that has somehow slipped through, we ignore it
"""

regex_number = "[0-9]+"
regex_text = "[A-Z][A-Za-z. ]+"
pn = re.compile(regex_number)
pt = re.compile(regex_text)

current_fam_number = 141
current_genus_number = 0
genus_numbers = []

for line in line_split:
    num = float(pn.findall(line)[0])
    text = pt.findall(line)[0]

    if num == current_fam_number + 1:
        current_fam_number += 1         # Got to next family, so update family number
        current_genus_number = 0        # reset genus numbers

    elif num == current_genus_number + 1:
        current_genus_number += 1
        genus_numbers.append(
            ["/".join(                                                # Put fam/genus together
                [str(current_fam_number), str(current_genus_number)]
            ),
                text])                                                # Along with the name

    # Else the number doesn't match our required pattern, so we ignore it.

print(genus_numbers)

"""OUTPUT THE RESULTS TO A CSV"""
# Create a csv file in the resources folder
csv_file = open('../resources/FOGBIvolume4.csv', 'w')
writer = csv.writer(csv_file)

# Create an array of the correct format
csv_contents = ["fam_genus_code", "genus"] + genus_numbers

# Write the contents:
writer.writerows(csv_contents)

csv_file.close()

print("Done")

