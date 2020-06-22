<!--suppress ALL -->
<template>
  <div>
    <br>
    <x-header class="header" :left-options="{showBack: false}">部门工作统计表</x-header>
    <br>
    <box gap="10px 10px">
      <x-button @click.native="searchLastWeek">最近一周</x-button>
      <x-button @click.native="searchLastMonth">最近一个月</x-button>
    </box>
    <!--<group>-->
      <!--<datetime-->
        <!--v-model="timePoint"-->
        <!--:title="$t('请选择时间点')"-->
        <!--@on-confirm="onTimePoint"></datetime>-->
    <!--</group>-->
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
    <divider>^</divider>
    <!--统计表的详细内容：部门 新客户 老客户 新客户面积 老客户面积 电话次数 带看次数-->
    <div v-for="(data, index) in respData" :key="index" style="padding:0 5px;">
      <x-table  :full-bordered="true" :cell-bordered="false" :content-bordered="true"  style="background-color:#fff;">
        <tbody >
        <tr >
          <td style="font-weight: 600">部门:</td>
          <td style="font-weight: 600" colspan="3">{{data.merchantDepartment}}</td>
        </tr>
        <tr >
          <td >新客户数:</td>
          <td >{{data.newClientCount}}</td>
          <td >老客户数:</td>
          <td >{{data.oldClientCount}}</td>
        </tr>
        <tr >
          <td >新客户面积:</td>
          <td >{{data.newClientSquareSum | thousandsFilter(data.newClientSquareSum)}}</td>
          <td >老客户面积:</td>
          <td >{{data.oldClientSquareSum | thousandsFilter(data.oldClientSquareSum)}}</td>
        </tr>
        <tr >
          <td >电话次数:</td>
          <td >{{data.phoneVisitCount}}</td>
          <td >带看次数:</td>
          <td >{{data.takeVisitCount}}</td>
        </tr>
        </tbody>
      </x-table>
      <divider></divider>
    </div>
  </div>
</template>

