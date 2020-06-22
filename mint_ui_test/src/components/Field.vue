<template>
    <div>
  <!--<mt-header fixed title="营销团队工作汇总"></mt-header>-->
  <br>
  <x-header class="header" :left-options="{showBack: false}">客户管理</x-header>
  <br>
  <div>
    <!--<search-->
      <!--placeholder="请输入客户名称"-->
      <!--@result-click="resultClick"-->
      <!--@on-change="getResult"-->
      <!--:results="resultsList"-->
      <!--v-model="value"-->
      <!--top="46px"-->
      <!--ref="search"></search>-->
    <group label-width="4.5em" label-margin-right="2em" label-align="right">
    <!--<group label-width="4.5em" label-margin-right="2em" label-align="right">-->
      <cell title="客户名称" style="display:inline-block"></cell>
      <el-autocomplete
        style="display:inline-block; width: auto;"
        class = "autoStyle"
        v-model="clientName"
        :fetch-suggestions="querySearch"
        :trigger-on-focus="false"
        placeholder="请输入客户名称"
        @select="handleSelect"></el-autocomplete>
      <!--<x-input title="客户名称" v-model="value1"></x-input>-->
      <datetime title="当前日期" v-model="datetime" placeholder="请选择时间" value-text-align="left"></datetime>
      <!--<x-input title="客户状态" placeholder="请输入客户状态" v-model="clientState"></x-input>-->
      <popup-picker :title="clientState1" :data="clientState9" v-model="clientStateTest" @on-change="onChangeClientState" :placeholder="$t('请选择客户状态')" value-text-align="left"></popup-picker>
      <popup-picker :title="clientStyle1" :data="clientStyle9" v-model="clientStyleTest" @on-change="onChangeClientStyle" :placeholder="$t('请选择类型')" value-text-align="left"></popup-picker>
      <!--<popup-radio class="radioStyle" title="客户类型" :options="clientStyle8" v-model="clientStyle" placeholder="请选择类型" value-text-align="left"></popup-radio>-->
      <x-input title="联系方式" type="number" placeholder="请输入联系方式" v-model="contactNumber" value-text-align="left"></x-input>
      <!--<x-input title="手机号码" name="mobile" placeholder="请输入手机号码" keyboard="number" is-type="china-mobile"></x-input>-->
      <x-input title="联系人员" placeholder="请输入联系人员" v-model="contactStaff"></x-input>
      <popup-picker title="人员职称" :data="personTitleP6" v-model="personTitleP" placeholder="请选择人员职称" value-text-align="left"></popup-picker>
      <!--<popup-radio title="人员职称" :options="personTitle6" v-model="personTitle" placeholder="请选择人员职称" value-text-align="left"></popup-radio>-->
      <popup-picker title="来源渠道" :data="sourceChannelP2" v-model="sourceChannelP" placeholder="请选择来源渠道" value-text-align="left"></popup-picker>
      <!--<popup-radio title="来源渠道" :options="sourceChannel2" v-model="sourceChannel" placeholder="请选择来源渠道"></popup-radio>-->
      <popup-picker title="电话沟通" :data="phoneVisitP3" v-model="phoneVisitP" placeholder="请选择是否沟通" value-text-align="left"></popup-picker>
      <!--<popup-radio title="电话沟通" :options="phoneVisit3" v-model="phoneVisit" placeholder="请选择是否电话沟通"></popup-radio>-->
      <popup-picker title="带&emsp;&emsp;看" :data="takeVisitP4" v-model="takeVisitP" placeholder="请选择是否带看" value-text-align="left"></popup-picker>
      <!--<popup-radio title="是否带看" :options="takeVisit4" v-model="takeVisit" placeholder="请选择是否带看"></popup-radio>-->
      <x-input title="需求面积" type="number" placeholder="请输入需求面积" v-model="needSquare" :disabled="styleQudao" v-if="styleQudao2"></x-input>
      <x-input title="价&emsp;&emsp;格" type="number" placeholder="请输入预估价格" v-model="price" :disabled="styleQudao" v-if="styleQudao2"></x-input>
      <popup-picker title="意向园区" :data="wantParkP2" v-model="wantParkP" placeholder="请选择意向园区" @on-hide="pickPark" value-text-align="left" :disabled="styleQudao" v-if="styleQudao2"></popup-picker>
      <!--<popup-radio title="意向园区" :options="wantPark2" v-model="wantPark" placeholder="请选择意向园区"  @on-hide="pickPark"></popup-radio>-->
      <popup-picker title="意向楼座" :data="wantBuildingP2" v-model="wantBuildingP" placeholder="请选择意向楼座" value-text-align="left" :disabled="styleQudao" v-if="styleQudao2"></popup-picker>
      <!--<popup-radio title="意向楼座" :options="wantBuilding2" v-model="wantBuilding" placeholder="请选择意向楼座" @on-show="pickBuilding(wantPark)"></popup-radio>-->
      <datetime title="预计成交" v-model="predictDealTime" placeholder="请选择预计时间" value-text-align="left" v-if="styleQudao2"></datetime>
      <x-textarea title="客户情况" placeholder="请填写详细信息" v-model="clientinfo" :show-counter="false" :rows="3"></x-textarea>
      <x-textarea title="所遇困难" placeholder="请填写详细信息" v-model="currentproblem" :show-counter="false" :rows="3"></x-textarea>
      <x-textarea title="需求支持" placeholder="请填写详细信息" v-model="requiredsupport" :show-counter="false" :rows="3"></x-textarea>
      <popup-picker title="当前等级" :data="currentLevelP4" v-model="currentLevelP" placeholder="请选择当前等级" value-text-align="left"></popup-picker>
      <!--<popup-radio title="当前等级" :options="currentLevel4" v-model="currentLevel" placeholder="请选择当前等级" value-text-align="left"></popup-radio>-->
      <x-input title="招商人员" placeholder="请输入招商人员" disabled="true" v-model="merchantStaff"></x-input>
      <x-input title="招商部门" placeholder="请输入招商部门" disabled="true" v-model="merchantDepartment"></x-input>
    </group>
    <!--尝试下新的表单-->
  </div>
  <div class="container" style="display:inline-block">
    <!--提交时判断必选项，除了预计成交时间 困难 需求支持，其余都为必须 还需要判断面积和价格的两位小数-->
    <mt-button class="confirmButton"  style="color: #FFFFFF" type="primary" @click.native="add">确认修改</mt-button>
  </div>
  <div class="content" style="display:inline-block">
    <mt-button class="clearButton"  type="danger" @click.native="clear">清空</mt-button>
  </div>
      <!--<div style="padding: 15px;">-->
        <!--<x-button @click.native="showLoading" type="primary">{{ $t('Show loading') }}</x-button>-->
      <!--</div>-->
