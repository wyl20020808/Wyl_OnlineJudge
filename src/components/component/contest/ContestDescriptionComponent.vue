<template>
      <!-- <a-divider style="border-color: gray" orientation="left" dashed >温馨提示</a-divider> -->
      
      <!-- <blockquote> 【比赛规范】
为保证比赛公平公正，营造健康积极的竞赛环境，我们采取以下措施：
1.赛后对代码系统查重+人工查重，不同账号相似度100%的提交直接算作弊处理，不区分提交先后。某些简单的签到题视情况处理。相似度80%以上进行人工核对。赛后提交代码全部公开，接受用户投诉。（相似度非文本比较）
2.同一用户使用多账号提交相同代码也按违规处理。
3.比赛期间在互联网公开寻求题目答案按违规处理，接受用户举报。
4.比赛结束前公开答案按违规处理
5.其它影响到比赛公平的行为按违规处理
对于违规用户：
第一次违规：取消本场比赛获得奖品的资格，扣除rating200分，公开违规用户名单在比赛说明页面。
第二次违规：封停账号</blockquote> -->
      <div style="background-color: rgb(238, 238, 238);border-radius: 5px;padding: 15px;" class="my-pre"> 
            {{contest.contestcontent? contest.contestcontent.description:'' }}
      </div>
   
      <!-- <a-divider type="vertical" style="height: 80px; background-color: rgb(175, 174, 174)"  /> -->
</template>
<script setup>
import axios from "axios";
import { SERVER_URL } from "@/js/functions/config";
import { onMounted } from "vue";
import { ref } from "vue";
import { useRoute } from "vue-router";
let route = useRoute();
let contest = ref({});
onMounted(async () => {
      await axios
        .get(`${SERVER_URL}/contest/query/contest`, {
          params: {
            contestid: route.query.contestid,
          },
        })
        .then((res) => {
          contest.value = res.data;;
        });
})

</script>

<style scoped>

.my-pre {
white-space: pre-wrap; /* 设置文本自动换行 */
}
</style>
