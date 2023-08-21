<template>
    <el-card class="card" v-if="problem">
      <div class="main-info">
        <span class="id">P1111</span>
        <span class="title">曾神</span>
      </div>
      <div class="sub-info">
        <p class="property">来源：123123</p>
        <p class="property">时间限制：1000ms</p>
        <p class="property">空间限制：128MB</p>
        <p class="property">提交数量：100</p>
        <p class="property">通过数量：27</p>
        <p class="property">难度：简单</p>
        <p class="property">算法标签：模拟</p>
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