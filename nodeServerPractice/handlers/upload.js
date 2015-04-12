/**
 * Created by fodrh on 2015. 4. 10..
 */
var formidable = require('formidable'),
    util = require('util'),
    path = require('path'),
    mime = require('mime'),
    querystring = require('querystring');

var TEST_TMP = __dirname + "/data";

exports.create = function (req, res) {
    var form = new formidable.IncomingForm(),
        files = [],
        fields = [];
    form.uploadDir = TEST_TMP;
    form.keepExtensions = true;

    form.on ('field', function(field, value) {
        console.log(field, value);
        fields.push([field, value]);
    }).on ('file', function (field, file) {
        console.log(field, file);
        files.push([field, file]);
    }).on ('progress', function(bytesReceived, bytesExpected) {
        console.log('progress: ' + bytesReceived + '/' + bytesExpected);
    }).on ('end', function() {
        console.log('-> upload done');
        console.log(JSON.stringify(util.inspect(files)));
    });

    form.parse(req, function(err, fields, files) {
        console.log('parse - ' + JSON.stringify(files));
        var result = files;
        var fileInfos = [];
        for (var file in files) {
            var path = files[file]['path'],
                index = path.lastIndexOf('/') + 1,
                _id = path.substr(index);

            var fileInfo = {
                size: files[file]['size'],
                path: path,
                name: files[file]['name'],
                _id:_id
            };
            console.log(_id);
            result[file]["_id"] = _id;
            console.log(files);
            fileInfos.push(fileInfo);
        }
        _insertMemo(req, fileInfos, function (error, results) {
            result["error"] = error;
            result["results"] = results;
            res.end(JSON.stringify(result));
        });

    });
};

exports.read = function(req, res) {
    var _id = req.param("fileName");
    var where = {};

    if (_id) {
        where = {_id:_id};
    }
    _findMemo(req, where, function (err, results) {
        res.json( { error : err, results: results });
    });
};

function _insertMemo(req, fileInfo, callback) {
    req.db.collection('test', function(err, collection) {
        collection.insert(fileInfo, {safe:true}, callback);
    });
}

function _findMemo(req, where, callback) {
    where = where || {};
    req.db.collection('test', function(err, collection) {
        collection.find(where).toArray(callback);
    });
}