<template>
  <div>
    <br>
    <x-header class="header" :left-options="{showBack: false}">营销团队工作汇报总表</x-header>
    <br>
    <grid :show-lr-borders="false" :show-vertical-dividers="true">
      <grid-item @on-item-click="goToAdd" :label="$t('新增')"></grid-item>
      <grid-item @on-item-click="goToOutline" :label="$t('招商信息明细表')"></grid-item>
      <grid-item  @on-item-click="goToValid" :label="$t('有效客户明细表')">
      </grid-item>
    </grid>
    <divider>最新数据</divider>
    <!--<search-->
      <!--placeholder="请输入客户名称"-->
      <!--@result-click="resultClick"-->
      <!--@on-change="getResult"-->
      <!--:results="resultsList"-->
      <!--v-model="value"-->
      <!--top="46px"-->
      <!--auto-fixed="false"-->
      <!--auto-scroll-to-top="false"-->
      <!--cancel-text="取消"-->
      <!--position="absolute"-->
      <!--ref="search">-->
    <!--</search>-->
    <group>
      <cell title="keyword">{{ value }}</cell>
    </group>
    <div style="padding:0 1px;">
      <x-table :full-bordered="true">
        <thead>
        <tr>
          <th style="font-weight: bold; font-size: 1rem" v-for="(header, index) in tableHeader" :key="index"> {{tableHeader[index]}}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item , i) in respData" :key="i" @click="seeDetail(item.id)">
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

<script>import { MessageBox } from 'mint-ui'
import { XTable, Divider, XHeader, XButton, Flexbox, FlexboxItem, Grid, GridItem, Search } from 'vux'
export default {
  name: 'Home',
  data: function () {
    return {
      // 测试用
      resultsList: [{title: 'A'}, {title: 'B'}, {title: 'C'}, {title: 'D'}],
      staffInforUrl: 'https://dropdbandescape.parkwing.cn/codiv/role_employee/details',
      merchantStaff: '',
      department: [],
      clientState: '',
      // 给预计成交时间添加一个判断
      predictdealtime: null,
      openid: '',
      content: [],
      respData: [],
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
    GridItem,
    Search
  },
  mounted: function () {
    // console.log('print the token content:', this.$route.query.token)
    this.loadData()
  },
  methods: {
    goToRank () {
      this.$router.push({
        name: 'rank'
      })
    },
    goToStatis () {
      this.$router.push({
        name: 'statis'
      })
    },
    goToValid () {
      this.openid = this.$route.query.token
      this.$router.push({
        name: 'validclientinfo',
        params: {
          openid: this.openid
        }
      })
    },
    goToOutline: function () {
      this.openid = this.$route.query.token
      this.$router.push({
        name: 'outline',
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
      // console.log('函数里index的值：')
      // console.log(index)
      this.$router.push({
        name: 'detail',
        params: {
          index: index
        }
      })
    },
    /**
     * 根据openid获取姓名 部门
     */
    async getNameAndDepart () {
      var _this = this
      // this.openid = this.$route.params.openid
      if (this.$route.query.token === undefined) {
        console.log('unlawful sign in!')
        MessageBox.alert('非法访问！请先登录！').then(action => {

        })
      }
      this.openid = this.$route.query.token
      console.log('print the openid:', this.$route.query.token)
      var param = {'openid': this.openid}
      // var test = await this.$axios.post(this.staffInforUrl, param)
      var test = await this.$axios.post(this.staffInforUrl, param)
      console.log('print the test content', test.data.list[0])
      _this.merchantStaff = test.data.list[0].name // 将部门名和员工名都放好
      _this.department[0] = test.data.list[0].deptname
    },
    /**
     * 根据姓名获取权限
     */
    async getPermission () {
      var username = this.merchantStaff
      var paramUserName = {
        'username': username
      }
      var back = await this.$axios.post('/customerManagePermit/outlinePermit', paramUserName)
      console.log('print the back data:', back)
      console.log('print the back:', back.data)
      // console.log('print the gerPermission data:', back.data[0].permit)
      if (back.data.length === 0) { // 不在权限表中 就是普通员工
        this.judgePermit = false
      } else if (back.data[0].permit === 'all') { // 最高权限 黄忠伟 陈凯奇权限
        this.judgePermit = true // 因为没有部门选择所以没用到这个
        this.department[0] = null // 表示可以返回全部部门的数据
        this.merchantStaff = null // 表示可以返回全部员工的数据
      } else { // 权限是part 是总经理 写死部门 不写死业务员
        this.judgePermit = false // 不显示部门选择的控件 并且写死了部门选项的值
        this.merchantStaff = null // 部门已经写死 员工是都可以
      }
    },
    /**
     * 初始化加载数据
     */
    async loadData () {
      await this.getNameAndDepart()
      await this.getPermission()
      // 正式加载数据
      var _this = this
      // if (this.clientState === '') { // 处理第一次选择时 客户状态为空时的判断
      //   this.clientState = '有效'
      // }
      var params = {
        'merchantDepartment': this.department[0],
        'merchantStaff': this.merchantStaff,
        'clientstate': this.clientState,
        'start': this.startTime,
        'end': this.endTime
      }
      console.log('打印发送的数据', params)
      this.$axios.post('/customerManage/outline', params).then((resp) => {
        // this.$axios.post('http://dropdbandescape.parkwing.cn/codiv/customerManage/outline', params).then((resp) => {
        if (resp.status === 200) {
          console.log('打印返回的终究数据11111111111111111111', resp.data)
          _this.respData = resp.data
        }
      })
    }
    // var _this = this
    // var paramPageList = {
    //   order: 'createtime',
    //   page: 1,
    //   limit: 10
    // }
    // this.$axios.post('/customerManage/pageList', paramPageList).then(function (resp) {
    //   // this.$axios.get('/customerManage/all').then(function (resp) {
    //   // this.$http.get('http://dropdbandescape.parkwing.cn/codiv/customerManage/all').then(function (resp) {
    //   // console.log(resp)
    //   if (resp.status === 200) {
    //     // console.log('成功获取')
    //     // console.log('loadData打印返回的数据', resp.data.list)
    //     _this.content = resp.data.list
    //     // console.log('打印id的值', _this.respData[0].id)
    //   }
    // }, function (err) {
    //   console.log(err)
    // })
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
