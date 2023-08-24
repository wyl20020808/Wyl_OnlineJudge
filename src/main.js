import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router/router.js'
import store from './store/mainStore.js'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import 'bootstrap/dist/css/bootstrap.css'
import { nextTick } from 'vue'
const app = createApp(App)
// 自动获取焦点
app.directive('focus', { 
    mounted(el) {
      nextTick(() => {
        el.focus()
      })
    }
  })
app.use(router).use(ElementPlus).use(store).use(Vuetify).mount('#app')