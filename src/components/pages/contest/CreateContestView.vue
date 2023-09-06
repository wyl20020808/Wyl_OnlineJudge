<template>
  <div class="card main1">
    <a-row style="margin-top: 20px">
      <a-col :offset="1" :span="24">
        <h2>比赛信息</h2>
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col class="input-container" :offset="1" :span="6">
        <label for="name">赛制：</label>
        <a-select
          ref="select"
          v-model:value="contestformat"
          style="width: 200px"
          :options="contestFormat"
          @focus="focus"
        ></a-select>
      </a-col>
      <a-col class="input-container" :span="14">
        <label for="name">标题：</label>
        <a-textarea
          v-model:value="title"
          placeholder="输入比赛的标题"
          auto-size
        />
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col class="input-container" :offset="1">
        <label for="name">比赛时间：</label>
        <a-space direction="vertical" :size="20">
          <a-range-picker
            style="width: 500px"
            v-model:value="contestdate"
            show-time
          />
        </a-space>
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col class="input-container" :offset="1">
        <label for="name">比赛题目：</label>
        <a-select
          v-model:value="problems"
          show-search
          mode="multiple"
          placeholder="输入题目名称、ID进行搜索"
          style="width: 700px"
          :default-active-first-option="false"
          :show-arrow="false"
          :filter-option="false"
          :not-found-content="null"
          :options="data"
          @search="handleSearch"
          @change="handleChange"
        ></a-select>
      </a-col>
    </a-row>

    <a-row style="margin-top: 20px">
      <a-col class="input-container" :offset="1" :span="20">
        <label for="name">比赛说明：</label>
        <a-textarea
          v-model:value="title"
          placeholder="输入比赛说明"
          :auto-size="{ minRows: 10, maxRows: 20 }"
          show-count
          :maxlength="114514"
        />
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col :offset="1" :span="24">
        <h2>比赛权限</h2>
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col class="input-container" :offset="1">
        <label for="name">管理员：</label>
        <a-select
          v-model:value="problems"
          show-search
          mode="multiple"
          placeholder="输入管理员名称、ID进行搜索"
          style="width: 700px"
          :default-active-first-option="false"
          :show-arrow="false"
          :filter-option="false"
          :not-found-content="null"
          :options="data"
          @search="handleSearch"
          @change="handleChange"
        ></a-select>
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col class="input-container" :offset="1" :span="6">
        <label for="name">权限：</label>
        <a-select
          ref="select"
          v-model:value="contestlimit"
          style="width: 200px"
          :options="[
            {
              value: '公开',
              label: '公开',
            },
            {
              value: '私密(需要密码)',
              label: '私密(需要密码)',
            },
          ]"
          @focus="focus"
        ></a-select>
      </a-col>
      <a-col
        v-if="contestlimit === '私密(需要密码)'"
        class="input-container"
        :span="10"
      >
        <label for="name">密码：</label>
        <a-textarea
          v-model:value="title"
          placeholder="输入比赛的密码"
          auto-size
        />
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col :offset="1" :span="24">
        <h2>比赛设置</h2>
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px">
      <a-col :offset="1" :span="2">
        <label for="name">Rated</label>
        <a-switch v-model:checked="rated" />
      </a-col>
      <a-col :offset="1" :span="2">
        <label for="name">赛时封榜</label>
        <a-switch v-model:checked="blockedlist" />
      </a-col>
    </a-row>
    <a-row style="margin-top: 20px;color:white;">
      <a-col :offset="1" :span="2">
        <a-button type="primary">确认创建</a-button>
      </a-col>
      <a-col style="color:black;" :offset="1" :span="2">
        <a-button>取消</a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import jsonp from "fetch-jsonp";
import qs from "qs";
export default {
  methods: {
    fetch: function (value, callback) {
      if (this.timeout) {
        clearTimeout(this.timeout);
        this.timeout = null;
      }
      this.currentValue = value;
      const fake = () => {
        const str = qs.stringify({
          code: "utf-8",
          q: value,
        });
        jsonp(`https://suggest.taobao.com/sug?${str}`)
          .then((response) => response.json())
          .then((d) => {
            if (this.currentValue === value) {
              const result = d.result;
              const data = [];
              result.forEach((r) => {
                data.push({
                  value: r[0],
                  label: r[0],
                });
              });
              callback(data);
            }
          });
      };
      this.timeout = setTimeout(fake, 300);
    },
  },
  data() {
    return {
      title: "",
      contestFormat: [
        { value: "ACM/ICPC", label: "ACM/ICPC" },
        { value: "IOI", label: "IOI", disabled: true },
        { value: "OI", label: "OI", disabled: true },
      ],
      focus: () => {
        console.log("focus");
      },
      handleChange: (val) => {
        console.log(val);
        this.problems = val;
        this.handleSearch("");
      },

      handleSearch: (val) => {
        this.fetch(val, (d) => (this.data = d));
      },
      contestdate: "",
      contestformat: "",
      data: [],
      problems: [],
      timeout: null,
      currentValue: "",
      contestlimit: "公开",
      rated: false,
      blockedlist: false,
    };
  },
};
</script>

<style scoped>
.main1 {
  background-color: white;
  /* border: black; */
  width: 1000px;
  position: relative;
  left: 200px;
  top: 30px;
}

.input-container {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 5px; /* 调整这个值可以改变文字和输入框之间的距离 */
  font-size: 18px;
  color: rgb(96, 95, 95);
}
</style>
