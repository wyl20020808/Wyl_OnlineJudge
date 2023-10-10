<template>
  <a-row>
    <a-col :span="24">
      <div class="code-container">
        <pre><code style="font-size: 16px;background-color: rgb(255, 255, 255);" ref="code" class="cpp">{{ judgeinfo.judge.code }}</code></pre>
   
          <a-button size="small" :style="copyButtonStyle" class="copy-button" @click="copyCode">{{ copyMessage }}</a-button>
       
      </div>
    </a-col>
  </a-row>
</template>

<script>
import hljs from "highlight.js";
import cpp from 'highlight.js/lib/languages/cpp';
import 'highlight.js/styles/vs.css';

export default {
  data() {
    return {
      copyMessage: '复制',
      copyButtonStyle: {
        color: '#5f9fd6',
      },
    };
  },
  props: {
    judgeinfo: {
      type: Object,
      required: true,
    },
  },
  mounted() {
    //在挂载的时候高亮
    hljs.registerLanguage('cpp', cpp);
    hljs.highlightBlock(this.$refs.code);
  },
  methods: {
    copyCode() {
      const textarea = document.createElement('textarea');
      textarea.textContent = this.judgeinfo.judge.code;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('copy');
      document.body.removeChild(textarea);
      this.copyMessage = '复制成功';
      this.copyButtonStyle = {
        color: 'white',
        backgroundColor: 'rgb(52,152,219)',
      };
      setTimeout(() => {
        this.copyMessage = '复制';
        this.copyButtonStyle = {
          color: '#5f9fd6',
        };
      }, 1000);
    },
  },
};
</script>

<style scoped>
.code-container {
  position: relative;
}

.copy-button {
  position: absolute;
  top: 10px;
  right: 10px;
}
</style>