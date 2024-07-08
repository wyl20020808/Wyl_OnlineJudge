
const { defineConfig } = require('@vue/cli-service')

module.exports = {
  
};

module.exports = defineConfig({
  devServer: {
    port:80,
    proxy: {
      '/prod-api': {
        target: 'https://tea.kedaoi.cn',
        changeOrigin: true,
        pathRewrite: {
          '^/prod-api': ''
        }
      }
    }
  },
  transpileDependencies: true,

  chainWebpack: config => {
    const svgRule = config.module.rule('svg')

    svgRule.uses.clear()

    svgRule
      .use('vue-svg-loader')
      .loader('vue-svg-loader')
  }
})