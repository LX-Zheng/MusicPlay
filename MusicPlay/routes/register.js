var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var dbconfig = require('../db/dbConfig');
var userSQL = require('../db/usersql');
var pool = mysql.createPool(dbconfig.mysql);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('register');
});

router.get('/emailconfirm',function(req,res,next){
  pool.getConnection(function(err,connection){
    connection.query(userSQL.emailconfirm,[req.url.split('?')[1]],function(err,result){
      if(result.length>0){
        res.send("1");
      }else{
        res.send("2");
      }
      res.end();
      if(err){
        console.log("[SELECT ERROR] - ", err.message);
          return;
      }
    })
  })
})

module.exports = router;
