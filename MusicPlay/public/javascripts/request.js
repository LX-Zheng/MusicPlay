var requests = {};
/**
 * 向服务端无刷新请求
 * @param {{method:'请求方法，默认GET',url:'请求的地址',send:'发送的内容',type:'返回的类型',resultObj:'返回的对象',result:'返回的结果内容',id:'用哪一个实例运行'}} options 选项
 */
function sendRequest(options) {
    let xhr = new XMLHttpRequest();
    if (xhr.onprogress)
        xhr.abort();
    let method = "GET"; //默认为GET方法
    if (options.method)
        method = options.method;
    let type = "text";
    if (options.type) {
        type = options.type;
    }
    let resultObj = "obj";
    if (options.resultObj) {
        resultObj = options.resultObj;
    }
    xhr.responseType = type; //获取的类型
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            //成功
            if (resultObj == "text")
                options.result(this.responseText);
            else
                options.result(this.response);
        }
    }
    xhr.onerror = function() { //如果发生错误，返回false
        if (options.result) {
            options.result(false);
        }
    }



    let id = options.id;
    if (requests[id]) {
        requests[id].xhr.abort();
        let req = {
            id: id ? id : requests.length,
            xhr: xhr
        }
        requests[id] = req;
    }
    //设置请求的id

    xhr.open(method, options.url);
    let sendThing = null;
    if (options.send)
        sendThing = options.send;
    xhr.send(sendThing);
}