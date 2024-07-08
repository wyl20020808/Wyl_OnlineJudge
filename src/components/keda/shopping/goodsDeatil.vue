<template>
    <div class="product-detail">
      <el-row gutter="20">
        <el-col :span="10">
          <div class="thumbnail-list">
            <img
              v-for="(image, index) in product.images"
              :key="index"
              src="./static/冰箱.jpg"
              :alt="'Product Image ' + (index + 1)"
              class="product-thumbnail"
              @click="currentImage = image"
            />
          </div>
        </el-col>
        <el-col :span="14">
          <div class="product-main-image">
            <img :src="currentImage" alt="Main Product Image" class="main-image"/>
          </div>
          <div class="product-info">
            <h2>{{ product.name }}</h2>
            <div class="product-price">¥{{ product.price }}</div>
            <el-tag v-if="product.freeShipping" type="success" class="free-shipping-tag">免运费</el-tag>
            <div class="product-description">{{ product.description }}</div>
            <el-divider></el-divider>
            <el-form :model="form">
              <el-form-item label="颜色分类">
                <el-radio-group v-model="form.color">
                  <el-radio v-for="(color, index) in product.colors" :label="color" :key="index">{{ color }}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="数量">
                <el-input-number v-model="form.quantity" :min="1"></el-input-number>
              </el-form-item>
            </el-form>
            <div class="product-actions">
              <el-button type="primary" @click="addToCart">加入购物车</el-button>
              <el-button type="success">立即购买</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue';
  
  const product = ref({
    name: '日本进口缝隙收纳架厨房置物架冰箱夹缝车可移动',
    price: 99,
    description: '配套收纳盒 使空间更加有效利用',
    freeShipping: true,
    images: [
      '/static/冰箱.jpg',
      '/static/冰箱2.jpg',
      '/static/冰箱3.jpg',
      '/static/冰箱4.jpg'
    ],
    colors: ['日本原装 F-2571', '日本原装 F-2572', '日本原装 F-2573', '日本原装 F-2544']
  });
  
  const currentImage = ref(product.value.images[0]);
  
  const form = reactive({
    color: product.value.colors[0],
    quantity: 1
  });
  
  const addToCart = () => {
    console.log('Add to cart:', form);
    // 在此添加加入购物车逻辑
  };
  </script>
  
  <style scoped>
  .product-detail {
    padding: 20px;
    background-color: #fff;
  }
  .thumbnail-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  .product-thumbnail {
    width: 100px;
    height: 100px;
    object-fit: cover;
    border: 1px solid #ccc;
    border-radius: 5px;
    cursor: pointer;
  }
  .product-main-image {
    width: 100%;
    height: 500px;
    margin-bottom: 20px;
  }
  .main-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  .product-info {
    padding: 15px;
    background-color: #f5f5f5;
    border-radius: 5px;
  }
  .product-price {
    font-size: 24px;
    color: #ff4c4c;
    margin-bottom: 10px;
  }
  .free-shipping-tag {
    display: inline-block;
    margin-bottom: 10px;
    background-color: #28a745;
    color: #fff;
    padding: 2px 6px;
    border-radius: 3px;
  }
  .product-description {
    margin-bottom: 20px;
  }
  .product-actions {
    display: flex;
    gap: 10px;
  }
  .el-button {
    border-radius: 5px;
  }
  </style>
  