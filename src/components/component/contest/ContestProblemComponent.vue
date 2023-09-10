<template>
  <a-table :dataSource="datas" :pagination="false">
    <a-table-column
      title="题号"
      dataIndex="id"
      key="id"
      width="100px"
      align="center"
    />
    <a-table-column
      title="题目名称"
      dataIndex="problemname"
      key="problemname"
      width="600px"
      align="center"
    >
      <template v-slot="{ record }">
        <div class="hoverable" @click="viewProblem(record.problemid)">
          {{ record.problemname }}
        </div>
      </template>
    </a-table-column>
    <a-table-column
      title="通过率"
      dataIndex="passrate"
      key="passrate"
      align="center"
    />
    <a-table-column
      title="我的状态"
      dataIndex="state"
      key="state"
      align="center"
    />
  </a-table>
</template>

<script>
import { defineComponent } from "vue";
import { Table } from "ant-design-vue";
import router from "@/router/router";
export default defineComponent({
  components: {
    "a-table": Table,
    "a-table-column": Table.Column,
  },
  props: {
    contest: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      datas: [],
      
    };
  },
  methods:{
    viewProblem(problemid){
      router.push({path: '/contest/problem',query:{problemid,contestid:this.contest.contestcontent.contestid}})
    }
  },
  watch: {
    contest(newVal, oldVal) {
      let problems = newVal.contestproblem;
      for (let i = 0; i < problems.length; i++) {
        this.datas.push({
          id: String.fromCharCode("A".charCodeAt(0) + i),
          problemname: problems[i].problemname,
          problemid: problems[i].problemid,
          passrate: problems[i].acceptedcount + "/" + problems[i].submitcount,
          state: "未通过",
        });
      }
    },
  },
});
</script>

<style scoped>
.hoverable {
  color: #3498db;
  transition: color 0.3s ease, text-decoration 0.3s ease;
  cursor: pointer;
}

.hoverable:hover {
  filter: brightness(1.3);
  text-decoration: underline;
}
</style>
