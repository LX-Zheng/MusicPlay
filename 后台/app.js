var express = require('express');
var app = express();
var mysql = require('mysql');
var dbconfig = require('./db/dbConfig');
var sql = require('./db/usersql');
var pool = mysql.createPool(dbconfig.mysql);

const bodyParser = require('body-parser');
app.use(bodyParser.json());//数据JSON类型
app.use(bodyParser.urlencoded({ extended: false }));//解析post请求数据

app.get('/',(req,res)=>{
    res.sendFile( __dirname + "/" + "login.html" );
});


app.post('/confirm',function(req,res){
    var param = req.body;
    pool.getConnection((err,connection)=>{
        connection.query(sql.select,[param.account],(err,result)=>{
            if(err){
                console.log(err.message);
                return;
            }
            if(result[0].password==param.password){
                console.log(result)
                console.log("成功");
                res.send(result);
                res.end();
            }else{
                console.log("失败");
                return;
            }
        })
    })
})

app.post('/changePwd',(req,res)=>{
    var param = req.body;
    pool.getConnection((err,connection)=>{
        connection.query(sql.select,[param.account],(err,result)=>{
            if(err){
                console.log(err.message);
                return;
            }
            if(result[0].password==param.password){
                connection.query(sql.updatePwd,[param.password,param.account],(err,result)=>{
                    if(err){
                        console.log(err);
                        return;
                    }else{
                        console.log("change success")
                        res.send("change success");
                        res.end();
                    }
                })
            }
        })
    })
})

app.post('/changeSex',(req,res)=>{
    var param = req.body;
    pool.getConnection((err,connection)=>{
        connection.query(sql.updateSex,[param.sex,param.id],(err,result)=>{
            if(err){
                console.log(err.message);
                return;
            } else {
                console.log("性别修改成功");
                res.send("1");
                res.end();
            }
        })
    })
})

app.post('/getfriend',(req,res)=>{
    var param = req.body;
    var db = [];
    pool.getConnection((err,connection)=>{
        if(err){
            return;
        }
        connection.query(sql.getfriend,[param.id],(err,result)=>{
            if(err){
                console.log(err);
            }
            for(var i=0;i<result.length;i++){
                var st = {
                    id:result[i].id,
                    name:result[i].name,
                    sex:result[i].sex,
                    account:result[i].account,
		    friend:result[i].friend,
		    memoname:result[i].memoname
                }
                db.push(st);
            }
	    console.log(db);
        res.send(db);
        res.end();
        })
    })
})

app.post('/setname',(req,res)=>{
    var param = req.body;
    pool.getConnection((err,connection)=>{
        if(err){
            return;
        }
        connection.query(sql.setmemoname,[param.memoname,param.id],(err,result)=>{
            if(err){
                console.log(err);
                return;
            }
            if(result != null){
                res.send("1");
                res.end();
            }
        })
    })
})

app.post('/search',(req,res)=>{
console.log("111");
    var param = req.body;
    pool.getConnection((err,connection)=>{
        if(err){
            console.log(err);
            return;
        }
        connection.query(sql.search,[param.name],(err,result)=>{
            if(err){
                console.log(err);
                return;
            }
            console.log(result);
            res.send(result);
            res.end();
        })
    })
})

app.post('/getUsers',(req,res)=>{
    var param = req.body;
    var db = [];
    pool.getConnection((err,connection)=>{
        if(err){
            console.log(err);
            return;
        }
        connection.query(sql.getUsers,["1",param.library],(err,result)=>{
            if(err){
                console.log(err);
                return;
            }
            for(var i=0;i<result.length;i++){
                var st = {
                    id:result[i].id,
                    account:result[i].account,
                    name:result[i].name,
                    sex:result[i].sex,
                }
                db.push(st);
            }
            console.log(db);
            res.send(db);
            res.end();
        })
    })
})

app.post('/changeState',(req,res)=>{
    var param = req.body;
    pool.getConnection((err,connection)=>{
        if(err){
            console.log(err);
            return;
        }
        connection.query(sql.changeState,[param.state,param.id],(err,result)=>{
            if(err){
                console.log(err);
                return;
            }
            if(result != null){
                console.log("修改状态成功");
                res.send("1");
                res.end();
            }
        })
    })
})

app.post('/getLibrary',(req,res)=>{
    var param = req.body;
    var db = [];
    pool.getConnection((err,connection)=>{
        connection.query(sql.getLibrary,[param.id],(err,result)=>{
            if(err){
                console.log(err);
                return;
            }
            for(var i=0;i<result.length;i++){
                var st = {
                    library:result[i].library,
                    floor:result[i].floor,
                    detail:result[i].detail
                }
                db.push(st);
            }
            console.log(db);
            res.send(db);
            res.end();
        })
    })
})

app.post('/deleteFreind',(req,res)=>{
    var param = req.body;
    pool.getConnection((err,connection)=>{
        connection.query(sql.deletefriend,[param.id],(err,result)=>{
            if(err){
                console.log(err);
                return;
            }
            if(result != null){
                console.log(param.id+"删除成功");
                res.send("1");
                res.end();
            }
        })
    })
})



app.listen(3000, function () {
    console.log('3000端口已起用');
})
