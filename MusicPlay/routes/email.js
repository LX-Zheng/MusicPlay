var nodemailer = require("nodemailer");
var smtpTransport = require('nodemailer-smtp-transport');
var express = require('express');
var router = express.Router();
//var session = require('express-session');


// 开启一个 SMTP 连接池
var transport = nodemailer.createTransport(smtpTransport({
  host: "smtp.163.com", // 主机
  secure: true, // 使用 SSL
  port: 465, // SMTP 端口
  auth: {
    user: "18860937595@163.com", // 账号
    pass: "qwer123" // 密码
  }
}));

//生成随机数
var rnd="";
for(var i=0;i<6;i++){
    rnd+=Math.floor(Math.random()*10);
}

router.get('/sendemail', function(req, res, next) {
  //获取用户的email
  console.log(req.url.split('?')[1]);
  req.session.emailtext=rnd;
  console.log(req.session.emailtext);
  // 设置邮件内容
  var mailOptions = {
    from: "Fre boo <18860937595@163.com>", // 发件地址
    to: req.url.split('?')[1], // 收件列表
    subject: "Hello World", // 标题
    html:rnd // html 内容
  }

  // 发送邮件
  transport.sendMail(mailOptions, function(error, response) {
    let result={
      res:'failed'
    }
    if (error) {
      console.error(error);
    } else {
      console.log(response);
      result.res='success';
    }
    res.send(result);
    res.end();
    transport.close(); // 如果没用，关闭连接池
  });
});


module.exports = router;