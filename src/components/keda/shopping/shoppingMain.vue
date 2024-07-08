<template>
    <div class="product-list">
      <el-row gutter="20">
        <el-col @click="jump('goodsDeatail')" :span="4" v-for="product in products" :key="product.id">
          <el-card  :body-style="{ padding: '0px' }" class="product-card">
            <img src="./static/冰箱.jpg" alt="Product Image" class="product-image" />
            <div class="product-info">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-price">¥{{ product.price }}</div>
              <el-tag v-if="product.freeShipping" type="success" class="free-shipping-tag">免运费</el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import router from "@/router/router";
  
  const products = ref([
    { id: 1, name: '日本进口缝隙收纳架厨房置物架冰箱夹缝车可移动', price: 29, image: '/static/冰箱.jpg', freeShipping: true },
    { id: 2, name: '北美红雀冬青浆果欧式陶瓷餐具汤锅汤碗汤盅手绘', price: 118, image: '/static/陶瓷餐具.jpg', freeShipping: true },
    { id: 3, name: '结婚酒壶婚庆用品 大红色双喜 喜庆敬酒杯 交杯酒杯', price: 29.85, image: '/static/婚庆用品.jpg', freeShipping: true },
    { id: 4, name: '他山集.日式毕川明风刺绣布艺棉盖把手防烫帽 烤箱', price: 11.5, image: '/static/布艺棉盖.jpg', freeShipping: true },
    { id: 5, name: '衣柜收纳神器分层置物架卧室柜子隔板储物白色宿', price: 37.8, image: '/static/衣柜收纳.jpg', freeShipping: true },
    { id: 6, name: '木果盘果盆创意家用木质干果盘实木果篮客厅木制', price: 33, image: '/static/木果盘.jpg', freeShipping: true },
  ]);

  function jump(total) {
  router.push({ path: "/" + total });
}
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
    width: 100%;
    height: auto;
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
    font-size: 18px;
    color: #ff4c4c;
    margin-bottom: 10px;
  }
  .free-shipping-tag {
    margin-top: 10px;
  }
  </style>
  