<template>
    <div>
      <el-card class="outer-card" v-if="problem">
        <el-card class="inner-card"><strong>题目背景</strong>
          <div class="content" v-html="parsedDescription(problem.problemid)">
         
        </div>
        </el-card>
        <el-card class="inner-card"><strong>题目描述</strong>
            <div class="content" v-html="parsedDescription(problem.description)">
         
            </div>
        </el-card>
        <el-card class="inner-card"><strong>输入格式</strong>
          <div class="content" v-html="parsedDescription(problem.inputformat)">
         
        </div>
        </el-card>
        <el-card class="inner-card"><strong>输出格式</strong>
          <div class="content" v-html="parsedDescription(problem.outputformat)">
         
        </div>
        </el-card>
        <div class="example-cards" v-for="input in problemSample" :key="input.id" >
          <el-card class="inner-card"  >
            <strong>输入样例</strong>
            <el-button class="copy-button" @click="copyToClipboard('inputExample')">复制</el-button>
            <div id="inputExample" ref="inputExample">
              <div>{{input.input}}</div>
            </div>
          </el-card>
          <el-card class="inner-card" >
            <strong>输出样例</strong>
            <el-button class="copy-button" @click="copyToClipboard('outputExample')">复制</el-button>
            <div id="outputExample" ref="outputExample">
              <div>{{input.output}}</div>
            </div>
          </el-card>
        </div>
        <el-card class="inner-card"><strong>数据范围</strong>
          <div class="content" v-html="parsedDescription(problem.hint)">
         
        </div>
        </el-card>
      </el-card>
    </div>
  </template>
  
  <script>
 import MarkdownIt from "markdown-it";
  import {watchEffect,toRefs,reactive} from 'vue';
 import 'markdown-it-texmath/css/texmath.css';
  import mk from 'markdown-it-katex';
  import 'katex/dist/katex.min.css';
  import axios from "axios";
  export default {
    name:"temp",
    props:{/*从父节点接受参数 */
        problem:{
            type:Object,
            required:true,
        },
    },
    setup(props) {
      const state = reactive({
        problemid: null,
        problemSample: {}
      })

      watchEffect(() => {
        state.problemid = props.problem.problemid
        if (state.problemid) {
          axios.get(`http://localhost:8088/problem/query/problemsample/${state.problemid}`)
            .then(response => {
              state.problemSample = response.data
            })
            .catch(error => {
              console.log(error)
            })
        }
      })

      return {
        ...toRefs(state)
      }
    },
    data(){
      return {
        // md: new MarkdownIt({html:true}).use(tm, { engine: require('katex'), delimiters:'dollars' })
        md: new MarkdownIt({html:true}).use(mk),
      }
    },
    computed:{
      
    },
    methods: {
      parsedDescription(content) {
        return this.md.render(String(content));
      },
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

  