let ilcc = new Vue({
    el: '#ilcc',
    data: {
        listShowed: false, //播放列表是否显示
        detailShowed: false, //当前音乐细节是否显示
        isplaying: false, //是否在播放
        audio: null, //声音实例
        barOut: null, //获取外边进度条
        soundBarOut: null, //获取声音进度条
        volume: 1, //声音音量
        musicIndex: 0, //当前播放得音乐id
        muted: false, //是否静音
        barindex: 0, //主界面选择条的索引
        nextLoopOption: 0, //播放顺序，0为列表循环，1为单曲循环，2为随机播放
        bars: ['搜索', '发现', '喜爱'], //左侧菜单
        searchList: [], //搜索结果列表
        searchName: '', //搜索名
        picUrl: '',
        username: '',
        curmusic: {
            name: '',
            singer: '',
            currentTime: 0,
            duration: 0,
        },
        musicList: [
            //     {
            //     name: 'Lengends Never Die',
            //     singer: 'Aganst the Current',
            //     src: '../legends.mp3',
            //     love: false
            // }, {
            //     name: 'Relentless',
            //     singer: 'Cre-sc3NT',
            //     src: '../Relentless.mp3',
            //     love: false
            // }, {
            //     name: 'Vagrant',
            //     singer: 'Feint',
            //     src: '../Vagrant.mp3',
            //     love: false
            // }, {
            //     name: 'Time Leaper',
            //     singer: 'Hinkik',
            //     src: '../Time Leaper.mp3',
            //     love: false
            // }
        ],
        loveList: [], //存储临时的热爱音乐列表
        curlrc: [], //存储歌词
        lrcindex: 0, //存储当前歌词播放到哪一句
    },
    methods: {
        init: function() { //对音乐播放器得初始化
            this.audio = new Audio(); //初始化声音实例
            // this.listToPlay('../legends.mp3');
            this.audio.addEventListener('canplay', this.autoPlay);
            this.audio.addEventListener('timeupdate', this.timeChange);
            this.audio.addEventListener('ended', this.musicEnd);
        },
        showList: function() { //显示/隐藏 列表框
            this.listShowed = !this.listShowed;
        },
        showDetail: function() { //显示歌曲细节面板
            this.detailShowed = !this.detailShowed;
        },
        listToPlay: function(id) { //当音乐放入播放区时
            // this.audio.src = src;
            // if (!this.musicList[id])
            //     return;
            let self = this;
            sendRequest({
                method: 'GET',
                // type: 'blob',
                url: '/index/geturl?' + id,
                result: function(url) {
                    // self.audio.src = window.URL.createObjectURL(data);
                    // self.audio.addEventListener('canplay', self.autoPlay);
                    // self.audio.addEventListener('timeupdate', self.timeChange);
                    // self.audio.addEventListener('ended', self.musicEnd);
                    self.audio.src = url;
                }
            });
            sendRequest({
                method: 'GET',
                url: '/index/getpic?' + id,
                result: function(url) {
                    self.picUrl = 'url(' + url + ')';
                }
            });
            sendRequest({
                method: 'GET',
                url: '/index/getlyrics?' + id,
                result: function(lrc) {
                    // let lrc = lrc;
                    // console.log(lrc);
                    self.setLrc(lrc);
                }
            });
        },
        autoPlay: function() { //当可以播放时，自动播放音乐
            if (this.isplaying) {
                this.audio.play();
            }
            this.audio.volume = this.volume;
            this.curmusic.duration = this.audio.duration;
            if (!this.musicList[this.musicIndex])
                return;
            this.curmusic.name = this.musicList[this.musicIndex].name;
            this.curmusic.singer = this.musicList[this.musicIndex].singer;
        },
        timeChange: function() { //当播放的音乐时间改变得时候
            this.curmusic.currentTime = this.audio.currentTime;
            this.curmusic.duration = this.audio.duration;

            let curt = this.audio.currentTime;
            if (this.curlrc[this.curlrc.length - 1].time > curt) {
                if (this.curlrc[this.lrcindex + 1].time < curt) {
                    for (; this.curlrc[this.lrcindex + 1].time < curt; this.lrcindex++);
                } else if (this.curlrc[this.lrcindex].time > curt) {
                    for (this.lrcindex = 0; this.curlrc[this.lrcindex + 1].time < curt; this.lrcindex++);
                }
            }
        },
        musicEnd: function() { //当音乐结束
            this.curmusic.currentTime = 0;
            this.isplaying = false;
            switch (this.nextLoopOption) {
                case 0:
                    this.nextMusic();
                    break;
                case 1:
                    this.playMusic();
                    break;
                case 2:
                    this.ramdomLoop();
                    break;
            }
        },
        timeFormat: function(time) { //将时间格式化
            if (isNaN(time))
                return '00:00';
            let min = Math.floor(time / 60);
            if (min < 10)
                min = '0' + min;
            let sec = Math.floor(time) % 60;
            if (sec < 10)
                sec = '0' + sec;
            return min + ':' + sec;
        },
        playMusic: function() { //播放/暂停 音乐
            if (this.curmusic.duration == 0) {
                //错误信息
                return;
            }
            this.isplaying = !this.isplaying;
            if (this.isplaying) {
                this.audio.play();
            } else {
                this.audio.pause();
            }
        },
        lastMusic: function() { //上一首歌曲
            // if (!this.audio.paused)
            //     this.audio.pause();
            if (this.musicIndex <= 0) {
                this.musicIndex = this.musicList.length - 1;
            } else {
                this.musicIndex--;
            }
            //this.listToPlay(this.musicList[this.musicIndex].src);
            if (!this.musicList[this.musicIndex]) {
                if (this.audio)
                    this.audio.currentTime = 0;
                return;
            }
            this.listToPlay(this.musicList[this.musicIndex].id);
            this.isplaying = true;
        },
        nextMusic: function() { //下一首歌曲
            // if (!this.audio.paused)
            //     this.audio.pause();
            if (this.musicIndex >= this.musicList.length - 1) {
                this.musicIndex = 0;
            } else {
                this.musicIndex++;
            }
            if (this.nextLoopOption != 2) {
                //this.listToPlay(this.musicList[this.musicIndex].src);
                if (!this.musicList[this.musicIndex]) {
                    if (this.audio) {
                        this.audio.currentTime = 0;
                        this.isplaying = true;
                    }
                    return;
                }
                this.listToPlay(this.musicList[this.musicIndex].id);
                this.isplaying = true;
            } else {
                this.ramdomLoop();
            }
        },
        setMusic: function(index) { //设置播放哪一首歌曲
            this.musicIndex = index;
            // this.listToPlay(this.musicList[this.musicIndex].src);
            this.listToPlay(this.musicList[this.musicIndex].id);
            this.isplaying = true;
        },
        mutMusic: function() { //设置静音
            if (!this.audio)
                return;
            this.muted = !this.muted;
            this.audio.muted = this.muted;
        },
        ramdomLoop: function() { //随机播放
            let ind = Math.random() * this.musicList.length;
            ind = Math.floor(ind);
            this.musicIndex = ind;
            //this.listToPlay(this.musicList[ind].src);
            if (!this.musicList[ind]) {
                if (this.audio) {
                    this.audio.currentTime = 0;
                    this.isplaying = true;
                }
                return;
            }
            this.listToPlay(this.musicList[ind].id);
            this.isplaying = true;
        },
        changeLoopOption: function() { //更换循环模式
            this.nextLoopOption++;
            if (this.nextLoopOption >= 3)
                this.nextLoopOption = 0;
        },
        setBarCtrl: function() { //设置控制条可控制
            let self = this;
            this.barOut.addEventListener('mousedown', function(event) { //进度条
                self.curmusic.currentTime = (event.x - self.barOut.offsetLeft - 300) / self.barOut.offsetWidth * self.curmusic.duration;
                if (self.audio) {
                    self.audio.currentTime = self.curmusic.currentTime;
                }
            });

            this.soundBarOut.addEventListener('mousedown', function(event) { //声音条
                let barleft = document.body.offsetWidth - 420;
                self.volume = (event.x - barleft) / self.soundBarOut.offsetWidth; //设置声音
                self.volume.toFixed(2);
                if (self.audio) {
                    self.audio.volume = self.volume;
                }
            });
        },
        selectMainBar: function(index) { //选择列表条
            this.barindex = index;
        },
        changeMainBarPic: function(index) { //选择条图片
            if (index == 0)
                return 'left-bar-search-pic';
            else if (index == 1)
                return 'left-bar-discover-pic';
            else if (index == 2)
                return 'left-bar-love-pic';
            else
                return 'left-bar-list-pic';
        },
        loveCurMusic: function() { //喜欢这首歌
            if (this.musicList.length <= 0)
                return;
            let love = this.musicList[this.musicIndex].love;
            this.musicList[this.musicIndex].love = !love;
            if (this.barindex == 2)
                this.getLoveList();
        },
        getLoveList: function() { //获取喜爱的音乐列表
            let self = this;
            this.loveList.splice(0, this.loveList.length);
            this.musicList.forEach(music => {
                if (music.love)
                    self.loveList.push(music);
            });
        },
        removeMusicList: function() { //清空播放列表
            this.musicList.splice(0, this.musicList.length);
        },
        searchSong: function() { //搜索音乐
            if (this.searchName == '')
                return;
            let self = this;
            sendRequest({
                url: '/index/search?' + self.searchName,
                method: 'GET',
                result: function(res) {
                    self.searchList = JSON.parse(res);
                }
            });
        },
        playSearchMusic: function(index) {
            //先不判断音乐列表中有相同的歌
            this.musicList.push(this.searchList[index]);
            this.listToPlay(this.musicList[this.musicList.length - 1].id);
            this.musicIndex = this.musicList.length - 1;
            this.isplaying = true;
        },
        setLrc: function(lrc) {
            this.curlrc.splice(0, this.curlrc.length);
            this.lrcindex = 0;
            let ls = lrc.split('\n');
            let lrcmatch = /\[[0-9]+\:[0-9]+.[0-9]+\]/;
            for (let i = 0; i < ls.length; i++) {
                let ma = ls[i].match(lrcmatch);
                if (ma != null) {
                    let t = ls[i].match(lrcmatch)[0];
                    t = t.substring(1, t.length - 1);
                    let t1 = t.split(':')[0];
                    let t2 = t.split(':')[1];
                    t = parseFloat(t1) * 60 + parseFloat(t2);
                    let l = {
                        time: t,
                        lrc: ls[i].split(']')[1]
                    }
                    this.curlrc.push(l);
                }
            }
        }
    },
    computed: {
        changePlayPic: function() { //更换播放暂停得按钮图片
            if (this.isplaying)
                return 'pause-btn';
            return 'play-btn';
        },
        changeSoundPic: function() { //更换声音
            if (this.muted)
                return 'muted-btn';
            return 'sound-btn';
        },
        changeLoopOptionPic: function() {
            if (this.nextLoopOption == 0) {
                return 'list-loop-btn';
            } else if (this.nextLoopOption == 1) {
                return 'one-loop-btn';
            } else {
                return 'random-loop-btn';
            }
        },
        changeBar: function() { //改变进度条
            if (!this.barOut)
                return '0px';
            return this.curmusic.currentTime / this.curmusic.duration * this.barOut.offsetWidth + 'px';
        },
        changeSoundBar: function() { //改变声音进度条
            if (!this.soundBarOut)
                return '80px';
            return this.volume * 80 + 'px';
        },
        changeLoved: function() { //喜欢音乐标识
            if (this.musicList.length <= 0)
                return 'love-btn';
            if (this.musicList[this.musicIndex].love)
                return 'love-active-btn';
            return 'love-btn';
        },
        getListCount: function() { //获取播放列表的数量
            return this.musicList.length;
        }
    },
    created: function() {

    },
    mounted: function() {
        this.init();
        this.barOut = document.getElementById('bar-out');
        this.soundBarOut = document.getElementById('sound-out');
        this.setBarCtrl();
        //this.listToPlay(this.musicList[this.musicIndex].src);
        let self = this;
        // sendRequest({
        //     method: 'GET',
        //     url: '/index/getmusiclist',
        //     result: function(data) {
        //         self.musicList = JSON.parse(data);
        //         self.listToPlay(0);
        //     }
        // });
        sendRequest({
            method: 'GET',
            url: '/index/getuserinfo',
            result: function(info) {
                self.username = JSON.parse(info).username;
            }
        })
    },
});