<template>
  <div>
    <br>
    <x-header class="header" :left-options="{showBack: false}">营销团队工作汇报总表</x-header>
    <br>
    <grid :show-lr-borders="false" :show-vertical-dividers="true">
      <grid-item style="font-weight: bold; font-size: 2rem" @on-item-click="goToAdd" :label="$t('新增')"></grid-item>
      <grid-item style="font-weight: bold; font-size: 2rem" @on-item-click="goToOutline" :label="$t('招商信息明细表')"></grid-item>
      <grid-item style="font-weight: bold; font-size: 2rem" @on-item-click="goToValid" :label="$t('有效客户明细表')">
      </grid-item>
    </grid>
    <divider>最新数据</divider>
    <div style="padding:0 1px;">
      <x-table :full-bordered="true">
        <thead>
        <tr>
          <th style="font-weight: bold; font-size: 1rem" v-for="(header, index) in tableHeader" :key="index"> {{tableHeader[index]}}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item , i) in homeStatis" :key="i">
          <td style="font-size: 0.9rem">{{item.clientindustry}}</td>
          <td style="font-size: 0.9rem">{{item.clientcount}}</td>
          <td style="font-size: 0.9rem">{{item.squareSum}}</td>
        </tr>
        </tbody>
      </x-table>
      <divider>我是有底线的</divider>
    </div>
  </div>
</template>

