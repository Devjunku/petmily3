<template>
  <div class="Jbgc" style="height: 100%;">
    <div id="main-container" class=" container">
      <div id="join" v-if="!session">
        <div id="join-dialog" class="">
          <div class="">
            <div class="font-bold  mb-4 title-live " style="font-size:40px">라이브 입장</div>
          </div>
          <div class="form-group">
            <div class="font-bold mb-3 d-flex flex-row" style="font-size:18px">
              닉네임
            </div>
            <input
              v-model="myUserName"
              class="form-control mb-5"
              style="height:45px;"
              type="text"
              required
              placeholder="닉네임 입력"
            />

            <div class="font-bold mb-3 d-flex flex-row" style="font-size:18px">방번호</div>
            <p>
              <input v-model="mySessionId" class="form-control" type="text" readonly />
            </p>
            <p class="text-center">
              <button class="btn-white mt-4 mb-5" style="height:45px;" @click="joinSession()">
                Join!
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>

    <div id="session" v-if="session">
      <div id="session-header" class="">
        <!-- <h1 id="session-title">{{ mySessionId }}</h1> -->
      </div>

      <div class="row">
        <div id="main-video" class="main-video col-10">
          <!-- <user-video :stream-manager="mainStreamManager" /> -->

          <div id="video-container" class="col">
            <div class="RoomInfo mb-3">{{ RoomInfo.title }}</div>
            <div>
              <span class="font-bold">현재 시청자수 : </span>
              <span class="font-bold"> {{ this.numberOfElements }}</span
              ><span class="font-bold"> 명 </span>
            </div>
            <user-video
              v-if="this.isHost"
              :stream-manager="publisher"
              @click="updateMainVideoStreamManager(publisher)"
            />
            <user-video
              v-else
              v-for="sub in subscribers"
              :key="sub.stream.connection.connectionId"
              :stream-manager="sub"
              @click="updateMainVideoStreamManager(sub)"
            />
            <div class="RoomInfo-con">{{ RoomInfo.description }}</div>
            <div class="mb-4">
              <div class="Live-button">
                <input
                  class="btn-out mt-4"
                  type="button"
                  id="buttonLeaveSession"
                  @click="leaveSession"
                  value="라이브 나가기"
                />
                <input
                  v-if="isHost"
                  class="btn-end mt-4"
                  type="button"
                  id="buttonEndSession"
                  @click="endSession"
                  value="라이브 종료"
                />
              </div>
            </div>
          </div>
        </div>
        <div class="col-2">
          <input class="btn-chat" type="button" id="buttonChatting" value="chatting" />

          <div>
            <MessageList :msgs="msgs" />
            <div class="msg-form mt-1">
              <MessageForm @sendMsg="sendMsg" :user-name="myUserName" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "./components/UserVideo";
import MessageForm from "./components/messageForm";
import MessageList from "./components/messageList";

axios.defaults.headers.post["Content-Type"] = "application/json";

const OPENVIDU_SERVER_URL = "https://" + "i5a408.p.ssafy.io:8443";
const OPENVIDU_SERVER_SECRET = "petmily";

