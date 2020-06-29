<template>
  <div id="app">
    <!--路由占位符-->
    <!--<keep-alive :include="['outline', 'validclientinfo']">-->
      <router-view></router-view>
    <!--</keep-alive>-->
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style lang="less">
  @import '~vux/src/styles/reset.less';
</style>
