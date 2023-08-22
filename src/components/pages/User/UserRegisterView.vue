<template>
  <div class="container py-5 outer">
    <div class="row">
      <div class="col-md-12">
        <h2 class="text-center text-dark mb-4">注册新用户</h2>
        <div class="row">
          <div class="col-md-6 mx-auto">
            <div class="card rounded-0">
              <div class="card-header">
                <h3 class="mb-0">注册</h3>
              </div>
              <div class="card-body">
                <form class="form" role="form" autocomplete="off" @submit.prevent="register">
                  <div class="form-group">
                    <label for="username">用户名</label>
                    <input type="text" class="form-control form-control-lg rounded-0" id="username" v-model="data.username" required>
                  </div>
                  <div class="form-group">
                    <label for="password">密码</label>
                    <input type="password" class="form-control form-control-lg rounded-0" id="password" v-model="data.password" required>
                  </div>
                  <div class="form-group">
                    <label for="email">邮箱</label>
                    <input type="email" class="form-control form-control-lg rounded-0" id="email" v-model="data.useremail" required>
                  </div>
                  <button  type="submit" class="btn btn-primary button1 ">注册</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from 'vue';
import { useStore } from 'vuex';

import router from '@/router/router';
export default {
  setup() {
    const store = useStore();
    const data = reactive({
      username: '',
      password: '',
      useremail: ''
    });

    const register = () => {
      // console.log(data.useremail,"wehajh")
      store.dispatch('signin', data)
      .then(() => {
      // 处理成功的情况
        console.log('Action was successful');
      })
      .catch(error => {
        // 处理失败的情况
        console.error('Action failed:', error);
      });
    };

    return {
      data,
      register
    };
  }
};
</script>

<style scoped>
.outer{
  position: relative;
  top:100px;
}
.container {
  background: #f8f9fa;
  padding-top: 100px;
  padding-bottom: 100px;
}
.card-header {
  background: #e9ecef;
  color: black;
}
.col-md-12{
  width:1000px;
}
.button1{
  color:#e9ecef;
  position: relative;
  top: 10px;
  left:80px;
  width: 300px;
}

</style>