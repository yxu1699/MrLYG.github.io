const INPINFO_TOKEN_ID = "0c71fd7bc99cde"
const GOOGLE_GEO_API_KEY = "AIzaSyDG0whBGCp_yx4zRwaIYyOFoVUHdhy9K-k"

function submitSearchForm(event) {
    // console.log("preventDefault")
    event.preventDefault()
    //clear 之前的responseTable
    let table = document.getElementById("search-response-table")
    table.innerHTML = ''

    let value_keyword = document.getElementById("keyword").value
    let value_distance = document.getElementById("distance").value
    if (value_distance == "") {
        value_distance = "10"
    }
    let value_category = document.getElementById("category").value

    let autoFindLocationCheckBox = document.getElementById("autoFindLocationCheckBox")
    let x
    let y
    if (autoFindLocationCheckBox.checked) {
        axios.get('https://ipinfo.io/?', {
            params: {
                token: INPINFO_TOKEN_ID
            }
        })
            .then(function (response) {
                // response.data.page.number==0 没数据
                let location = response.data.loc
                x = location.substring(0, location.indexOf(','))
                y = location.substring(location.indexOf(',') + 1)
                search_tickets(value_keyword, value_distance, value_category, x, y)
            })
            .catch(function (error) {
                console.log(error)
            })
    } else {
        let locationEle = document.getElementById("location")
        axios.get('https://maps.googleapis.com/maps/api/geocode/json?', {
            params: {
                address: locationEle.value,
                key: GOOGLE_GEO_API_KEY
            }
        })
            .then(function (response) {
                // response.data.page.number==0 没数据
                // console.log(response)
                x = response.data.results[0].geometry.location.lat
                y = response.data.results[0].geometry.location.lng
                search_tickets(value_keyword, value_distance, value_category, x, y)
            })
            .catch(function (error) {
                console.log(error)
            })
    }



}
function search_tickets(keyword, distance, category, latitude, longitude) {
    console.log(latitude, longitude)
    let events = {}
    axios.get('/search_tickets?', {
        params: {
            keyword: keyword,
            distance: distance,
            category: category,
            latitude: latitude,
            longitude: longitude
        }
    })
        .then(function (response) {
            // response.data.page.totalElements==0 没数据
            console.log("response.data.page.totalElements = " + response['data'].page.totalElements)
            if (response['data'].page.totalElements > 0) {
                events = response.data._embedded.events
                console.log(events)
                generateResponseTable(events)
            } else {
                generateResponseTableWithoutContent()
            }

        })
        .catch(function (error) {
            console.log(error)
        })
}

let AutoLocationCheckBox = document.getElementById("autoFindLocationCheckBox")
if (AutoLocationCheckBox.checked) {
    AutoLocationCheckBox.checked = false
}

function clearForm() {
    document.getElementById("form").reset()
    let locEle = document.getElementById("location")
    let AutoLocationCheckBox = document.getElementById("autoFindLocationCheckBox")
    if (locEle.style.display = 'none') {
        locEle.style.display = 'block'
        AutoLocationCheckBox.checked = false
        locEle.setAttribute("required", "")
    }
    //clear 之前的responseTable
    let table = document.getElementById("search-response-table")
    table.innerHTML = ''

    //clear 之前的event-detail
    let detail = document.getElementById("event-detail")
    detail.innerHTML = ''
}

// auto find location
function locationCheckBox(autoLocationCheckBox) {
    let locEle = document.getElementById("location")
    if (autoLocationCheckBox.checked) {
        locEle.removeAttribute("required")
        locEle.style.display = 'none'
    }
    else {
        locEle.style.display = 'block'
        locEle.setAttribute("required", "")
    }
}

