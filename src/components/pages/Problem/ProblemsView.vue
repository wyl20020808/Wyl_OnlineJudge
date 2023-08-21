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
        <div v-for="problem in questions" :key="problem.problemid" class="card-container">
          <a @click="push_to_problemcontent(problem)">
          <el-card class="card">
            <div class="card-content">
              <div class="card-row">
                <span id="id1" class="id1">P{{ problem.problemid }}</span>
                <span id="title1" class="title1">{{ problem.title }}</span>
                <span id="difficulty1" class="difficulty1">{{ problem.difficulty }}</span>
                <span id="knowledge1" class="knowledge1">{{ problem.algorithm }}</span>
                <span id="source1" class="source1">{{ problem.source }}</span>
                <span id="count1" class="count1">{{ problem.aceptedcount }}/{{ problem.submitcount }}</span>
              </div>
            </div>
          </el-card>
        </a>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import {
    Search,
  } from '@element-plus/icons-vue'
  import axios from 'axios';
  import { useRouter } from 'vue-router'
  export default {
    setup(){
      const router = useRouter();
      const push_to_problemcontent =(problem) =>{
        router.push({
          path: '/problemdetail',
          query: {
            problemid: problem.problemid,
            title: problem.title,
            difficulty: problem.difficulty,
            algorithm: problem.algorithm,
            source: problem.source,
            aceptedcount: problem.aceptedcount,
            submitcount: problem.submitcount,
            description: problem.description,
            hint:problem.hint,
            timelimit: problem.timelimit,
            memorylimit: problem.memorylimit,
            background:problem.background,
            inputformat: problem.inputformat,
            outputformat: problem.outputformat,
          }
        })
      }
      return{
          Search,
          push_to_problemcontent
      }
    },
    data() {
      
     
      return {
        searchText: '',
        questions:[],
        question: {
          id:"0001",
          title: 'A+B',
          difficulty: '简单',
          algorithm: '模拟',
          source: '原创',
          submissionCount: '100',
          aceptedCount:'100',
        }
      };
    },
    created(){
      // var title = document.getElementById('title1').getboundingClientRect().right;
      // console.log(title);
      axios.get('http://localhost:8088/problem/query')
      .then(response => {
        console.log(response.data);
        this.questions = response.data;
      })
      .catch(error => {
        console.log(error);
      });

    },
    methods: {
      handleSearch() {
        // 在这里处理搜索逻辑
        console.log(this.searchText);
      },
      
      
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
  .card-row span.id1 {
   position: absolute;
   left:10px;
   bottom: 10px;
  }
  .card-row span.title1 {
   position: absolute;
   left:50px;
   bottom: 10px;
  }
  .card-row span.difficulty1 {
   position: absolute;
   left:600px;
   bottom: 10px;
  }
  .card-row span.knowledge1 {
    position: absolute;
   left:700px;
   bottom: 10px;
  }
  .card-row span.source1 {
    position: absolute;
   left:800px;
   bottom: 10px;
  }
  .card-row span.count1 {
    position: absolute;
   left:940px;
   bottom: 10px;
  }
  </style>

