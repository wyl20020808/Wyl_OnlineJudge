<template>
  <a-row style="position: relative;top:10px;">
    <a-col :span="16" :offset="3" class="contest-list">
      <ContestListComponent />
    </a-col>
    <a-col :span="5" class="button1">
      <a-button @click="createContest" style="color: white;background-color:rgb(37, 187, 155);" type="primary">+ 创建比赛</a-button>
    </a-col>
  </a-row>
  
</template>

<script>
import router from '@/router/router';

import ContestListComponent from '../component/contest/ContestListComponent.vue';
export default {
  name: "Competition",
  components: {
    ContestListComponent,
  },

  methods:{ 
    createContest(){
      router.push({
        path: '/contest/create',
      })
    }
  }
};
</script>

<style scoped></style>