function generateResponseTable(events) {
    let table = document.getElementById("search-response-table")
    table.style.display = 'block'
    let headersOftable = ['Date', 'Icon', 'Event', 'Genre', 'Venue']
    let thead = document.createElement('thead')


    let trOfHead = document.createElement('tr')
    headersOftable.forEach(headerName => {
        let th = document.createElement('th')
        th.innerHTML = headerName
        if (headerName != "Date" && headerName != "Icon") {

            th.onclick = function () {
                tableSortByHeadName("search-response-table", this.className)
            }
        }
        th.className = headerName.toLowerCase()
        trOfHead.appendChild(th)
    })
    thead.appendChild(trOfHead)



    //tbody
    // console.log(events)
    let tbody = document.createElement('tbody')

    console.log(events)
    events.forEach(event => {
        let tr = document.createElement('tr')
        let tdDate = document.createElement('td')
        tdDate.className = "date"
        tdDate.style.width = "195px"
        let dateHtmlContent = ""
        if (event.dates.start.localDate !== null && typeof event.dates.start.localDate !== "undefined") {
            dateHtmlContent = '<p style="display:inline;">' + event.dates.start.localDate + '</p>'
            if (event.dates.start.localTime !== null && typeof event.dates.start.localTime !== "undefined") {
                dateHtmlContent = dateHtmlContent + '<br>' + '<p style="display:inline;">' + event.dates.start.localTime + '</p>'
            }
        }

        tdDate.innerHTML = dateHtmlContent

        let tdIcon = document.createElement('td')
        tdIcon.className = "icon"
        tdIcon.style.width = "195px"
        let icon_url = ""
        if (event.images !== null && typeof event.images !== "undefined" && event.images.length > 0) {
            icon_url = event.images[0].url
        }
        tdIcon.innerHTML = '<img src="' + icon_url + '" alt="image">'

        let tdEvent = document.createElement('td')
        tdEvent.className = "event"
        tdEvent.style.width = "520px"
        tdEvent.onclick = function () {
            showEventDetail(event.id)
        }
        let EventHtmlContent = ""
        if (event.name !== null && typeof event.name !== "undefined" && event.name.length > 0) {
            EventHtmlContent = '<p style="display:inline;">' + event.name + '</p>'
        }
        tdEvent.innerHTML = EventHtmlContent

        let tdGenre = document.createElement('td')
        tdGenre.className = "genre"
        tdGenre.style.width = "130px"
        let GenreHtmlContent = ""
        if (event.classifications[0].segment.name !== null && typeof event.classifications[0].segment.name !== "undefined" && event.classifications[0].segment.name.length > 0) {
            GenreHtmlContent = '<p style="display:inline;">' + event.classifications[0].segment.name + '</p>'
        }
        tdGenre.innerHTML = GenreHtmlContent

        let tdVenue = document.createElement('td')
        tdVenue.className = "venue"
        tdVenue.style.width = "260px"
        let VenueHtmlContent = ""
        if (event._embedded.venues[0].name !== null && typeof event._embedded.venues[0].name !== "undefined" && event._embedded.venues[0].name.length > 0) {
            VenueHtmlContent = '<p style="display:inline;">' + event._embedded.venues[0].name + '</p>'
        }
        tdVenue.innerHTML = VenueHtmlContent
        // tdDate.innerText = event.dates.start.localDate
        // tdDate.innerText = event.dates.start.localTime
        tr.appendChild(tdDate)
        tr.appendChild(tdIcon)
        tr.appendChild(tdEvent)
        tr.appendChild(tdGenre)
        tr.appendChild(tdVenue)

        tbody.appendChild(tr)
        console.log(event)
    })
    table.appendChild(tbody)
    // add thead to table 
    table.appendChild(thead)

}

function generateResponseTableWithoutContent() {
    let table = document.getElementById("search-response-table")
    table.style.display = 'block'
    let th = document.createElement('th')
    th.innerHTML = "No Records found"
    th.style.width = "1300px"
    th.style.color = "red"
    table.appendChild(th)
}

// Method was found from https://piazza.com/class/lccnfyaky8yrr/post/195 and I did some changed.
// Same as bubble Sort Alg.
function tableSortByHeadName(tableId, headName) {
    // console.log(tableId+" "+headName)
    let table = document.getElementById(tableId)
    let swapcount = 0
    let swapping = true
    let sortDirection = "asc"
    let swapflag
    while (swapping) {
        swapping = false
        let tbody = table.getElementsByTagName("tbody")
        let rowsOfTable = tbody[0].getElementsByTagName("tr")
        for (i = 0; i < rowsOfTable.length - 1; i++) {
            // swapflag
            swapflag = false
            let currentTd = rowsOfTable[i].getElementsByClassName(headName)[0]
            let currentNextTd = rowsOfTable[i + 1].getElementsByClassName(headName)[0]
            let a = currentTd.innerText.toLowerCase()
            let b = currentNextTd.innerText.toLowerCase()
            if (sortDirection == "asc") {
                if (a.localeCompare(b) > 0) {
                    swapflag = true
                    break
                }
            } else if (sortDirection == "desc") {
                if (a.localeCompare(b) < 0) {
                    swapflag = true
                    break
                }
            }
        }
        if (swapflag) {
            rowsOfTable[i].parentNode.insertBefore(rowsOfTable[i + 1], rowsOfTable[i])
            swapping = true
            swapcount++
        } else {
            if (swapcount == 0 && sortDirection == "asc") {
                sortDirection = "desc"
                swapping = true
            }
        }
    }
}

function showEventDetail(eventId) {
    axios.get('/event_detail?', {
        params: {
            eventid: eventId
        }
    })
        .then(function (response) {
            // response.data.page.totalElements==0 没数据
            console.log(response)
            generateEventDetail(response.data)
        })
        .catch(function (error) {
            console.log(error)
        })
}

function generateEventDetail(EventDetail) {
    // EventDetail
    /**
     * Date  event.dates.start.localDate event.dates.start.localDate
     * Artist/Team event._embedded.attractions[loop].name event._embedded.attractions[loop].url
     * Venue event._embedded.venues[0].name
     * Genre event.classifications[0].subGenre genre segment  subType type
     * Price Ranges combined with “ -”   event.priceRanges[0].min max
     * Ticket Status event.dates.status.code
     * Buy Ticket At  event.url
     * Seat Map event.seatmap.staticUrl
     */
    
}