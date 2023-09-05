<template>
  <div class="card card1">
    <div class="code">{{ judgeinfo.judge.code }}</div>
  </div>
</template>

<script>
export default {
  props: {
    judgeinfo: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.card1 {
  width: 800px;
  position: relative;
  min-height: 500px;
  height: auto;
  left: 200px;
  top: 50px;
}
.code{
    /* left:20px;
    position: relative;
    top:20px; */
}
</style>
