var express = require("express");
var router = express.Router();
var crypto = require("crypto");
var md5 = crypto.createHash('md5');
//导入mysql模块
var mysql = require("mysql");
var dbConfig = require("../db/dbConfig");
var userSQL = require("../db/usersql");

//使用dbConfig.js的配置信息创建一个mysql连接池
var pool = mysql.createPool(dbConfig.mysql);


var responseJSON = function(res, ret) {
  if (typeof ret === "undefined") {
    res.json({
      code: "-200",
      msg: "操作失败"
    });
  } else {
    res.json(ret);
  }
};


// 添加用户
router.post("/addUser", function(req, res,next) {
  // 从连接池获取连接
  pool.getConnection(function(err, connection) {
    // 获取前台页面传过来的参数
    //var param = req.query || req.params;
    var param = req.body;
    if (param.emailtext == req.session.emailtext) {
      var content = param.pwd;
      md5.update(content);
      var sign = md5.digest('hex')
      // 建立连接 增加一个用户信息
      connection.query(userSQL.insert, [param.email, param.username, sign], function(err,result) {
        if (result) {
          result = {
            code: 200,
            msg: "增加成功"
          };
        }
        if (err) {
          console.log("[INSERT ERROR] - ", err.message);
          return;
        }
        // 以json形式，把操作结果返回给前台页面
        responseJSON(res, result);
        // 释放连接
        //connection.release();
      });
      //每个新建的用户添加默认的歌单
      connection.query(userSQL.InsertSheet,[param.email,'我喜欢的音乐','1'],(err,result)=>{
        if(result){
          result={
            code:200,
            msg:"添加喜欢的音乐成功"
          }
        };
        if(err){
            console.log("[InsertSheet]",err.message);
            return;
        }
        //responseJSON(res,result);
        connection.release();
      });
    } else {
      res.render("error");
    }
  });
  //console.log(req.body.username);
});

module.exports = router;
