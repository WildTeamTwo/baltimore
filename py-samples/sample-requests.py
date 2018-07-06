import requests


#resp = requests.get('https://data.baltimorecity.gov/resource/icjs-e3jg.json',  headers={'PRIVATE-TOKEN':  'botaw-C4uXG6E2v-ZybJ'})

response = requests.get('https://data.baltimorecity.gov/resource/icjs-e3jg.json')
print(resp.status_code)
print(resp.json())
