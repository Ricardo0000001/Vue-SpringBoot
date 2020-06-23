<template>
  <div>
    <br>
    <x-header class="header" :left-options="{showBack: false}">有效客户明细表</x-header>
    <br>
    <!--部门选择-->
    <!--<group label-width="5em">-->
    <group>
      <popup-picker :title="departmentshow" :data="departmentList" v-model="department" @on-change="onChangeDepart" v-if="judgePermit"></popup-picker>
    </group>
    <!--客户等级-->
    <!--<group label-width="5em">-->
    <group>
      <popup-picker :title="currentlevelshow" :data="currentlevelList" v-model="currentLevel" @on-change="onChangeCurrentLevel"></popup-picker>
    </group>
    <group >
      <datetime
        v-model="startTime"
        :title="$t('请选择起始时间')"
        @on-confirm="changeStartTime"></datetime>
    </group>
    <group >
      <datetime
        v-model="endTime"
        :title="$t('请选择终止时间')"
        @on-confirm="changeEndTime"></datetime>
    </group>
    <!--<load-more :show-loading="false" background-color="#fbf9fe"></load-more>-->
    <divider>^</divider>
    <!--<div v-for="(data , i) in respData" :key="i" style="padding:0 5px;" @click="seeDetail(i)" >-->
    <div v-for="(data , i) in respCountData" :key="i" style="padding:0 5px;" @click="seeDetail(respCountData[i].id)" >
      <x-table  :full-bordered="true" :cell-bordered="false" :content-bordered="true"  style="background-color:#fff;">
        <tbody >
        <tr >
          <td style="font-weight: 600">部门:</td>
          <td style="font-weight: 600">{{data.merchantDepartment}}</td>
          <td style="font-weight: 600">日期:</td>
          <!--<td style="font-weight: 600">{{// data.dateTime.substr(0, 10)}}</td>-->
          <td style="font-weight: 600">{{data.datetime}}</td>
          <!--<td> x 1</td>-->
        </tr>
        <tr>
          <td style="font-size: 15px; color: #181818 ">招商人员:</td>
          <td style="font-size: 15px">{{data.merchantStaff}}</td>
          <td style="font-size: 15px">客户名称:</td>
          <td style="font-size: 15px">{{data.clientname}}</td>
          <!--<td> x 2</td>-->
        </tr>
        <!--添加面积和价格-->
        <tr>
          <td style="font-size: 15px; color: #181818 ">需求面积:</td>
          <td style="font-size: 15px">{{data.needSquare}}</td>
          <td style="font-size: 15px">价格:</td>
          <td style="font-size: 15px">{{data.price}}</td>
          <!--<td> x 2</td>-->
        </tr>
        <tr>
          <td style="font-size: 15px">电话沟通:</td>
          <td style="font-size: 15px">{{data.phonevisitcount}}</td>
          <td style="font-size: 15px">带看次数:</td>
          <td style="font-size: 15px; color: #CE3C39; ">{{data.takeVisitCount}}</td>
          <!--<td> x 2</td>-->
        </tr>
        </tbody>
      </x-table>
      <divider></divider>
    </div>
  </div>
</template>

<script>import { dateFormat, PopupPicker, XTable, Divider, Datetime, DatetimePlugin, Group, LoadMore, XSwitch, XHeader } from 'vux'
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
    PopupPicker,
    dateFormat
  },
  name: 'outline',
  data: function () {
    return {
      // 电话次数和带看次数
      phoneVisitCount: null,
      takeVisitCount: null,
      // 客户等级选择
      currentLevel: [], // 绑定的值
      currentlevelshow: '请选择等级',
      currentlevelList: [['A', 'B', 'C']],
      // 选择部门组件
      department: [], // 绑定的值
      departmentshow: '请选择部门',
      departmentList: [['全部', '事业一部', '事业二部', '事业三部', '事业四部', '事业五部', '事业六部', '项目发展部']],
      // 客户状态需要用到的字段
      state: '',
      startTime: null,
      endTime: null,
      // 概览显示的数据字段：部门 日期 招商人员 客户名称 电话 客户状态
      merchantDepartment: '',
      merchantStaff: '',
      datetime: '',
      // 带上次数的后台请求数据
      respCountData: [],
      //
      judgePermit: false,
      // 员工信息
      staffInforUrl: 'https://dropdbandescape.parkwing.cn/codiv/role_employee/details'
    }
  },
  mounted: function () {
    /**
     * （1）根据openid获取姓名部门
     * （2）根据姓名部门获取权限
     * （3）根据权限 加载数据
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
      console.log('print the gerPermission data:', back)
      // console.log('print the gerPermission data:', back.data[0].permit)
      if (back.data.length === 0) {
        this.judgePermit = false
      } else if (back.data[0].permit === 'all') { // 黄忠伟 陈凯奇权限
        this.judgePermit = true
        this.department[0] = null // 表示可以返回全部部门的数据
      } else {
        this.judgePermit = false // 不显示部门选择的控件 并且写死了部门选项的值
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
      var test = await this.$axios.post(this.staffInforUrl, param)
      console.log('print the test content', test.data.list[0])
      _this.merchantStaff = test.data.list[0].name
      _this.department[0] = test.data.list[0].deptname
    },
    /**
    * 时间格式化
    */
    timestampToTime (timestamp) {
      var date = new Date(timestamp)// 时间戳为10位需*1000，时间戳为13位的话不需乘1000
      var Y = date.getFullYear() + '-'
      var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
      var D = date.getDate() + 1 < 10 ? '0' + date.getDate() : date.getDate()
      // var D = date.getDate() + ' '
      // var h = date.getHours() + ':'
      // var m = date.getMinutes() + ':'
      // var s = date.getSeconds()
      return Y + M + D
    },
    /**
     * 选择客户等级
     */
    onChangeCurrentLevel () {
      var _this = this
      console.log('打印一下选择的等级', this.currentLevel[0])
      if (this.department[0] === '全部') {
        this.department[0] = null
      }
      var paramValidClient = {
        'merchantDepartment': this.department[0],
        'merchantStaff': this.merchantStaff,
        'currentLevel': this.currentLevel[0],
        'start': this.startTime,
        'end': this.endTime,
        'clientstate': '有效'
      }
      this.$axios.post('/customerManage/validClientCompute', paramValidClient).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respCountData = resp.data
          // 用一个for循环遍历全部的时间然后格式化
          for (let i = 0; i < _this.respCountData.length; i++) {
            _this.respCountData[i].datetime = _this.timestampToTime(_this.respCountData[i].datetime)
            _this.respCountData[i].predictdealtime = _this.timestampToTime(_this.respCountData[i].predictdealtime)
          }
        }
      })
    },
    /**
     * 选择部门
     */
    onChangeDepart (value) {
      var _this = this
      this.department = value
      console.log('打印一下选择的部门', this.department[0])
      if (this.department[0] === '全部') {
        this.department[0] = null
      }
      var paramValidClient = {
        'merchantDepartment': this.department[0],
        'merchantStaff': this.merchantStaff,
        'currentLevel': this.currentLevel[0],
        'start': this.startTime,
        'end': this.endTime,
        'clientstate': '有效'
      }
      this.$axios.post('/customerManage/validClientCompute', paramValidClient).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respCountData = resp.data
          // 用一个for循环遍历全部的时间然后格式化
          for (let i = 0; i < _this.respCountData.length; i++) {
            _this.respCountData[i].datetime = _this.timestampToTime(_this.respCountData[i].datetime)
            _this.respCountData[i].predictdealtime = _this.timestampToTime(_this.respCountData[i].predictdealtime)
          }
        }
      })
    },
    /**
     * 查看详情
     */
    seeDetail: function (index) {
      console.log('函数里index的值：')
      console.log(index)
      this.$router.push({
        name: 'detail',
        params: {
          index: index,
          openid: this.openid
        }
      })
    },
    /**
    * 选择开始时间
    */
    changeStartTime (val) {
      var _this = this
      this.start = val
      if (this.department[0] === '全部') {
        this.department[0] = null
      }
      var paramValidClient = {
        'merchantDepartment': this.department[0],
        'merchantStaff': this.merchantStaff,
        'currentLevel': this.currentLevel[0],
        'start': this.startTime,
        'end': this.endTime,
        'clientstate': '有效'
      }
      this.$axios.post('/customerManage/validClientCompute', paramValidClient).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respCountData = resp.data
          // 用一个for循环遍历全部的时间然后格式化
          for (let i = 0; i < _this.respCountData.length; i++) {
            _this.respCountData[i].datetime = _this.timestampToTime(_this.respCountData[i].datetime)
            _this.respCountData[i].predictdealtime = _this.timestampToTime(_this.respCountData[i].predictdealtime)
          }
        }
      })
    },
    /**
     * 选择结束时间
     */
    changeEndTime (val) {
      var _this = this
      this.end = val
      if (this.department[0] === '全部') {
        this.department[0] = null
      }
      var paramValidClient = {
        'merchantDepartment': this.department[0],
        'merchantStaff': this.merchantStaff,
        'currentLevel': this.currentLevel[0],
        'start': this.startTime,
        'end': this.endTime,
        'clientstate': '有效'
      }
      this.$axios.post('/customerManage/validClientCompute', paramValidClient).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respCountData = resp.data
          // 用一个for循环遍历全部的时间然后格式化
          for (let i = 0; i < _this.respCountData.length; i++) {
            _this.respCountData[i].datetime = _this.timestampToTime(_this.respCountData[i].datetime)
            _this.respCountData[i].predictdealtime = _this.timestampToTime(_this.respCountData[i].predictdealtime)
          }
        }
      })
    },
    /**
     * 初始化加载数据
     * 接口 /customerManage/validClientCompute
     * 参数： 部门
     *        等级
     *        开始
     *        结束
     *        状态默认为有效
     */
    async loadData () {
      await this.getNameAndDepart()
      await this.getPermission()
      var _this = this
      var paramValidClient = {
        'merchantDepartment': this.department[0],
        'merchantStaff': this.merchantStaff,
        'currentLevel': this.currentLevel[0],
        'start': this.startTime,
        'end': this.endTime,
        'clientstate': '有效'
      }
      this.$axios.post('/customerManage/validClientCompute', paramValidClient).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respCountData = resp.data
          // 用一个for循环遍历全部的时间然后格式化
          for (let i = 0; i < _this.respCountData.length; i++) {
            _this.respCountData[i].datetime = _this.timestampToTime(_this.respCountData[i].datetime)
            _this.respCountData[i].predictdealtime = _this.timestampToTime(_this.respCountData[i].predictdealtime)
          }
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
