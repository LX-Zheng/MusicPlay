var sql={
    select:'select * from user where account=?',
    updatePwd:'update user set password=? where account=?',
    updateSex:'update user set sex=? where id=?',
    //getfriends:'select * from relationship where user=?',
    //getfriend:'select * from user where id=?',
    getfriend:'select * from user,relationship where user=? and relationship.friend=user.id',
    setmemoname:'update relationship set memoname=? where friend=?',
    search:'select * from user where name=?',
    getUsers:'select * from user where state=? and library=?',
    changeState:'update user set state=? where id=?',
    getLibrary:'select * from user,library where user.id=? and user.library=library.library',
    deletefriend:'delete from relationship where id=?',
}
module.exports = sql;