var getmusiclist = document.getElementById('getmusiclist');
var play = document.getElementById('play');
getmusiclist.onclick=function(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange=function(){
        if(this.readyState==4&&this.status==200){
            var res=JSON.parse(this.responseText);
            console.log(res);
            // for(var i=0;i<res.length;i++){
            //     document.getElementById('musicname').innerHTML=res[i].name+
            //     "<input type='button' value='播放' onclick='getSong("+res[i].musicid +")''>";
            // }
            document.getElementById('musicname').innerHTML=res[0].name+
            "<input type='button' value='播放' onclick='getSong("+res[0].musicid +")''>";
        }
    }
    xhr.open('GET','/index/getmusiclist');
    xhr.send(null);
}

function getSong(id){
    var xhr=new XMLHttpRequest();
    xhr.responseType="blob";
    xhr.onreadystatechange=function(){
        if(this.readyState==4&&this.status==200){
            console.log(this.response);
            var url = window.URL.createObjectURL(this.response);
            play.src=url;
        }
    }
    xhr.open('POST','/index/getsong?'+id);
    xhr.send(null);
}
