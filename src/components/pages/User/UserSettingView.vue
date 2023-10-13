<template>
  <a-row style="margin-top: 10px;margin-bottom: 20px;">
    <a-col style="width: 100%">
      <a-row>
        <a-col style="width: 100%">
          <div class="topcard">
            <div
              style="
                color: white;
                font-size: 30px;
                font-weight: bold;
                margin-left: 160px;
                padding-top: 50px;
              "
            >
              个人设置
            </div>
          </div>
        </a-col>
      </a-row>
      <a-row
        style="
          margin-top: 20px;
          display: flex;
          align-items: center;
          justify-content: center;
        "
      >
        <a-col>
          <div
            style="
              background-color: white;
              border-radius: 5px;
              padding: 30px;
              width: 1200px;
            "
          >
            <a-row>
              <a-col style="font-size: 18px; font-weight: bold">
                用户头像
              </a-col>
              <a-col :offset="2">
                <a-row
                  ><img
                    style="width: 100px; border-radius: 50%"
                    :src="user.userpicture"
                    alt=""
                /></a-row>
                <a-row style="margin-top: 10px"
                  ><a-button style="color: white" type="primary"
                    >选择图片</a-button
                  ></a-row
                >
                <a-row style="margin-top: 10px"
                  ><div>您可以在本地选择您的图片进行上传，作为您的头像</div>
                </a-row>
              </a-col>
            </a-row>
            <a-row style="margin-top: 20px">
              <a-col style="font-size: 18px; font-weight: bold">
                个性签名
              </a-col>
              <a-col :offset="2">
                <a-row>
                  <a-col>
                    <a-textarea
                      style="width: 600px; font-size: 16px"
                      v-model:value="user.usermotto"
                      placeholder="个性签名"
                      :auto-size="{ minRows: 2, maxRows: 5 }"
                    />
                  </a-col>
                  <a-col>
                    <a-button
                      style="color: white; margin-left: 10px"
                      type="primary"
                    >
                      修改
                    </a-button>
                  </a-col>
                </a-row>
              </a-col>
            </a-row>
            <a-row style="margin-top: 20px">
              <a-col style="font-size: 18px; font-weight: bold">
                用户名称
              </a-col>
              <a-col :offset="2">
                <a-row
                  ><a-input-search
                    style="width: 200px; height: 40px; font-size: 16px"
                    v-model:value="user.username"
                    placeholder="用户名"
                  >
                    <template #enterButton>
                      <a-button style="color: white" type="primary"
                        >修改</a-button
                      >
                    </template>
                  </a-input-search></a-row
                >
              </a-col>
            </a-row>
            <a-row style="margin-top: 20px">
              <a-col style="font-size: 18px; font-weight: bold">
                原始密码
              </a-col>
              <a-col :offset="2">
                <a-row
                  ><a-input-password
                    style="width: 200px; height: 30px; font-size: 16px"
                    v-model:value="user.password"
                    placeholder="用户名"
                  >
                  </a-input-password
                ></a-row>
              </a-col>
            </a-row>
            <a-row style="margin-top: 20px">
              <a-col style="font-size: 18px; font-weight: bold">
                新的密码
              </a-col>
              <a-col :offset="2">
                <a-row
                  ><a-input-password
                    style="width: 200px; height: 30px; font-size: 16px"
                    v-model:value="newPassword"
                  >
                  </a-input-password
                ></a-row>
              </a-col>
            </a-row>
            <a-row style="margin-top: 20px">
              <a-col style="font-size: 18px; font-weight: bold">
                再次输入
              </a-col>
              <a-col :offset="2">
                <a-row
                  ><a-input-password
                    style="width: 200px; height: 30px; font-size: 16px"
                    v-model:value="againPassword"
                  >
                  </a-input-password
                ></a-row>
                <a-row style="margin-top: 10px">
                  <a-button
                    style="background-color: rgb(255, 39, 39); color: white"
                    >修改密码</a-button
                  >
                </a-row>
              </a-col>
            </a-row>
            <a-row style="margin-top: 20px">
              <a-col style="font-size: 18px; font-weight: bold">
                安全邮箱
              </a-col>
              <a-col :offset="2">
                <a-row
                  ><a-input
                    style="width: 200px; height: 30px; font-size: 16px"
                    v-model:value="user.useremail"
                  >
                  </a-input
                ></a-row>
                <a-row style="margin-top: 10px">
                  <a-button
                    style="background-color: rgb(255, 39, 39); color: white"
                    >更换绑定</a-button
                  >
                </a-row>
              </a-col>
            </a-row>
            <a-row style="margin-top: 20px">
              <a-col style="font-size: 18px; font-weight: bold">
                安全手机
              </a-col>
              <a-col :offset="2">
                <a-row
                  ><a-input
                    style="width: 200px; height: 30px; font-size: 16px"
                    v-model:value="user.userphone"
                  >
                  </a-input
                ></a-row>
                <a-row style="margin-top: 10px">
                  <a-button
                    style="background-color: rgb(255, 39, 39); color: white"
                    >更换绑定</a-button
                  >
                </a-row>
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>

<script setup>
import { ref } from "vue";
let user = ref(JSON.parse(localStorage.getItem("user")));
console.log(user.value)
let newPassword = ref("");
let againPassword = ref("");
</script>

<style scoped>
.topcard {
  width: 100%;
  height: 150px;
  background: linear-gradient(to right, rgb(35, 37, 38), rgb(66, 67, 70));
}
</style>
