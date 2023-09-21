<template>
  <div>
  <a-row>
    <a-col >
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
