function submitSearchForm(event) {
    // console.log("preventDefault")
    event.preventDefault()

    let value_keyword = document.getElementById("keyword").value
    let value_distance = document.getElementById("distance").value
    if (value_distance == "") {
        value_distance = "10"
    }
    let value_category = document.getElementById("category").value
    search_tickets(value_keyword,value_distance,value_category,1,1)

}
function search_tickets(keyword, distance, category, latitude, longitude) {
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
            // response.data.page.number==0 没数据
            console.log(response['data'].page.number)
            
        })
        .catch(function (error) {
            console.log(error)
        })
}



function clearForm() {
    document.getElementById("form").reset()
    let locEle = document.getElementById("location")
    let AutoLocationCheckBox = document.getElementById("autoFindLocationCheckBox")
    if (locEle.style.display = 'none') {
        locEle.style.display = 'flex'
        AutoLocationCheckBox.checked = false
    }
}

// auto find location
function locationCheckBox(AutoLocationCheckBox) {
    let locEle = document.getElementById("location")
    if (AutoLocationCheckBox.checked) {
        locEle.style.display = 'none'
    }
    else {
        locEle.style.display = 'flex'
    }
}

