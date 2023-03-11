const express = require('express')
const app = express()
const axios = require('axios')
const cors = require('cors')


app.use(cors({
    origin: '*'
}))

TicketsMasterAPIKey = "Hi7gKkDkYdLmz9jNZOnvWveX8zfDYYSI"

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


app.listen(8080, function () {
    console.log('Server is running on port 8080')
    console.log('Let\'s Start!')
})
