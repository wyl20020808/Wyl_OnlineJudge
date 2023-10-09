<template>
  <a-row>
    <a-col :span="24">
      <pre><code style="font-size: 16px;background-color: rgb(255, 255, 255);" ref="code" class="cpp">{{ judgeinfo.judge.code }}</code></pre>
    </a-col>
  </a-row>
</template>

<script>
import hljs from "highlight.js";
import cpp from 'highlight.js/lib/languages/cpp';
import 'highlight.js/styles/vs.css';

export default {
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
};
</script>

<style scoped>
</style>