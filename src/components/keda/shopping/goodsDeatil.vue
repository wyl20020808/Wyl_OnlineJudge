<template>
  <div class="product-detail" v-if="product">
    <el-row gutter="20">
      <el-col :span="9">
        <a-carousel arrows dots-class="slick-dots slick-thumb">
          <template #customPaging="props">
            <a>
              <img :src="product.images[props.i]" />
            </a>
          </template>
          <div v-for="(image, index) in product.images" :key="index">
            <img :src="image" class="product-thumbnail" />
          </div>
        </a-carousel>
      </el-col>
      <el-col :span="15">
        <div class="product-info">
          <h2 class="product-name">{{ product.name }}</h2>
          <div style="margin-top: 30px" class="product-description">
            {{ product.description }}
          </div>
          <div class="product-price">
            <span
              ><img
                src="../liveClass/static/奖杯.png"
                style="width: 50px; margin-top: -2px"
                alt=""
            /></span>
            {{ product.price * 10 }}
          </div>
          <!-- <div>
            <el-form-item label="商品描述">
                <span>{{product.description }}</span>
           
              </el-form-item>
          </div> -->

          <span class="product-status">
            <el-tag
              v-if="product.freeShipping"
              type="success"
              class="free-shipping-tag"
              >免运费</el-tag
            >
          </span>
          <span class="product-tags">
            <el-tag
              style="margin-left: 5px"
              v-for="(tag, index) in product.tags"
              :key="index"
              >{{ tag }}</el-tag
            >
          </span>

          <el-divider></el-divider>
          <div class="product-options">
            <el-form :model="form" label-width="80px">
              <el-form-item label="颜色分类">
                <el-radio-group v-model="form.color">
                  <el-radio
                    v-for="(color, index) in product.colors"
                    :label="color"
                    :key="index"
                    >{{ color }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
              <el-form-item label="数量">
                <el-input-number
                  v-model="form.quantity"
                  :min="1"
                ></el-input-number>
              </el-form-item>
            </el-form>
          </div>
          <div class="product-actions">
            <el-button
              :disabled="!hadEnoughTropy"
              type="primary"
              class="buy-now"
              @click="redeemProduct"
              >立即购买</el-button
            >
            <el-button type="warning" class="add-to-cart">加入购物车</el-button>
            <el-button type="info" class="collect">收藏</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog title="兑换商品" v-model="dialogVisible">
      <el-form :model="redeemForm">
        <el-form-item required label="收件人姓名" prop="recipientName">
          <el-input v-model="redeemForm.recipientName"></el-input>
        </el-form-item>
        <el-form-item label="收件人地址" prop="address">
          <el-input v-model="redeemForm.address"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="redeemForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="产品 ID" prop="productId">
          <el-input v-model="redeemForm.productId" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number
            v-model="redeemForm.quantity"
            :min="1"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRedemption">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";
import { SERVER_URL } from "@/js/functions/config";
import { Carousel } from "ant-design-vue";

import { useStore } from "vuex";

const store = useStore();
const route = useRoute();
const productId = route.query.id;
const product = ref({});
const currentImage = ref("");
const dialogVisible = ref(false);
const BASE_URL = "http://localhost:8088"; // 设置服务器的基础URL

const form = reactive({
  color: "",
  quantity: 1,
});

const redeemForm = reactive({
  recipientName: "",
  address: "",
  phone: "",
  productId: productId,
  quantity: 1,
  redeemDate: new Date().toISOString().slice(0, 10), // 当前日期
});

const fetchProduct = async () => {
  try {
    const response = await axios.get(`${SERVER_URL}/product/${productId}`);
    const productData = response.data;

    // 确保 images 是 JSON 字符串并解析为数组
    try {
      const imagesArray = JSON.parse(productData.images);
      if (Array.isArray(imagesArray)) {
        productData.images = imagesArray.map((image) => `${BASE_URL}${image}`);
      } else {
        productData.images = [];
      }
    } catch (error) {
      console.error("解析商品图片出错:", error);
      productData.images = [];
    }

    // 确保 tags 是数组（由空格分割）
    productData.tags = productData.tags ? productData.tags.split(" ") : [];
    product.value = productData;
    currentImage.value = product.value.images[0] || ""; // 设置默认的当前图片
  } catch (error) {
    console.error("获取商品详情出错:", error);
  }
};

const redeemProduct = () => {
  redeemForm.productId = productId;
  redeemForm.quantity = form.quantity;
  dialogVisible.value = true;
};

const userinfo = ref({});

const submitRedemption = async () => {
  try {
    if(userinfo.value.trophy < product.value.price * 10 * redeemForm.quantity){
      store.dispatch("notice", {
        title: "您的奖杯不足！",
        message: "",
        type: "error",
      });
      return;
    }
    const response = await axios.post(
      `${SERVER_URL}/redeem/submit`,
      redeemForm
    );
    console.log("提交兑换信息成功:", response.data);
    dialogVisible.value = false;
    store.dispatch("notice", {
      title: "兑换成功~ 即将准备为您发货！",
      message: "",
      type: "success",
    });

    await axios
      .post(
        `${SERVER_URL}/userextra/update/special`,
        {
          userid: JSON.parse(localStorage.getItem("user")).userid, //这里不会出问题，因为前面保证了有的
          special: "trophycount",
        },
        {
          params: {
            count: product.value.price * -10 * redeemForm.quantity, //减去对应奖杯
          },
        }
      )
      .then((res) => {
        store.dispatch("notice", {
          title: `您的奖杯已经扣除${10 * product.value.price * redeemForm.quantity}个`,
          message: "",
          type: "success",
        });
      })

      .catch((error) => {
        console.log(error);
      });
  } catch (error) {
    console.error("提交兑换信息出错:", error);
  }
};
const hadEnoughTropy = ref(false);
const getTrophy = async () => {
  const user = JSON.parse(localStorage.getItem("user"));
  if (!user) return;

  try {
    const response = await axios.get(`${SERVER_URL}/userextra/query/id`, {
      params: {
        userid: user.userid,
      },
    });
    userinfo.value = response.data;
    // console.log(response.data.trophy, "这是积分");
    hadEnoughTropy.value =
      parseInt(response.data.trophy) >= parseInt(product.value.price * 10);
    console.log(
      parseInt(response.data.trophy),
      parseInt(product.value.price * 10),
      "钱够吗",
      hadEnoughTropy.value
    );
  } catch (error) {
    console.error("获取用户积分出错:", error);
  }
};
onMounted(async () => {
  await fetchProduct();
  await getTrophy();
});
</script>
<style scoped>
.product-detail {
  padding: 20px;
  min-height: 1000px;
  background-color: #fff;
}
.thumbnail-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.product-thumbnail {
  width: 100%;
  height: auto;
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
}
.product-name {
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 10px;
}
.product-price {
  font-size: 30px;
  color: #ff4c4c;
  margin-bottom: 20px;
}
.product-status {
  margin-bottom: 10px;
}
.free-shipping-tag {
  background-color: #28a745;
  color: #fff;
  padding: 5px 10px;
  border-radius: 3px;
}
.product-tags {
  margin-bottom: 20px;
}
.product-description {
  font-size: 20px;
  line-height: 1.7;
  color: #666;
  margin-bottom: 20px;
}
.product-options {
  margin-bottom: 20px;
}
.product-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}
.product-actions .el-button {
  width: 150px;
  height: 40px;
}
.product-actions .buy-now {
  color: #fff;
  border: none;
}
.product-actions .add-to-cart {
  background-color: #f60;
  color: #fff;
  border: none;
}
.product-actions .collect {
  background-color: #ddd;
  color: #333;
  border: none;
}
:deep(.slick-dots) {
  position: relative;
  height: auto;
}
:deep(.slick-slide img) {
  border: 5px solid #fff;
  display: block;
  margin: auto;
  max-width: 80%;
}
:deep(.slick-arrow) {
  display: none !important;
}
:deep(.slick-thumb) {
  bottom: 0px;
}
:deep(.slick-thumb li) {
  width: 60px;
  height: 45px;
}
:deep(.slick-thumb li img) {
  width: 100%;
  height: 100%;
  filter: grayscale(100%);
  display: block;
}
:deep .slick-thumb li.slick-active img {
  filter: grayscale(0%);
}
</style>
