
const axios = require('axios')

module.exports.suggest = function suggest(keyword, apikey) {
    axios.get('https://app.ticketmaster.com/discovery/v2/suggest?', {
        params: {
            keyword: keyword,
            apikey: apikey
        }
    }).then(response => {
        // 响应数据已经被自动解析为 JavaScript 对象或数组
        const data = response.data

        // 处理响应数据
        return data
    })
        .catch(function (error) {
            console.log(error)
        })
    
}
