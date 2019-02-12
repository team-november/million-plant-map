from bs4 import BeautifulSoup  # To sift through the HTML data
import requests  # To make a search
import re  # For matching regular expressions in the text
import csv  # To write to a csv file

search_text = "\"apps.kew.org/ecbot/specimen/\" \"Plant Name,\""  # The google search query for kew gardens ecbot

# a google search query with flags to turn off the filter, which result to start from and how many results to request
url_page_1 = 'https://google.com/search?' + "&q=" + search_text + "&start=0&num=100&filter=0"  # getting page 1
url_page_2 = 'https://google.com/search?' + "&q=" + search_text + "&start=100&num=200&filter=0"  # getting page 2

# Sending the internet requests
response_page_1 = requests.get(url_page_1)
response_page_2 = requests.get(url_page_2)

# Getting soup objects to go through the HTML data
soup1 = BeautifulSoup(response_page_1.text, features="html5lib")
soup2 = BeautifulSoup(response_page_2.text, features="html5lib")

# Regular expression to match 'Plant Name,' as in the descriptions, along with a decimal point number and a name
regex = "Plant Name, [0-9]+\.[0-9]* [A-Z]+"
p = re.compile(regex)
# The same match, but without the 'Plant Name,' section.
regex_in_match = "[0-9]+\.[0-9]* [A-Z]+"
p_in_match = re.compile(regex_in_match)


def get_index(text, pattern, p_in_match, data):
    """Looks for the plant name and id in the text, using the patterns in the regex,
        and appends the data to the data list"""
    m = pattern.findall(text)

    for match in m:
        string_pair = p_in_match.findall(match)[0]
        id = ""
        name = ""

        # Manual search for the id and name, more efficient than another regex
        for c in string_pair:
            if c.isdigit() or c == '.':
                id += c
            else:
                name += c.lower()

        data.append([id, name])

    return data


def get_text(soup, counter, p, p_in_match, data):
    """ Takes a soup, extracts the google search
        query data, passes it to the regex matcher and returns the result of that function """
    for g in soup.find_all(class_='g'):
        counter += 1
        data = get_index(g.text, p, p_in_match, data)
    return counter, data


# The array that all the records will be stored in.
csvData = [['Id', 'Family']]

# Extract the relevant data, keeping track of how many records
count = 0
count, csvData = get_text(soup1, count, p, p_in_match, csvData)
count1 = count
count, csvData = get_text(soup2, count, p, p_in_match, csvData)
print(count1)
print(count)

# Write the data to the csv file
with open('BHKewScrape1.csv', 'w') as csvFile:
    writer = csv.writer(csvFile)
    writer.writerows(csvData)

csvFile.close()


print("Done")
