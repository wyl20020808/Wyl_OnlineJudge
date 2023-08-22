<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <router-link class="nav-link" :to="{name:'home'}">首页</router-link>
                </li>
                <li class="nav-item">
                    <router-link class="nav-link" :to="{name:'problems'}">题库</router-link>
                </li>
                <li class="nav-item">
                    <router-link class="nav-link" :to="{name:'competition'}">比赛</router-link>
                </li>
                <li class="nav-item logo">
                    <router-link  :to="{name:'home'}">
                    <img  class="avatar" src="../../assets/static/pictures/templogo.png" alt="Avatar">
                    </router-link>
                </li>
                <li class="nav-item">
                    <router-link class="nav-link" :to="{name:'ranklist'}">排行榜</router-link>
                </li>
                <li class="nav-item">
                    <router-link class="nav-link" :to="{name:'tissue'}">组织</router-link>
                </li>
                <li class="nav-item">
                    <router-link class="nav-link" :to="{name:'evaluationqueue'}">评测队列</router-link>
                </li>
                <li class="nav-item dropdown" v-if="$store.state.userInfo.userloginstate">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <img class="avatar" src="https://cdn.acwing.com/media/user/profile/photo/70660_lg_145a4eca09.jpg" alt="Avatar">
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li><router-link :to="{name:'home'}" class="dropdown-item">个人资料</router-link></li>
                        <li><router-link :to="{name:'home'}" class="dropdown-item">设置</router-link></li>
                        <li><router-link :to="{name:'home'}" class="dropdown-item">消息</router-link></li>
                        <li><router-link :to="{name:'home'}" class="dropdown-item">通知</router-link></li>
                        <li><a @click="logout" class="dropdown-item">退出</a></li>
                    </ul>
                </li>
                <li class="nav-item" v-else>
                    <router-link class="nav-link" :to="{name:'userlogin'}">登录</router-link>
                </li>
                
            </ul>
        </div>
    </nav>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.js'
export default {
    name:"BaseComponentView",
    created(){
        if(JSON.parse(localStorage.getItem('user') !== null)){
            this.$store.commit('updateUserState',
            JSON.parse(localStorage.getItem('user')))
        }
    },
    methods:{
        logout(){
            const userinfo = JSON.parse(localStorage.getItem('user'));
            this.$store.dispatch("notice",{
              title: '退出成功！',
              message: "再见！" + userinfo.username,
              type: 'success',
            })
            // console.log("logout")
            localStorage.removeItem('user');
            this.$store.commit('updateLoginState',false)
        },
    }   
}
</script>

<style scoped>
 .navbar {
            padding-left: 50px;
            border-bottom: 1px solid #ccc;
        }
        .navbar-nav {
            width: 100%;
            justify-content: space-between;
        }
        .navbar-nav .logo {
            flex-grow: 2.5;
        }
        .nav-item {
            flex-grow: 1;
            text-align: center;
            /* border-right: 1px solid #ccc; */
        }
        .nav-item:last-child {
            border-right: none;
            margin-right: 20px;
        }
        .navbar-brand {
            font-size: 2em;
        }
        .nav-link {
            font-size: 1.2em;
        }
        .avatar {
            border-radius: 50%;
            width: 40px;
            height: 40px;
        }
</style>