
const express = require('express');
const socket = require('socket.io');
const http = require('http');
const fs = require('fs');
const bodyParser = require('body-parser');


const app = express();

const server = http.createServer(app);
const io = socket(server);

const db = new (require('./Database_Connecter'))('db_configure.json');

function sendResult(res, json){
  json.result = true;
  res.send(JSON.stringify(json));
}

function sendError(res, json){
  json.result = false;
  res.send(JSON.stringify(json));
}

app.use(bodyParser.json({limit: '10mb', extended : true}));
//첫 접속
app.get('/FirstConnect', (req, res) =>{
  //토큰 새로 만들기
  //토큰 전송
  console.log('first connect');
  let query = 'insert into usertable values(default) returning user_id';
  db.query(query).then((data) =>{
    let id = data.rows[0].user_id;
    console.log(`First connect : ${id}`);
    res.send(JSON.stringify({result: true, id: id}));
  }).catch((e) =>{
    console.log(e);
    sendError(res, {description: '알 수 없는 이유'});
  });
});


//일반적인 접속
app.post('/Connect', (req, res) =>{
  //아이디 받기
  //룸 리스트 주기
  //룸 정보 : id, title, myNickname, nofity
  console.log(req.body); //왜 undefined
  let id = req.body.id;
  console.log(`connect ${id}`);
  let query = `select user_room.room_id as room_id,
  room_title, user_nickname, last_modifyed, last_fetch_date
  from user_room left join roomtable on user_room.room_id = roomtable.room_id
  where user_id = $1`;
  // let query = `select last_fetch_date, user_nickname, room_id, room_title, last_modifyed
  // from (
  //   select *
  //   from user_room
  //   where user_id = $1
  // ) roomlist
  // left join roomtable`;
  let value = [id];

  db.query(query, value).then((r)=>{
    let result = {
      roomlist: r.rows
    };
    sendResult(res, result);
  }).catch(e =>{
    console.log(e);
    sendError(res, {description: '알 수 없는 이유'});
  });
});


//방 만들기
app.post('/CreateRoom', (req, res)=>{
  let body = req.body;
  let userId = body.id;
  let roomTitle = body.title;
  let roomPasswd = body.password;
  let nickname = body.nickname;

  //title이 이미 쓰고 있는지
  //password, nickname으로 방하고 user_room 만들기

  let findRoomQuery = `select room_title = $1 as isexist
  from roomtable where room_title = $1`;
  let findRoomValue = [roomTitle];
  db.query(findRoomQuery, findRoomValue).then(r=>{
    if(r.rows[0] == null){
      db.query('BEGIN');
      let makeRoomQuery = `insert into roomtable values(default, $1, $2, now()) returning room_id`;
      let makeRoomValue = [roomTitle, roomPasswd];
      db.query(makeRoomQuery, makeRoomValue).then(roomResult =>{
        let roomId = roomResult.rows[0].room_id;
        let makeRelationQuery = `insert into user_room values($1, $2, $3, now())`;
        let makeRelationValue = [userId, roomId, nickname];
        db.query(makeRelationQuery, makeRelationValue).then(relResult =>{
          db.query('COMMIT');
          let result = {
            roomId: roomId,
            roomTitle: roomTitle,
            nickname: nickname
          };
          sendResult(res, result);
          console.log(`created room : ${roomTitle}`);
        });
      });
    }
    else{
      sendError(res, {description: '이미 있는 방 제목입니다.'});
    }
  }).catch(e=>{
    db.query('ROLLBACK');
    sendError(res, {description: '알수없는 이유'});
  });
});

