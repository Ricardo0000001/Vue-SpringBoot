import Vue from 'vue'
import Router from 'vue-router'
import Field from '../components/Field.vue'
import Home from '../components/Home.vue'
import Detail from '../components/Detail'
import Outline from '../components/Outline'
import Statis from '../components/Statis'
import ValidClientInfo from '../components/ValidClientInfo'
import Rank from '../components/Rank'
import StatisAndRank from '../components/StatisAndRank'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      name: 'field',
      path: '/field',
      component: Field
    },
    {
      path: '/home',
      component: Home
    },
    {
      name: 'detail',
      path: '/detail',
      component: Detail
    },
    {
      name: 'outline',
      path: '/outline',
      component: Outline
    },
    {
      name: 'statis',
      path: '/statis',
      component: Statis
    },
    {
      name: 'validclientinfo',
      path: '/validclientinfo',
      component: ValidClientInfo
    },
    {
      name: 'rank',
      path: '/rank',
      component: Rank
    },
    {
      name: 'statisdandrank',
      path: '/statisAndRank',
      component: StatisAndRank
    }
  ]
})
