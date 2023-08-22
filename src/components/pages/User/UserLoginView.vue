<template>
    <div class="container py-5 outer">
      <div class="row">
        <div class="col-md-12">
          <h2 class="text-center text-dark mb-4">用户登录</h2>
          <div class="row">
            <div class="col-md-6 mx-auto">
              <div class="card rounded-0">
                <div class="card-header">
                  <h3 class="mb-0">登录</h3>
                </div>
                <div class="card-body">
                  <form class="form" role="form" autocomplete="off" @submit.prevent="login">
                    <div class="form-group">
                      <label for="username">用户名</label>
                      <input type="text" class="form-control form-control-lg rounded-0" id="username" v-model="data.username" required>
                    </div>
                    <div class="form-group">
                      <label for="password">密码</label>
                      <input type="password" class="form-control form-control-lg rounded-0" id="password" v-model="data.password" required>
                    </div>
                    <button  type="submit" class="btn btn-primary button1 ">登录</button>
    
                  </form>
                  <div class="signin">
                    <router-link :to="{name:'userregitser'}">
                        还没有账号？点击注册
                    </router-link>
                    
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script >
  import { reactive } from 'vue';
  import { useStore } from 'vuex';
 
  
  export default {
    setup() {
      const store = useStore();
      const data = reactive({
        username: '',
        password: '',
        useremail:''
      });
  
      const login = () => {
        store.dispatch('login', data)
        .then(() => {
        // 处理成功的情况
          console.log('登录部分成功');
        })
        .catch(error => {
          // 处理失败的情况
          console.error('Action failed:', error);
        });
      };
      
  
      return {
        data,
        login,

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
  .signin{
    position: relative;
    top: 10px;
  }
  
  </style>