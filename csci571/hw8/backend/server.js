const express = require('express')
const app = express()
const axios = require('axios')
const cors = require('cors')
const geohash = require('ngeohash')
const SpotifyWebApi = require('spotify-web-api-node')

app.use(cors({
    origin: '*'
}))

const TicketsMasterAPIKey = "Hi7gKkDkYdLmz9jNZOnvWveX8zfDYYSI"
const GOOGLE_GEO_API_KEY = "AIzaSyDG0whBGCp_yx4zRwaIYyOFoVUHdhy9K-k"
// credentials are optional
const spotifyApi = new SpotifyWebApi({
    clientId: 'a0e80f8b30514f55923ba98a4567f4e9',
    clientSecret: 'c8fddba504184614ad0e704af15b18a7'
})

app.get('/', function (req, res) {
    res.send('Hello, World!')
})

function checkValue(va) {
    if (va !== null && typeof va != "undefined" && va != "Undefined") {
        return true
    }
    return false
}

app.get('/suggest', function (req, res) {
    console.log('-----------------------suggest')
    let keyword = req.query.keyword
    console.log(keyword)
    axios.get('https://app.ticketmaster.com/discovery/v2/suggest?', {
        params: {
            keyword: req.query.keyword,
            apikey: TicketsMasterAPIKey
        }
    }).then(response => {
        let data = response.data
        let suggestArray = []
        let attractions = []
        if (checkValue(data._embedded.attractions)) {
            attractions = data._embedded.attractions
        }
        attractions.forEach(attraction => {
            if (checkValue(attraction.name)) {
                let el = {
                    name: attraction.name
                }
                suggestArray.push(el)
            }
        })
        res.send({
            "suggest": suggestArray
        })
    }).catch(err => {
        console.log(err)
        res.send(
            {
                "error": err.toString()
            })
    })
})

app.get('/latlong', function (req, res) {
    let location = req.query.location
    axios.get('https://maps.googleapis.com/maps/api/geocode/json?', {
        params: {
            address: location,
            key: GOOGLE_GEO_API_KEY
        }
    })
        .then(function (response) {
            // response.data.page.number==0 没数据
            // console.log(response)
            x = response.data.results[0].geometry.location.lat
            y = response.data.results[0].geometry.location.lng
            res.send(
                {
                    'lat': x,
                    'lng': y,
                    'original': response.data
                },
            )
        })
        .catch(function (error) {
            res.send(
                {
                    "error": error.toString()
                })
        })
})


const SegmentId = {
    "Music": "KZFzniwnSyZfZ7v7nJ",
    "Sports": "KZFzniwnSyZfZ7v7nE",
    "Arts & Theatre": "KZFzniwnSyZfZ7v7na",
    "theatre": "KZFzniwnSyZfZ7v7na",
    "Film": "KZFzniwnSyZfZ7v7nn",
    "Miscellaneous": "KZFzniwnSyZfZ7v7n1"

}
app.get('/tickets', function (req, res) {
    let keyword = req.query.keyword
    let distance = req.query.distance
    let category = req.query.category
    let latitude = req.query.latitude
    let longitude = req.query.longitude
    let url = "https://app.ticketmaster.com/discovery/v2/events?apikey=" + TicketsMasterAPIKey
    url += "&keyword=" + keyword
    url += "&radius=" + distance.toString() + "&unit=miles"
    if (category != "Default") {
        url += "&segmentId=" + SegmentId[category]
    }
    url += "&geoPoint=" + geohash.encode(latitude, longitude, 7)
    console.log(url)
    axios.get(url).then(response => {
        let data = response.data
        res.send(data)
    }).catch(err => {
        console.log(err)
        res.send(
            {
                "error": err.toString()
            })
    })

})




app.get('/event', function (req, res) {
    let id = req.query.id
    url = "https://app.ticketmaster.com/discovery/v2/events/"+id
    axios.get(url, {
        params: {
            apikey: TicketsMasterAPIKey
        }
    }).then(response => {
        let data = response.data
        res.send(data)
    }).catch(err => {
        console.log(err)
        res.send(
            {
                "error": err.toString()
            })
    })
})



app.get('/artistinfo', function (req, res) {
    let artist = req.query.artist
    spotifyApi.clientCredentialsGrant().then(
        function (data) {
            console.log('The access token is ' + data.body['access_token'])
            spotifyApi.setAccessToken(data.body['access_token'])
            spotifyApi.searchArtists(artist)
                .then(function (data) {
                    res.send(data.body)
                }, function (err) {
                    res.send(
                        {
                            "error": err.toString()
                        })
                })
        },
        function (err) {
            res.send(
                {
                    "error": err.toString()
                })
        }
    )


})









app.listen(8080, function () {
    console.log('Server is running on port 8080')
    console.log('Let\'s Start!')
})
