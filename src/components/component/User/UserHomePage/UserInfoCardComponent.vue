<template>
  <div class="card card1">
    <a-row style="position: relative; top: 160px" justify="space-between">
      <a-col style="margin-left: 25px">
        <a-row
          ><a-col
            ><input
              type="file"
              @change="handleFileChange"
              style="display: none"
              ref="fileInput" />
            <!-- <img class="userPicture" :src="userPicture" @click="uploadPicture" alt="" /> -->
            <img
              class="userPicture"
              :src="userinfo.userpicture"
              @click="uploadPicture"
              alt="" /></a-col
          ><a-col style="margin-left: 20px"
            ><el-tooltip
              class="box-item"
              effect="dark"
              content="点击修改昵称"
              placement="left-start"
            >
              <div
                v-if="!clickName"
                @click="inputInfo('username')"
                class="nickName"
              >
                {{ userinfo.nickname }}
              </div>
              <div v-else class="nickName">
                <!-- blur是来监听焦点的 -->
                <input
                  class="inputUserName"
                  @change="changeInfo('username')"
                  v-model="nickName"
                  @blur="clickName = false"
                  v-focus
                />
              </div>
            </el-tooltip>
            <div style="position: relative; top: 10px">
              <div
                v-if="!clickMotto"
                @click="inputInfo('usermotto')"
                class="userMotto"
              >
                {{ userinfo.usermotto }}
              </div>
              <div v-else class="userMotto">
                <input
                  class="inputUserName"
                  @change="changeInfo('usermotto')"
                  v-model="userMotto"
                  @blur="clickMotto = false"
                  v-focus
                />
              </div>
            </div> </a-col
        ></a-row>
      </a-col>
      <a-col>
        <a-row style="margin-top: 60px; margin-right: 20px">
          <a-col
            v-if="parseInt(userid) !== parseInt(myinfo.userid)"
            style="z-index: 1000; margin-right: 10px"
          >
            <a-tooltip title="私聊" color="rgb(52, 152, 219)">
              <MessageTwoTone
                @click="messageConnect"
                style="font-size: 38px; margin-right: 15px"
              />
            </a-tooltip>
            <a-button
              v-if="fansed === false"
              class="hoverable"
              @click="addFans"
              style="
                background-color: rgb(52, 152, 219);

                color: white;
              "
              >+ 关注</a-button
            >
            <a-button
              v-else
              class="hoverable"
              @click="cancelFans"
              style="
                background-color: rgb(172, 168, 168);

                color: white;
              "
              >已关注</a-button
            >
          </a-col>
          <a-col v-else style="margin-right: 10px">
            <a-button
              @click="jump('usersetting')"
              ghost
              style="
                display: flex;
                align-items: center;
                cursor: pointer;
                z-index: 1000;
              "
            >
              <img
                src="../../../../assets/static/pictures/usersetting2.png"
                style="margin-right: 7px"
                width="27"
              />
              <span>个人设置</span>
            </a-button>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
    <a-row class="contact">
      <div class="functionAndInfo">
        <a-row
          class="nowrap-row"
          style="margin-top: 0px"
          justify="space-between"
        >
          <a-col style="margin-left: 30px; position: relative">
            <a-tabs
              tabBarGutter="0px"
              class="my-tabs"
              size="large"
              style=""
              v-model:activeKey="activeKey"
              @change="chooseTab"
            >
              <a-tab-pane key="1" tab="主页"> </a-tab-pane>
              <a-tab-pane key="2" tab="动态"></a-tab-pane>
              <a-tab-pane key="3" tab="我的"></a-tab-pane>
              <a-tab-pane key="4" tab="比赛"></a-tab-pane>
              <a-tab-pane key="5" tab="收藏"></a-tab-pane> </a-tabs
          ></a-col>
          <a-col>
            <a-row>
              <a-col
                style="
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  height: 100%;
                  font-size: 16px;
                  margin-right: 10px;
                  margin-top: 10px;
                "
              >
                <span style="color: black; display: block">关注</span>
                <span style="color: black; display: block">
                  {{ userextra.concern }}
                </span>
              </a-col>
              <a-col
                style="
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  height: 100%;
                  margin-left: 10px;
                  margin-right: 10px;
                  font-size: 16px;
                  margin-top: 10px;
                "
              >
                <span style="color: black; display: block">粉丝</span>
                <span style="color: black; display: block">{{
                  userextra.fans
                }}</span>
              </a-col>
              <a-col
                style="
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  height: 100%;
                  margin-left: 10px;
                  margin-right: 10px;
                  font-size: 16px;
                  margin-top: 10px;
                "
              >
                <span style="color: black; display: block">提交</span>
                <span style="color: black; display: block">{{
                  userextra.submitcount
                }}</span>
              </a-col>
              <a-col
                style="
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  height: 100%;
                  margin-left: 10px;
                  margin-right: 10px;
                  font-size: 16px;
                  margin-top: 10px;
                "
              >
                <span style="color: black; display: block">通过</span>
                <span style="color: black; display: block">{{
                  userextra.aceptedcount
                }}</span>
              </a-col>
              <a-col
                style="
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  height: 100%;
                  margin-left: 10px;
                  margin-right: 10px;
                  font-size: 16px;
                  margin-top: 10px;
                "
              >
                <span style="color: black; display: block">排名</span>
                <span
                  v-if="userextra.rank !== -1"
                  style="color: black; display: block"
                  >暂无排名</span
                >
                <span v-else style="color: black; display: block">{{
                  userextra.ranking
                }}</span>
              </a-col>
              <a-col
                style="
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  height: 100%;
                  margin-left: 10px;
                  margin-right: 40px;
                  font-size: 16px;
                  margin-top: 10px;
                "
              >
                <span style="color: black; display: block">Rating</span>
                <span style="color: black; display: block">{{
                  userextra.rating
                }}</span>
              </a-col>
            </a-row>
          </a-col>
        </a-row>
      </div>
    </a-row>
    <a-row style="margin-top: 70px">
      <div
        style="
          width: 100%;
          border-radius: 5px;
          background-color: white;
          padding: 20px;
        "
      >
        <a-col v-if="tab === 1">
          <UserHomeComponentVue :userid="userid" />
        </a-col>
        <a-col v-if="tab === 2"> </a-col>
        <a-col v-if="tab === 3"> </a-col>
        <a-col v-if="tab === 4"> </a-col>
        <a-col v-if="tab === 5"> </a-col>
      </div>
    </a-row>
  </div>

  <el-dialog v-model="centerDialogVisible" title="提示" width="30%" center>
    <span>
      {{ noticeInfo }}
    </span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="confirmChange('cancel')">取消</el-button>
        <el-button
          style="color: white"
          type="primary"
          @click="confirmChange('confirm')"
        >
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { SERVER_URL } from "../../../../js/functions/config";
import axios from "axios";
import { getNowTime } from "@/js/functions/TimeAbout";
import { MessageTwoTone } from "@ant-design/icons-vue";
import router from "@/router/router";
import UserHomeComponentVue from "../UserHomeComponent.vue";
export default {
  components: {
    MessageTwoTone,
    UserHomeComponentVue,
  },
  data() {
    return {
      noticeInfo: "",
      userPicture: JSON.parse(localStorage.getItem("user")).userpicture,
      userinfo: {},
      userextra: {},
      fansed: false,
      myinfo: JSON.parse(localStorage.getItem("user")),
      userid: this.$route.query.userid,
      tab: 1,
    };
  },
  async created() {
    await this.getUserInfo();
    await this.checkFans();
  },

  methods: {
    chooseTab(index) {
      this.tab = parseInt(index);
    },
    jump(total) {
      router.push({ path: "/" + total });
    },
    async messageConnect() {
      await axios.post(`${SERVER_URL}/message/add/connect`, {
        belong: this.myinfo.userid,
        target: this.userid,
        belongname: this.myinfo.username,
        targetname: this.userinfo.nickname,
        connecttime: this.getNowTime(),
        latestconnecttime: this.getNowTime(),
        targetpicture: this.userinfo.userpicture,
      });
      router.push({ path: "/message" });
    },

    async cancelFans() {
      await axios
        .get(`${SERVER_URL}/userfans/delete/fans`, {
          params: {
            userid: this.$route.query.userid,
            fansid: JSON.parse(localStorage.getItem("user")).userid,
          },
        })
        .then((res) => {
          this.fansed = false;
          this.userextra.fans -= 1;
        })
        .catch((err) => {
          {
            console.log(err);
          }
        });
    },
    async checkFans() {
      await axios
        .get(`${SERVER_URL}/userfans/query/fans`, {
          params: {
            userid: this.$route.query.userid,
            fansid: JSON.parse(localStorage.getItem("user")).userid,
          },
        })
        .then((res) => {
          this.fansed = res.data;
        })
        .catch((err) => {
          {
            console.log(err);
          }
        });
    },
    async getUserInfo() {
      await axios
        .get(`${SERVER_URL}/userextra/query/id`, {
          params: {
            userid: this.$route.query.userid,
          },
        })
        .then((res) => {
          this.userextra = res.data;
        })
        .catch((err) => {
          console.log(err);
        });

      await axios
        .post(`${SERVER_URL}/user/query`, {
          userid: this.$route.query.userid,
        })
        .then((res) => {
          this.userinfo = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async addFans() {
      let fansuserid = JSON.parse(localStorage.getItem("user")).userid;
      let userid = this.$route.query.userid;
      // if (parseInt(userid) === parseInt(fansuserid)) {
      //   this.$store.dispatch("notice", {
      //     title: "不可以关注自己哦~~",
      //     message: "",
      //     type: "success",
      //   });
      //   return;
      // }
      await axios
        .post(`${SERVER_URL}/userfans/add`, {
          userid,
          fansuserid,
        })
        .then((res) => {
          this.fansed = true;
          this.userextra.fans += 1;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    requireImage(image) {
      //使用 require 或 import 来引入图片。这样，webpack 会知道你正在引用一个模块，它会正确地处理路径。
      return require(`../../../../assets/static/pictures/${image}`);
    },
    inputInfo(userInfo) {
      if (userInfo === "username") {
        this.clickName = true;
      } else if (userInfo === "usermotto") {
        this.clickMotto = true;
      }
    },
    changeInfo(userInfo) {
      if (userInfo === "username") {
        if (this.nickName === "") {
          this.noticeInfo = "昵称不能为空！";
          this.nickName = JSON.parse(localStorage.getItem("user")).nickname;
        } else {
          this.noticeInfo = "你确定把昵称修改为：" + this.nickName + " 吗？";
        }
        this.centerDialogVisible = true;
      } else if (userInfo === "usermotto") {
        if (this.userMotto === "") {
          this.noticeInfo = "个性签名不能为空！";
          this.userMotto = JSON.parse(localStorage.getItem("user")).usermotto;
        } else {
          this.noticeInfo =
            "你确定把个性签名修改为：" + this.userMotto + " 吗？";
        }
        this.centerDialogVisible = true;
      }
    },
    async confirmChange(confirmInfo) {
      if (confirmInfo === "cancel") {
        this.nickName = JSON.parse(localStorage.getItem("user")).nickname;
        this.userMotto = JSON.parse(localStorage.getItem("user")).usermotto;
        this.centerDialogVisible = false;
      } else if (confirmInfo === "confirm") {
        const userinfo = JSON.parse(localStorage.getItem("user"));
        userinfo.nickname = this.nickName;
        userinfo.usermotto = this.userMotto;
        await axios
          .post(`${SERVER_URL}/user/update/cardinfo`, userinfo)
          .then((response) => {
            this.$store.dispatch("notice", {
              title: "修改成功！",
              message: "",
              type: "success",
            });
            localStorage.setItem("user", JSON.stringify(userinfo));
          })
          .catch((error) => {
            this.$store.dispatch("notice", {
              title: "修改失败！",
              message: "服务器异常" + error,
              type: "error",
            });
            this.nickName = JSON.parse(localStorage.getItem("user")).nickname;
            this.userMotto = JSON.parse(localStorage.getItem("user")).usermotto;
          });
        this.centerDialogVisible = false;
      }
    },
    uploadPicture() {
      this.$refs.fileInput.click();
    },
    saveUserpicture(userPicture) {
      if (userPicture === "Upload failed") return;
      const userinfo = JSON.parse(localStorage.getItem("user"));
      userinfo.userpicture = userPicture;
      // this.userPicture = userPicture;
      axios
        .post(`${SERVER_URL}/user/update/userpicture`, userinfo)
        .then((response) => {
          // 服务器应该返回图片的 URL
          localStorage.setItem("user", JSON.stringify(userinfo));
        })
        .catch((error) => {
          this.$store.dispatch("notice", {
            title: "上传失败！",
            message: "服务器异常" + error,
            type: "error",
          });
        });
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const formData = new FormData();
        formData.append("file", file);
        const userinfo = JSON.parse(localStorage.getItem("user"));
        formData.append("userinfo", JSON.stringify(userinfo));
        axios
          .post(`${SERVER_URL}/user/image/upload`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then((response) => {
            // 服务器应该返回图片的 URL
            this.saveUserpicture(response.data);
            this.$store.dispatch("notice", {
              title: "头像上传成功！",
              message: "",
              type: "success",
            });
            if (response.data !== "Upload failed")
              this.userPicture = response.data;
            window.location.reload();
          })
          .catch((error) => {
            this.$store.dispatch("notice", {
              title: "上传失败！",
              message: "服务器异常" + error,
              type: "error",
            });
            this.userPicture = JSON.parse(
              localStorage.getItem("user").userpicture
            );
          });
      }
    },
  },
};
</script>

<style scoped>
.card1 {
  border: 1px;
  width: 80%;
  position: relative;
  top: 20px;
  left: 150px;
  height: 320px;
  background-image: url("../../../../assets/static/pictures/UserBackground.jpg");
  background-size: cover;
  background-position: center;
}
/*  */
.user {
  position: relative;
  bottom: 0px;
}
.userPicture {
  position: relative;

  width: 100px;
  border-radius: 50%;
}
.nickName {
  color: rgb(239, 231, 231);
  font-size: 30px;
  position: relative;
  width: 200px;
  font-family: Arial;
}

.userMotto {
  color: rgb(239, 231, 231);
  font-size: 20px;
  position: relative;
  height: 50px;
  width: 400px;
}
.contact {
  margin-top: 125px;
}
.functionAndInfo {
  border-radius: 0 0 5px 5px;
  width: 100%;
  top: 53px;
  position: relative;
  height: 75px;
  background-color: white;
}
.cover1 {
  position: relative;
  top: 70px;
  height: 20px;
  background-color: white;
}

.inputUserName {
  width: 400px;
  background-color: white;
  color: rgb(6, 107, 239);
}
.dialog-footer button:first-child {
  margin-right: 10px;
}
/* 不换行 */
.nowrap-row {
  display: flex;
  flex-wrap: nowrap;
}

.hoverable {
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
  text-decoration: underline;
}
</style>
