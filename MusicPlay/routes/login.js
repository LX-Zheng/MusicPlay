var express = require('express');
var router = express.Router();
var crypto = require('crypto');
/* GET login page. */
router.get('/', function(req, res, next) {
  res.render('login');
});

//导入mysql模块
var mysql = require('mysql');
var dbconfig = require('../db/dbConfig');
var userSQL = require('../db/usersql');
var pool = mysql.createPool(dbconfig.mysql);


//验证登录的信息
router.post('/checkinfo',function(req,res,next){
  pool.getConnection(function (err,connection) { 
    var param = req.body;
    //console.log(param.email+"----------")
    connection.query(userSQL.select,[param.email],function(err,result){
      req.session.username=result[0].username;
      var verifysign = crypto.createHash('md5').update(param.pwd, 'utf8').digest("hex");
      console.log(verifysign);
      if(result.length==0){
        res.render('error');
      }
      if(verifysign==result[0].pwd){
        res.render('index');
      }else{
        res.render('errors');
      }
      if(err){
        console.log(err.message);
        return;
      }
      //connection.release();
    })
    connection.query(userSQL.SelectUserInfo,[param.email],(err,result)=>{
        console.log(result);
        if(err){
            console.log(err.message);
            return;
        }
    })
  })
});


module.exports = router;