<script>import { XHeader, Datetime, DatetimePlugin, Group, XTable, Divider, XButton, Box } from 'vux'
export default {
  components: {
    XHeader,
    Datetime,
    DatetimePlugin,
    Group,
    XTable,
    Divider,
    XButton,
    Box
  },
  name: 'Statis',
  data: function () {
    return {
      // 选择时间段
      startTime: null,
      endTime: null,
      // 可以选择的时间点弃用
      timePoint: null,
      // 获取的返回数据
      respData: [],
      //
      // openid
      openid: null,
      judgePermit: false,
      staffInforUrl: 'https://dropdbandescape.parkwing.cn/codiv/role_employee/details',
      department: []
    }
  },
  /**
   * 新增千分位过滤器 组件内过滤可以不改变原数字只用做数字显示
   */
  filters: {
    thousandsFilter (val) {
      if (!val) {
        return '0'
      }
      var intPart = Number(val).toFixed(0) // 获取整数部分
      var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,') // 将整数部分逢三一断
      return intPartFormat
    }
  },
  mounted () {
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
      console.log('print the gerPermission data:', back.data[0].permit)
      if (back.data[0].permit === 'all') { // 黄忠伟 陈凯奇权限
        this.judgePermit = true
        this.department[0] = null // 表示可以返回全部部门的数据
      } else {
        this.judgePermit = false // 不显示部门选择的控件 并且写死了部门选项的值
      }
    },
    /**
     * 根据openid获取姓名 部门
     */
    async getNameAndDepart () {
      var _this = this
      this.openid = this.$route.params.openid
      console.log('print the openid:', this.openid)
      var param = {'openid': this.openid}
      // var test = await this.$axios.post(this.staffInforUrl, param)
      var test = await this.$axios.post(this.staffInforUrl, param)
      console.log('print the test content', test.data.list[0])
      _this.merchantStaff = test.data.list[0].name
      _this.department[0] = test.data.list[0].deptname
      console.log(_this.merchantStaff)
      console.log(_this.department[0])
    },
    /**
     * 当前日期后一天
     */
    afterCurrentDate () {
      var d = new Date(Date.now() + 1 * 24 * 60 * 60 * 1000)
      var month = d.getMonth() + 1
      var date = d.getDate()
      if (d.getMonth() + 1 < 10) {
        month = '0' + month
      }
      if (d.getDate() + 1 < 10) {
        date = '0' + date
      }
      return d.getFullYear() + '-' + month + '-' + date
    },
    /**
     * 本周第一天
     */
    showWeekFirstDay: function () {
      console.log('11111111111111')
      let Nowdate = new Date()
      let WeekFirstDay = new Date(Nowdate - (Nowdate.getDay() - 1) * 86400000)
      let M = Number(WeekFirstDay.getMonth()) + 1
      if (M < 10) {
        M = '0' + M
      }
      let D = WeekFirstDay.getDate()
      if (D < 10) {
        D = '0' + D
      }
      return WeekFirstDay.getFullYear() + '-' + M + '-' + D
    },
    /**
     * 得到某月天数
     */
    getMonthDays: function (myMonth) {
      let monthStartDate = new Date(new Date().getFullYear(), myMonth, 1)
      let monthEndDate = new Date(new Date().getFullYear(), myMonth + 1, 1)
      let days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24)
      return days
    },
    /**
     *  时间解析
     */
    analyseTime: function (now) {
      let year = new Date(now).getFullYear()
      let month = new Date(now).getMonth() + 1
      let date = new Date(now).getDate()
      if (month < 10) month = '0' + month
      if (date < 10) date = '0' + date
      return year + '-' + month + '-' + date
    },
    /**
     * 最近一周的排名数据
     */
    searchLastWeek () {
      var _this = this
      this.startTime = this.showWeekFirstDay()
      this.endTime = this.afterCurrentDate()
      console.log('print the first day of the week', this.start)
      var paramClientCountRank = {
        'department': this.department[0],
        'start': this.startTime,
        'end': this.endTime
      }
      this.$axios.post('/customerManage/statis', paramClientCountRank).then((resp) => {
        if (resp.status === 200) {
          _this.respData = resp.data
          console.log('print the return data', _this.respData)
          if (_this.department[0] !== null) {
            for (var i = 0; i < _this.respData.length; i++) {
              if (_this.respData[i].merchantDepartment !== _this.department[0]) {
                console.log('print the be deleted data:', _this.respData[i].merchantDepartment)
                // console.log('print the correct data:',_this.department[0])
                _this.respData.splice(i)
                // console.log('print the lenght:', _this.respData.length)
              }
            }
          }
          console.log('print the handling data', _this.respData)
          // _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].newClientCount === undefined) {
              this.$set(_this.respData[i], 'newClientCount', 0)
            }
            if (_this.respData[i].oldClientCount === undefined) {
              this.$set(_this.respData[i], 'oldClientCount', 0)
            }
            if (_this.respData[i].newClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'newClientSquareSum', 0)
            }
            if (_this.respData[i].oldClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'oldClientSquareSum', 0)
            }
            if (_this.respData[i].phoneVisitCount === undefined) {
              this.$set(_this.respData[i], 'phoneVisitCount', 0)
            }
            if (_this.respData[i].takeVisitCount === undefined) {
              this.$set(_this.respData[i], 'takeVisitCount', 0)
            }
          }
        }
      })
      // this.$axios.post('/customerManage/statis', paramClientCountRank).then((resp) => {
      //   if (resp.status === 200) {
      //     console.log('print the return data:', resp.data)
      //     _this.respData = resp.data
      //     for (let i = 0; i < _this.respData.length; i++) {
      //       if (_this.respData[i].newClientCount === undefined) {
      //         this.$set(_this.respData[i], 'newClientCount', 0)
      //       }
      //       if (_this.respData[i].oldClientCount === undefined) {
      //         this.$set(_this.respData[i], 'oldClientCount', 0)
      //       }
      //       if (_this.respData[i].newClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'newClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].oldClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'oldClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].phoneVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'phoneVisitCount', 0)
      //       }
      //       if (_this.respData[i].takeVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'takeVisitCount', 0)
      //       }
      //     }
      //   }
      // })
    },
    /**
     * 最近一个月的排名数据
     */
    searchLastMonth () {
      var _this = this
      this.startTime = this.analyseTime(new Date(new Date().getFullYear(), new Date().getMonth(), 1))
      this.endTime = this.afterCurrentDate()
      var paramClientCountRank = {
        'department': this.department[0],
        'start': this.startTime,
        'end': this.endTime
      }
      this.$axios.post('/customerManage/statis', paramClientCountRank).then((resp) => {
        if (resp.status === 200) {
          _this.respData = resp.data
          console.log('print the return data', _this.respData)
          if (_this.department[0] !== null) {
            for (var i = 0; i < _this.respData.length; i++) {
              if (_this.respData[i].merchantDepartment !== _this.department[0]) {
                console.log('print the be deleted data:', _this.respData[i].merchantDepartment)
                // console.log('print the correct data:',_this.department[0])
                _this.respData.splice(i)
                // console.log('print the lenght:', _this.respData.length)
              }
            }
          }
          console.log('print the handling data', _this.respData)
          // _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].newClientCount === undefined) {
              this.$set(_this.respData[i], 'newClientCount', 0)
            }
            if (_this.respData[i].oldClientCount === undefined) {
              this.$set(_this.respData[i], 'oldClientCount', 0)
            }
            if (_this.respData[i].newClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'newClientSquareSum', 0)
            }
            if (_this.respData[i].oldClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'oldClientSquareSum', 0)
            }
            if (_this.respData[i].phoneVisitCount === undefined) {
              this.$set(_this.respData[i], 'phoneVisitCount', 0)
            }
            if (_this.respData[i].takeVisitCount === undefined) {
              this.$set(_this.respData[i], 'takeVisitCount', 0)
            }
          }
        }
      })
      // this.$axios.post('/customerManage/statis', paramClientCountRank).then((resp) => {
      //   if (resp.status === 200) {
      //     console.log('print the return data:', resp.data)
      //     _this.respData = resp.data
      //     for (let i = 0; i < _this.respData.length; i++) {
      //       if (_this.respData[i].newClientCount === undefined) {
      //         this.$set(_this.respData[i], 'newClientCount', 0)
      //       }
      //       if (_this.respData[i].oldClientCount === undefined) {
      //         this.$set(_this.respData[i], 'oldClientCount', 0)
      //       }
      //       if (_this.respData[i].newClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'newClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].oldClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'oldClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].phoneVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'phoneVisitCount', 0)
      //       }
      //       if (_this.respData[i].takeVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'takeVisitCount', 0)
      //       }
      //     }
      //   }
      // })
    },
    /**
     *加载数据
     */
    async loadData () {
      await this.getNameAndDepart()
      await this.getPermission()
      var _this = this
      console.log('print the department before sending the info:', this.department[0])
      var paramStatis = {
        'department': this.department[0],
        'start': this.startTime,
        'end': this.endTime
      }
      this.$axios.post('/customerManage/statis', paramStatis).then((resp) => {
        if (resp.status === 200) {
          _this.respData = resp.data
          console.log('print the return data', _this.respData)
          if (_this.department[0] !== null) {
            for (var i = 0; i < _this.respData.length; i++) {
              if (_this.respData[i].merchantDepartment !== _this.department[0]) {
                console.log('print the be deleted data:', _this.respData[i].merchantDepartment)
                // console.log('print the correct data:',_this.department[0])
                _this.respData.splice(i)
                // console.log('print the lenght:', _this.respData.length)
              }
            }
          }
          console.log('print the handling data', _this.respData)
          // _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].newClientCount === undefined) {
              this.$set(_this.respData[i], 'newClientCount', 0)
            }
            if (_this.respData[i].oldClientCount === undefined) {
              this.$set(_this.respData[i], 'oldClientCount', 0)
            }
            if (_this.respData[i].newClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'newClientSquareSum', 0)
            }
            if (_this.respData[i].oldClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'oldClientSquareSum', 0)
            }
            if (_this.respData[i].phoneVisitCount === undefined) {
              this.$set(_this.respData[i], 'phoneVisitCount', 0)
            }
            if (_this.respData[i].takeVisitCount === undefined) {
              this.$set(_this.respData[i], 'takeVisitCount', 0)
            }
          }
        }
      })
    },
    /**
     * 时间段选择
     *    新客户：      该时间段之内的是新客户
     *    老客户：      时间段之前的是老客户
     *    新客户面积：  当前时间段内新客户的面积总数
     *    老客户面积：  当前时间段之前的老客户的面积总数
     *    电话次数：    当前时间段内该部门的电话次数
     *    带看：        前时间段内该部门的带看次数
     * @param val
     */
    onConfirmStart (val) {
      var _this = this
      this.startTime = val
      var paramStatis = {
        'department': this.department[0],
        'start': this.startTime,
        'end': this.endTime
      }
      this.$axios.post('/customerManage/statis', paramStatis).then((resp) => {
        if (resp.status === 200) {
          _this.respData = resp.data
          console.log('print the return data', _this.respData)
          if (_this.department[0] !== null) {
            for (var i = 0; i < _this.respData.length; i++) {
              if (_this.respData[i].merchantDepartment !== _this.department[0]) {
                console.log('print the be deleted data:', _this.respData[i].merchantDepartment)
                // console.log('print the correct data:',_this.department[0])
                _this.respData.splice(i)
                // console.log('print the lenght:', _this.respData.length)
              }
            }
          }
          console.log('print the handling data', _this.respData)
          // _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].newClientCount === undefined) {
              this.$set(_this.respData[i], 'newClientCount', 0)
            }
            if (_this.respData[i].oldClientCount === undefined) {
              this.$set(_this.respData[i], 'oldClientCount', 0)
            }
            if (_this.respData[i].newClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'newClientSquareSum', 0)
            }
            if (_this.respData[i].oldClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'oldClientSquareSum', 0)
            }
            if (_this.respData[i].phoneVisitCount === undefined) {
              this.$set(_this.respData[i], 'phoneVisitCount', 0)
            }
            if (_this.respData[i].takeVisitCount === undefined) {
              this.$set(_this.respData[i], 'takeVisitCount', 0)
            }
          }
        }
      })
      // this.$axios.post('/customerManage/statis', paramStatis).then((resp) => {
      //   if (resp.status === 200) {
      //     console.log('print the return data', resp.data)
      //     _this.respData = resp.data
      //     for (let i = 0; i < _this.respData.length; i++) {
      //       if (_this.respData[i].newClientCount === undefined) {
      //         this.$set(_this.respData[i], 'newClientCount', 0)
      //       }
      //       if (_this.respData[i].oldClientCount === undefined) {
      //         this.$set(_this.respData[i], 'oldClientCount', 0)
      //       }
      //       if (_this.respData[i].newClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'newClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].oldClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'oldClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].phoneVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'phoneVisitCount', 0)
      //       }
      //       if (_this.respData[i].takeVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'takeVisitCount', 0)
      //       }
      //     }
      //   }
      // })
    },
    onConfirmEnd (val) {
      var _this = this
      this.endTime = val
      var paramStatis = {
        'department': this.department[0],
        'start': this.startTime,
        'end': this.endTime
      }
      this.$axios.post('/customerManage/statis', paramStatis).then((resp) => {
        if (resp.status === 200) {
          _this.respData = resp.data
          console.log('print the return data', _this.respData)
          if (_this.department[0] !== null) {
            for (var i = 0; i < _this.respData.length; i++) {
              if (_this.respData[i].merchantDepartment !== _this.department[0]) {
                console.log('print the be deleted data:', _this.respData[i].merchantDepartment)
                // console.log('print the correct data:',_this.department[0])
                _this.respData.splice(i)
                // console.log('print the lenght:', _this.respData.length)
              }
            }
          }
          console.log('print the handling data', _this.respData)
          // _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].newClientCount === undefined) {
              this.$set(_this.respData[i], 'newClientCount', 0)
            }
            if (_this.respData[i].oldClientCount === undefined) {
              this.$set(_this.respData[i], 'oldClientCount', 0)
            }
            if (_this.respData[i].newClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'newClientSquareSum', 0)
            }
            if (_this.respData[i].oldClientSquareSum === undefined) {
              this.$set(_this.respData[i], 'oldClientSquareSum', 0)
            }
            if (_this.respData[i].phoneVisitCount === undefined) {
              this.$set(_this.respData[i], 'phoneVisitCount', 0)
            }
            if (_this.respData[i].takeVisitCount === undefined) {
              this.$set(_this.respData[i], 'takeVisitCount', 0)
            }
          }
        }
      })
      // this.$axios.post('/customerManage/statis', paramStatis).then((resp) => {
      //   if (resp.status === 200) {
      //     console.log('print the return data', resp.data)
      //     _this.respData = resp.data
      //     for (let i = 0; i < _this.respData.length; i++) {
      //       if (_this.respData[i].newClientCount === undefined) {
      //         this.$set(_this.respData[i], 'newClientCount', 0)
      //       }
      //       if (_this.respData[i].oldClientCount === undefined) {
      //         this.$set(_this.respData[i], 'oldClientCount', 0)
      //       }
      //       if (_this.respData[i].newClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'newClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].oldClientSquareSum === undefined) {
      //         this.$set(_this.respData[i], 'oldClientSquareSum', 0)
      //       }
      //       if (_this.respData[i].phoneVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'phoneVisitCount', 0)
      //       }
      //       if (_this.respData[i].takeVisitCount === undefined) {
      //         this.$set(_this.respData[i], 'takeVisitCount', 0)
      //       }
      //     }
      //   }
      // })
    }
  }
}
</script>

<style >
  .header{
    margin-top: -1.5em;
  }

</style>
