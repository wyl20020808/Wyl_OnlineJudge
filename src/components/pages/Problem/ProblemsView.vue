<template>
  <a-row style="margin-left: 170px; margin-top: 20px">
    <a-col>
      <a-card style="width: 1175px; margin-bottom: 20px">
        <a-row style="display: flex; align-items: center; margin-bottom: 20px">
          <a-col>
            <h5>所属题库：</h5>
          </a-col>
          <a-col>
            <a-radio-group v-model:value="questionBank">
              <a-radio-button value="jxust">江理题库</a-radio-button>
              <a-radio-button value="loj">LOJ</a-radio-button>
              <a-radio-button value="luogu" disabled>洛谷</a-radio-button>
              <a-radio-button value="codeforces" disabled
                >CodeForces</a-radio-button
              >
            </a-radio-group>
          </a-col>
          <a-col style="margin-left: 570px">
            <el-button
              @click="validateLogon"
              class=""
              style="width: 150px; height: 30px; margin-left: 0px"
              ><el-icon><Plus /></el-icon> &ensp; 新建题目</el-button
            >
          </a-col>
        </a-row>
        <a-row style="display: flex; align-items: center; margin-bottom: 0px">
          <a-col>
            <h5>难度：</h5>
          </a-col>
          <a-col style="margin-left: 40px"
            ><a-select
              ref="select"
              v-model:value="difficulty"
              style="width: 200px"
              :options="difficultys"
              @focus="focus"
              placeholder="难度"
            ></a-select
          ></a-col>

          <a-col style="margin-left: 20px">
            <h5>算法：</h5>
          </a-col>
          <a-col>
            <a-select
              v-model:value="algorithm"
              mode="multiple"
              show-search
              placeholder="算法"
              style="width: 200px"
              :options="algorithmsAndDataStructuresOptions"
              :filter-option="filterOption"
              @focus="handleFocus"
              @blur="handleBlur"
              @change="handleChange"
            ></a-select>
          </a-col>
        </a-row>
      </a-card>
      <a-row>
        <a-col style="">
          <div style="margin-bottom: 20px">
            <ProblemListViewVue :questionBank="questionBank" /></div
        ></a-col>
      </a-row>
    </a-col>
  </a-row>

  <el-dialog
    v-model="centerDialogVisible"
    title="用户未登录"
    width="30%"
    center
  >
    <span> 新建题目功能必须登录后才能使用 </span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取消</el-button>
        <el-button style="color: white" type="primary" @click="toLogin">
          登录
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import router from "@/router/router";
import { Plus } from "@element-plus/icons-vue";

import ProblemListViewVue from "@/components/component/problem/ProblemListView.vue";
export default {
  components: {
    ProblemListViewVue,
    Plus,
  },
  data() {
    const difficultys = [
      {
        value: "无",
        label: "无",
      },
      {
        value: "暂无评定",
        label: "暂无评定",
      },
      {
        value: "入门",
        label: "入门",
      },
      {
        value: "简单",
        label: "简单",
      },
      {
        value: "还行",
        label: "还行",
      },
      {
        value: "一般",
        label: "一般",
      },
      {
        value: "小难",
        label: "小难",
      },
      {
        value: "好难",
        label: "好难",
      },
      {
        value: "骚难",
        label: "骚难",
      },
    ];
    const {
      algorithmsAndDataStructuresOptions,
    } = require("../../../js/values/value");
    return {
      questionBank: "loj",
      centerDialogVisible: false,
      difficultys,
      difficulty: "无",
      algorithm: [],
      algorithmsAndDataStructuresOptions,
    };
  },
  methods: {
    validateLogon() {
      if (JSON.parse(localStorage.getItem("user")).userloginstate !== "true") {
        this.centerDialogVisible = true;
        return;
      }
      router.push({ name: "createproblem" });
    },
    toLogin() {
      this.centerDialogVisible = false;
      router.push({ name: "userlogin" });
    },
  },
};
</script>

<style></style>
