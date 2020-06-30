<template>
  <div>
    <br>
      <x-header class="header" :left-options="{showBack: false}">客户管理</x-header>
    <br>
    <div>
        <flexbox>
          <flexbox-item></flexbox-item>
          <flexbox-item></flexbox-item>
          <flexbox-item></flexbox-item>
          <flexbox-item>
            <x-button  mini  :gradients="['#FA8072', '#FA8072']" @click.native="temporartySave">暂存</x-button>
            <!--<x-button  mini  :gradients="['#F4A460', '#F4A460']" @click.native="temporartySave">暂存</x-button>-->
          </flexbox-item>
        </flexbox>
    <group label-width="4.5em" label-margin-right="2em" label-align="right">
      <!--<div v-transfer-dom>-->
      <div>
        <alert v-model="show2" :title="$t('临时保存成功！')" :content="$t('数据临时保存请尽快提交数据！')"></alert>
      </div>
      <cell title="客户名称" style="display:inline-block"></cell>
      <el-autocomplete
        style="display:inline-block; width: auto;"
        class = "autoStyle"
        v-model="clientName"
        :fetch-suggestions="querySearch"
        :trigger-on-focus="false"
        placeholder="请输入客户名称"
        @select="handleSelect"></el-autocomplete>
      <datetime title="当前日期" v-model="datetime" placeholder="请选择时间" value-text-align="left"></datetime>
      <popup-picker :title="clientState1" :data="clientState9" v-model="clientStateTest" @on-change="onChangeClientState" :placeholder="$t('请选择客户状态')" value-text-align="left"></popup-picker>
      <popup-picker :title="clientStyle1" :data="clientStyle9" v-model="clientStyleTest" @on-change="onChangeClientStyle" :placeholder="$t('请选择类型')" value-text-align="left"></popup-picker>
      <popup-picker :title="clientIndustry1" :data="clientIndustry9" v-model="clientIndustryTest" @on-change="onChangeClientIndustry" :placeholder="$t('请选择行业')" value-text-align="left"></popup-picker>
      <x-input title="联系方式" type="number" placeholder="请输入联系方式" v-model="contactNumber" value-text-align="left"></x-input>
      <x-input title="联系人员" placeholder="请输入联系人员" v-model="contactStaff"></x-input>
      <popup-picker title="人员职称" :data="personTitleP6" v-model="personTitleP" placeholder="请选择人员职称" value-text-align="left"></popup-picker>
      <popup-picker title="来源渠道" :data="sourceChannelP2" v-model="sourceChannelP" placeholder="请选择来源渠道" value-text-align="left"></popup-picker>
      <popup-picker title="电话沟通" :data="phoneVisitP3" v-model="phoneVisitP" placeholder="请选择是否沟通" value-text-align="left"></popup-picker>
      <popup-picker title="带&emsp;&emsp;看" :data="takeVisitP4" v-model="takeVisitP" placeholder="请选择是否带看" value-text-align="left"></popup-picker>
      <x-input title="需求面积" type="number" placeholder="请输入需求面积" v-model="needSquare" :disabled="styleQudao" v-if="styleQudao2"></x-input>
      <x-input title="价&emsp;&emsp;格" type="number" placeholder="请输入预估价格" v-model="price" :disabled="styleQudao" v-if="styleQudao2"></x-input>
      <popup-picker title="意向园区" :data="wantParkP2" v-model="wantParkP" placeholder="请选择意向园区" @on-hide="pickPark" value-text-align="left" :disabled="styleQudao" v-if="styleQudao2"></popup-picker>
      <popup-picker title="意向楼座" :data="wantBuildingP2" v-model="wantBuildingP" placeholder="请选择意向楼座" value-text-align="left" :disabled="styleQudao" v-if="styleQudao2"></popup-picker>
      <datetime title="预计成交" v-model="predictDealTime" placeholder="请选择预计时间" value-text-align="left" v-if="styleQudao2"></datetime>
      <x-textarea title="客户情况" placeholder="请填写详细信息" v-model="clientinfo" :show-counter="false" :rows="3"></x-textarea>
      <x-textarea title="所遇困难" placeholder="请填写详细信息" v-model="currentproblem" :show-counter="false" :rows="3"></x-textarea>
      <x-textarea title="需求支持" placeholder="请填写详细信息" v-model="requiredsupport" :show-counter="false" :rows="3"></x-textarea>
      <popup-picker title="当前等级" :data="currentLevelP4" v-model="currentLevelP" placeholder="请选择当前等级" value-text-align="left"></popup-picker>
      <x-input title="招商人员" placeholder="请输入招商人员" disabled="true" v-model="merchantStaff"></x-input>
      <x-input title="招商部门" placeholder="请输入招商部门" disabled="true" v-model="merchantDepartment"></x-input>
    </group>
  </div>
    <div>
      <confirm v-model="show"
               :title="$t('是否加载上次临时保存的数据')"
               @on-cancel="onCancel"
               @on-confirm="onConfirm">
      </confirm>
    </div>
      <box gap="20px 40px">
      <flexbox>
        <flexbox-item><div class="flex-demo"><x-button  @click.native="add">确认修改</x-button></div></flexbox-item>
        <flexbox-item gap="40px 40px"><div class="flex-demo"><x-button type="warn" @click.native="clear">清空</x-button></div></flexbox-item>
      </flexbox>
      </box>
