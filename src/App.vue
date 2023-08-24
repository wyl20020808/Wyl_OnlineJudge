<template>
  <BaseComponentView />
  <router-view :key="$route.fullPath"/>
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

</style>
