<template>
    <div class="card card1">
      <div class="user">
        <img class="userPicture" :src="requireImage('wyl.jpg')" alt="" />
        <div class="userName">
          {{ userName }}
        </div>
        <div class="userMotto">
          {{ userMotto }}
        </div>
      </div>
      <div class="contact">
        <div class="cover1"></div>
        <div class="functionAndInfo">
  
          
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        userName: "汪一林",
        userMotto: "天将降大任于斯人也",
      };
    },
    methods: {
      requireImage(image) {
        //使用 require 或 import 来引入图片。这样，webpack 会知道你正在引用一个模块，它会正确地处理路径。
        return require(`../../../../assets/static/pictures/${image}`);
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
  .userName {
    color: rgb(239, 231, 231);
    font-size: 30px;
    position: relative;
    left: 150px;
    top: 60px;
    font-family: Arial;
  }
  .userMotto {
    color: rgb(239, 231, 231);
    font-size: 20px;
    position: relative;
    left: 150px;
    top: 75px;
  }
  .contact {
    position: relative;
    bottom: -30px;
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
  
  </style>
  