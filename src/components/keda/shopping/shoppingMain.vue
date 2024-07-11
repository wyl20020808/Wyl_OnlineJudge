<template>
  <div class="product-list">
    <el-row justify="end" style="">
      <el-button type="primary" @click="jumpToAddGood">添加商品</el-button>
    </el-row>
    <el-row gutter="20">
      <el-col
        @click="jump(product.id)"
        :span="4"
        v-for="product in products"
        :key="product.id"
      >
        <el-card
          style="min-height: 630px"
          :body-style="{ padding: '0px' }"
          class="product-card"
        >
          <img :src="product.image" alt="Product Image" class="product-image" />
          <div class="product-info">
            <div class="product-name">{{ product.name }}</div>
            <div class="product-price">
            <span><img src="../liveClass/static/奖杯.png" style="width:40px;margin-top: -2px" alt=""></span>
            {{ product.price * 10 }}</div>
            <a-row >
              

              <a-col class="product-tags" >
                <el-tag style="margin-right: 5px;" v-for="(tag, index) in product.tags" :key="index">{{
                  tag
                }}</el-tag>
              </a-col>
              <a-col>
                <el-tag
                  v-if="product.freeShipping"
                  type="success"
                  class="free-shipping-tag"
                  >免运费</el-tag
                >
              </a-col>
            </a-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import router from "@/router/router";
import { SERVER_URL } from "@/js/functions/config";

const products = ref([]);
const DEFAULT_IMAGE = "path/to/default/image.jpg"; // 设置默认图片路径
const BASE_URL = "http://localhost:8088"; // 设置服务器的基础URL

const fetchProducts = async () => {
  try {
    const response = await axios.get(`${SERVER_URL}/product/list`);
    products.value = response.data.map((product) => {
      let image = DEFAULT_IMAGE;
      try {
        const images = product.images ? JSON.parse(product.images) : [];
        if (images.length > 0) {
          image = `${BASE_URL}${images[0]}`; // 确保图片URL包含正确的端口号
        }
      } catch (error) {
        console.error("解析商品图片出错:", error);
      }

      // 将标签字符串转换为数组
      const tags = product.tags ? product.tags.split(" ") : [];

      return {
        ...product,
        image,
        tags,
      };
    });
  } catch (error) {
    console.error("获取商品列表出错:", error);
  }
};

function jump(id) {
  router.push({
    path: `/goodsDetail`,
    query: {
      id: id,
    },
  });
}

function jumpToAddGood() {
  router.push({ path: "/addGood" });
}

onMounted(() => {
  fetchProducts();
});
</script>
<style scoped>
.product-list {
  padding: 20px;
  background-color: #f5f5f5;
}
.el-card {
  border-radius: 10px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.el-card:hover {
  cursor: pointer;
  transform: scale(1.1);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
.product-image {
  width: 400px;
  height: 400px;
  object-fit: cover;
}
.product-info {
  padding: 15px;
  text-align: left;
}
.product-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}
.product-price {
  font-size: 24px;
  color: #ff4c4c;
  margin-bottom: 10px;
}


</style>
