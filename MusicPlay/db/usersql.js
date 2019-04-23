var UserSQL = {
    insert:'insert into user(email,username,pwd) values(?,?,?)',
    select:'select * from user where email=?',
    selectmusic:'select * from music where name like ?',
    selectsinger:'select * from music where singer=?',
    changePwd:'update user set pwd=? where email=?',
    emailconfirm:'select email from user where email= ?',
    getmusiclist:'select id,name,singer,songpath,love from music',
    getmusicid:'select songpath from music where id=?',
    InsertSheet:'insert into songsheet(email,songsheet,songsheetid) values(?,?,?)',
    SelectUserInfo:'select username,songsheet,songsheetid from user,songsheet where user.email=songsheet.email and user.email=?',
}
module.exports = UserSQL;