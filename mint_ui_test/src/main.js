// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import { Switch, Field, Header, DatetimePicker, Button, MessageBox, Picker, Cell, Search } from 'mint-ui'
import { AjaxPlugin, Datetime, DatetimePlugin, LoadingPlugin } from 'vux'
import Vuex from 'vuex'
import vuexI18n from 'vuex-i18n'
// import { Input } from 'element-ui'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// Vue.use(VueResource)
Vue.use(vuexI18n)
Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    i18n: vuexI18n.store
  }
})
Vue.use(vuexI18n.plugin, store)
const translationsEn = {
  'content': 'This is some {type} content'
}
Vue.i18n.add('en', translationsEn)
Vue.i18n.set('en')

// 设置反向代理
var axios = require('axios')
/**
 *在这里修改全部的路径
 */
axios.defaults.baseURL = 'http://localhost:6501'
// axios.defaults.baseURL = 'https://dropdbandescape.parkwing.cn/codiv'
// 全局注册
Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.prototype.$messagebox = MessageBox
Vue.use(ElementUI)
Vue.use(AjaxPlugin)
Vue.use(Datetime)
Vue.use(DatetimePlugin)
Vue.use(LoadingPlugin)

Vue.component(Button.name, Button)
Vue.component(Field.name, Field)
Vue.component(Header.name, Header)
Vue.component(DatetimePicker.name, DatetimePicker)
Vue.component(Picker.name, Picker)
Vue.component(Cell.name, Cell)
Vue.component(Switch.name, Switch)
Vue.component(Search.name, Search)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  render: h => h(App)
})
