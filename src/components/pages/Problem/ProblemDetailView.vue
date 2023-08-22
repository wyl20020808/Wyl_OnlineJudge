<template>

  <ProblemCardView :problem="problem" />
  <div class="problem-operator">
    <ProblemOperateView :problem="problem"/>
  </div>
  <div class="problem-content">
    <PrombleContentView :problem="problem"/> 
  </div>

</template>
<script>
import { reactive,toRefs} from 'vue';
import { useRoute } from 'vue-router';
import ProblemCardView from '../../component/problem/ProblemCardView.vue';
import ProblemOperateView from '../../component/problem/ProblemOperateView.vue';
import PrombleContentView from '../../component/problem/PrombleContentView.vue';
import axios from 'axios';
export default {
  name:"problemdetail",
  components:{
    ProblemCardView,
    ProblemOperateView,
    PrombleContentView
  },
  setup() {
  const route = useRoute();
  const state = reactive({
    problemid: route.query.problemid,
    problem: {}
  });
  axios.get(`http://localhost:8088/problem/query/${state.problemid}`)
    .then(response => {
      state.problem = response.data;
      console.log(state.problem.pro,"weyuw");
    })
    .catch(error => {
      console.log(error);
    });
  return {
    ...toRefs(state)
  };
},
  methods:{
    
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