</div>
</template>

<script>import { MessageBox } from 'mint-ui'
import { Search, PopupPicker, XTextarea, Box, Group, PopupRadio, Selector, Datetime, DatetimePlugin, XHeader, XInput, Cell, Loading, XButton, LoadingPlugin, Flexbox, FlexboxItem, Confirm, Alert, Grid, GridItem } from 'vux'
export default {
  components: {
    PopupRadio,
    Group,
    Selector,
    Datetime,
    DatetimePlugin,
    XHeader,
    XInput,
    Cell,
    XTextarea,
    PopupPicker,
    Search,
    Loading,
    XButton,
    LoadingPlugin,
    Flexbox,
    FlexboxItem,
    Box,
    Confirm,
    Alert,
    Grid,
    GridItem
  },
  /** 1.先取得openid用于显示员工信息
      2.客户状态默认为有效
      3.加载园区信息放入列表
      4.加载员工信息
   */
  mounted: function () {
    this.clientState = '有效'
    // this.loadAll()
    this.loadParkData()
    this.loadStaffInfo()
    // 初始化时间为当前日期
    this.initDatetime()
    console.log('print the localstorage :', window.localStorage.getItem('globalOpenid'))
    // 判断临时数据 temporary的缓存是否为空 如果为空 就不提示 如果存在集弹窗询问是否加载上次录入的数据
    this.loadTemporaryData()
  },
  // computed: {
  //   styleQudao: true
  // },
  data: function () {
    return {
      // 测试用
      show: false,
      show2: false,
      styleQudao: false,
      styleQudao2: true,
      value: '',
      test: '运维',
      clientStyle1: '客户类型',
      clientState1: '客户状态',
      clientIndustry1: '客户行业',
      // 固定的下拉框
      currentLevel4: ['A', 'B', 'C'],
      currentLevelP4: [['A', 'B', 'C']],
      sourceChannel2: ['中介推介', '微信推介', '网络广告', '楼宇广告', '全员招商', '其他来源'],
      sourceChannelP2: [['中介推介', '微信推介', '网络广告', '楼宇广告', '全员招商', '其他来源']],
      phoneVisit3: ['是', '否'],
      phoneVisitP3: [['是', '否']],
      takeVisit4: ['是', '否'],
      takeVisitP4: [['是', '否']],
      personTitle6: ['经理级以下', '经理级-总监级', '总监级以上'],
      personTitleP6: [['经理级以下', '经理级-总监级', '总监级以上']],
      clientStyle7: ['客户', '渠道'],
      clientStyle8: ['客户', '渠道'],
      clientStyle9: [['客户', '渠道']],
      clientIndustry9: [['物流行业', '汽车行业', '信息技术行业', '加盟行业', '医美行业', '其他行业']],
      clientState9: [['有效', '不活跃']],
      // 接口请求数据预留
      wantPark2: [],
      wantParkP2: [],
      wantBuilding2: [],
      wantBuildingP2: [],
      parkList: null,
      buildingList: null,
      openid: null,
      // 数据字段
      datetime: null,
      clientName: null,
      clientState: null,
      clientIndustry: null,
      clientStateTest: ['有效'],
      clientStyle: null,
      clientStyleTest: [],
      clientIndustryTest: [],
      clientStyle2: null,
      contactNumber: null,
      contactStaff: null,
      sourceChannel: null,
      sourceChannelP: [],
      phoneVisit: null,
      phoneVisitP: [],
      takeVisit: null,
      takeVisitP: [],
      personTitle: null,
      personTitleP: [],
      needSquare: null,
      price: null,
      wantPark: null,
      wantParkP: [],
      wantBuilding: null,
      wantBuildingP: [],
      predictDealTime: null,
      currentproblem: null,
      clientinfo: null,
      requiredsupport: null,
      currentLevel: null,
      currentLevelP: [],
      merchantStaff: null,
      merchantDepartment: null,
      // loadAll加载进来的数据
      content: [],
      // 接口url
      parkUrl: 'https://dropdbandescape.parkwing.cn/room/parkinfo/name/list',
      buildingUrl: 'https://dropdbandescape.parkwing.cn/room/parkinfo/pageList',
      staffInforUrl: 'https://dropdbandescape.parkwing.cn/codiv/role_employee/details'
    }
  },
  methods: {
    onCancel () {
      console.log('on cancel')
    },
    /**
     *如果确认加载上次书写的数据 那就一个一个放进去
     */
    onConfirm () {
      console.log('on confirm')
      var tempData = window.localStorage.getItem('temporary')
      var tempDataJSON = JSON.parse(tempData)
      console.log('print the tempDataJSON', tempDataJSON)
      this.clientName = tempDataJSON.clientName
      this.datetime = tempDataJSON.dateTime
      this.clientState = tempDataJSON.clientstate
      // 在加载列表数据时要先判断是否为空 为空就不赋值了 否则在选择时会有bug
      if (tempDataJSON.clientStyle) {
        this.clientStyleTest.push(tempDataJSON.clientStyle)
      }
      if (tempDataJSON.clientindustry) {
        this.clientIndustryTest.push(tempDataJSON.clientindustry) // 存疑
      }
      this.contactNumber = tempDataJSON.contactNumber
      this.contactStaff = tempDataJSON.contactStaff
      if (tempDataJSON.personTitle) {
        this.personTitleP.push(tempDataJSON.personTitle)
      }
      if (tempDataJSON.sourceChannel) {
        this.sourceChannelP.push(tempDataJSON.sourceChannel)
      }
      if (tempDataJSON.phoneVisit) {
        this.phoneVisitP.push(tempDataJSON.phoneVisit)
      }
      if (tempDataJSON.takeVisit) {
        this.takeVisitP.push(tempDataJSON.takeVisit)
      }
      this.needSquare = tempDataJSON.needSquare
      this.price = tempDataJSON.price
      if (tempDataJSON.wantPark) {
        this.wantParkP.push(tempDataJSON.wantPark)
      }
      if (tempDataJSON.wantBuilding) {
        this.wantBuildingP.push(tempDataJSON.wantBuilding)
      }
      this.predictDealTime = tempDataJSON.predictdealtime
      this.clientinfo = tempDataJSON.clientinfo // 模糊查询时新增字段
      this.currentproblem = tempDataJSON.currentproblem
      this.requiredsupport = tempDataJSON.requiredsupport
      if (tempDataJSON.currentLevel) {
        this.currentLevelP.push(tempDataJSON.currentLevel)
      }
      this.merchantStaff = tempDataJSON.merchantStaff
      this.merchantDepartment = tempDataJSON.merchantDepartment
    },
    /**
     * 页面加载时 判断是否存在缓存 如果存在就弹窗提示是否要加载上次录入的数据
     */
    loadTemporaryData () {
      // console.log('pritn the data:', window.localStorage.getItem('temporary'))
      if (window.localStorage.getItem('temporary') !== null) {
        this.show = true
      }
    },
    /**
     * 临时保存当前写好的数据到缓存中
     */
    temporartySave () {
      this.show2 = true
      // 临时保存的数据对象
      let data = {
        dateTime: this.datetime,
        clientName: this.clientName,
        // clientStyle: this.clientStyle,
        clientStyle: this.clientStyleTest[0],
        clientindustry: this.clientIndustryTest[0],
        contactNumber: this.contactNumber,
        contactStaff: this.contactStaff,
        // personTitle: this.personTitle,
        personTitle: this.personTitleP[0],
        // sourceChannel: this.sourceChannel,
        sourceChannel: this.sourceChannelP[0],
        // phoneVisit: this.phoneVisit,
        phoneVisit: this.phoneVisitP[0],
        // takeVisit: this.takeVisit,
        takeVisit: this.takeVisitP[0],
        needSquare: this.needSquare,
        price: this.price,
        // wantBuilding: this.wantBuilding,
        wantBuilding: this.wantBuildingP[0],
        // wantPark: this.wantPark,
        wantPark: this.wantParkP[0],
        predictdealtime: this.predictDealTime,
        currentproblem: this.currentproblem,
        requiredsupport: this.requiredsupport,
        // currentLevel: this.currentLevel,
        currentLevel: this.currentLevelP[0],
        // currentLevel: '123',
        merchantStaff: this.merchantStaff,
        merchantDepartment: this.merchantDepartment,
        clientstate: this.clientStateTest[0],
        clientinfo: this.clientinfo // 新增数据时候的 新增字段 后台也要对应有
      }
      // console.log('pirnt the data:', data)
      window.localStorage.setItem('temporary', JSON.stringify(data))
      // console.log('temporary data:', window.localStorage.getItem('temporary'))
      // console.log('temporary data:', window.localStorage.getItem('dateTime'))
      // var tempData = window.localStorage.getItem('temporary')
      // var tempDataJSON = JSON.parse(tempData)
      // console.log('111111111111', tempDataJSON)
      // console.log('222222222', tempDataJSON.dateTime)
      // console.log('print the tempData:', tempData)
      // console.log('print the tempData dateTime:', tempData.get('dataTime'))
    },
    /**
     * 初始化时间
     */
    initDatetime () {
      // var d = new Date(Date.now() + 1 * 24 * 60 * 60 * 1000)
      var d = new Date(Date.now())
      var month = d.getMonth() + 1
      var date = d.getDate()
      if (d.getMonth() + 1 < 10) {
        month = '0' + month
      }
      if (d.getDate() + 1 < 10) {
        date = '0' + date
      }
      this.datetime = d.getFullYear() + '-' + month + '-' + date
    },
    /**
     * 时间格式化
     */
    timestampToTime (timestamp) {
      var date = new Date(timestamp)// 时间戳为10位需*1000，时间戳为13位的话不需乘1000
      var Y = date.getFullYear() + '-'
      var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
      // var D = date.getDate() + ' '
      var D = date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()
      // var h = date.getHours() + ':'
      // var m = date.getMinutes() + ':'
      // var s = date.getSeconds()
      return Y + M + D
    },
    // 测试用
    onChangeClientState (val) {
      this.clientState = val
    },
    onChangeClientStyle (val) {
      this.clientStyle = val
      // console.log('print hte val:', val)
      // console.log('print hte this.clientStyle[0]:', this.clientStyle[0])
      if (this.clientStyle[0] === '渠道') {
        this.styleQudao = true
        this.styleQudao2 = false
      } else {
        this.styleQudao = false
        this.styleQudao2 = true
      }
    },
    onChangeClientIndustry (val) {
      this.clientIndustry = val
    },
    datetimeConfirm (val) {
      this.datetime = val
    },
    datetimechange (val) {
      this.datetime = val
    },
    /**
     * 添加判断：
     * 1. 如果当前用户和这条记录的输入用户信息不一致就不触发这个自动获得的功能
     */
    handleSelect (item) {
      if (this.merchantStaff !== item.merchantStaff) {
        this.clear()
        // console.log('print the merchantstaff the same:', this.merchantStaff !== item.merchantStaff)
        this.clientName = item.clientname
        return
      }
      // 先清空
      this.clear()
      this.sourceChannelP.push(item.sourceChannel)
      this.phoneVisitP.push(item.phoneVisit)
      this.takeVisitP.push(item.takeVisit)
      this.personTitleP.push(item.personTitle)
      this.clientStyleTest.push(item.clientStyle)
      this.clientIndustryTest.push(item.clientindustry)
      this.datetime = this.timestampToTime(item.datetime)
      this.clientName = item.clientname
      this.contactNumber = item.contactNumber
      this.contactStaff = item.contactStaff
      this.needSquare = item.needSquare
      this.price = item.price
      this.wantParkP.push(item.wantPark1)
      this.pickPark()
      this.wantBuildingP[0] = (item.wantBuilding1)
      if (item.predictdealtime1 === '') {
        this.predictDealTime = null
      } else {
        this.predictDealTime = this.timestampToTime(item.predictdealtime1)
      }
      this.currentproblem = item.currentproblem1
      this.requiredsupport = item.requiredsupport1
      this.currentLevelP.push(item.currentLevel)
      this.merchantStaff = item.merchantStaff
      this.merchantDepartment = item.merchantDepartment
      this.clientstate = item.clientstate
      this.clientinfo = item.clientinfo1 // 模糊查询时新增字段
    },
    /**
     *el-autocomplete实现模糊查询
     */
    querySearch (queryString, cb) {
      console.log('打印下发送的queryString是什么', queryString)
      var paramQuery = {
        'keywords': queryString,
        'merchantStaff': this.merchantStaff
      }
      this.$axios.post('/customerManage/search', paramQuery).then((resp) => {
        console.log('打印下发送的paramQuery是什么', paramQuery)
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          var tempResult = resp.data
          for (let i = 0; i < resp.data.length; i++) {
            tempResult[i].value = resp.data[i].clientname
          }
          cb(tempResult)
        }
      })
    },
    /** 预加载全部的数据到本地
     *  1.向后台发送请求 全部数据
     *  2.添加value字段 el-autocomplete用于展示
     *  3.将添加了value的全部数据赋值给content */
    loadAll: function () {
      var _this = this
      this.$axios.get('/customerManage/all').then(function (resp) {
        if (resp.status === 200) {
          let j = 0
          for (let i of resp.data) {
            i.value = resp.data[j].clientName
            j++
            if (j === resp.data.length) {
              break
            }
          }
          _this.content = resp.data
          // return resp.data
        }
      }, function (err) {
        console.log(err)
      })
    },
    /** 开启选择楼座触发
        如果园区的值为待定 就值为空 */
    pickBuilding: function (val) {
      this.wantBuilding = null
      if (val === '待定') {
        this.wantBuilding = '待定'
      }
    },
    /** 根据已经选好的园区信息去接口请求楼座的信息
        1.先把楼座的列表置空  避免上次的楼座信息残留
        2.根据园区的数据 发送请求获得对应的楼座数据
        3.加载进楼座列表 */
    pickPark () {
      this.buildingList = []
      this.wantBuildingP2 = []
      var param = {
        'limit': 100,
        'parkname': this.wantParkP[0],
        'page': 1
      }
      this.$axios.post(this.buildingUrl, param).then((resp) => {
        // this.slots1[0].values = resp.data.list
        for (let i = 0; i < resp.data.list.length; i++) {
          this.buildingList.push(resp.data.list[i].buildname)
        }
        this.buildingList.push('待定') // 增加待定和取消
        this.wantBuildingP2.push(this.buildingList)
        console.log('print the wantBuildingP2', this.wantBuildingP2)
        console.log('print the buildingList', this.buildingList)
      })
      if (this.wantParkP[0] === '待定') {
        this.wantBuilding = '待定'
        this.wantBuildingP = ['待定']
      }
    },
    /** 根据获得的openid
     *  去接口获取员工信息 */
    loadStaffInfo: function () {
      this.openid = this.$route.params.openid
      console.log('print the OpenID', this.$route.params.token)
      var param = {'openid': this.openid}
      this.$axios.post(this.staffInforUrl, param).then((resp) => {
        if (resp.status === 200) {
          // 如果状态200 就赋值部门和园区信息
          this.merchantStaff = resp.data.list[0].name
          this.merchantDepartment = resp.data.list[0].deptname
          // console.log('打印一下部门和园区信息', this.merchantStaff, this.merchantDepartment )
        }
      })
    },
    /** 加载园区信息
     *  加载进园区列表中直接展现 */
    loadParkData: function () {
      this.$http.get(this.parkUrl).then((resp) => {
        if (resp.status === 200) {
          // 状态200将园区信息赋值到列表
          // this.wantPark2 = resp.data.data.rows
          // console.log('print the real park info:', resp.data.data)
          this.wantParkP2.push(resp.data.data.rows)
          console.log('111111111111111', resp.data.data.rows)
          // this.wantParkP = resp.data.data
          this.wantParkP2[0].push('待定') // 增加待定和取消
          console.log('this.wantParkP2:', this.wantParkP2)
        }
      })
    },
    /** 返回按钮方法 */
    back: function () {
      this.$router.push({
        path: '/home'
      })
    },
    /** 新增按钮方法 */
    add: function () {
      // 将所有数值赋给即将发送到后台的data
      let data = {
        dateTime: this.datetime,
        clientName: this.clientName,
        clientStyle: this.clientStyleTest[0],
        clientindustry: this.clientIndustryTest[0],
        contactNumber: this.contactNumber,
        contactStaff: this.contactStaff,
        personTitle: this.personTitleP[0],
        sourceChannel: this.sourceChannelP[0],
        phoneVisit: this.phoneVisitP[0],
        takeVisit: this.takeVisitP[0],
        needSquare: this.needSquare,
        price: this.price,
        wantBuilding: this.wantBuildingP[0],
        wantPark: this.wantParkP[0],
        predictdealtime: this.predictDealTime,
        currentproblem: this.currentproblem,
        requiredsupport: this.requiredsupport,
        currentLevel: this.currentLevelP[0],
        merchantStaff: this.merchantStaff,
        merchantDepartment: this.merchantDepartment,
        clientstate: this.clientStateTest[0],
        clientinfo: this.clientinfo // 新增数据时候的 新增字段 后台也要对应有
      }
      /** 判断所有的必填项是否填好了
       *  数字最多8位保留两位小数
       *  */
      if (this.clientName === null) {
        MessageBox.alert('客户名称不得为空').then(action => {

        })
        return
      }
      if (this.datetime === null) {
        MessageBox.alert('日期不得为空').then(action => {

        })
        return
      }
      if (this.clientStyleTest.length === 0) {
        MessageBox.alert('客户类型不得为空').then(action => {

        })
        return
      }
      if (this.clientIndustryTest.length === 0) {
        MessageBox.alert('客户行业不得为空').then(action => {

        })
        return
      }
      if (this.contactNumber === null) {
        MessageBox.alert('联系方式不得为空').then(action => {

        })
        return
      }
      /** 判断手机号码的长度是否符合要求 */
      if (this.contactNumber == null || this.contactNumber.length !== 11) {
        MessageBox.alert('请输入正确手机号').then(action => {
        })
        return
      }
      if (this.contactStaff === null) {
        MessageBox.alert('联系人员不得为空').then(action => {

        })
        return
      }
      if (this.personTitleP.length === 0) {
        MessageBox.alert('人员职称不得为空').then(action => {

        })
        return
      }
      // if (this.sourceChannel === null) {
      //   MessageBox.alert('来源渠道不得为空').then(action => {
      //
      //   })
      //   return
      // }
      if (this.sourceChannelP.length === 0) {
        MessageBox.alert('来源渠道不得为空').then(action => {

        })
        return
      }
      // if (this.phoneVisit === null) {
      //   MessageBox.alert('电话沟通不得为空').then(action => {
      //
      //   })
      //   return
      // }
      if (this.phoneVisitP.length === 0) {
        MessageBox.alert('电话沟通不得为空').then(action => {

        })
        return
      }
      // if (this.takeVisit === null) {
      //   MessageBox.alert('带看不得为空').then(action => {
      //
      //   })
      //   return
      // }
      if (this.takeVisitP.length === 0) {
        MessageBox.alert('带看不得为空').then(action => {

        })
        return
      }
      if (this.clientStyleTest[0] !== '渠道' && this.needSquare === null) {
        MessageBox.alert('需求面积不得为空').then(action => {

        })
        return
      }

      if (this.clientStyleTest[0] !== '渠道' && this.needSquare === '0') {
        MessageBox.alert('需求面积不得为0').then(action => {

        })
        return
      }

      /** 判断两位小数 */
      if (this.clientStyleTest[0] !== '渠道' && !/^(?!0+$)(?!0*\.0*$)\d{1,8}(\.\d{1,2})?$/.test(this.needSquare)) {
        MessageBox.alert('请输入正确的需求面积(请保留两位小数)').then(action => {

        })
        return
      }
      if (this.clientStyleTest[0] !== '渠道' && this.price === null) {
        MessageBox.alert('价格不得为空').then(action => {

        })
        return
      }

      if (this.clientStyleTest[0] !== '渠道' && this.price === '0') {
        MessageBox.alert('价格不得为0').then(action => {

        })
        return
      }
      /**
      *保留两位小数
       * 最多8位数字
      */
      if (this.clientStyleTest[0] !== '渠道' && !/^(?!0+$)(?!0*\.0*$)\d{1,8}(\.\d{1,2})?$/.test(this.price)) {
        MessageBox.alert('请输入正确价格(请保留两位小数)').then(action => {

        })
        return
      }
      // if (this.wantPark === null) {
      //   MessageBox.alert('意向园区不得为空').then(action => {
      //
      //   })
      //   return
      // }
      // console.log('the content of wantParkP', this.wantParkP)
      if (this.clientStyleTest[0] !== '渠道' && this.wantParkP.length === 0 && this.wantParkP !== []) {
        MessageBox.alert('意向园区不得为空').then(action => {

        })
        return
      }
      // if (this.wantBuilding === null) {
      //   MessageBox.alert('意向楼座不得为空').then(action => {
      //
      //   })
      //   return
      // }
      if (this.clientStyleTest[0] !== '渠道' && this.wantBuildingP.length === 0 && this.wantBuildingP !== []) {
        MessageBox.alert('意向楼座不得为空').then(action => {

        })
        return
      }
      if (this.currentLevelP.length === 0) {
        MessageBox.alert('当前等级不得为空').then(action => {

        })
        return
      }
      /** 向后台发送数据 */
      this.$axios.post('/customerManage', data).then(function (resp) {
        console.log(resp)
        if (resp.status === 200) {
          MessageBox.alert('添加成功！').then(action => {
          })
          // 提交成功后清除缓存
          window.localStorage.removeItem('temporary')
          console.log('print the remove data', window.localStorage)
        }
      },
      function (err) {
        console.log(err)
      })
    },
    /** 清空所有的已填入的数据 */
    clear: function () {
      console.log('清空')
      this.datetime = null
      this.clientName = null
      // this.clientStyle = null
      this.clientStyleTest = []
      this.clientIndustryTest = []
      this.clientstate = '有效'
      this.contactNumber = null
      this.contactStaff = null
      this.personTitleP = []
      this.sourceChannelP = []
      this.phoneVisitP = []
      this.takeVisitP = []
      this.needSquare = null
      this.predictDealTime = null
      this.currentproblem = null
      this.wantParkP = []
      // 清空的时候需要把楼座的列表也清空
      this.wantBuilding2 = []
      this.wantBuilding = null
      this.wantBuildingP2 = []
      this.wantBuildingP = []
      this.requiredsupport = null
      this.price = null
      this.currentLevelP = []
      this.clientinfo = null
    }
  }
}
</script>

<style>
  /*.header{*/
    /*margin-top: -1.5em;*/
  /*}*/
  .autoStyle{
    margin: 0.5em 0.1em 0.5em -1em;
  }
</style>
