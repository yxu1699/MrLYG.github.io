// const express = require('express')
// const app = express()
// const path = require('path')
const express = require('express')
const cors = require('cors')
const app = express()
const path = require('path')
app.use(cors({
    origin: '*'
}))

app.use(express.static(path.join(__dirname, 'dist/event_serach')))

app.get('/*', (req, res) => {
    res.sendFile(path.join(__dirname, 'dist/event_serach/index.html'))
})
app.get('/search', (req, res) => {
    res.sendFile(path.join(__dirname, 'dist/event_serach/index.html'))
})
app.get('/favorites', (req, res) => {
    res.sendFile(path.join(__dirname, 'dist/event_serach/index.html'))
})
const port = process.env.PORT || 8080
app.listen(port, () => {
    console.log(`Server is running on port ${port}`)
})
