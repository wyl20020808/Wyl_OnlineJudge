<template>
  <a-row style="position: relative;top:50px;">
    <a-col :span="5" :offset="19" class="button1">
      <a-button @click="createContest" style="color: white" type="primary">+ 创建比赛</a-button>
    </a-col>
  </a-row>
  
</template>

<script>
import router from '@/router/router';
export default {
  name: "Competition",
  components: {},

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
