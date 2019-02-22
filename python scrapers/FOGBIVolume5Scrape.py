import csv


# This volume was done using other tools, as no way to directly extract data
# Now all that needs to be done is read in the csv and add the family numbers,
# They range from 149-176
# And each time I reach a genus number of 1, I know to increment to the next genus number

family_number = 148 # Since the first line will update it by 1 to 149

# read in the data from the csv
file_scrape = open("../../../books for indexing/sell and murell/volume5JustValuesExtract.csv", 'r')

lines_scrape = file_scrape.readlines()
# [0:-1] to remove the newline char
# [1:len...] to remove the header of the file
pairs_from_scrape = [l[0:-1].lower().split(',') for l in lines_scrape][1:len(lines_scrape)]

csv_data = [['fam-genus-index', 'genus-name']]

for pair in pairs_from_scrape:
    genus_num = int(pair[0])

    if genus_num == 1:
        family_number += 1

    csv_data.append([
        "/".join([                  # distinguish family and genus numbers by /
            str(family_number),     # Family number
            pair[0]]                # Genus number
        ),
        pair[1]                     # Genus name
    ])


csv_file = open('../resources/FOGBIvolume5.csv', 'w')
writer = csv.writer(csv_file)

# Write the contents:
writer.writerows(csv_data)

csv_file.close()

print("Done")
