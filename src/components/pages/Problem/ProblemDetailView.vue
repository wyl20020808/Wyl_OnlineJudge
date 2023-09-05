<template>

  <ProblemCardView :problem="problemcontent" />
  <div class="problem-operator">
    <ProblemOperateView :problem="problemcontent"/>
  </div>
  <div class="problem-content">
    <PrombleContentView :problem="problemcontent"/> 
  </div>

</template>
<script>
import {SERVER_URL} from "../../../js/functions/config"
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
    problemcontent: {}
  });
  axios.get(`${SERVER_URL}/problem/query/${state.problemid}`)//通过id查询题目的数据
    .then(response => {
      // console.log(response.data.title)
      state.problemcontent = response.data;
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