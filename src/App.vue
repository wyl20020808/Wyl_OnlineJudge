<template>
  <div>
  <BaseComponentView />
  <router-view :key="$route.fullPath"/>
  </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import BaseComponentView from './components/component/NavBar.vue'
import MainView from './components/pages/MainView.vue'
export default {
  name: 'App',
  components: {
    BaseComponentView,
    MainView
  }
}
</script>

<style>
body {
  background-color: rgb(240, 240, 240); /* 你想要的颜色 */
}
</style>
