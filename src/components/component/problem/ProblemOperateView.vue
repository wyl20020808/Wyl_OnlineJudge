<template>
    <div class="button-container">
      <el-button class="button">编辑题目</el-button>
      <el-button class="button">提交代码</el-button>
      <el-button class="button">题解</el-button>
      <el-button class="button">讨论</el-button>
      <el-button class="button">收藏</el-button>
      <el-button class="button">报告错误</el-button>
      <el-button class="button">下载数据</el-button>
    </div>
  </template>
  
  <style scoped>
  .button-container {
    display: flex;
    flex-direction: column;
    gap: 10px; /* 减小按钮之间的距离 */
    padding: 20px; /* 减小内边距 */
    background-color: #f5f5f5;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    width: 60%; /* 减小容器的宽度 */
    max-width: 300px; /* 限制容器的最大宽度 */
    margin: auto;
  }
  
  .button {
    width: 100%;
    margin-left: 0;
    background-color: #f5f5f5;
    border-color: #ddd;
    color: #333;
    transition: all 0.3s ease;
    font-size: 16px; /* 减小字体大小 */
    padding: 8px 0; /* 减小按钮大小 */
  }
  
  .button:hover {
    background-color: #ddd;
    color: #555;
  }
  </style>