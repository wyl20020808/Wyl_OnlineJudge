<template>
  <div class="card1">
    <!-- 这里为了布局所以用了div 2023年10月12日14:57:22 -->
    <div class="text">
      <a-row style="display: flex; align-items: center">
        <a-col style="color: #4183c4; font-size: 16px">
          {{ discuss.username }}
        </a-col>
        <a-col style="margin-left: 10px; color: gray; font-size: 16px">
          发表于：{{ discuss.createtime }}
        </a-col>
        <a-col
          v-if="discuss.createtime !== discuss.edittime"
          style="margin-left: 10px; color: gray; font-size: 16px"
        >
          更新于：{{ discuss.edittime }}
        </a-col>
      </a-row>
    </div>
  </div>
  <div
    style="
      z-index: 1000;
      position: relative;
      bottom: 3px;
      border: 1px solid #c6c5c5;
      border-bottom: 1px solid white;
      width: 90%;
      height: 10px;
      background-color: white;
    "
  ></div>
  <div class="card2">
    <a-row>
      <a-col>
        <div
          v-html="parsedDescription(formattedText(discuss.content))"
        ></div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup>
import MarkdownIt from "markdown-it";
import "markdown-it-texmath/css/texmath.css";
import mk from "markdown-it-katex";
import "katex/dist/katex.min.css";
import { defineProps } from "vue";
import { SearchOutlined, PlusOutlined } from "@ant-design/icons-vue";
import router from "@/router/router";
const md = new MarkdownIt({ html: true }).use(mk)
const props = defineProps({
  discuss: Object,
});
function jump(total) {
  router.push({ path: "/" + total });
}
function parsedDescription(content) {
  return md.render(String(content));
}
function formattedText(text) {
  return text;
  if (typeof text !== "string") {
    text = String(text);
  }
  console.log(text.replace(/\n/g, "<br>"));
  return text.replace(/\n/g, "<br>"); //为了体现间距，更加美观
}
</script>

<style scoped>
.card1 {
  display: flex;
  align-items: center;
  position: relative;
  width: 90%;
  height: 40px;
  background-color: rgb(238, 238, 238);
  border-radius: 5px;
  border: 1px solid #c6c5c5;
  box-sizing: border-box;
}
.card2 {
  position: relative;
  bottom: 7px;
  display: flex;
  align-items: center;
  position: relative;
  width: 90%;
  padding: 10px;
  border-radius: 5px;
  background-color: rgb(255, 255, 255);
  border-left: 1px solid #c6c5c5;
  border-right: 1px solid #c6c5c5;
  border-bottom: 1px solid #c6c5c5;
  box-sizing: border-box;
}

.card1::before {
  content: "";
  position: absolute;
  top: 13px;
  left: -6px;
  width: 10px;
  height: 10px;
  border-left: 1px solid #c6c5c5;
  border-bottom: 1px solid #c6c5c5;
  transform: rotate(45deg);
  background-color: rgb(238, 238, 238);
}

.text {
  padding: 0 10px;
}
</style>
