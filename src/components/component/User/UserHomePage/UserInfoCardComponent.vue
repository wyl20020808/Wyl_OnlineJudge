<template>
  <div class="card card1">
    <div class="user">
        <input type="file" @change="handleFileChange" style="display: none" ref="fileInput">
    <img class="userPicture" :src="userPicture" @click="uploadPicture" alt="" />
      <el-tooltip
        class="box-item"
        effect="dark"
        content="点击修改昵称"
        placement="left-start"
      >
        <div v-if="!clickName" @click="inputInfo('username')"  class="nickName">
          {{ nickName }}
        </div>
        <div v-else class="nickName">
            <!-- blur是来监听焦点的 -->
            <input class="inputUserName" @change="changeInfo('username')" v-model="nickName" @blur="clickName = false" v-focus />
        </div>
      </el-tooltip>
      
      <el-tooltip
        class="box-item"
        effect="dark"
        content="点击修改格言"
        placement="left-start"
      >
      <div v-if="!clickMotto" @click="inputInfo('usermotto')" class="userMotto">
        {{ userMotto }}
      </div>
      <div v-else class="userMotto">
        <input class="inputUserName" @change="changeInfo('usermotto')" v-model="userMotto" @blur="clickMotto = false" v-focus />
        </div>
    </el-tooltip>
    </div>
    <div class="contact">
      <div class="cover1"></div>
      <div class="functionAndInfo">
        <div class="options">
          <ul
            class="homepage"
            @click="showPage('主页')"
            :class="{ active: activeOption === '主页' }"
          >
            主页
          </ul>
          <ul
            class="trends"
            @click="showPage('动态')"
            :class="{ active: activeOption === '动态' }"
          >
            动态
          </ul>
          <ul
            class="question"
            @click="showPage('题库')"
            :class="{ active: activeOption === '题库' }"
          >
            题库
          </ul>
          <ul
            class="organization"
            @click="showPage('组织')"
            :class="{ active: activeOption === '组织' }"
          >
            组织
          </ul>
          <ul
            class="collect"
            @click="showPage('收藏')"
            :class="{ active: activeOption === '收藏' }"
          >
            收藏
          </ul>
        </div>
      </div>
    </div>
  </div>

  <el-dialog  v-model="centerDialogVisible" title="提示" width="30%" center>
    <span>
      {{noticeInfo}}
    </span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="confirmChange('cancel')">取消</el-button>
        <el-button style="color: white;" type="primary" @click="confirmChange('confirm')">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import {SERVER_URL} from "../../../../js/functions/config"
import axios from 'axios';
export default {
  data() {
    return {
      nickName: JSON.parse(localStorage.getItem('user')).nickname,
      userMotto: JSON.parse(localStorage.getItem('user')).usermotto,
      activeOption: "主页",
      clickMotto:false,
      clickName:false,
      centerDialogVisible: false,
      noticeInfo:"",
      userPicture: JSON.parse(localStorage.getItem('user')).userpicture,
    };
  },
  setup(){

  },
  methods: {
    requireImage(image) {
      //使用 require 或 import 来引入图片。这样，webpack 会知道你正在引用一个模块，它会正确地处理路径。
      return require(`../../../../assets/static/pictures/${image}`);
    },
    showPage(pageName) {
      this.activeOption = pageName;
    },
    inputInfo(userInfo){
        if(userInfo === 'username'){
            this.clickName = true;
        }else if(userInfo === 'usermotto'){
            this.clickMotto = true;
        }
    },
    changeInfo(userInfo){
        if(userInfo === 'username'){
            if(this.nickName === ''){
                this.noticeInfo = "昵称不能为空！";
                this.nickName = JSON.parse(localStorage.getItem('user')).nickname;
            } 
            else{
                this.noticeInfo = "你确定把昵称修改为：" +this.nickName+" 吗？";
            } 
            this.centerDialogVisible = true;
        }else if(userInfo === 'usermotto'){
            if(this.userMotto === ''){
                this.noticeInfo = "格言不能为空！";
                this.userMotto = JSON.parse(localStorage.getItem('user')).usermotto;
            } 
            else{
                this.noticeInfo = "你确定把格言修改为：" +this.userMotto+" 吗？";
            } 
            this.centerDialogVisible = true;
        }
    },
    confirmChange(confirmInfo){
        if(confirmInfo === 'cancel'){
            this.nickName = JSON.parse(localStorage.getItem('user')).nickname;
            this.userMotto = JSON.parse(localStorage.getItem('user')).usermotto;
            this.centerDialogVisible = false;
        }else if(confirmInfo === 'confirm'){
            const userinfo = JSON.parse(localStorage.getItem('user'));
            userinfo.nickname = this.nickName;
            userinfo.usermotto = this.userMotto;
            axios.post(`${SERVER_URL}/user/update/cardinfo`,userinfo)
            .then(response => {
                this.$store.dispatch("notice",{
                    title: '修改成功！',
                    message: "",
                    type: 'success',
                })
                localStorage.setItem('user', JSON.stringify(userinfo))
            })
            .catch(error => {
                this.$store.dispatch("notice",{
                    title: '修改失败！',
                    message:"服务器异常" + error,
                    type: 'error',
                })
                this.nickName = JSON.parse(localStorage.getItem('user')).nickname;
                this.userMotto = JSON.parse(localStorage.getItem('user')).usermotto;
            })
            this.centerDialogVisible = false;
        }
    },
    uploadPicture() {
      this.$refs.fileInput.click();
    },
    saveUserpicture(userPicture){
        if(userPicture === 'Upload failed')return;
        const userinfo = JSON.parse(localStorage.getItem('user'));
        userinfo.userpicture = userPicture;
        // this.userPicture = userPicture;
        axios.post(`${SERVER_URL}/user/update/userpicture`, userinfo,)
        .then(response => {
          // 服务器应该返回图片的 URL
                localStorage.setItem('user', JSON.stringify(userinfo));
        })
        .catch(error => {
            this.$store.dispatch("notice",{
                    title: '上传失败！',
                    message:"服务器异常" + error,
                    type: 'error',
                })
        });
        
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const formData = new FormData();
        formData.append('file', file);
        const userinfo = JSON.parse(localStorage.getItem('user'));
        formData.append('userinfo', JSON.stringify(userinfo));
        axios.post(`${SERVER_URL}/user/image/upload`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(response => {
          // 服务器应该返回图片的 URL
          this.saveUserpicture(response.data);
          this.$store.dispatch("notice",{
                    title: '头像上传成功！',
                    message: "",
                    type: 'success',
                })
          if(response.data !== "Upload failed")this.userPicture = response.data;
          window.location.reload();
        })
        .catch(error => {
            this.$store.dispatch("notice",{
                    title: '上传失败！',
                    message:"服务器异常" + error,
                    type: 'error',
                })
            this.userPicture = JSON.parse(localStorage.getItem('user').userpicture);
        });
      }
    },
  },
};
</script>

