<template>
    <el-card class="card" v-if="problem">
      <div class="main-info">
        <span class="id">P{{problem.problemid}}</span>
        <span class="title">{{problem.title}}</span>
      </div>
      <div class="sub-info">
        <p class="property">来源：{{problem.source}}</p>
        <p class="property">时间限制：{{ problem.timelimit }}ms</p>
        <p class="property">空间限制：{{problem.memorylimit}}MB</p>
        <p class="property">提交数量：{{problem.submitcount}}</p>
        <p class="property">通过数量：{{problem.aceptedcount}}</p>
        <p class="property">难度：{{problem.difficulty}}</p>
        <p class="property">算法标签：{{problem.algorithm}}</p>
      </div>
    </el-card>
  </template>
  
  <script > 
  export default {
    name:"problemcard",
    props:{
        problem:{
            type:Object,
            required:true,
        },
    },
  }

 
  </script>
  
  <style scoped>
  .card {
    left: 80px;
    width:60%;
    margin: 20px;
    background-color: #f5f5f5;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  }
  
  .main-info {
    font-size: 20px;
    margin-bottom: 10px;
    display: flex;
    justify-content: flex-start;
    padding: 10px;
    border-radius: 10px;
  }
  
  .main-info span.title{
    margin-left: 10px;
    position: relative;
    bottom: 3px;
  }
  
  .sub-info {
    font-size: 14px;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    padding: 10px;
  }
  
  .property {
    position: relative;
    bottom: 10px;
    left:-8px;
    background-color: #e0e0e0;
    padding: 5px;
    border-radius: 5px;
    margin: 5px 5px 10px 5px;
  }
  </style>