var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var session = require('express-session');
var ejs = require('ejs');  //引入ejs插件

var registerRouter = require('./routes/register');
var usersRouter = require('./routes/users');
var qqconfirm = require('./routes/email');
var loginRouter = require('./routes/login');
var indexRouter = require('./routes/index');
var changeRouter = require('./routes/change.js');

var app = express();

// view engine setup
app.engine('html', ejs.__express); // 使用ejs引擎解析html文件中ejs语法
app.set('view engine', 'html'); // 设置解析模板文件类型：这里为html文件
app.set('views', path.join(__dirname, 'views')); //设置模板的目录


app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


app.use(session({
  secret : 'secret', // 对session id 相关的cookie 进行签名
  resave : true,
  saveUninitialized: false, // 是否保存未初始化的会话
  cookie : {
    maxAge : 1000 * 60 * 3, // 设置 session 的有效时间，单位毫秒
  },
}));


app.use('/', loginRouter);
app.use('/users', usersRouter);
app.use('/email',qqconfirm);
app.use('/register',registerRouter);
app.use('/index',indexRouter);
app.use('/change',changeRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
