var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var dbconfig = require('../db/dbConfig');
var userSQL = require('../db/usersql');
var pool = mysql.createPool(dbconfig.mysql);

router.get('/',function(req,res,next){
    res.render('change');
});

router.get('/changefinish',function(req,res,next){
    var param = req.query || req.param;
    if(param.textconfirm==req.session.emailtext){
        pool.getConnection(function(err,connection){      
            connection.query(userSQL.changePwd,[param.newPwd,param.emailinfo],function(err,result){
                res.render('login');
                //console.log("密码改变完成");
                if (err) {
                    console.log("[INSERT ERROR] - ", err.message);
                    return;
                }
            });
        })
    }
});

module.exports = router;