<template>
    <div>
      <el-card class="outer-card" v-if="problem">
        <el-card class="inner-card"><strong>题目背景</strong></el-card>
        <el-card class="inner-card"><strong>题目描述</strong>
            <div class="content">
                {{problem.description }}
            </div>
        </el-card>
        <el-card class="inner-card"><strong>{{problem.hint }}</strong></el-card>
        <el-card class="inner-card"><strong>输出格式</strong></el-card>
        <div class="example-cards">
          <el-card class="inner-card">
            <strong>输入样例</strong>
            <el-button class="copy-button" @click="copyToClipboard('inputExample')">复制</el-button>
            <div id="inputExample" ref="inputExample"></div>
          </el-card>
          <el-card class="inner-card">
            <strong>输出样例</strong>
            <el-button class="copy-button" @click="copyToClipboard('outputExample')">复制</el-button>
            <div id="outputExample" ref="outputExample">这里是输出样例</div>
          </el-card>
        </div>
        <el-card class="inner-card"><strong>数据范围</strong></el-card>
      </el-card>
    </div>
  </template>
  
  <script>
  export default {
    name:"temp",
    props:{/*从父节点接受参数 */
        problem:{
            type:Object,
            required:true,
        },
    },
    setup(){
  
    },
    methods: {
      copyToClipboard(refName) {
        const textToCopy = this.$refs[refName].innerText;
        navigator.clipboard.writeText(textToCopy).then(() => {
          this.$message({
            message: '复制成功',
            type: 'success',
            duration: 2000
          });
        }, () => {
          this.$message({
            message: '复制失败',
            type: 'error'
          });
        });
      }
    }
  }
  </script>
  
  <style scoped>
  .outer-card {
    margin: 20px;
    background-color: #f5f5f5;
    width: 60%;
    min-height: 500px;
  }
  
  .inner-card {
    margin: 10px 0;
    width: 100%;
    min-height: 50px;
    position: relative;
  }
  
  .example-cards {
    display: flex;
    justify-content: space-between;
  }
  
  .copy-button {
    position: absolute;
    top: 0;
    right: 0;
  }
  </style>