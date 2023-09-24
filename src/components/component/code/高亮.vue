<template>
    <!-- 语言选择器 -->
    <a-select
      v-model:value="selectedLanguage"
      style="width: 120px"
      :options="language"
    ></a-select>
    <!-- 其他语言 -->
  
    <!-- 代码区域 -->
    <pre :class="'language-' + selectedLanguage">
      <code ref="codeArea" v-html="highlightedCode"></code>
      </pre>
    <a-textarea
      v-model:value="code"
      :auto-size="{ minRows: 10, maxRows: 20 }"
    ></a-textarea>
  </template>
  
  <script setup>
  import { ref, watch } from "vue";
  import { onMounted } from "vue";
  import Prism from "prismjs";
  import "prismjs/components/prism-c";
  import "prismjs/components/prism-cpp";
  import "prismjs/components/prism-javascript";
  import "prismjs/components/prism-java";
  import "prismjs/components/prism-python";
  
  const selectedLanguage = ref("javascript");
  const code = ref("const a = 1;");
  const codeArea = ref(null);
  const highlightedCode = ref("");
  
  watch(
    [selectedLanguage, code],
    () => {
      console.log(code.value);
      // 重新高亮代码
      highlightedCode.value = Prism.highlight(
        code.value,
        Prism.languages[selectedLanguage.value],
        selectedLanguage.value
      );
    },
    { immediate: true }
  );
  
  const language = ref([
    {
      label: "javascript",
      value: "javascript",
    },
    {
      label: "cpp",
      value: "cpp",
    },
    {
      label: "c",
      value: "c",
    },
    {
      label: "java",
      value: "java",
    },
    {
      label: "python",
      value: "python",
    },
  ]);
  
  onMounted(() => {
    // 确保 codeArea 引用已经被设置
    Prism.highlightElement(codeArea.value);
  });
  </script>
  
  <style scoped>
  @import "prismjs/themes/prism.css";
  </style>
  