<style scoped>
.card1 {
  border-radius: 20px;
  border: 1px;
  width: 80%;
  position: relative;
  top: 20px;
  left: 150px;
  height: 350px;
  background-image: url("../../../../assets/static/pictures/UserBackground.jpg");
  background-size: cover;
  background-position: center;
}
/*  */
.user {
  position: relative;
  bottom: 20px;
}
.userPicture {
  position: relative;
  top: 160px;
  left: 30px;
  width: 100px;
  border-radius: 50%;
}
.nickName {
  color: rgb(239, 231, 231);
  font-size: 30px;
  position: relative;
  left: 150px;
  top: 60px;
  width: 200px;
  font-family: Arial;
}

.userMotto {
  color: rgb(239, 231, 231);
  font-size: 20px;
  position: relative;
  left: 150px;
  height: 50px;
  width: 400px;
  top: 75px;
}
.contact {
  position: relative;
  bottom: -20px;
}
.functionAndInfo {
  border-radius: 20px;
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
.options {
  position: relative;
  left: 15px;
  top: 20px;
  font-size: 24px;
}
.homepage {
  width: 100px;
  cursor: pointer;
}
.trends {
  width: 100px;
  position: relative;
  left: 90px;
  bottom: 50px;
  cursor: pointer;
}
.question {
  width: 100px;
  position: relative;
  left: 180px;
  bottom: 102px;
  cursor: pointer;
}
.organization {
  width: 100px;
  position: relative;
  left: 270px;
  bottom: 154px;
  cursor: pointer;
}
.collect {
  width: 100px;
  position: relative;
  left: 360px;
  bottom: 206px;
  cursor: pointer;
}
.homepage.active {
  color: rgb(6, 106, 236);
  border-bottom: 2px solid rgb(6, 106, 236);
}
.trends.active {
  color: rgb(6, 106, 236);
  border-bottom: 2px solid rgb(6, 106, 236);
}
.question.active {
  color: rgb(6, 106, 236);
  border-bottom: 2px solid rgb(6, 106, 236);
}
.organization.active {
  color: rgb(6, 106, 236);
  border-bottom: 2px solid rgb(6, 106, 236);
}
.collect.active {
  color: rgb(6, 106, 236);
  border-bottom: 2px solid rgb(6, 106, 236);
}
.inputUserName {
    width: 400px;
  background-color: white;
  color: rgb(6, 107, 239);
}
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>

<!-- <ul class="homepage">主页</ul>
          <ul class="trends">动态</ul>
          <ul class="question">题库</ul>
          <ul class="organization">组织</ul>
          <ul class="collect">收藏</ul> -->
