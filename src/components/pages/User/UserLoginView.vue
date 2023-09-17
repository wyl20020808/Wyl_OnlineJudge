<template>
  <span class="inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-600 ring-1 ring-inset ring-gray-500/10">Badge</span>
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

    <div class="h-full bg-white">
    <div class="h-full">
      <div class="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
        <div class="sm:mx-auto sm:w-full sm:max-w-sm">
          <img
            class="mx-auto h-10 w-auto"
            src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
            alt="Your Company"
          />
          <h2
            class="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900"
          >
            Sign in to your account
          </h2>
        </div>

        <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form class="space-y-6" action="#" method="POST">
            <div>
              <label
                for="email"
                class="block text-sm font-medium leading-6 text-gray-900"
                >Email address</label
              >
              <div class="mt-2">
                <input
                  id="email"
                  name="email"
                  type="email"
                  autocomplete="email"
                  required
                  class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <div class="flex items-center justify-between">
                <label
                  for="password"
                  class="block text-sm font-medium leading-6 text-gray-900"
                  >Password</label
                >
                <div class="text-sm">
                  <a
                    href="#"
                    class="font-semibold text-indigo-600 hover:text-indigo-500"
                    >Forgot password?</a
                  >
                </div>
              </div>
              <div class="mt-2">
                <input
                  id="password"
                  name="password"
                  type="password"
                  autocomplete="current-password"
                  required
                  class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Sign in
              </button>
            </div>
          </form>

          <p class="mt-10 text-center text-sm text-gray-500">
            Not a member?
            <a
              href="#"
              class="font-semibold leading-6 text-indigo-600 hover:text-indigo-500"
              >Start a 14 day free trial</a
            >
          </p>
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

@tailwind base;
@tailwind components;
@tailwind utilities;
  
  </style>