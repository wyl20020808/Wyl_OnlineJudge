<template>

    <ProblemCardView />
    <div class="problem-operator">
      <ProblemOperateView :problem="problem"/>
    </div>
    <div class="problem-content">
     <PrombleContentView v-bind:problem="problem"/> 
    </div>
    
  </template>
  
  <script>
import { reactive } from 'vue';
  import { useRoute } from 'vue-router';
  import ProblemCardView from './ProblemCardView.vue';
  import ProblemOperateView from './ProblemOperateView.vue';
  import PrombleContentView from './PrombleContentView.vue';

  export default {
    name:"problemdetail",
    components:{
      ProblemCardView,
      ProblemOperateView,
      PrombleContentView
    },
    setup(){
      const route = useRoute();
      const problemId = route.query;
      const problem = reactive(problemId);
      console.log(problem);
      return {
        problemId,
        problem
      }

    }

  }
  </script>
  
  <style scoped>
  .problem-operator{
    width: 500px;
    position: absolute;
    top:80px;
    left:950px
  }
  .problem-content{
    width: 100%;
    position: relative;
    left: 80px;
  }
  </style>