</div>
</template>

<script>import { MessageBox } from 'mint-ui'
import { Search, PopupPicker, XTextarea, Group, PopupRadio, Selector, Datetime, DatetimePlugin, XHeader, XInput, Cell, Loading, XButton, LoadingPlugin } from 'vux'
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
    LoadingPlugin
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
  },
  // computed: {
  //   styleQudao: true
  // },
  data: function () {
    return {
      // 测试用
      styleQudao: false,
      styleQudao2: true,
      resultsList: ['A', 'B', 'C', 'D'],
      results: [{'熊大': 1}],
      value: '',
      test: '运维',
      clientStyle1: '客户类型',
      clientState1: '客户状态',
      // 固定的下拉框
      currentLevel4: ['A', 'B', 'C'],
      currentLevelP4: [['A', 'B', 'C']],
      // currentLevel4: ['小米', 'iPhone', '华为', '情怀', '三星', '其他', '不告诉你'],
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
      clientStateTest: ['有效'],
      clientStyle: null,
      clientStyleTest: [],
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
    /**
     * 测试用
     */
    showLoading () {
      this.$vux.loading.show({
        text: 'Loading'
      })
      setTimeout(() => {
        this.$vux.loading.hide()
      }, 2000)
    },
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
      console.log('print hte val:', val)
      console.log('print hte this.clientStyle[0]:', this.clientStyle[0])
      if (this.clientStyle[0] === '渠道') {
        console.log('stop the conetent square')
        this.styleQudao = true
        this.styleQudao2 = false
      } else {
        this.styleQudao = false
        this.styleQudao2 = true
      }
    },
    datetimeConfirm (val) {
      console.log('打印下condirm时间：', val)
      this.datetime = val
    },
    datetimechange (val) {
      console.log('打印下时间：', val)
      this.datetime = val
    },
    log (str1, str2 = '') {
      console.log(str1, str2)
      // console.log('当前时间：', this.datetime)
    },
    /**
     * 添加判断：
     * 1. 如果当前用户和这条记录的输入用户信息不一致就不触发这个自动获得的功能
     */
    handleSelect (item) {
      // this.showLoading()
      if (this.merchantStaff !== item.merchantStaff) {
        this.clear()
        // console.log('print the merchantstaff the same:', this.merchantStaff !== item.merchantStaff)
        this.clientName = item.clientname
        return
      }
      // 先清空
      this.clear()
      // console.log('item是什么')
      // console.log(item)
      // this.sourceChannel = item.sourceChannel
      this.sourceChannelP.push(item.sourceChannel)
      // this.phoneVisit = item.phoneVisit
      this.phoneVisitP.push(item.phoneVisit)
      // this.takeVisit = item.takeVisit
      this.takeVisitP.push(item.takeVisit)
      // this.personTitle = item.personTitle
      this.personTitleP.push(item.personTitle)
      // this.clientStyle = item.clientStyle
      this.clientStyleTest.push(item.clientStyle)
      this.datetime = this.timestampToTime(item.datetime)
      // console.log('打印一下这个赋值后的日期')
      // console.log(this.datetime)
      this.clientName = item.clientname
      this.contactNumber = item.contactNumber
      this.contactStaff = item.contactStaff
      this.needSquare = item.needSquare
      this.price = item.price
      // this.wantPark = item.wantPark
      console.log('print the item.wantPark', item.wantPark)
      this.wantParkP.push(item.wantPark)
      this.pickPark()
      // this.wantBuilding = item.wantBuilding
      this.wantBuildingP[0] = (item.wantBuilding)
      // this.wantBuildingP.push(item.wantBuilding)
      // console.log('打印一下这个赋值后的楼座')
      // console.log(this.wantBuilding)
      this.predictDealTime = this.timestampToTime(item.predictdealtime)
      this.currentproblem = item.currentproblem
      this.requiredsupport = item.requiredsupport
      // this.currentLevel = item.currentLevel
      this.currentLevelP.push(item.currentLevel)
      this.merchantStaff = item.merchantStaff
      this.merchantDepartment = item.merchantDepartment
      this.clientstate = item.clientstate
      this.clientinfo = item.clientinfo // 模糊查询时新增字段
    },
    /**
     *el-autocomplete实现模糊查询
     */
    querySearch (queryString, cb) {
      console.log('打印下发送的queryString是什么', queryString)
      // this.$axios.post('http://localhost:8443/api/search?keywords=' + queryString).then((resp) => {
      var paramQuery = {
        'keywords': queryString

      }
      console.log('打印下发送的paramQuery是什么', paramQuery)
      // this.$axios.post('http://localhost:6501/customerManage/search', queryString).then((resp) => {
      // this.$axios.post('http://localhost:6501/customerManage/search', paramQuery).then((resp) => {
      // this.$axios.post('http://dropdbandescape.parkwing.cn/codiv/customerManage/search', paramQuery).then((resp) => {
      this.$axios.post('/customerManage/search', paramQuery).then((resp) => {
        console.log('打印下发送的paramQuery是什么', paramQuery)
        if (resp.status === 200) {
          // var temp = [] // 用于临时存储数据
          var tempResult = [] // 用于存储最终的呈现列表
          console.log('打印一下模糊查询的返回值', resp.data)
          // 遍历数组 1.先判断是否有属于自己的客户 有就留下来 2.再次遍历 如果客户已经存在了就不放进去否则就放进去
          // 这里还要添加一次重复判断 万一都是自己的客户但是是重复的
          for (let i = 0; i < resp.data.length; i++) {
            if (tempResult.length !== 0 && resp.data[i].merchantStaff === this.merchantStaff) { // 长度不为零且是自己客户要看下这个客户是否已经存在了
              for (let j = 0; j < tempResult.length; j++) {
                if (tempResult[j].clientname === resp.data[i].clientname) { // 客户名已经存在就跳过
                  console.log('跳过的重复的客户名：', resp.data[i].clientname)
                  break
                }
              }
            } else if (tempResult.length === 0 && resp.data[i].merchantStaff === this.merchantStaff) {
              tempResult.push(resp.data[i])
              console.log('成功进入的客户名：', resp.data[i].clientname)
            }
            // if (resp.data[i].merchantStaff === this.merchantStaff) {
            //   tempResult.push(resp.data[i])
            //   // tempResult[i].value = resp.data[i].clientname
            // }
          }
          console.log('print 是自己的客户:', tempResult)
          /**
           * 1.先判断tempResult是否存在长度
           *    如果存在长度    遍历resp.data 由于是倒序排列 所以只需要留下第一条 第二条同样的用户名就删掉
           *    如果不存在长度   就先插入第一条数据
           */
          if (tempResult.length !== 0) {
            // tempResult.push(resp.data[0])
            console.log('print the no-null tempResult')
            for (let i = 0; i < resp.data.length; i++) {
              for (let j = 0; j < tempResult.length; j++) {
                if (resp.data[i].clientname === tempResult[j].clientname) {
                  console.log('print the same clientname:', resp.data[i].clientname)
                  break
                } else {
                  console.log('print the diff clientname:', resp.data[i].clientname)
                  if (j === tempResult.length - 1) {
                    tempResult.push(resp.data[i])
                  }
                }
              }
            }
          } else {
            console.log('print the null tempResult')
            tempResult.push(resp.data[0])
            for (let i = 0; i < resp.data.length; i++) {
              for (let j = 0; j < tempResult.length; j++) {
                if (resp.data[i].clientname === tempResult[j].clientname) {
                  console.log('print the same clientname:', resp.data[i].clientname)
                  break
                } else {
                  console.log('print the diff clientname:', resp.data[i].clientname)
                  if (j === tempResult.length - 1) {
                    tempResult.push(resp.data[i])
                  }
                }
              }
            }
          }
          // for (let i = 0; i < resp.data.length; i++) {
          //   for (let j = 0; j < tempResult.length; j++) {
          //     if (resp.data[i].clientname === tempResult[j].clientname) {
          //       console.log('print the same clientname:', resp.data[i].clientname)
          //       break
          //     } else {
          //       console.log('print the diff clientname:', resp.data[i].clientname)
          //       if (j === tempResult.length - 1) {
          //         tempResult.push(resp.data[i])
          //       }
          //     }
          //   }
          // }
          if (tempResult[0] !== undefined) {
            console.log('print the new tempResult length:', tempResult.length)
            console.log('print the new tempResult:', tempResult)
            for (let i = 0; i < tempResult.length; i++) {
              tempResult[i].value = tempResult[i].clientname
            }
            console.log('pritn has the value:', tempResult)
            cb(tempResult)
          } else {
            var tempNull = [{value: '查无此人'}]
            cb(tempNull)
          }
          // console.log('print the new tempResult:', tempResult)
          // for (let i = 0; i < tempResult.length; i++) {
          //   tempResult[i].value = tempResult[i].clientname
          // }
          // cb(tempResult)
          // var j = 0
          // for (let i of temp) {
          //   i.value = temp[j].clientname
          //   j++
          // }
          // console.log('222222222:', resp.data)
          // var j = 0
          // for (let i of resp.data) {
          //   i.value = resp.data[j].clientname
          //   j++
          // }
          // // console.log('222222222:', resp.data)
          // cb(resp.data)
          /**
           * 新建一个temp 存储已经存在的clientname
           * 每次进来一个数据就判断是否在temp中
           */
          // var temp = []
          // for (let i of resp.data) {
          //   console.log('i:', i.clientName)
          //   // for (let j = 0; j < temp.length; j++) {
          //   //   if(temp[j] == re){
          //   //
          //   //   }
          //   // }
          // }
          // let j = 0
          // var temp = []
          // for (let i of resp.data) {
          //   // var temp = []
          //   i.value = resp.data[j].clientName
          //   /**
          //    * 遍历temp 判断是否存在重复
          //    * 如果存在重复就从resp.data中删除
          //    * 这里有break和没有break差距很大 是为什么
          //    */
          //   for (let m = 0; m < temp.length; m++) {
          //     console.log('temp[m] === i.value值为')
          //     console.log(temp[m] === i.value)
          //     if (temp[m] === i.value) {
          //       resp.data.splice(j, j + 1)
          //       console.log('resp.data是什么')
          //       console.log(resp.data)
          //       break
          //     }
          //   }
          //   temp.push(i.value)
          //   j++
          //   if (j === resp.data.length) {
          //     break
          //   }
          // }
          // cb(resp.data)
        }
      })
    },
    /** 预加载全部的数据到本地
     *  1.向后台发送请求 全部数据
     *  2.添加value字段 el-autocomplete用于展示
     *  3.将添加了value的全部数据赋值给content */
    loadAll: function () {
      var _this = this
      // this.$http.get('http://localhost:8443/api/data').then(function (resp) {
      // 这里要改成服务器所在的端口
      // this.$http.get('http://dropdbandescape.parkwing.cn/codiv/customerManage/all').then(function (resp) {
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
      // console.log('print the wantPark', this.wantPark)
      // console.log('print the wantParkP[0]', this.wantParkP[0])
      // var param = {
      //   'limit': 100,
      //   'parkname': this.wantPark,
      //   'page': 1
      // }
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
        // clientStyle: this.clientStyle,
        clientStyle: this.clientStyleTest[0],
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
      // if (this.clientStyle === null) {
      //   MessageBox.alert('类型不得为空').then(action => {
      //
      //   })
      //   return
      // }
      if (this.clientStyleTest.length === 0) {
        MessageBox.alert('客户类型不得为空').then(action => {

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
      // if (this.personTitle === null) {
      //   MessageBox.alert('人员职称不得为空').then(action => {
      //
      //   })
      //   return
      // }
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
      // if (this.currentLevel === null) {
      //   MessageBox.alert('当前等级不得为空').then(action => {
      //
      //   })
      //   return
      // }
      /** 打印一下发送的数据 */
      console.log('向后台发送的数据', data)
      /** 向后台发送数据 */
      // http://localhost:8443
      // this.$axios.post('http://localhost:6501/customerManage',
      // this.$axios.post('http://dropdbandescape.parkwing.cn/codiv/customerManage',
      this.$axios.post('/customerManage', data).then(function (resp) {
        console.log(resp)
        if (resp.status === 200) {
          MessageBox.alert('添加成功！').then(action => {
          })
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
      this.clientstate = '有效'
      this.contactNumber = null
      this.contactStaff = null
      // this.personTitle = null
      this.personTitleP = []
      // this.sourceChannel = null
      this.sourceChannelP = []
      // this.phoneVisit = null
      this.phoneVisitP = []
      // this.takeVisit = null
      this.takeVisitP = []
      this.needSquare = null
      this.predictDealTime = null
      this.currentproblem = null
      // this.wantPark = null
      this.wantParkP = []
      // 清空的时候需要把楼座的列表也清空
      this.wantBuilding2 = []
      this.wantBuilding = null
      this.wantBuildingP2 = []
      this.wantBuildingP = []
      this.requiredsupport = null
      this.price = null
      // this.currentLevel = null
      this.currentLevelP = []
      // this.merchantStaff = null
      // this.merchantDepartment = null
      this.clientinfo = null
    }
  }
}
</script>

<style>
  /*.radioStyle::-webkit-input-placeholder {*/
    /*color: lightskyblue;*/
    /*text-align: left;*/
  /*}*/
  .header{
    margin-top: -1.5em;
  }
  .autoStyle{
    margin: 0.5em 0.1em 0.5em -1em;
  }
  .confirmButton{
    margin: 0.5em 0.5em 0.5em 5em;
  }
  .clearButton{
    margin: 0.5em 0.5em 0.5em 0.5em;
  }
  .container {
    float:left;
  }
  .content{
    float:left;
  }

</style>
