<template>
    <div class="main-container">
      <div class="button-container">
        <el-button-group>
          <router-link :to="{name:'createproblem'}">
          <el-button class="button-gp first-button" style="width: 100%; height: 50%">+ 新建题目</el-button>
          </router-link>
          <el-button class="button-gp second-button" style="width: 100%; height: 50%">按钮2</el-button>
          <el-button class="button-gp third-button" style="width: 100%; height: 50%">按钮3</el-button>
        </el-button-group>
      </div>
      <div class="container settop">
        <div class="search-container">
          <el-input
            v-model="searchText"
            placeholder="请输入搜索内容"
          ></el-input>
          <el-button :icon="Search" circle  @click="handleSearch"></el-button>
        </div>
        <div class="card-container">
          <el-card class="card">
            <div class="card-content">
              <div class="card-row">
                <span class="id1">P{{ question.id }}</span>
                <span class="title1">{{ question.title }}</span>
                <span class="difficulty1">{{ question.difficulty }}</span>
                <span class="knowledge1">{{ question.knowledge }}</span>
                <span style="margin: 20px;">{{ question.source }}</span>
                <span style="margin-left: 100px;">{{ question.submissionCount }}/{{ question.aceptedCount }}</span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import {
    Check,
    Delete,
    Edit,
    Message,
    Search,
    Star,
  } from '@element-plus/icons-vue'
  export default {
    setup(){
        return{
            Search,
        }
    },
    data() {
      return {
        searchText: '',
        question: {
          id:"0001",
          title: 'A+B',
          difficulty: '简单',
          knowledge: '模拟',
          source: '原创',
          submissionCount: '100',
          aceptedCount:'100',
        }
      };
    },
    methods: {
      handleSearch() {
        // 在这里处理搜索逻辑
        console.log(this.searchText);
      }
    }
  };
  </script>
  
  <style>
  .first-button{
    right: 10px;
  }
  .third-button{
    left: 10px;
  }
  .button-gp{
    margin: 10px;
  }
  .main-container {
    display: flex;
    justify-content: center;
    width: 100%;
  }
  
  .settop{
    position: relative;
    top:20px;
    height: 30px;
    right:80px;
  }
  .container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 66%;
    margin: 0 auto;
  }
  
  .button-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-right: 20px;
    width: 12%;
    height: 100%;
    margin: 20px;
  }
  
  .search-container {
    display: flex;
    justify-content: space-between;
    width: 80%;
    margin-bottom: 20px;
  }
  
  .el-input {
    flex-grow: 1;
  }
  
  .card-container {
    right:17px;
    position: relative;
    width: 77.5%;
    margin-bottom: 20px;
  }
  
  .card {
    width: 100%;
  }
  
  .card-content {
    display: flex;
    justify-content: space-between;
  }
    .card-row span.title1 {
    margin: 0 10px;
  }
  .card-row span.difficulty1 {
    margin: 10px;
    margin-left: 500px;
  }
  .card-row span.knowledge1 {
    margin: 10px;
    margin-left: 20px;
  }
  </style>