export default {
  name: "LiveRoom",
  props: {
    live: Object,
  },

  components: {
    UserVideo,
    MessageForm,
    MessageList,
  },

  data() {
    return {
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      URL: location.pathname,

      mySessionId: location.pathname.substring(10, location.pathname.length),
      myUserName: "",

      //chatting
      msgs: [],
      vOnOff: true,
      aOnOff: true,
      size: true,
      chatting: false,
      isHost: false,
      title: "",
      discription: "",
      RoomInfo: [],

      connections: [],
      numberOfElements: 0,
    };
  },
  created() {
    this.isHost = localStorage.getItem("agencycode") === this.mySessionId ? true : false;
    console.log(this.mySessionId);
    console.log(localStorage.getItem("agencycode"));
    console.log(this.isHost);
    console.log(localStorage.getItem("agencycode") === this.mySessionId ? true : false);
  },

  methods: {
    chattingOnOff() {
      this.chatting = !this.chatting;
      console.log(" 공유 여부 ");
      console.log(this.subscribers[1].stream.typeOfVideo);
    },

    sendMsg(msg) {
      // Sender of the message (after 'session.connect')
      this.session
        .signal({
          data: msg, // Any string (optional)
          to: [], // Array of Connection objects (optional. Broadcast to everyone if empty)
          type: "my-chat", // The type of message (optional)
        })
        .then(() => {
          console.log("Message successfully sent");
        })
        .catch((error) => {
          console.error(error);
        });
    },
    joinSession() {
      // --- Get an OpenVidu object ---
      this.OV = new OpenVidu();

      // --- Init a session ---
      this.session = this.OV.initSession();

      // --- Specify the actions when events take place in the session ---

      // On every new Stream received...
      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      this.session.on("connectionCreated", ({ stream }) => {
        this.numberOfElements++;
      });

      this.session.on("sessionDisconnected", ({ stream }) => {
        // const subscriber = this.session.subscribe(stream);
        // this.subscribers.push(subscriber);
        alert("라이브가 종료되었습니다.");
        location.replace("live");
      });

      // On every asynchronous exception...
      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });
      this.session.on("signal:my-chat", (event) => {
        // console.log(event.data); // Message
        // console.log(event.from); // Connection object of the sender
        // console.log(event.type); // The type of message ("my-chat")
        const tmp = this.msgs.slice();
        tmp.push(event.data);
        this.msgs = tmp;
      });

      // --- Connect to the session with a valid user token ---

      // 'getToken' method is simulating what your server-side should do.
      // 'token' parameter should be retrieved and returned by your own backend
      this.getToken(this.mySessionId).then((token) => {
        this.session
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            // --- Get your own camera stream with the desired properties ---

            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "1280x720", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            this.mainStreamManager = publisher;

            this.publisher = publisher;

            // --- Publish your stream ---

            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.log("There was an error connecting to the session:", error.code, error.message);
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;
      this.numberOfElements--;
      console.log(this.numberOfElements);

      window.removeEventListener("beforeunload", this.leaveSession);
    },

    endSession() {
      let mySessionId = this.mySessionId;
      let session_con = confirm("라이브를 종료하시겠습니까?");
      if (session_con == true) {
        axios
          .delete("/live/" + this.mySessionId)
          .then((res) => {
            console.log(res.data);

            //location.replace("/live");

            axios
              .delete(
                `${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${mySessionId}`,

                {
                  auth: {
                    username: "OPENVIDUAPP",
                    password: OPENVIDU_SERVER_SECRET,
                  },
                }
              )
              .then((response) => {
                location.replace("/live");
              })

              .catch((error) => {
                if (error.response.status === 404) {
                  console.log("실패~!~!!~");
                }
              });
          })
          .catch((err) => {
            console.log("실패함");
          });
      } else if (session_con == false) {
      }
    },
    findRoom() {
      axios.get("/live/detail" + this.agencycode).then((res) => {
        console.log(res.data);
        console.log(res.data.title);
      });
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },

    deleteMainVideoStreamManager() {
      this.mainOnOff = false;
    },
    /**
     * --------------------------
     * SERVER-SIDE RESPONSIBILITY
     * --------------------------
     * These methods retrieve the mandatory user token from OpenVidu Server.
     * This behavior MUST BE IN YOUR SERVER-SIDE IN PRODUCTION (by using
     * the API REST, openvidu-java-client or openvidu-node-client):
     *   1) Initialize a Session in OpenVidu Server	(POST /openvidu/api/sessions)
     *   2) Create a Connection in OpenVidu Server (POST /openvidu/api/sessions/<SESSION_ID>/connection)
     *   3) The Connection.token must be consumed in Session.connect() method
     */

    getToken(mySessionId) {
      return this.createSession(mySessionId).then((sessionId) => this.createToken(sessionId));
    },

    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessions
    createSession(sessionId) {
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions`,
            JSON.stringify({
              customSessionId: sessionId,
            }),
            {
              auth: {
                username: "OPENVIDUAPP",
                password: OPENVIDU_SERVER_SECRET,
              },
            }
          )
          .then((response) => response.data)
          .then((data) => resolve(data.id))
          .catch((error) => {
            if (error.response.status === 409) {
              resolve(sessionId);
            } else {
              console.warn(
                `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}`
              );
              if (
                window.confirm(
                  `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`
                )
              ) {
                location.assign(`${OPENVIDU_SERVER_URL}/accept-certificate`);
              }
              reject(error.response);
            }
          });
        axios.get("/live/detail/" + this.mySessionId).then((res) => {
          console.log(res.data);
          console.log(res.data.title);
          this.RoomInfo = res.data;
        });
        let mySessionId = this.mySessionId;
      });
    },

    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessionsltsession_idgtconnection
    createToken(sessionId) {
      let myrole = this.isHost ? "PUBLISHER" : "SUBSCRIBER";
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
            {
              role: myrole,
            },
            {
              auth: {
                username: "OPENVIDUAPP",
                password: OPENVIDU_SERVER_SECRET,
              },
            }
          )
          .then((response) => response.data)
          .then((data) => resolve(data.token))
          .catch((error) => reject(error.response));
      });
    },
  },
};
</script>
<style>
.LiveImg {
  display: inline;
}
.btn-chat {
  color: white;
  width: 100%;
  height: 30px;
  background-color: #a4b5f0;
  border-right: #a4b5f0 1px solid;
  border-left: #a4b5f0 1px solid;
  border-top: #a4b5f0 1px solid;
  border-bottom: #a4b5f0 1px solid;
  border-style: none;
  border-radius: 12px;
  margin-bottom: 3px;
}
.btn-out {
  color: rgb(255, 255, 255);
  width: 150px;
  height: 50px;
  background-color: #b2abee;
  border-style: none;
  border-radius: 12px;
  margin-left: 10px;
}
.btn-end {
  color: rgb(255, 255, 255);
  width: 150px;
  height: 50px;
  background-color: #fe9090;
  border-style: none;
  border-radius: 12px;
  margin-left: 10px;
}
.RoomInfo {
  padding-top: 3%;
  font-weight: bold;
  font-size: 30px;
  text-align: center;
}
.RoomInfo-con {
  /* //font-weight: bold; */
  font-size: 18px;
}
.Live-button {
  text-align: center;
}
.title-live {
  padding-top: 8%;
}
</style>
