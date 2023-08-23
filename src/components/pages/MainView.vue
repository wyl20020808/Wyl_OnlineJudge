<template>
    
    <div class="news-card card">
        <div class="news-card-header">
            <h2 class="news-card-title">新闻</h2>
        </div>
        <div class="news-card-body">
            <!-- 这里可以通过后端传递的数据来展示新闻内容 -->
            <p>这是一条新闻的内容。</p>
        </div>
    </div>
     <div class="fortune-card card">
        <div class="fortune-card-header">
            <h2 class="fortune-card-title">今日</h2>
        </div>
        <div class="fortune-card-body">
            <div class="fortune-data">
                <!-- 这里可以通过后端传递的数据来展示"今日人品" -->
                <p>今日人品：</p>
            </div>
            <button class="refresh-button">点击打卡</button>
        </div>
    </div>
</template>

<script>
import BaseComponentViewVue from "../component/NavBar.vue";
export default{
    name:"home",
    components: {
    BaseComponentViewVue,
  }
}
</script>

<style scoped>
.news-card {
            height: 200px;
            width: 50%;
            position: absolute;
            top: 80px;
            left: 100px;
        }
        .news-card-header {
            background-color: #f8f9fa;
            padding: 10px;
        }
        .news-card-title {
            font-size: 1.5em;
            font-weight: bold;
        }
        .news-card-body {
            padding: 10px;
        }
/* ----------------------------------------- */
    .fortune-card {
            height: 200px;
            width: 25%;
            position: absolute;
            top: 80px;
            right: 250px;
        }
        .fortune-card-header {
            background-color: #f8f9fa;
            padding: 10px;
        }
        .fortune-card-title {
            font-size: 1.5em;
            font-weight: bold;
        }
        .fortune-card-body {
            position: relative;
            padding: 10px;
        }
        .fortune-data {
            margin-bottom: 30px;
        }
        .refresh-button {
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            bottom: 10px;
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 5px 10px;
            border-radius: 5px;
        }
</style>