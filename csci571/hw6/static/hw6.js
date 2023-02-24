const INPINFO_TOKEN_ID = "0c71fd7bc99cde"
const GOOGLE_GEO_API_KEY = "AIzaSyDG0whBGCp_yx4zRwaIYyOFoVUHdhy9K-k"

function submitSearchForm(event) {
    // console.log("preventDefault")
    event.preventDefault()
    //clear 之前的responseTable
    let table = document.getElementById("search-response-table")
    table.innerHTML = ''
    //clear 之前的event-detail
    let detail = document.getElementById("event-detail")
    detail.innerHTML = ''
    detail.style.display = 'none'
    //clear 之前的show-venue-detail
    let showVenue = document.getElementById("show-venue-detail")
    showVenue.style.display = 'none'
    //clear 之前的venue-detail
    clearShowDetail()

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
    axios.get('/search_tickets', {
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
    detail.style.display = 'none'
    //clear 之前的show-venue-detail
    let showVenue = document.getElementById("show-venue-detail")
    showVenue.style.display = 'none'
    //clear 之前的venue-detail
    clearShowDetail()
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
        if (event.dates.start.localDate !== null && typeof event.dates.start.localDate !== "undefined" && event.dates.start.localDate !== "Undefined") {
            dateHtmlContent = '<p style="display:inline;">' + event.dates.start.localDate + '</p>'
            if (event.dates.start.localTime !== null && typeof event.dates.start.localTime !== "undefined" && event.dates.start.localTime !== "Undefined") {
                dateHtmlContent = dateHtmlContent + '<br>' + '<p style="display:inline;">' + event.dates.start.localTime + '</p>'
            }
        }

        tdDate.innerHTML = dateHtmlContent

        let tdIcon = document.createElement('td')
        tdIcon.className = "icon"
        tdIcon.style.width = "195px"
        let icon_url = ""
        if (event.images !== null && typeof event.images !== "undefined" && event.images.length > 0) {
            icon_url = '<img src="' + event.images[0].url + '" alt="image">'

        }
        tdIcon.innerHTML = icon_url

        let tdEvent = document.createElement('td')
        tdEvent.className = "event"
        tdEvent.style.width = "520px"

        let EventHtmlContent = ""
        if (event.name !== null && typeof event.name !== "undefined" && event.name.length > 0 && event.name !== "Undefined") {
            EventHtmlContent = '<p style="display:inline;">' + event.name + '</p>'
        }
        tdEvent.innerHTML = EventHtmlContent
        let p_tdEvent = tdEvent.getElementsByTagName("p")[0]
        if (p_tdEvent !== null && typeof p_tdEvent !== "undefined") {
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
        }






        let tdGenre = document.createElement('td')
        tdGenre.className = "genre"
        tdGenre.style.width = "130px"
        let GenreHtmlContent = ""

        if (event.classifications[0].segment.name !== null && typeof event.classifications[0].segment.name !== "undefined" && event.classifications[0].segment.name.length > 0 && event.classifications[0].segment.name.toLowerCase() !== "undefined") {
            // let classes = event.classifications
            // let clasi 
            // classes.forEach(element => {
            //     if(element.primary == true){
            //         clasi = element.segment.name
            //     }
            // });
            // GenreHtmlContent = '<p style="display:inline;">' + clasi + '</p>'
            
            GenreHtmlContent = '<p style="display:inline;">' + event.classifications[0].segment.name + '</p>'
        }
        tdGenre.innerHTML = GenreHtmlContent

        let tdVenue = document.createElement('td')
        tdVenue.className = "venue"
        tdVenue.style.width = "260px"
        let VenueHtmlContent = ""
        if (event._embedded.venues[0].name !== null && typeof event._embedded.venues[0].name !== "undefined" && event._embedded.venues[0].name.length > 0 && event._embedded.venues[0].name.toLowerCase() !== "undefined") {
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
    clearShowVenue()
    clearShowDetail()
    axios.get('/event_detail', {
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

    // console.log(eventDetailEle.style.display)
    eventDetailEle.innerHTML = ""

    let showFlag = false
    //Head Part
    let headEle = document.createElement("div")
    headEle.id = "detail-head-div"

    let EventHtmlContent = ""
    if (event.name !== null && typeof event.name !== "undefined" && event.name.length > 0 && event.name !== "Undefined") {
        showFlag = true
        EventHtmlContent = '<h2 style="margin-top:20px;margin-bottom:20px;display:block">' + event.name + '</h2>'
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
     * 
     */

    // Date  event.dates.start.localDate event.dates.start.localDate
    if (event.dates.start.localDate !== null && typeof event.dates.start.localDate !== "undefined" && event.dates.start.localDate !== "Undefined") {
        showFlag = true
        let bodyLeftDateDivEle = document.createElement("div")
        bodyLeftDateDivEle.className = "bodyLeftDiv"
        let dateHtmlContent = ""
        dateHtmlContent = '<p style="display:inline;">' + event.dates.start.localDate + '</p>'
        if (event.dates.start.localTime !== null && typeof event.dates.start.localTime !== "undefined" && event.dates.start.localTime !== "Undefined") {
            dateHtmlContent = dateHtmlContent + '<p style="display:inline;"> ' + event.dates.start.localTime + '</p>'
        }
        bodyLeftDateDivEle.innerHTML = '<h3 style="color:#97fff9">Date</h3>' + dateHtmlContent
        bodyLeftEle.appendChild(bodyLeftDateDivEle)
    }

    // Artist/Team event._embedded.attractions[loop].name event._embedded.attractions[loop].url
    if (typeof event._embedded.attractions !== "undefined") {
        if (event._embedded.attractions[0].name !== null && typeof event._embedded.attractions[0].name !== "undefined") {
            showFlag = true
            let attractions = event._embedded.attractions
            let bodyLeftArtistDivEle = document.createElement("div")
            bodyLeftArtistDivEle.className = "bodyLeftDiv"
            let htmlContent = ""
            // 0 
            for (let index = 0; index < attractions.length; index++) {
                htmlContent = htmlContent + '<a style="text-decoration: none;color:#3284a5" href="' + attractions[index].url + '" target="_blank" >' + attractions[index].name + '</a>'
                if (index != attractions.length - 1) {
                    htmlContent = htmlContent + " | "
                }

            }
            bodyLeftArtistDivEle.innerHTML = '<h3 style="color:#97fff9">Artist/Team</h3>' + htmlContent
            bodyLeftEle.appendChild(bodyLeftArtistDivEle)
        }
    }

    // Venue event._embedded.venues[0].name
    let isExistVenue = false
    if (event._embedded.venues[0].name !== null && typeof event._embedded.venues[0].name !== "undefined") {
        showFlag = true
        isExistVenue = true
        let bodyLeftVenueDivEle = document.createElement("div")
        bodyLeftVenueDivEle.className = "bodyLeftDiv"
        let htmlContent = ""
        htmlContent = '<p style="display:inline;">' + event._embedded.venues[0].name + '</p>'
        bodyLeftVenueDivEle.innerHTML = '<h3 style="color:#97fff9">Venue</h3>' + htmlContent
        bodyLeftEle.appendChild(bodyLeftVenueDivEle)
    }
    // Genre event.classifications[0].subGenre genre segment  subType type
    if (event.classifications[0] !== null && typeof event.classifications[0] !== "undefined") {
        showFlag = true
        let bodyLeftGenreDivEle = document.createElement("div")
        bodyLeftGenreDivEle.className = "bodyLeftDiv"
        let genreArray = []
        if (event.classifications[0].subGenre !== null && typeof event.classifications[0].subGenre !== "undefined" && event.classifications[0].subGenre.name !== null && typeof event.classifications[0].subGenre.name !== "undefined" && event.classifications[0].subGenre.name !== "Undefined") {
            if (event.classifications[0].subGenre.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].subGenre.name
            }

        }
        if (event.classifications[0].genre !== null && typeof event.classifications[0].genre !== "undefined" && event.classifications[0].genre.name !== null && typeof event.classifications[0].genre.name !== "undefined" && event.classifications[0].genre.name !== "Undefined") {

            if (event.classifications[0].genre.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].genre.name
            }
        }
        if (event.classifications[0].segment !== null && typeof event.classifications[0].segment !== "undefined" && event.classifications[0].segment.name !== null && typeof event.classifications[0].segment.name !== "undefined" && event.classifications[0].segment.name !== "Undefined") {

            if (event.classifications[0].segment.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].segment.name
            }
        }
        if (event.classifications[0].subType !== null && typeof event.classifications[0].subType !== "undefined" && event.classifications[0].subType.name !== null && typeof event.classifications[0].subType.name !== "undefined" && event.classifications[0].subType.name !== "Undefined") {

            if (event.classifications[0].subType.name !== "Undefined") {
                genreArray[genreArray.length] = event.classifications[0].subType.name
            }
        }
        if (event.classifications[0].type !== null && typeof event.classifications[0].type !== "undefined" && event.classifications[0].type.name !== null && typeof event.classifications[0].type.name !== "undefined" && event.classifications[0].type.name !== "Undefined") {

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

            bodyLeftGenreDivEle.innerHTML = '<h3 style="color:#97fff9">Genres</h3>' + htmlContent
            bodyLeftEle.appendChild(bodyLeftGenreDivEle)
        }
    }
    // Price Ranges combined with “ -”   event.priceRanges[0].min max
    if (event.priceRanges !== null && typeof event.priceRanges !== "undefined") {
        if (event.priceRanges[0].min !== null && typeof event.priceRanges[0].min !== "undefined" && event.priceRanges[0].max !== null && typeof event.priceRanges[0].max !== "undefined") {
            showFlag = true
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
        if (event.dates.status.code !== null && typeof event.dates.status.code !== "undefined" && event.dates.status.code !== "Undefined") {
            showFlag = true
            let bodyLeftStatusDivEle = document.createElement("div")
            bodyLeftStatusDivEle.className = "bodyLeftDiv"
            let htmlContent = ""
            let code = event.dates.status.code
            let style = "display:inline;"

            let cc = code.toLowerCase()
            if (cc.includes("sale")) {
                if (cc.includes("on")) {
                    style = style + "background-color: green;"
                }
                if (cc.includes("off")) {
                    style = style + "background-color: red;"
                }
            }
            if (cc.includes("cancel")) {
                style = style + "background-color: black;"
            }
            if (cc.includes("postpone")) {
                style = style + "background-color: Orange;"
            }
            if (cc.includes("reschedul")) {
                style = style + "background-color: Orange;"
            }

            htmlContent = '<div style="' + style + '" id="bodyLeftStatusCode">' + code + '</div>'
            bodyLeftStatusDivEle.innerHTML = '<h3 style="color:#97fff9">Ticket Status</h3>' + htmlContent
            bodyLeftEle.appendChild(bodyLeftStatusDivEle)
        }
    }

    // Buy Ticket At  event.url
    if (event.url !== null && typeof event.url !== "undefined") {
        showFlag = true
        let bodyLeftTicketDivEle = document.createElement("div")
        bodyLeftTicketDivEle.className = "bodyLeftDiv"
        let htmlContent = ""
        htmlContent = htmlContent + '<a style="text-decoration: none;color:#297f93" href="' + event.url + '" target="_blank" >Ticketmaster</a>'
        bodyLeftTicketDivEle.innerHTML = '<h3 style="color:#97fff9">Buy Ticket At:</h3>' + htmlContent
        bodyLeftEle.appendChild(bodyLeftTicketDivEle)
    }
    // Seat Map event.seatmap.staticUrl
    if (event.seatmap !== null && typeof event.seatmap !== "undefined") {
        if (event.seatmap.staticUrl !== null && typeof event.seatmap.staticUrl !== "undefined") {
            showFlag = true
            let img = document.createElement("img")
            img.src = event.seatmap.staticUrl
            img.style.width = "100%"
            img.style.height = "100%"
            img.style.objectFit = "contain"
            bodyRightEle.appendChild(img)
        }
    }

    eventDetailEle.appendChild(headEle)
    eventDetailEle.appendChild(bodyEle)

    bodyEle.appendChild(bodyLeftEle)
    bodyEle.appendChild(bodyRightEle)



    let showVenue = document.getElementById("show-venue-detail")
    if (showFlag) {
        eventDetailEle.style.display = "block"

        if (isExistVenue) {
            axios.get('/venue_detail', {
                params: {
                    keyword: event._embedded.venues[0].name
                }
            })
                .then(function (response) {
                    console.log("/venue_detail")
                    console.log(response)
                    if (response.data.page.totalElements > 0) {
                        showVenue.style.display = "block"
                        let venueArrow = document.getElementById("venue-arrow")
                        venueArrow.onclick = function () {
                            showVenueDetails(event._embedded.venues[0].name)
                        }

                        venueArrow.onmouseout = function () {
                            venueArrow.style.color = ""
                        }
                    }
                    window.scrollTo({
                        top: eventDetailEle.offsetTop + 20,
                        behavior: 'smooth'
                    })

                })
                .catch(function (error) {
                    console.log(error)
                    window.scrollTo({
                        top: eventDetailEle.offsetTop + 20,
                        behavior: 'smooth'
                    })
                })


        }


        // window.scrollTo({
        //     top: eventDetailEle.offsetTop + 20,
        //     behavior: 'smooth'
        // })

    }




}

function clearShowVenue() {
    let showVenue = document.getElementById("show-venue-detail")
    showVenue.style.display = 'none'
}

function clearShowDetail() {
    let venueDetailEle = document.getElementById("venue-detail")
    venueDetailEle.style.display = 'none'
    let innerVenueEle = document.getElementById("inner-venue-detail")
    innerVenueEle.innerHTML = ''
}


function showVenueDetails(VenueName) {
    axios.get('/venue_detail', {
        params: {
            keyword: VenueName
        }
    })
        .then(function (response) {
            console.log("venuedetailsbyName")
            console.log(response)
            if (response.data.page.totalElements == 0) {
                console.log("show more venue details, can't find some venue by venue name")
            } else {
                generateVenueDetails(response.data)
            }


        })
        .catch(function (error) {
            console.log(error)
        })

}

function generateVenueDetails(venueData) {
    clearShowVenue()
    let venueDetailEle = document.getElementById("venue-detail")
    venueDetailEle.style.display = 'block'

    let innerVenueEle = document.getElementById("inner-venue-detail")
    // venueData._embedded.venues
    if (venueData._embedded !== null && typeof venueData._embedded !== "undefined") {
        if (venueData._embedded.venues[0] !== null && typeof venueData._embedded.venues[0] !== "undefined") {
            let venue = venueData._embedded.venues[0]
            // name

            if (venue.name !== null && typeof venue.name !== "undefined" && venue.name !== "Undefined") {
                innerVenueEle.innerHTML = '<div id="inner-venue-detail-head"><div style="margin: auto;">' + venue.name + '</div></div>'
            }

            // id="inner-venue-detail-logo"
            // <div id="inner-venue-detail-logo">
            // </div>
            if (venue.images !== null && typeof venue.images !== "undefined") {
                if (venue.images[0] !== null && typeof venue.images[0] !== "undefined") {
                    if (venue.images[0].url !== null && typeof venue.images[0].url !== "undefined") {
                        innerVenueEle.innerHTML = innerVenueEle.innerHTML + '<div id="inner-venue-detail-logo"></div>'
                        let innerVenueDetailLogo = document.getElementById("inner-venue-detail-logo")
                        innerVenueDetailLogo.style.display = 'flex'
                        let VenueImg = document.createElement('img')
                        VenueImg.src = venue.images[0].url
                        VenueImg.style.width = "163px"
                        VenueImg.style.height = "92px"
                        VenueImg.style.objectFit = "contain"
                        innerVenueDetailLogo.appendChild(VenueImg)
                    }
                }
            } else {
                innerVenueEle.innerHTML = innerVenueEle.innerHTML + '<div style="min-height:20px;"></div>'
            }




            // <div id="inner-venue-detail-body">
            //     <div id="inner-venue-detail-body-left"></div>
            //     <div id="inner-venue-detail-body-right"></div>
            // </div>
            //
            //  city.name,   state.stateCode
            // postalCode
            //
            if (checkValid(venue.address) || checkValid(venue.city.name) || checkValid(venue.state.stateCode) || checkValid(venue.postalCode) || checkValid(venue.url)) {
                innerVenueEle.innerHTML = innerVenueEle.innerHTML + '<div id="inner-venue-detail-body"><div id="inner-venue-detail-body-left"></div><div id="inner-venue-detail-body-right"></div></div>'
                let bodyleft = document.getElementById("inner-venue-detail-body-left")
                let bodyright = document.getElementById("inner-venue-detail-body-right")
                let bodyleftdiv = document.createElement("div")
                let bodyrightdiv = document.createElement("div")
                if (checkValid(venue.address) && venue.address.line1 !== null && typeof venue.address.line1 !== "undefined") {
                    let p = document.createElement("p")
                    p.style.display = "inline"
                    p.innerHTML = "<b>Address: </b>" + venue.address.line1
                    bodyleftdiv.appendChild(p)
                    bodyleft.appendChild(bodyleftdiv)
                }

                // Los Angeles,CA
                let city_state_p = document.createElement("p")
                city_state_p.style.paddingLeft = "75px"
                if (checkValid(venue.city) && checkValid(venue.city.name)) {
                    city_state_p.innerHTML = venue.city.name
                    bodyleftdiv.appendChild(city_state_p)
                }
                if (checkValid(venue.city) && checkValid(venue.state) && checkValid(venue.city.name) && checkValid(venue.state.stateCode)) {
                    city_state_p.innerHTML = city_state_p.innerHTML + ", " + venue.state.stateCode
                } else if (checkValid(venue.state) && venue.state.stateCode) {
                    city_state_p.innerHTML = city_state_p.innerHTML + venue.state.stateCode
                }


                if (checkValid(venue.postalCode)) {
                    let p = document.createElement("p")
                    p.style.paddingLeft = "75px"
                    p.innerHTML = venue.postalCode
                    bodyleftdiv.appendChild(p)
                }

                let maplink = document.createElement("div")
                maplink.style.textAlign = "center"
                maplink.style.marginTop = "10px"
                maplink.innerHTML = '<a style="text-decoration: none;color:#357a8a" href="https://www.google.com/maps/search/?api=1&query=' + venue.name + ', ' + venue.address.line1 + ', ' + city_state_p.innerHTML + ', ' + venue.postalCode + '" target="_blank" >Open in Google Maps</a>'

                // if (checkValid(venue.location)) {
                //     maplink.innerHTML = '<a style="text-decoration: none;color:#357a8a" href="https://www.google.com/maps/search/?api=1&query=' + venue.location.latitude + '%2C' + venue.location.longitude + '" target="_blank" >Open in Google Maps</a>'
                // } else {
                //     maplink.innerHTML = '<a style="text-decoration: none;color:#357a8a" href="https://www.google.com/maps/search/?api=1&query=' + venue.name + ',' + venue.address.line1 + ', ' + city_state_p.innerHTML + ', ' + venue.postalCode + '" target="_blank" >Open in Google Maps</a>'

                // }
                bodyleftdiv.appendChild(maplink)


                if (checkValid(venue.url)) {
                    bodyrightdiv.innerHTML = '<a style="text-decoration: none;color:#357a8a" href="' + venue.url + '" target="_blank" >More events at this venue</a>'
                } else {
                    bodyrightdiv.innerHTML = '<a style="text-decoration: none;color:gray" >More events at this venue</a>'
                }
                bodyright.appendChild(bodyrightdiv)

            }


        }
    }

    window.scrollTo({
        top: venueDetailEle.offsetTop,
        behavior: 'smooth'
    })

}

function checkValid(x) {
    if (x !== null && typeof x !== "undefined" && x !== "Undefined") {
        return true
    }
    return false
}