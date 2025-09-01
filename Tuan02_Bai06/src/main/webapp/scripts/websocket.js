let ws;
let username = prompt("Enter your username:");

if (username) {
    ws = new WebSocket("ws://localhost:8080/Tuan02_Bai06/chat/" + username);

    ws.onmessage = function(event) {
        let msg = JSON.parse(event.data);
        let chatBox = document.getElementById("chat-box");
        chatBox.innerHTML += "<b>" + msg.user + ":</b> " + msg.content + "<br>";
        chatBox.scrollTop = chatBox.scrollHeight;
    };
}

function sendMessage() {
    let input = document.getElementById("message");
    if (input.value && ws) {
        ws.send(input.value);
        input.value = "";
    }
}
