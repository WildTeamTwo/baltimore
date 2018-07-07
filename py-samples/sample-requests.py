import requests


# resp = requests.get('https://data.baltimorecity.gov/resource/icjs-e3jg.json',  headers={'PRIVATE-TOKEN':  '*********'})

response = requests.get('https://data.baltimorecity.gov/resource/icjs-e3jg.json')
print(response.status_code)
print(response.json())
