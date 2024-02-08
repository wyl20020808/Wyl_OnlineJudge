<template>
  <div style="width: 100%; margin-top: 20px;">
   
      <a-row style="align-items: center;justify-content: center;">
        <a-col :span="23">
          <el-carousel :interval="4000" type="card" height="250px">
            <el-carousel-item v-for="item in images" :key="item">
              <img :src="item" alt="image" style="width: 100%; height: 100%" />
            </el-carousel-item>
          </el-carousel>
        </a-col>
      </a-row>
      <a-row style="padding: 2%;width: 50%;">
        <NoticeVue />
      </a-row>
     <!-- <a-row>
      <a-button @click="tiren(17)">
        踢人下线！
      </a-button>
     </a-row> -->
  </div>
</template>

<script>
import axios from 'axios';
import NoticeVue from '../component/Home/Notice.vue';
import { SERVER_URL } from '@/js/functions/config';
export default {
  components:{
    NoticeVue,
  },
  data() {
    return {
      images: [
        require("../../assets/static/pictures/jxust/1.png"),
        require("../../assets/static/pictures/jxust/2.png"),
        require("../../assets/static/pictures/jxust/3.jpg"),
        require("../../assets/static/pictures/jxust/4.jpg"),
        require("../../assets/static/pictures/jxust/5.jpg"),
        // 其他的图片URL...
      ],
    };
  },
  methods: {
    async tiren(userid){
      await axios.post(`${SERVER_URL}/user/kickout?userid=${userid}`).then(res => {
        console.log(res.data);
      })
      .catch(res => {
        console.log("踢人失败")
      })
    }
    // 其他的方法...
  },
};
</script>

<style scoped>



</style>
