<template>
  <a-row style="align-items: center; justify-content: center; margin-top: 10px">
    <a-col>
      <a-row style="align-items: center; justify-content: center;">
        <a-card style="width: 1100px; margin-bottom: 20px">
          <a-row style="display: flex; align-items: center">
            <a-col>
              <h5>排行榜：</h5>
            </a-col>
            <a-col>
              <a-radio-group v-model:value="ranks">
                <a-radio-button value="rating">Rating</a-radio-button>
                <a-radio-button value="contribute" disabled
                  >贡献</a-radio-button
                >
                <a-radio-button value="pass">通过数</a-radio-button>
              </a-radio-group>
            </a-col>
          </a-row>
        </a-card>
      </a-row>
      <a-row
        v-if="ranks === 'rating'"
        style="align-items: center; justify-content: center"
      >
        <a-row
          style="width: 100%; align-items: center; justify-content: center"
        >
          <a-table :dataSource="displayData" :pagination="false">
            <a-table-column
              width="200px"
              title="排名"
              dataIndex="rank"
              key="rank"
              align="center"
            >
              <template v-slot="{ record }">
                <div>
                  {{ record.rank }}
                </div>
              </template>
            </a-table-column>
            <a-table-column
              width="200px"
              title="用户"
              dataIndex="user"
              key="user"
              align="center"
            >
              <template v-slot="{ record }">
                <div
                  class="hoverable2"
                  @click="goToUser(record.userid)"
                  style="margin-left: 50px; display: flex; align-items: center"
                >
                  <img
                    style="width: 50px; height: 50px; border-radius: 50%"
                    :src="record.userpicture"
                    alt=""
                  />
                  <div style="margin-left: 10px">{{ record.username }}</div>
                </div>
              </template>
            </a-table-column>
            <a-table-column
              title="Rating"
              dataIndex="problemchar"
              key="problemchar"
              align="center"
              width="700px"
            >
              <template v-slot="{ record }">
                <div class="hoverable2">
                  {{ record.rating }}
                </div>
              </template>
            </a-table-column>
          </a-table>
        </a-row>
        <a-row>
          <el-pagination
            style="margin: 10px"
            background
            class="pagination-container"
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="prev, pager, next"
            :total="tableData.length"
          ></el-pagination>
        </a-row>
      </a-row>
      <a-row
        v-else-if="ranks === 'pass'"
        style="align-items: center; justify-content: center"
      >
        <a-row
          style="width: 100%; align-items: center; justify-content: center"
        >
          <a-table :dataSource="displayData" :pagination="false">
            <a-table-column
              width="200px"
              title="排名"
              dataIndex="rank"
              key="rank"
              align="center"
            >
              <template v-slot="{ record }">
                <div>
                  {{ record.rank }}
                </div>
              </template>
            </a-table-column>
            <a-table-column
              width="200px"
              title="用户"
              dataIndex="user"
              key="user"
              align="center"
            >
              <template v-slot="{ record }">
                <div
                  @click="goToUser(record.userid)"
                  class="hoverable2"
                  style="margin-left: 50px; display: flex; align-items: center"
                >
                  <img
                    style="width: 50px; height: 50px; border-radius: 50%"
                    :src="record.userpicture"
                    alt=""
                  />
                  <div style="margin-left: 10px">{{ record.username }}</div>
                </div>
              </template>
            </a-table-column>
            <a-table-column
              title="通过数"
              dataIndex="passedcount"
              key="passedcount"
              align="center"
              width="450px"
            >
              <template v-slot="{ record }">
                <div>
                  {{ record.aceptedcount }}
                </div>
              </template>
            </a-table-column>
            <a-table-column
              title="提交数"
              dataIndex="submitcount"
              key="submitcount"
              align="center"
              width="250px"
            >
              <template v-slot="{ record }">
                <div>
                  {{ record.submitcount }}
                </div>
              </template>
            </a-table-column>
          </a-table>
        </a-row>
        <a-row
          ><el-pagination
            style="margin: 10px"
            background
            class="pagination-container"
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="prev, pager, next"
            :total="tableData.length"
          ></el-pagination
        ></a-row>
      </a-row>
    </a-col>
  </a-row>
</template>

<script>
import axios from "axios";
import { SERVER_URL } from "../../js/functions/config.js";
import router from "@/router/router";
export default {
  data() {
    return {
      tableData: [],
      ranks: "rating",
      currentPage: 1,
      pageSize: 10,
      displayData: [],
    };
  },
  methods: {
    updateDisplayData(data) {
      let startIndex = (this.currentPage - 1) * this.pageSize;
      let endIndex = Math.min(
        startIndex + this.pageSize,
        this.tableData.length
      );
      // 从tableData中获取相应的元素
      this.displayData = data.slice(startIndex, endIndex);
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
      this.updateDisplayData(this.tableData);
    },
    goToUser(userid) {
      router.push({
        name: "userhome",
        query: {
          userid: userid,
        },
      });
    },
  },
  watch: {
    async ranks(newVal, oldVal) {
      if (newVal === "pass") {
        this.tableData.sort((a, b) => {
          return b.submitcount - a.submitcount;
        });
        for (let i = 0; i < this.tableData.length; i++) {
          this.tableData[i].rank = i + 1;
        }
      } else if (newVal === "rating") {
        this.tableData.sort((a, b) => {
          return b.rating - a.rating;
        });
        for (let i = 0; i < this.tableData.length; i++) {
          this.tableData[i].rank = i + 1;
        }
      }
      this.currentPage = 1;
      this.updateDisplayData(this.tableData);
    },
  },
  async created() {
    await axios
      .get(`${SERVER_URL}/user/query/all`)
      .then((response) => {
        console.log(response.data);
        let data = response.data;
        for (let i = 0; i < data.length; i++) {
          this.tableData.push({
            username: data[i].userinfo.username,
            userpicture: data[i].userinfo.userpicture,
            userid: data[i].userinfo.userid,
            rating: data[i].userextra.rating,
            submitcount: data[i].userextra.submitcount,
            aceptedcount: data[i].userextra.aceptedcount,
          });
        }
        this.tableData.sort((a, b) => {
          return a.rating - b.rating;
        });
        for (let i = 0; i < this.tableData.length; i++) {
          this.tableData[i].rank = i + 1;
        }
        this.updateDisplayData(this.tableData);
      })
      .catch((error) => {
        console.log(error);
      });
  },
};
</script>

<style scoped>
.hoverable2 {
  color: #3498db;
  transition: color 0.3s ease;
  cursor: pointer;
}

.hoverable2:hover {
  filter: brightness(1.3);
}
</style>
