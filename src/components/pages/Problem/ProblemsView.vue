<template>

    
    <a-row  style="display: flex; align-items: center;margin-left: 200px; margin-top: 20px;margin-bottom: 20px;">
      <a-col >
      <h5>所属题库：</h5>
      </a-col>
      <a-col>
      <a-radio-group v-model:value="questionBank">
        <a-radio-button value="jxust">江理题库</a-radio-button>
        <a-radio-button value="loj" >LOJ</a-radio-button>
        <a-radio-button value="luogu" disabled>洛谷</a-radio-button>
        <a-radio-button value="codeforces" disabled>CodeForces</a-radio-button>
      </a-radio-group>
    </a-col>
    
    </a-row>
    <a-row>
      <a-col style="z-index: 1;" :span="3"><ProblemCreateComponent /></a-col>
      <a-col style="margin-left: 10px;"> <div style="margin-bottom:20px ;"><ProblemListViewVue :questionBank="questionBank" /></div></a-col>
    </a-row>
    
    
   
    
 
</template>

<script>
import ProblemCreateComponent from "@/components/component/problem/ProblemCreateComponent.vue";
import ProblemListViewVue from "@/components/component/problem/ProblemListView.vue";
export default {
  components: {
    ProblemCreateComponent,
    ProblemListViewVue,
  },
  data() {
    return {
      questionBank: "loj",
    };
  },
};
</script>

<style></style>
