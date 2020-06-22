<template>
  <div>
    <br>
    <x-header class="header" :left-options="{showBack: false}">数据统计和排名</x-header>
    <br>
    <!--<grid :show-lr-borders="false" :show-vertical-dividers="true">-->
      <!--&lt;!&ndash;<grid-item link="/component/Field" :label="$t('新增')"></grid-item>&ndash;&gt;-->
      <!--&lt;!&ndash;<grid-item :link="{ path: '/component/Outline'}" :label="$t('招商信息明细表')"></grid-item>&ndash;&gt;-->
      <!--<grid-item @on-item-click="goToAdd" :label="$t('新增')"></grid-item>-->
      <!--<grid-item @on-item-click="goToOutline" :label="$t('招商信息明细表')"></grid-item>-->
      <!--<grid-item  @on-item-click="goToValid" :label="$t('有效客户明细表')">-->
        <!--&lt;!&ndash;<span slot="label">{{ $t('有效客户明细表') }}</span>&ndash;&gt;-->
      <!--</grid-item>-->
    <!--</grid>-->
    <grid :show-lr-borders="false" :show-vertical-dividers="true">
      <grid-item  @on-item-click="goToStatis" :label="$t('部门数据统计表')"></grid-item>
      <grid-item  @on-item-click="goToRank" :label="$t('排名')"></grid-item>
    </grid>
    <!--<flexbox orient="vertical">-->
    <!--&lt;!&ndash;<flexbox-item><div class="flex-demo">1</div></flexbox-item>&ndash;&gt;-->
    <!--&lt;!&ndash;<flexbox-item><div class="flex-demo">2</div></flexbox-item>&ndash;&gt;-->
    <!--<flexbox-item><x-button  class='buttonStyle' type="default" @click.native="goToAdd">新增</x-button></flexbox-item>-->
    <!--<flexbox-item><x-button  class='buttonStyle' type="default" @click.native="goToOutline">招商信息明细表</x-button></flexbox-item>-->
    <!--<flexbox-item> <x-button  class='buttonStyle' type="default" @click.native="goToValid">有效客户明细表</x-button></flexbox-item>-->
    <!--<flexbox-item> <x-button  class='buttonStyle' type="default" @click.native="goToStatis">部门信息统计表</x-button></flexbox-item>-->
    <!--<flexbox-item> <x-button  class='buttonStyle' type="default" @click.native="goToRank">排名</x-button></flexbox-item>-->
    <!--</flexbox>-->
    <!--<flexbox>-->
    <!--<flexbox-item>-->
    <!--<x-button  class='buttonStyle' type="default" @click.native="goToAdd">新增</x-button>-->
    <!--</flexbox-item>-->
    <!--<flexbox-item>-->
    <!--<x-button  class='buttonStyle' type="default" @click.native="goToOutline">招商明细</x-button>-->
    <!--</flexbox-item>-->
    <!--<flexbox-item>-->
    <!--<x-button  class='buttonStyle' type="default" @click.native="goToValid">有效客户</x-button>-->
    <!--</flexbox-item>-->
    <!--</flexbox>-->
    <divider>最新数据</divider>
    <div style="padding:0 1px;">
      <x-table :full-bordered="true">
        <thead>
        <tr>
          <th style="font-weight: bold; font-size: 1rem" v-for="(header, index) in tableHeader" :key="index"> {{tableHeader[index]}}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item , i) in content" :key="i" @click="seeDetail(item.id)">
          <td style="font-size: 0.9rem">{{item.clientName}}</td>
          <td style="font-size: 0.9rem">{{item.contactNumber}}</td>
          <td style="font-size: 0.9rem">{{item.wantPark}}</td>
        </tr>
        </tbody>
      </x-table>
      <divider>我是有底线的</divider>
    </div>
  </div>
</template>

<script>import { XTable, Divider, XHeader, XButton, Flexbox, FlexboxItem, Grid, GridItem } from 'vux'
export default {
  name: 'Home',
  data: function () {
    return {
      // 用户的姓名和部门
      openId: null,
      merchantStaff: null,
      merchantDepartment: null,
      // 获取姓名和部门的接口
      staffInforUrl: 'https://dropdbandescape.parkwing.cn/codiv/role_employee/details',
      // 给预计成交时间添加一个判断
      predictdealtime: null,
      openid: '',
      content: [],
      tableHeader:
        [
          '客户名称', '联系方式', '意向园区'
          // '预计成交时间'
        ]

    }
  },
  components: {
    XTable,
    Divider,
    XHeader,
    XButton,
    Flexbox,
    FlexboxItem,
    Grid,
    GridItem
  },
  mounted: function () {
    this.loadData()
    // this.initialNameAndDepart()
  },
  methods: {
    /**
     * 初始化时根据token去获取 姓名和部门
     */
    initialNameAndDepart () {
      this.openid = this.$route.query.token
      var param = {'openid': this.openid}
      this.$axios.post(this.staffInforUrl, param).then((resp) => {
        if (resp.status === 200) {
          // 如果状态200 就赋值部门和园区信息
          this.merchantStaff = resp.data.list[0].name
          this.merchantDepartment = resp.data.list[0].deptname
          console.log('打印一下部门和园区信息', this.merchantStaff, this.merchantDepartment)
        }
      })
    },
    goToRank () {
      this.openid = this.$route.query.token
      this.$router.push({
        name: 'rank'
      })
    },
    goToStatis () {
      this.openid = this.$route.query.token
      this.$router.push({
        name: 'statis',
        params: {
          openid: this.openid
        }
      })
    },
    /**
     * http://localhost:8080/#/home?token=oioHn5fNfbAscxdg558DRDstYDms
     * ?后面的参数 需要用this.$route.query.token获取
     */
    goToAdd: function () {
      console.log('print this.$route.token :', this.$route.query.token)
      this.openid = this.$route.query.token
      this.$router.push({
        // path: '/field',
        name: 'field',
        params: {
          openid: this.openid
        }
      })
    },
    /**
     * 查看详情
     * @param index
     */
    seeDetail: function (index) {
      this.$router.push({
        name: 'detail',
        params: {
          index: index
        }
      })
    },
    /**
     * 初始化加载数据
     */
    loadData: function () {
      var _this = this
      var paramPageList = {
        order: 'createtime',
        page: 1,
        limit: 10
      }
      this.$axios.post('/customerManage/pageList', paramPageList).then(function (resp) {
        // this.$axios.get('/customerManage/all').then(function (resp) {
        // this.$http.get('http://dropdbandescape.parkwing.cn/codiv/customerManage/all').then(function (resp) {
        // console.log(resp)
        if (resp.status === 200) {
          // console.log('成功获取')
          // console.log('loadData打印返回的数据', resp.data.list)
          _this.content = resp.data.list
          // console.log('打印id的值', _this.respData[0].id)
        }
      }, function (err) {
        console.log(err)
      })
    }
  }
}
</script>

<!--<style lang="less">-->
<style>
  .header{
    margin-top: -1.5em;
  }
  /*.buttonStyle{*/
  /*margin: 0.5em 5em 0.5em 1em;*/
  /*}*/
</style>
