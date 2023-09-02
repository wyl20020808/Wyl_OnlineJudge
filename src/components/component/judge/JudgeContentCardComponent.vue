<template>
  <div :style="{ color: statusColor }"></div>
</template>

<script>
export default {
  data() {
    return {
      status: "Accepted", // 这个值可能会改变
    };
  },
  computed: {
    statusColor() {
      return this.status === "active" ? "green" : "red";
    },
  },
};
</script>

<style scoped></style>
