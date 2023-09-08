<template>
  <a-table :dataSource="datas" :columns="columns" :pagination="false" />
</template>

<script>
export default {
  props: {
    contest: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      datas: [],
      columns: [
        {
          title: "题号",
          dataIndex: "id",
          key: "id",
          width:"100px",
          align: 'center',  // 设置列中的元素居中
        },
        {
          title: "题目名称",
          dataIndex: "problemname",
          key: "problemname",
          width:"600px",
          align: 'center',  // 设置列中的元素居中
        },
        {
          title: "通过率",
          dataIndex: "passrate",
          key: "passrate",
          align: 'center',  // 设置列中的元素居中
        },
        {
          title: "我的状态",
          dataIndex: "state",
          key: "state",
          align: 'center',  // 设置列中的元素居中
        },
      ],
    };
  },
  watch:{
    contest(newVal, oldVal) {
      // 当contest的值改变时，这个函数会被调用
      // newVal是新的值，oldVal是旧的值
      let problems = newVal.contestproblem;
      for (let i = 0; i < problems.length; i++) {
      this.datas.push({
        id: String.fromCharCode("A".charCodeAt(0) + i),
        problemname: problems[i].problemname,
        passrate:
          problems[i].acceptedcount + "/" + problems[i].submitcount,
        state: "未通过",
      });
    }
    },
  },
  created() {

  },
};
</script>

<style scoped></style>
