<template>
  <a-row style="width: 100%; margin-top: 20px;">
    <a-col :span="24">
      <a-row style="align-items: center;justify-content: center;">
        <a-col :span="23">
          <el-carousel :interval="4000" type="card" height="300px">
            <el-carousel-item v-for="item in images" :key="item">
              <img :src="item" alt="image" style="width: 100%; height: 100%" />
            </el-carousel-item>
          </el-carousel>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24">
          <div style="padding: ;">

          </div>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>

<script>
export default {
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
  
    // 其他的方法...
  },
};
</script>

<style scoped>



</style>
