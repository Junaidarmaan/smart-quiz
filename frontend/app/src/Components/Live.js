import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient = null;

function connect(onConnectCallback) {
  if (stompClient && stompClient.connected) {
    return;
  }

  stompClient = new Client({
    brokerURL: undefined, // Disable raw websocket for SockJS
    webSocketFactory: () => new SockJS("https://smart-quiz-xmzm.onrender.com/server"),

    reconnectDelay: 5000,

    debug: (str) => {
      console.log("[STOMP] " + str);
    },

    onConnect: () => {
      console.log("STOMP connected (SockJS)");
      if (onConnectCallback) onConnectCallback(stompClient);
    },

    onStompError: (frame) => {
      console.error("STOMP error:", frame.headers["message"]);
    },
  });

  stompClient.activate();
}

function subscribe(destination, callback) {
  if (!stompClient || !stompClient.connected) {
    console.error("STOMP not connected. Call connect() first.");
    return;
  }

  return stompClient.subscribe(destination, (message) => {
    const body = JSON.parse(message.body);
    callback(body);
  });
}

function send(destination, payload) {
  if (!stompClient || !stompClient.connected) {
    console.error("STOMP not connected. Cannot send.");
    return;
  }

  stompClient.publish({
    destination,
    body: JSON.stringify(payload),
  });
}

function disconnect() {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
    console.log("STOMP disconnected");
  }
}

export default {
  connect,
  subscribe,
  send,
  disconnect,
};
