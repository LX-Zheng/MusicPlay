var wait=60;
function time(o){
	if(wait==0){
		o.removeAttribute("disabled");
		alue="获取验证码";
		wait=60;
	}else{ 
		o.setAttribute("disabled",true);				
        wait--;
        o.value="重新发送("+wait+"s)";
		setTimeout(function(){
			time(o)
		},
		1000)
	}
}
var username = document.getElementById('name');
var send=document.getElementById('send-email');
var email=document.getElementById('email');
send.onclick=function(){
    time(this);
    var xhr=new XMLHttpRequest();
    xhr.onreadystatechange=function(){
        if(this.readyState==4&&this.status==200){
            var res=JSON.parse(this.responseText);
                console.log(res);
            }
        }
    xhr.open('get','/email/sendemail?'+email.value);
    xhr.send(null);
}


//判断二次密码是否一致
function vaildate(){
    var pwd1 = document.getElementById('pwd1').value;
    var pwd2 = document.getElementById('pwd2').value;
    if(pwd1==pwd2){
        document.getElementById('point').innerHTML="<img src='/images/check.png' width=20px height=20px>";
        document.getElementById('register').disabled=false;
    }else{
        document.getElementById('point').innerHTML="<img src='/images/error.png' width=20px height=20px>";
        document.getElementById('register').disabled=true;
    }
}

//判断邮箱是否注册
function Mailbox(){
    var emailcontext = document.getElementById('email');
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange=function(){
        if(this.readyState==4&&this.status==200){
            var res = this.responseText;
        }
        //验证密码是否一致
        if(res==2){
            document.getElementById('emailconfirm').innerHTML="<img src='/images/check.png' width=20px height=20px>";
            document.getElementById('register').disabled=false;
        }
        if(res==1){
            document.getElementById('emailconfirm').innerHTML="<img src='/images/error.png' width=20px height=20px>";
            document.getElementById('register').disabled=true;
        }
    }
    xhr.open('GET','/register/emailconfirm?'+emailcontext.value);
    xhr.send(null);
}


//验证密码强度
function checkpassword(){
    var password = document.getElementById('pwd1').value;
    var reg1 = /\d/; 
    var reg2 = /[a-zA-Z]/;
    var reg3 = /\W/;
    var level = 0;
    if(reg1.test(password)){
        //密码等级增加
        level++;
    }
    if(reg2.test(password)){
    //密码等级增加
        level++;
    }
    if(reg3.test(password)){
        //密码等级增加
        level++;
    }
    if(level==1){
        document.getElementById('level').innerHTML="低";
    }
    if(level==2){
        document.getElementById('level').innerHTML="中";
    }
    if(level==3){
        document.getElementById('level').innerHTML="强";
    }   
}

