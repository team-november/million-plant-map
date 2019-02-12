from bs4 import BeautifulSoup   # to handle the html data
import requests                 # to make url requests
import numpy                    # to organise the table data
import csv                      # to output the results of the wiki table to a csv

""" This code is to extract the geo codes from the List of codes used in the World Geographical Scheme for 
Recording Plant Distributions - as found on wikipedia"""

url = "https://en.wikipedia.org/wiki/List_of_codes_used_in_" \
      "the_World_Geographical_Scheme_for_Recording_Plant_Distributions"

response = requests.get(url)  # fetches page contents of the wiki

soup = BeautifulSoup(response.text, features="html5lib")

table = soup.find('table', {'class': 'wikitable sortable'})  # locates the table in the wiki

links = table.findAll('td')  # extracts all the td links that contain text data from the table

table = numpy.reshape(links, (-1, 6))  # turn the data into a 6 column table as in the wiki

table = [numpy.delete(l, [0, 1, 4, 5], 0) for l in table]  # removes unnecessary columns, only need code and its meaning

print(table)  # See what the table is

# All values of form
# <td style="background-color:#DFD">ASP</td>,       text starts 34 characters in always, since colour always 3 chars
#        <td>Amsterdam-St.Paul Is.</td>             text always starts 4 chars in
# So will extract the relevant characters

# there's probably a way to do this with beautiful soup, but I didn't want to crawl through stack overflow so did
# it manually with numpy and python string/list operations:

codes = [str(entry[0])[34:-5] for entry in table]
names = [str(entry[1])[4:-5] for entry in table]

print(str(table[0][0]))

print(codes)
print(names)
print(len(codes))

# Now to write this to one big csv:

# Create a csv file in the resources folder
csv_file = open('resources/geoCodes.csv', 'w')
writer = csv.writer(csv_file)

# Create an array of the correct format
csv_contents = [['code', 'definition']] + [[codes[i], names[i]] for i in range(0, len(codes))]

# Write the contents:
writer.writerows(csv_contents)

csv_file.close()

print("Done")
