// Copyright: https://github.com/mauriciolobo/todo
const express = require('express')
const DbStore = require('nedb')
const cors = require('cors')
const uuid = require('uuid/v4')
const Vertx = require('@vertx/core').Vertx

const PORT = process.env.PORT || 3000

        // futureJSClass = (ScriptObjectMirror) engine.eval("require('vertx-js/future');");
        // // Put the globals in
        // engine.put("__jvertx", vertx);
        // String globs =
        //   "var Vertx = require('vertx-js/vertx'); var vertx = new Vertx(__jvertx);" +
        //   "var console = require('vertx-js/util/console');" +
        //   "var setTimeout = function(callback,delay) { return vertx.setTimer(delay, callback).doubleValue(); };" +
        //   "var clearTimeout = function(id) { vertx.cancelTimer(id); };" +
        //   "var setInterval = function(callback, delay) { return vertx.setPeriodic(delay, callback).doubleValue(); };" +
        //   "var clearInterval = clearTimeout;" +
        //   "var parent = this;" +
        //   "var global = this;";
        // if (ADD_NODEJS_PROCESS_ENV) {
        //   globs += "var process = {}; process.env=java.lang.System.getenv();";
        // }
        // engine.eval(globs);

const app = express()
const db = new DbStore({ autoload: true, filename: 'todo' })

app.use(cors())
app.use(express.json())

function dbGetAll(res) {
    db.find({}, (err, doc) => {
        res.send(doc)
    })
}

function dbGetOne(res, _id){
    db.findOne({ _id }, (err, doc) => {
        res.send(doc)
    })
}

app.get('/', (req, res) => {
    dbGetAll(res)
})

app.get('/:id', (req, res) => {
    dbGetOne(res,req.params.id)
})

app.post('/', (req, res) => {
    var id = uuid();
    var doc = {
        ...req.body,
        completed: false,
        _id : id,
        id,
        url: req.protocol + '://' + req.get('host') + '/' + id
    };
    db.insert(doc, (err, doc) => {
        res.send(doc)
    })
})

app.patch('/:id', (req, res) => {
    db.update({ _id: req.params.id }, { $set: req.body }, {}, (err, number) => {
        dbGetOne(res,req.params.id)
    })
})

app.delete('/', (req, res) => {
    db.remove({}, { multi: true }, (err, n) => {
        dbGetAll(res)
    })
})

app.delete('/:id', (req, res) => {
    db.remove({ _id: req.params.id }, {}, (err, n) => {
        dbGetOne(res,req.params.id)
    })
})

app.listen(PORT, () => { console.log('Server is running...') })