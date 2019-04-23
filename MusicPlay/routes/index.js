var express = require("express");
var router = express.Router();
var mysql = require("mysql");
var dbconfig = require("../db/dbConfig");
var userSQL = require("../db/usersql");
var pool = mysql.createPool(dbconfig.mysql);
const http = require('http');
/* GET home page. */
router.get("/", function(req, res, next) {
  res.render("index");
});

router.get("/getuserinfo",function(req,res){
  var info={
    username:req.session.username,
  }
  res.send(info);
})

//搜索音乐
router.get("/search", function(req, res, next) {
  var self=res;
  var keyword = req.url.split("?")[1]
  var msg = {
    host: '47.107.54.79',
    port: 3000,
    path: '/search?keywords=' + encodeURI(keyword),
    method: 'GET',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
    }
  }
 
  var req = http.request(msg, function(res) {
    res.setEncoding('utf-8');
    let str = '';
    res.on('data', function(data) {
        str += data;
    });
    res.on('end', function() {
      var dbs=[];
      var data=JSON.parse(str);
      for(var i=0;i<data.result.songs.length;i++){
          var st={
              id:data.result.songs[i].id,
              name:data.result.songs[i].name,
              singer:data.result.songs[i].artists[0].name,
              love:false
          };
          dbs[i] = st;
      }
      //console.log(dbs);
      //console.log(self);
      self.send(dbs);
      self.end();
  }); 
  });
 
  req.on('error', function(e) {
    console.log(e.message);
  });
  req.end();
});
//获得歌曲的图片
router.get("/getpic",function(req,res){
  var self=res;
  var id=req.url.split("?")[1];
  const getJSON = function(url){
    const promise = new Promise(function(resolve,reject){
        http.get(url,function(res){
            var str = '';
            res.on('data',function(data){
                str += data;
            })
            res.on('end',function(){
                resolve(str)
            })
        }).on('error',function(e){
            reject(e);
        })
    });
    return promise;
  }
  getJSON('http://47.107.54.79:3000/song/detail?ids='+id).then(function(result){
    var dbs=[];
    var data = JSON.parse(result);
    var st={
        picUrl:data.songs[0].al.picUrl
    }
    dbs[0] = st;
    self.send(st.picUrl);
    console.log(st.picUrl);
  },function(error){
    console.log(error);
  })
  
});


//获得歌曲的歌词
router.get('/getlyrics',function(req,res){
  var self=res;
  var id=req.url.split("?")[1];
  const getJSON = function(url){
    const promise = new Promise(function(resolve,reject){
        http.get(url,function(res){
            var str = '';
            res.on('data',function(data){
                str += data;
            })
            res.on('end',function(){
                resolve(str)
            })
        }).on('error',function(e){
            reject(e);
        })
    });
    return promise;
  }
  getJSON('http://47.107.54.79:3000/lyric?id='+id).then(function(result){
    var data = JSON.parse(result);
    var st={
        lyric:data.lrc.lyric
    }
    self.send(st.lyric);
    console.log(st.lyric);
  },function(error){
    console.log(error);
  })
})

router.get("/geturl",function(req,res){
  var self=res;
  var id=req.url.split('?')[1];
  const getJSON = function(url){
    const promise = new Promise(function(resolve,reject){
        http.get(url,function(res){
            var str = '';
            res.on('data',function(data){
                str += data;
            })
            res.on('end',function(){
                resolve(str)
            })
        }).on('error',function(e){
            reject(e);
        })
    });
    return promise;
  }
  getJSON('http://47.107.54.79:3000/music/url?id='+id).then(function(result){
    var dbs=[];
    var data = JSON.parse(result);
    var st={
        url:data.data[0].url
    }
    dbs[0] = st;
    self.send(st.url);
    self.end();
    // console.log(dbs)
  },function(error){
    console.log(error);
  })
  // console.log(dbs)
  
});

//获取播放列表
router.get("/getmusiclist", function(req, res, next) {
  pool.getConnection(function(err, connection) {
    connection.query(userSQL.getmusiclist, function(err, result) {
      res.send(result);
      res.end();
    });
  });
});

router.post("/getsong", function(req, res, next) {
  pool.getConnection(function(err, connection) {
    connection.query(userSQL.getmusicid, [req.url.split("?")[1]], function(
      err,
      result
    ) {
      res.sendFile(result[0].songpath);
      console.log(result[0].songpath);
    });
  });
});

//发现音乐
router.get("/finder", (req, res) => {
  var rad = [];
  for (var i = 0; i < 2; i++) {
    var random = Math.floor(Math.random() * 3);
    rad[i] = random;
    for (var j = 0; j < i; j++) {
      if (rad[i] == rad[j]) {
        i--;
        break;
      }
    }
  }
  console.log(rad);
  res.send(rad);
  res.end();
});

module.exports = router;
