<template>
  <div style="width: 800px;">
  <a-row>
    <a-col style="margin-left: 200px;margin-top: 30px;" :span="24">
        <a-typography-paragraph>
          
          <pre>{{ judgeinfo.judge.code }}</pre>
        </a-typography-paragraph>
    </a-col>
  </a-row>
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

<style scoped></style>