//방 들어가기
app.post('/JoinRoom', (req, res)=>{
  let body = req.body;
  let userId = body.id;
  let roomTitle = body.title;
  let roomPasswd = body.password;
  let nickname = body.nickname;

  //title에 맞는 룸이 있는지
  //password가 맞는지
  //다 만족하면 user_room 만들기

  let findRoomQuery = `select room_id, passwd = $1 as isvalid from roomtable where room_title = $2`;
  let findRoomValue = [roomPasswd, roomTitle];
  db.query(findRoomQuery, findRoomValue).then(r=>{
    //row가 없으면 맞는 아이디가 없는것
    //isvalid가 false이면 비밀번호가 틀린것
    if(r.rows[0].isvalid){
      let makeRelationQuery = `insert into user_room values($1, $2, $3, now())`;
      let makeRelationvalues = [userId, r.rows[0].room_id, nickname];
      db.query(makeRelationQuery, makeRelationvalues).then(r2=>{
        //방 정보 보내기
        //정보 : roomid, title, nickname
        let result = {
          roomId: r2.rows[0].room_id,
          roomTitle: roomTitle,
          nickname: nickname
        };
        sendResult(res, result);
        console.log(`joined room : ${roomTitle}`);
      }).catch(e=>{
        sendError(res, {description: '이미 방에 있습니다.'});
      });
    }
    else{
      sendError(res, {description: '비밀번호가 틀립니다.'});
    }

  }).catch(e=>{
    let result = {
      description: '맞는 방이 없습니다.'
    };
    sendError(res, result);
  });
});


//여기부터 소켓
io.sockets.on('connection', (socket)=>{
  console.log('connected someone');
  //연결 종료
  socket.on('disconnect', () =>{
    let userId = socket.user_id;
    let query = `update user_room set last_fetch_date = now() where user_id = $1`;
    let values = [userId];
    db.query(query, values).catch(e=>{
      console.log(e);
    });
    console.log('disconnected');
  });


  //chat.on('connection', (socket) =>{

    //모든 유저 리스트
    //채팅 리스트
    //보내기
  const chat = io.of('/chat');
    //유저 리스트
    socket.on('configure', (data)=>{
      let userId = data.userId;
      socket.user_id = userId;
      console.log('configure');
      let roomId = data.roomId;
      let userListQuery = `select user_id, user_nickname from user_room where room_id = $1`;
      let values = [roomId];
      db.query(userListQuery, values).then(r=>{
        let userlist = {
          userList: r.rows
        };
        socket.emit('userlist', userlist);
      }).catch(e=>{
        console.log(e);
        socket.emit('error', {description: "알수 없는 이유(방 불러오기 실패)"});
      });
    });

    //채팅 로드
    socket.on('load', (data)=>{
      console.log('load');
      let roomId = data.roomId;
      let loadNo = data.loadNo;
      let chatListQuery = `select user_id, contents, chat_time from chatlist where room_id = $1
      order by chat_time limit 50 offset $2`;
      let values = [roomId, loadNo];
      db.query(chatListQuery, values).then(r=>{
        let chatlist = {
          chatList: r.rows
        };
        socket.emit(`chatlist`, chatlist);
      }).catch(e =>{
        console.log(e);
        socket.emit('error', {description: "알수 없는 이유(채팅 불러오기 실패)"});
      });
    });
    //socket.emit('configure', 'null');

    //채팅
    socket.on('chat', (data) =>{
      console.log('chat');
      let userId = data.userId;
      let roomId = data.roomId;
      let contents = data.contents;

      let query = `insert into chatlist values($1, $2, $3, now()) returning chat_time`;
      let values = [roomId, userId, contents];

      db.query(query, values).then(r=>{
        let chat_time = r.rows[0].chat_time;
        console.log(`room: ${roomId}, ${userId}: ${contents} at ${chat_time}`);
        io.of(`/chat/${roomId}`).emit('chat', {user_id: userId, contents: contents, chat_time: r.rows[0].chat_time});

      }).catch(e=>{
        console.log(e);
        socket.emit('error', {description: "알수 없는 이유(채팅 불러오기 실패)"});
      });

    });


  //});
});


server.listen(55252, ()=>{
  console.log('서버 실행');
});
