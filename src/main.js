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
import Antd from 'ant-design-vue'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// import 'ant-design-vue/dist/antd.less'
const app = createApp(App)

const vuetify = createVuetify({
  components,
  directives,
})
// 自动获取焦点
app.directive('focus', { 
    mounted(el) {
      nextTick(() => {
        el.focus()
      })
    }
  })
app.use(router).use(ElementPlus).use(store).use(Vuetify).use(Antd).use(vuetify).mount('#app')