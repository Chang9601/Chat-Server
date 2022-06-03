// 로그인 시스템 대신 임시방편
let username = prompt('아이디를 입력하세요.');
let roomNum = prompt('채팅방 번호를 입력하세요.');

document.querySelector('#username').innerHTML = username;

// 파란박스 만들기
const getSentMsgBox = (data) => {

    let md = data.creationTime.substring(5,10)
    let tm = data.creationTime.substring(11,16)
    convertTime = tm + " | " + md

    return `<div class="sent_msg">
    <p>${data.msg}</p>
    <span class="time_date">${convertTime} / <b>${data.sender}</b></span> 
    </div>`;
};

// 회색박스 만들기
const getReceivedMsgBox = (data) => {
    
    let md = data.creationTime.substring(5,10)
    let tm = data.creationTime.substring(11,16)
    convertTime = tm + " | " + md

    return `<div class="received_withd_msg">
    <p>${data.msg}</p>
    <span class="time_date">${convertTime} / <b>${data.sender}</b></span> 
    </div>`;
};

// 초기화 시 1번 방에 3건이 있으면 3건 가져옴
// addMsg() 함수 호출 DB에 삽입되고, 데이터가 자동으로 흘러들어온다.
// 파란박스 초기화
const initMyMsg = (data) => {
    let chatBox = document.querySelector('#chat-box');
    let sentBox = document.createElement('div');

    sentBox.className = 'outgoing_msg';
    sentBox.innerHTML = getSentMsgBox(data);
    chatBox.append(sentBox);

    document.documentElement.scrollTop = document.body.scrollHeight;
};

// 회색박스 초기화
const initYourMsg = (data) => {
    let chatBox = document.querySelector('#chat-box');
    let receivedBox = document.createElement('div');

    receivedBox.className = 'received_msg';
    receivedBox.innerHTML = getReceivedMsgBox(data);
    chatBox.append(receivedBox);

    document.documentElement.scrollTop = document.body.scrollHeight;
};

// Ajax 채팅 메시지 전송
const addMsg = async () => {
    let msg = document.querySelector('#chat-outgoing-msg');

    let chat = {
        sender: username,
        roomNum: roomNum,
        msg: msg.value
    };

    await fetch('http://localhost:8080/chat', {
        method: 'POST',
        body: JSON.stringify(chat),
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    });

    msg.value = ''; 
};

// SSE 연결
const eventSource = new EventSource(`http://localhost:8080/chat/roomNum/${roomNum}`);

eventSource.onmessage = (e) => {
    const data = JSON.parse(e.data);
    if (data.sender === username) { // 로그인한 유저가 보낸 메시지
        // 파란박스 (오른쪽)
        initMyMsg(data);
    } else {
        // 회색박스 (왼쪽)
        initYourMsg(data);
    }
}

// 버튼 클릭 시 메시지 전송
document.querySelector('#chat-outgoing-button').addEventListener('click', () => {
    addMsg();
})

// Enter 클릭 시 메시지 전송
document.querySelector('#chat-outgoing-msg').addEventListener('keydown', (e) => {
    let key = e.keyCode;

    if(key === 13) 
        addMsg();
});