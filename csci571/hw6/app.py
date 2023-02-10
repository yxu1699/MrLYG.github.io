from flask import Flask
from flask import request
import requests
from geolib import geohash
app = Flask(__name__)

TicketsMasterAPIKey = "Hi7gKkDkYdLmz9jNZOnvWveX8zfDYYSI"
SegmentId = {
    "music": "KZFzniwnSyZfZ7v7nJ",
    "sports": "KZFzniwnSyZfZ7v7nE",
    "arts": "KZFzniwnSyZfZ7v7na",
    "theatre" : "KZFzniwnSyZfZ7v7na",
    "film": "KZFzniwnSyZfZ7v7nn",
    "miscellaneous": "KZFzniwnSyZfZ7v7n1"
}


def request_parse(request):
    if request.method == 'POST':
        return request.json
    elif request.method == 'GET':
        return request.args


@app.route("/")
def hello_world():
    return app.send_static_file("index.html")


@app.route("/search_tickets", methods=["GET", "POST"])
def search_tickets():
    data = request_parse(request)
    keyword = data.get('keyword')
    distance = data.get('distance')
    category = data.get('category')
    latitude = data.get('latitude')
    longitude = data.get('longitude')
    url = "https://app.ticketmaster.com/discovery/v2/events?apikey="+TicketsMasterAPIKey
    url += "&keyword=" + keyword
    url += "&radius=" + str(distance) + "&unit=miles&locale=*"
    if category != "all":
        url += "&segmentId=" + SegmentId[category]
    url += "&geoPoint=" + geohash.encode(latitude, longitude, 7)
    response = requests.get(url)
    eventsJson = response.json()

    resJson = {
        "date": {
            "localDate": "",
            "localTime": ""
        },
        "icon": "",
        "event": {},
        "genre": "",
        "venue": "",
    }
    # 根据前端页面需要生成json
    print(url)
    return eventsJson


@app.route("/event_detail", methods=["GET"])
def event_detail():
    data = request_parse(request)
    eventid = data.get('eventid')
    url = "https://app.ticketmaster.com/discovery/v2/events/" + \
        str(eventid) + "?apikey="+TicketsMasterAPIKey
    response = requests.get(url)
    print(url)
    eventDetailJson = response.json()
    return eventDetailJson


# @app.route("/venue_detail", methods=["GET"])
# def venue_detail():
#     data = request_parse(request)
#     keyword = data.get('keyword')
#     url = "https://app.ticketmaster.com/discovery/v2/venues?apikey="+TicketsMasterAPIKey
#     url += "&keyword=" + keyword
#     response = requests.get(url)
#     venueDetailJson = response.json()3
#     return venueDetailJson

# @app.route('/static/img/<path:filename>')
# def server_jpg(filename):
#     return send_from_directory(('static/img'), filename)

# @app.route('/static/<path:filename>')
# def serve_static(filename):
#     return send_from_directory(('static'), filename)
