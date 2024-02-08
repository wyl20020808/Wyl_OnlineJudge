import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router/router.js'
import store from './store/mainStore.js'
import 'bootstrap/dist/css/bootstrap.css'
import { nextTick } from 'vue'
import Antd from 'ant-design-vue'

// markdown
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

// highlightjs 核心代码
import hljs from 'highlight.js/lib/core';
// 按需引入语言包
import json from 'highlight.js/lib/languages/json';
import cpp from 'highlight.js/lib/languages/cpp';
import javascript from 'highlight.js/lib/languages/javascript';
import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
import '@kangc/v-md-editor/lib/style/codemirror-editor.css';
import Codemirror from 'codemirror';
import 'codemirror/mode/markdown/markdown';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/css/css';
import 'codemirror/mode/htmlmixed/htmlmixed';
import 'codemirror/mode/vue/vue';
// edi
import 'codemirror/addon/edit/closebrackets';
import 'codemirror/addon/edit/closetag';
import 'codemirror/addon/edit/matchbrackets';
// placeholder
import 'codemirror/addon/display/placeholder';
// active-line
import 'codemirror/addon/selection/active-line';
// scrollbar
import 'codemirror/addon/scroll/simplescrollbars';
import 'codemirror/addon/scroll/simplescrollbars.css';

import 'codemirror/lib/codemirror.css';

// 2023年9月14日08:17:06
//支持markdown和代码高亮？
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import Prism from 'prismjs';
// 选择使用主题

// 代码高亮
import 'prismjs/components/prism-json';
// 显示代码行数
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';

// 快速复制代码
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';


// 引入使用主题的样式

//2023年10月9日09:47:17
import VueCodemirror from 'vue-codemirror'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/theme/base16-dark.css'

//2023年10月10日10:34:12
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { aliases, mdi } from 'vuetify/iconsets/mdi'

//2023年10月23日16:24:11
import VMdPreviewHtml from '@kangc/v-md-editor/lib/preview-html';
import '@kangc/v-md-editor/lib/style/preview-html.css';

//2024年1月9日11:38:30

//2024年1月10日10:43:21
// main.js
import Ionicons from 'ionicons';

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
})

// hljs.registerLanguage('json', json);
// hljs.registerLanguage('cpp', cpp);
// hljs.registerLanguage('cpp', javascript);
VMdEditor.Codemirror = Codemirror;
VMdEditor.use(githubTheme, {
  Hljs: hljs,
});
VMdPreview.use(githubTheme, {
  Hljs: hljs,
});

VueMarkdownEditor.use(githubTheme, {
  Hljs: hljs,
  extend(md) {
    // md为 markdown-it 实例，可以在此处进行修改配置,并使用 plugin 进行语法扩展
    // md.set(option).use(plugin);
  },
});

VMdPreview.use(createLineNumbertPlugin());
VMdPreview.use(createCopyCodePlugin());
// 
// import 'ant-design-vue/dist/antd.less'
const app = createApp(App)

// 自动获取焦点
app.directive('focus', { 
    mounted(el) {
      nextTick(() => {
        el.focus()
      })
    }
  })


app.config.errorHandler = (err, instance, info) => {
  // 检查错误信息，决定是否忽略
  if (err.message.includes("ResizeObserver loop completed with undelivered notifications")) {
    // 忽略这个错误
    console.log("Ignored ResizeObserver error");
  } else {
    // 对于其他错误，可以抛出异常或者使用console.error记录错误
    console.error(err, instance, info);
  }
};
import axios from 'axios'
// // 全局配置axios的 头部 token传递 2024年2月8日17:14:30
// axios.interceptors.request.use(
//   config => {
//     // 从localStorage中获取token
//     const token = localStorage.getItem('token');
//     if (token) {
//       // 如果token存在，则在请求头中携带token
//       config.headers.Authorization = `${token}`;
//     }
//     return config;
//   },
//   error => {
//     // 对请求错误做些什么
//     return Promise.reject(error);
//   }
// );
axios.defaults.withCredentials = true;
  
app.use(router).use(ElementPlus).use(vuetify).use(store).use(Antd)
app.use(VMdEditor).use(VMdPreview)
app.use(VMdPreviewHtml)
//2023年10月9日09:59:18
app.use(VueCodemirror).mount('#app')