import csv

# Read in the un-formatted csv
file = open("../../../books for indexing/sell and murell/families_and_synonyms_unformatted.csv")

lines_scrape = file.readlines()
# [0:-1] to remove the newline char
# [1:len...] to remove the header of the file
pairs_from_scrape = [l[0:-1].lower().split(',') for l in lines_scrape][1:len(lines_scrape)]

csv_data = [['family-index', 'family-name']]

# Remove any space character that might be present at the start
# Set to lower case
for pair in pairs_from_scrape:
    if pair[1][0] == ' ':
        csv_data.append([pair[0], pair[1][1:].lower()])
    else:
        csv_data.append([pair[0], pair[1].lower()])

# Write data out to resources folder
csv_file = open('../resources/FOGBIFamilyIndex.csv', 'w')
writer = csv.writer(csv_file)

# Write the contents:
writer.writerows(csv_data)

csv_file.close()

print("Done")

