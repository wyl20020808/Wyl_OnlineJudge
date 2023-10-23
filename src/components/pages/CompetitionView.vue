<template>
  <a-row style="justify-content: center;align-items: center;">
  <a-row style="margin-top:30px;">
    <a-col :span="15" :offset="0" class="contest-list">
      <ContestListComponent />
    </a-col >
    <a-col :offset="6">
      <a-button @click="createContest" style="color: white;background-color:rgb(37, 187, 155);" type="primary">+ 创建比赛</a-button>
    </a-col>
  </a-row>
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
