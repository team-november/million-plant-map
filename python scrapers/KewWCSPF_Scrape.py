from bs4 import BeautifulSoup
import csv
import requests

# This is the base URL
url_base = 'https://wcsp.science.kew.org/namedetail.do?name_id='

csv_data = [['Accepted Name', 'Geo Codes']]

index = 1


