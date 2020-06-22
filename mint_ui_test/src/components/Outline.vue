<template>
  <div>
    <br>
    <x-header class="header" :left-options="{showBack: false}">招商信息明细表</x-header>
    <br>
    <group label-width="5em">
      <popup-picker :title="departmentshow" :data="departmentList" v-model="department" @on-change="onChangeDepart" v-if="judgePermit"></popup-picker>
      <!--<popup-picker :title="departmentshow" :data="departmentList" v-model="department" @on-change="onChangeDepart"></popup-picker>-->
    </group>
    <group >
      <x-switch :title="$t('客户状态')" :inline-desc="statement + ''" v-model="state" @on-change="changeClientState"></x-switch>
    </group>
    <divider>^</divider>
    <group >
      <datetime
        v-model="startTime"
        :title="$t('请选择起始时间')"
        @on-confirm="onConfirmStart"></datetime>
    </group>
    <group >
      <datetime
        v-model="endTime"
        :title="$t('请选择终止时间')"
        @on-confirm="onConfirmEnd"></datetime>
    </group>
    <!--<load-more :show-loading="false" background-color="#fbf9fe"></load-more>-->
    <divider>^</divider>
    <div v-for="(data , i) in respData" :key="i" style="padding:0 5px;" @click="seeDetail(respData[i].id)" >
      <x-table  :full-bordered="true" :cell-bordered="false" :content-bordered="true"  style="background-color:#fff;">
        <tbody >
        <tr >
          <td style="font-weight: 600">部门:</td>
          <td style="font-weight: 600">{{data.merchantDepartment}}</td>
          <td style="font-weight: 600">日期:</td>
          <!--<td style="font-weight: 600">{{// data.dateTime}}</td>-->
          <td style="font-weight: 600">{{data.dateTime.substr(0, 10)}}</td>
          <!--<td> x 1</td>-->
        </tr>
        <tr>
          <td style="font-size: 15px; color: #181818 ">招商人员:</td>
          <td style="font-size: 15px">{{data.merchantStaff}}</td>
          <td style="font-size: 15px">客户名称:</td>
          <td style="font-size: 15px">{{data.clientName}}</td>
          <!--<td> x 2</td>-->
        </tr>
        <tr>
          <td style="font-size: 15px">电话沟通:</td>
          <td style="font-size: 15px">{{data.phoneVisit}}</td>
          <td style="font-size: 15px">是否带看:</td>
          <td style="font-size: 15px; color: #CE3C39; ">{{data.takeVisit}}</td>
          <!--<td> x 2</td>-->
        </tr>
        <tr>
          <td style="font-size: 15px">需求面积:</td>
          <td style="font-size: 15px">{{data.needSquare}}</td>
          <td style="font-size: 15px">客户等级:</td>
          <td style="font-size: 15px; color: #CE3C39; ">{{data.currentLevel}}</td>
          <!--<td> x 2</td>-->
        </tr>
        </tbody>
      </x-table>
      <divider></divider>
    </div>
  </div>
</template>

<script>import { PopupPicker, XTable, Divider, Datetime, DatetimePlugin, Group, LoadMore, XSwitch, XHeader } from 'vux'
export default {
  components: {
    XTable,
    Divider,
    Datetime,
    DatetimePlugin,
    Group,
    LoadMore,
    XSwitch,
    XHeader,
    PopupPicker
  },
  name: 'outline',
  data: function () {
    return {
      // openid
      openid: null,
      judgePermit: false,
      staffInforUrl: 'https://dropdbandescape.parkwing.cn/codiv/role_employee/details',
      // 选择部门组件
      department: [],
      departmentshow: '请选择部门',
      departmentList: [['全部', '事业一部', '事业二部', '事业三部', '事业四部', '事业五部', '事业六部', '项目发展部']],
      // 客户状态需要用到的字段
      state: '',
      statement: '有效',
      startTime: '',
      endTime: '',
      // 概览显示的数据字段：部门 日期 招商人员 客户名称 电话 客户状态
      merchantDepartment: '',
      datetime: '',
      merchantStaff: '',
      clientName: '',
      contactNumber: '',
      clientState: '',
      // 后端请求返回的数据
      respData: []
    }
  },
  /**
   * (1) 根据openid拿取姓名 部门
   *（2）根据姓名 获取权限
   *（3）根据权限加载数据 隐藏空间 写死部门
   */
  mounted: function () {
    /**
     * 初始化加载数据
     * 默认   部门：全部
     *        客户状态：全部
     *        起始时间：不设置
     *        结束之间：不设置
     *        后台处理可以可能为空的情况
     */
    this.loadData()
  },
  methods: {
    /**
     * 根据姓名获取权限
     */
    async getPermission () {
      var username = this.merchantStaff
      var paramUserName = {
        'username': username
      }
      var back = await this.$axios.post('/customerManagePermit/outlinePermit', paramUserName)
      // console.log('print the gerPermission data:', back.data[0].permit)
      if (back.data.length === 0) { // 不存在表中 就是普通权限
        this.judgePermit = false // 不显示部门选择的控件
      } else if (back.data[0].permit === 'all') { // 黄忠伟 陈凯奇权限
        this.judgePermit = true // 显示部门选择的控件
        this.department[0] = null // 表示可以返回全部部门的数据
      } else {
        this.judgePermit = false // 不显示部门选择的控件 并且写死了部门选项的值
        this.merchantStaff = null // 当前部门下的全部员工都可以出现
      }
      // if (back.data[0].permit === 'all') { // 黄忠伟 陈凯奇权限
      //   this.judgePermit = true
      //   this.department[0] = null // 表示可以返回全部部门的数据
      // } else {
      //   this.judgePermit = false // 不显示部门选择的控件 并且写死了部门选项的值
      // }
    },
    /**
     * 根据openid获取姓名 部门
     */
    async getNameAndDepart () {
      var _this = this
      this.openid = this.$route.params.openid
      var param = {'openid': this.openid}
      // var test = await this.$axios.post(this.staffInforUrl, param)
      var test = await this.$axios.post(this.staffInforUrl, param)
      console.log('print the test content', test.data.list[0])
      _this.merchantStaff = test.data.list[0].name
      _this.department[0] = test.data.list[0].deptname
    },
    /**
     *  判断权限
     *  向后台数据库查询数据 判断是否是 黄忠伟或者陈凯奇
     *  如果  是 judgePermit 值为true 显示 选择部门的控件
     *        否则 值为false 并且写死部门控件
     *
     *        发送请求到后台  查询数据库中 tab_conclusion_permit 对应的名字所有的权限和部门
     */
    // judgePermission () {
    //   var _this = this
    //   this.openid = this.$route.params.openid
    //   // console.log('print the openid:', this.openid)
    //   var param = {'openid': this.openid}
    //   var username = ''
    //   this.$axios.post(this.staffInforUrl, param).then((resp) => {
    //     if (resp.status === 200) {
    //       // 如果状态200 就赋值部门和园区信息
    //       _this.merchantStaff = resp.data.list[0].name
    //       _this.merchantDepartment = resp.data.list[0].deptname
    //       username = resp.data.list[0].name
    //       // depart = resp.data.list[0].deptname
    //       console.log(_this.merchantStaff)
    //       console.log('print in the username:', username)
    //       console.log(_this.merchantDepartment)
    //       /**
    //        * 这里请求的顺序很有趣
    //        * @type {{username: (*|string)}}
    //        */
    //       var paramUserName = {
    //         'username': username
    //       }
    //       this.$axios.post('/customerManagePermit/outlinePermit', paramUserName).then((resp) => {
    //         console.log('print the back data:', resp.data)
    //         if (resp.status === 200 && !resp.data) {
    //           if (resp.data[0] === 'all') {
    //             this.judgePermit = true
    //             this.department[0] = null
    //           }
    //         } else {
    //           this.judgePermit = false
    //           this.department[0] = this.merchantDepartment
    //           console.log('print the insert depart', this.department[0])
    //         }
    //       })
    //     }
    //   })
    //   console.log('print out the username:', username)
    // },
    /**
     * 选择部门
     */
    onChangeDepart (value) {
      var _this = this
      this.department = value
      // 部门如果为'全部' 就置为空
      if (this.department[0] === '全部') {
        this.department[0] = null
      }
      console.log('打印一下选择的值', this.department)
      if (this.clientState === '') { // 处理第一次选择时 客户状态为空时的判断
        this.clientState = '有效'
      }
      var params = {
        'merchantDepartment': this.department[0],
        'clientstate': this.clientState,
        'start': this.startTime,
        'end': this.endTime
      }
      console.log('打印发送的数据', params)
      this.$axios.post('/customerManage/outline', params).then((resp) => {
      // this.$axios.post('http://dropdbandescape.parkwing.cn/codiv/customerManage/outline', params).then((resp) => {
        if (resp.status === 200) {
          console.log('打印返回的值', resp.data)
          _this.respData = resp.data
        }
      })
    },
    /**
    * 查看详细
    */
    seeDetail: function (index) {
      console.log('函数里index的值：')
      console.log(index)
      this.$router.push({
        name: 'detail',
        params: {
          index: index
        }
      })
    },
    /**
     * 起始时间 弹窗关闭时触发向后台发起请求
     */
    onConfirmStart (val) {
      var _this = this
      this.startTime = val
      // 部门如果为'全部' 就置为空
      if (this.department[0] === '全部') {
        this.department[0] = null
      }
      // console.log('打印下这个值confirmStart', this.startTime)
      if (this.clientState === '') {
        this.clientState = '有效'
      }
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
          console.log('打印返回的值', resp.data)
          _this.respData = resp.data
        }
      })
    },
    /**
     * 终止始时间 弹窗关闭时触发向后台发起请求
     */
    onConfirmEnd (val) {
      var _this = this
      this.endTime = val
      // 部门如果为'全部' 就置为空
      if (this.department[0] === '全部') {
        this.department[0] = null
      }
      // console.log('打印结束时间confirmEnd', this.endTime)
      if (this.clientState === '') {
        this.clientState = '有效'
      }
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
          console.log('打印返回的值', resp.data)
          _this.respData = resp.data
        }
      })
    },
    /**
     * 这里预加载数据
     * 后台接口 /pageList
     * 参数： page
     *       limit
     *       order
     */
    async loadData () {
      await this.getNameAndDepart()
      await this.getPermission()
      // 正式加载数据
      var _this = this
      if (this.clientState === '') { // 处理第一次选择时 客户状态为空时的判断
        this.clientState = '有效'
      }
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
          console.log('打印返回的值', resp.data)
          _this.respData = resp.data
        }
      })
    },
    /**
     * switch切换客户状态
     * 后台接口： /customerManage/outline
     *  参数：部门
     *        客户状态
     *        开始时间
     *        结束时间
     *        可以为空
     */
    changeClientState: function () {
      var _this = this
      if (this.state) { // 如果为true就为有效
        this.statement = '有效'
      } else {
        this.statement = '不活跃'
      }
      var params = {
        'merchantDepartment': this.department[0],
        'merchantStaff': this.merchantStaff,
        'clientstate': this.statement,
        'start': this.startTime,
        'end': this.endTime
      }
      console.log('打印发送的数据', params)
      this.$axios.post('/customerManage/outline', params).then((resp) => {
        // this.$axios.post('http://dropdbandescape.parkwing.cn/codiv/customerManage/outline', params).then((resp) => {
        if (resp.status === 200) {
          console.log('打印返回的值', resp.data)
          _this.respData = resp.data
        }
      })
    }
  }
}
</script>

<style >
  .header{
    margin-top: -1.5em;
  }
</style>
