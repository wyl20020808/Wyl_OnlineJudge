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
                <li class="nav-item">
                    <router-link  :to="{name:'userinfo'}">
                    <img  class="avatar" src="https://cdn.acwing.com/media/user/profile/photo/70660_lg_145a4eca09.jpg" alt="Avatar">
                    </router-link>
                </li>
            </ul>
        </div>
    </nav>
</template>

<script>
export default {
    name:"BaseComponentView",
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
            margin-right: 100px;
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