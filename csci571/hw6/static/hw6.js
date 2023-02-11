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
        th.onmouseover = function () {
            th.style.cursor = "pointer"
        }
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

        let EventHtmlContent = ""
        if (event.name !== null && typeof event.name !== "undefined" && event.name.length > 0) {
            EventHtmlContent = '<p style="display:inline;">' + event.name + '</p>'
        }
        tdEvent.innerHTML = EventHtmlContent
        let p_tdEvent = tdEvent.getElementsByTagName("p")[0]
        p_tdEvent.onmouseover = function () {
            p_tdEvent.style.cursor = "pointer"
            p_tdEvent.style.color = "#6354ad"
        }
        p_tdEvent.onmouseout = function () {
            p_tdEvent.style.color = ""
        }
        p_tdEvent.onclick = function () {
            showEventDetail(event.id)
        }



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

function generateEventDetail(event) {
    // EventDetail

    console.log(event)
    let eventDetailEle = document.getElementById("event-detail")
    // eventDetailEle.style.removeProperty("display");
    eventDetailEle.style.display = "block"
    // console.log(eventDetailEle.style.display)
    eventDetailEle.innerHTML = ""

    //Head Part
    let headEle = document.createElement("div")
    headEle.id = "detail-head-div"

    let EventHtmlContent = ""
    if (event.name !== null && typeof event.name !== "undefined" && event.name.length > 0) {
        EventHtmlContent = '<h2 style="margin-top:20px;display:block">' + event.name + '</h2>'
    }
    headEle.innerHTML = EventHtmlContent


    //Body left and right
    let bodyEle = document.createElement("div")
    bodyEle.id = "detail-body-div"
    let bodyLeftEle = document.createElement("div")
    bodyLeftEle.id = "detail-body-left-div"
    let bodyRightEle = document.createElement("div")
    bodyRightEle.id = "detail-body-right-div"

    /**
     * 
     * Buy Ticket At  event.url
     * Seat Map event.seatmap.staticUrl
     */

    // Date  event.dates.start.localDate event.dates.start.localDate
    if (event.dates.start.localDate !== null && typeof event.dates.start.localDate !== "undefined") {
        let bodyLeftDateDivEle = document.createElement("div")
        bodyLeftDateDivEle.className = "bodyLeftDiv"
        let dateHtmlContent = ""
        dateHtmlContent = '<p style="display:inline;">' + event.dates.start.localDate + '</p>'
        if (event.dates.start.localTime !== null && typeof event.dates.start.localTime !== "undefined") {
            dateHtmlContent = dateHtmlContent + '<p style="display:inline;"> ' + event.dates.start.localTime + '</p>'
        }
        bodyLeftDateDivEle.innerHTML = '<h3 style="color:#97fff9">Date</h3>' + dateHtmlContent
        bodyLeftEle.appendChild(bodyLeftDateDivEle)
    }

    // Artist/Team event._embedded.attractions[loop].name event._embedded.attractions[loop].url
    if (typeof event._embedded.attractions !== "undefined") {
        if (event._embedded.attractions[0].name !== null && typeof event._embedded.attractions[0].name !== "undefined") {
            let attractions = event._embedded.attractions
            let bodyLeftArtistDivEle = document.createElement("div")
            bodyLeftArtistDivEle.className = "bodyLeftDiv"
            let htmlContent = ""
            // 0 
            for (let index = 0; index < attractions.length; index++) {
                htmlContent = htmlContent + '<a style="text-decoration: none;color:#297f93" href="' + attractions[index].url + '" target="_blank" >' + attractions[index].name + '</a>'
                if (index != attractions.length - 1) {
                    htmlContent = htmlContent + " | "
                }

            }
            bodyLeftArtistDivEle.innerHTML = '<h3 style="color:#97fff9">Artist/Team</h3>' + htmlContent
            bodyLeftEle.appendChild(bodyLeftArtistDivEle)
        }
    }

    // Venue event._embedded.venues[0].name
    if (event._embedded.venues[0].name !== null && typeof event._embedded.venues[0].name !== "undefined") {
        let bodyLeftVenueDivEle = document.createElement("div")
        bodyLeftVenueDivEle.className = "bodyLeftDiv"
        let htmlContent = ""
        htmlContent = '<p style="display:inline;">' + event._embedded.venues[0].name + '</p>'
        bodyLeftVenueDivEle.innerHTML = '<h3 style="color:#97fff9">Venue</h3>' + htmlContent
        bodyLeftEle.appendChild(bodyLeftVenueDivEle)
    }
    // Genre event.classifications[0].subGenre genre segment  subType type
    if (event.classifications[0] !== null && typeof event.classifications[0] !== "undefined") {
        let bodyLeftGenreDivEle = document.createElement("div")
        bodyLeftGenreDivEle.className = "bodyLeftDiv"
        let genreArray = []
        if (event.classifications[0].subGenre !== null && typeof event.classifications[0].subGenre !== "undefined" && event.classifications[0].subGenre.name !== null && typeof event.classifications[0].subGenre.name !== "undefined") {
            if (event.classifications[0].subGenre.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].subGenre.name
            }

        }
        if (event.classifications[0].genre !== null && typeof event.classifications[0].genre !== "undefined" && event.classifications[0].genre.name !== null && typeof event.classifications[0].genre.name !== "undefined") {

            if (event.classifications[0].genre.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].genre.name
            }
        }
        if (event.classifications[0].segment !== null && typeof event.classifications[0].segment !== "undefined" && event.classifications[0].segment.name !== null && typeof event.classifications[0].segment.name !== "undefined") {

            if (event.classifications[0].segment.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].segment.name
            }
        }
        if (event.classifications[0].subType !== null && typeof event.classifications[0].subType !== "undefined" && event.classifications[0].subType.name !== null && typeof event.classifications[0].subType.name !== "undefined") {

            if (event.classifications[0].subType.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].subType.name
            }
        }
        if (event.classifications[0].type !== null && typeof event.classifications[0].type !== "undefined" && event.classifications[0].type.name !== null && typeof event.classifications[0].type.name !== "undefined") {

            if (event.classifications[0].type.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].type.name
            }
        }
        if (genreArray.length > 0) {
            let htmlContent = ""
            for (let index = 0; index < genreArray.length; index++) {
                htmlContent = htmlContent + '<p style="display:inline;">' + genreArray[index] + '</p>'
                if (index != genreArray.length - 1) {
                    htmlContent = htmlContent + " | "
                }
            }

            bodyLeftGenreDivEle.innerHTML = '<h3 style="color:#97fff9">Genre/Team</h3>' + htmlContent
            bodyLeftEle.appendChild(bodyLeftGenreDivEle)
        }
    }
    // Price Ranges combined with “ -”   event.priceRanges[0].min max
    if (event.priceRanges !== null && typeof event.priceRanges !== "undefined") {
        if (event.priceRanges[0].min !== null && typeof event.priceRanges[0].min !== "undefined" && event.priceRanges[0].max !== null && typeof event.priceRanges[0].max !== "undefined") {
            let bodyLeftPriceDivEle = document.createElement("div")
            bodyLeftPriceDivEle.className = "bodyLeftDiv"
            let htmlContent = ""
            htmlContent = '<p style="display:inline;">' + event.priceRanges[0].min + ' - ' + event.priceRanges[0].max + ' USD</p>'
            bodyLeftPriceDivEle.innerHTML = '<h3 style="color:#97fff9">Price Ranges</h3>' + htmlContent
            bodyLeftEle.appendChild(bodyLeftPriceDivEle)
        }
    }


    // ● On sale: Green
    // ● Off sale: Red
    // ● Canceled: Black
    // ● Postponed: Orange
    // ● Rescheduled: Orange
    // * Ticket Status event.dates.status.code
    if (event.dates.status !== null && typeof event.dates.status !== "undefined") {
        if (event.dates.status.code !== null && typeof event.dates.status.code !== "undefined") {
            let bodyLeftStatusDivEle = document.createElement("div")
            bodyLeftStatusDivEle.className = "bodyLeftDiv"
            let htmlContent = ""
            let code = event.dates.status.code
            let style = "display:inline;"
            
            let cc = code.toLowerCase()
            if (cc.includes("sale")){
                if(cc.includes("on")) {
                    style = style+ "background-color: green;"
                }
                if(cc.includes("off")) {
                    style = style+ "background-color: red;"
                }
            }
            if(cc.includes("canceled")) {
                style = style+ "background-color: black;"
            }
            if(cc.includes("postponed")) {
                style = style+ "background-color: Orange;"
            }
            if(cc.includes("rescheduled")) {
                style = style+ "background-color: Orange;"
            }

            htmlContent = '<div style="'+style+'" id="bodyLeftStatusCode">' + code + '</div>'
            bodyLeftStatusDivEle.innerHTML = '<h3 style="color:#97fff9">Ticket Status</h3>' + htmlContent
            bodyLeftEle.appendChild(bodyLeftStatusDivEle)
        }
    }

    // Buy Ticket At  event.url
    if (event.url !== null && typeof event.url !== "undefined") {
        let bodyLeftTicketDivEle = document.createElement("div")
        bodyLeftTicketDivEle.className = "bodyLeftDiv"
        let htmlContent = ""
        // 0 
        htmlContent = htmlContent + '<a style="text-decoration: none;color:#297f93" href="' + event.url + '" target="_blank" >Ticketmaster</a>'
        bodyLeftTicketDivEle.innerHTML = '<h3 style="color:#97fff9">Buy Ticket At</h3>' + htmlContent
        bodyLeftEle.appendChild(bodyLeftTicketDivEle)
    }

    eventDetailEle.appendChild(headEle)
    eventDetailEle.appendChild(bodyEle)

    bodyEle.appendChild(bodyLeftEle)
    bodyEle.appendChild(bodyRightEle)

    window.scrollTo({
        top: eventDetailEle.offsetTop,
        behavior: 'smooth'
    })
}