<script>
import { XTable, Divider, XHeader, XButton, Flexbox, FlexboxItem, Grid, GridItem, Search } from 'vux'
export default {
  name: 'Home',
  data: function () {
    return {
      // 请求数据的存放
      homeStatis: [],
      // 请求员工数据的url
      staffInforUrl: 'https://dropdbandescape.parkwing.cn/codiv/role_employee/details',
      // 发送请求时会用到的 员工姓名 部门 以及客户状态（默认为有效）
      merchantStaff: '',
      department: [],
      // 给双部门员工的预留
      department1: null,
      department2: null,
      clientState: '有效',
      // 保存到本页面的openid
      openid: '',
      tableHeader:
        [
          '行业', '活跃客户数', '需求面积'
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
  // activated () {
  //   console.log('哎呀看见我了')
  //   console.log('----------activated--------')
  // },
  // deactivated () {
  //   console.log('讨厌！！你又走了')
  //   console.log('----------deactivated--------')
  // },
  mounted: function () {
    // console.log('print the token content:', this.$route.query.token)
    this.loadData()
  },
  methods: {
    /**
     * 按钮跳转到排名（暂未使用）
     */
    goToRank () {
      this.$router.push({
        name: 'rank'
      })
    },
    /**
     * 按钮跳转到统计表（暂未使用）
     */
    goToStatis () {
      this.$router.push({
        name: 'statis'
      })
    },
    /**
     * 按钮跳转到有效客户明细表
     */
    goToValid () {
      this.openid = this.$route.query.token
      this.$router.push({
        name: 'validclientinfo',
        params: {
          openid: this.openid
        }
      })
    },
    /**
     * 按钮跳转到招商信息明细表
     */
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
     * 按钮跳转新增页面
     * http://localhost:8080/#/home?token=oioHn5fNfbAscxdg558DRDstYDms
     * ?后面的参数 需要用this.$route.query.token获取
     */
    goToAdd: function () {
      // no openid will gap into null
      if (this.$route.query.token === undefined) {
        alert('非法访问！请先登录！')
        this.$router.push({
          name: 'null'
        })
      } else { // get the correct openid and then  jump to field
        this.openid = this.$route.query.token
        this.$router.push({
          name: 'field',
          params: {
            openid: this.openid
          }
        })
      }
    },
    /**
     * 根据openid获取姓名 部门
     */
    async getNameAndDepart () {
      // 保存openid
      this.openid = this.$route.query.token
      // openid 存入缓存
      window.localStorage.setItem('globalOpenid', this.$route.query.token)
      // 根据openid获得员工姓名和部门
      var param = {'openid': this.openid}
      var test = await this.$axios.post(this.staffInforUrl, param)
      // console.log('print the staff return data:', test)
      // 判断员工是否为 王火荣和杨朔 如果是 就要分别添加两个部门
      if (test.data.list[0].name === '王火荣') {
        window.localStorage.setItem('globalDepart1', '事业五部')
        window.localStorage.setItem('globalDepart2', '事业六部')
      } else if (test.data.list[0].name === '杨朔') {
        window.localStorage.setItem('globalDepart1', '事业一部')
        window.localStorage.setItem('globalDepart2', '项目发展部')
      } else {
        window.localStorage.setItem('globalDepart1', test.data.list[0].deptname) // 其余领导只有一个部门 另一个部门为空
        window.localStorage.setItem('globalDepart2', '')
      }
      // 员工姓名 部门 存入缓存
      window.localStorage.setItem('globalStaff', test.data.list[0].name)
    },
    /**
     * 根据姓名获取权限
     *    陈凯奇 黄忠伟 all
     *    总监们 part
     *    不在权限表中 普通员工
     */
    async getPermission () {
      var username = window.localStorage.getItem('globalStaff')
      console.log('print the username:', username)
      var paramUserName = {
        'username': username
      }
      var back = await this.$axios.post('/customerManagePermit/outlinePermit', paramUserName)
      // 这里根据权限写好 员工姓名 部门
      if (back.data.length === 0) { // 不在权限表中 就是普通员工
        this.judgePermit = false
        this.department1 = window.localStorage.getItem('globalDepart1') // 部门写死
        this.department2 = window.localStorage.getItem('globalDepart2')
        this.merchantStaff = window.localStorage.getItem('globalStaff') // 员工写死
        window.localStorage.setItem('globalPermit', 'staff')
      } else if (back.data[0].permit === 'all') { // 最高权限 黄忠伟 陈凯奇权限
        this.judgePermit = true // 允许选择部门 因为没有部门选择所以没用到这个
        this.department1 = null // 表示可以返回全部部门的数据
        this.department2 = null // 表示可以返回全部部门的数据
        this.merchantStaff = null // 员工不受限制
      } else if (this.merchantStaff === '王火荣' || this.merchantStaff === '杨朔') {
        this.judgePermit = false // 不显示部门选择控件
        this.department1 = window.localStorage.getItem('globalDepart1') // 这位领导有两个部门
        this.department2 = window.localStorage.getItem('globalDepart2')
        this.merchantStaff = null // 该部门下员工不限制
      } else if (back.data[0].permit === 'part') { // 权限是part  而且除去了王火荣和杨朔 写死部门 不写死业务员
        this.judgePermit = false // 权限是part 不显示部门选择的控件 并且写死了部门选项的值
        this.department1 = window.localStorage.getItem('globalDepart1')
        this.department2 = window.localStorage.getItem('globalDepart2') // 第二个部门为null因为该领导只有一个部门
        this.merchantStaff = null // 该部门下员工不限制
      } else {
        alert('There is no related staff info, please contact the admin!')
      }
    },
    /**
     * 初始化加载数据
     * 判断是否有openid 没有就跳转到空页面
     *                有就继续去查询姓名和权限
     */
    async loadData () {
      // 判断是否有openid 没有就跳转到空页面
      if (this.$route.query.token === undefined) {
        alert('非法访问！请先登录！')
        this.$router.push({
          name: 'null'
        })
      } else {
        // 有openid就查询姓名部门权限
        await this.getNameAndDepart()
        // 根据姓名部门去拿权限
        await this.getPermission()
        var _this = this
        // 获取 行业 活跃客户 需求面积
        // 筛选条件 行业 时间30天以内 客户状态 面积 业务员名字 部门
        var paramHome = {
          'merchantDepartment1': this.department1,
          'merchantDepartment2': this.department2,
          'merchantStaff': this.merchantStaff,
          'clientstate': this.clientState,
          'start': this.startTime,
          'end': this.endTime
        }

        // 发送请求数据
        this.$axios.post('/customerManage/homeStatis', paramHome).then((resp) => {
          // console.log('print the send param:', paramHome)
          // console.log('print the return data:', resp.data)
          if (resp.status === 200) {
            _this.homeStatis = resp.data
          }
        })
      }
    }
  }
}
</script>

<style>
  .header{
    margin-top: -1.5em;
  }
</style>
