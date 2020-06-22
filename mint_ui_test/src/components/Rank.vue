<template>
  <div>
    <br>
    <x-header class="header" :left-options="{showBack: false}">招商人员客户数量排名</x-header>
    <br>
    <box gap="10px 10px">
      <x-button @click.native="searchLastWeek">最近一周</x-button>
      <x-button @click.native="searchLastMonth">最近一个月</x-button>
    </box>
    <group >
      <datetime
        v-model="start"
        :title="$t('请选择起始时间')"
        @on-confirm="onConfirmStart"></datetime>
    </group>
    <group >
      <datetime
        v-model="end"
        :title="$t('请选择终止时间')"
        @on-confirm="onConfirmEnd"></datetime>
    </group>
    <!--<load-more tip="" :show-loading="false" background-color="#fbf9fe"></load-more>-->
    <divider>^</divider>
    <x-table :cell-bordered="false" :content-bordered="true" style="background-color:#fff;">
      <thead>
      <tr style="background-color: #F7F7F7">
        <th>招商部门</th>
        <th>招商人员</th>
        <th>客户数量</th>
        <th>客户面积</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(data, i) in respData" :key="i">
        <td>{{data.merchantDepartment}}</td>
        <td>{{data.merchantStaff}}</td>
        <td>{{data.clientCount}}</td>
        <td>{{data.squareData}}</td>
      </tr>
      </tbody>
    </x-table>
  </div>
</template>

<script>import {Datetime, DatetimePlugin, Group, XHeader, LoadMore, XTable, Divider, XButton, Box} from 'vux'
export default {
  name: 'Rank',
  components: {
    Datetime, DatetimePlugin, Group, XHeader, LoadMore, XTable, Divider, XButton, Box
  },
  data: function () {
    return {
      // 获取返回数据
      respData: [],
      // 起始时间和终止时间
      start: null,
      end: null
    }
  },
  mounted () {
    this.loadData()
  },
  methods: {
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
     * 最近一周的排名数据
     */
    searchLastWeek () {
      var _this = this
      // this.start = this.analyseTime(new Date(new Date().getFullYear(),
      //   new Date().getMonth(),
      //   new Date().getDate() - new Date().getDay() - 6))
      // this.end = this.analyseTime(new Date(new Date().getFullYear(),
      //   new Date().getMonth(),
      //   new Date().getDate() + (6 - new Date().getDay() - 6)))
      this.start = this.showWeekFirstDay()
      // var d = new Date(Date.now() + 1 * 24 * 60 * 60 * 1000)
      // var month = d.getMonth() + 1
      // var date = d.getDate()
      // if (d.getMonth() + 1 < 10) {
      //   month = '0' + month
      // }
      // if (d.getDate() + 1 < 10) {
      //   date = '0' + date
      // }
      this.end = this.afterCurrentDate()
      // this.end = d.getFullYear() + '-' + month + '-' + date
      console.log('end:', this.end)
      // this.end = this.analyseTime(new Date(new Date().getFullYear(),
      //   new Date().getMonth(),
      //   new Date().getDate()))
      console.log('print the first day of the week', this.start)
      var paramClientCountRank = {
        'start': this.start,
        'end': this.end
      }
      this.$axios.post('/customerManage/rank', paramClientCountRank).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].clientCount === undefined) {
              console.log('if the clientCount is undefined, then push the 0 as the number of the clientcount')
              this.$set(_this.respData[i], 'clientCount', 0)
            }
          }
        }
      })
    },
    /**
     * 最近一个月的排名数据
     */
    searchLastMonth () {
      var _this = this
      this.start = this.analyseTime(new Date(new Date().getFullYear(), new Date().getMonth(), 1))
      this.end = this.afterCurrentDate()
      // this.start = this.analyseTime(new Date(new Date().getFullYear(),
      //   new Date().getMonth(),
      //   1))
      // this.end = this.analyseTime(new Date(new Date().getFullYear(),
      //   new Date().getMonth(),
      //   this.getMonthDays(new Date().getMonth() - 1)))
      var paramClientCountRank = {
        'start': this.start,
        'end': this.end
      }
      this.$axios.post('/customerManage/rank', paramClientCountRank).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].clientCount === undefined) {
              console.log('if the clientCount is undefined, then push the 0 as the number of the clientcount')
              this.$set(_this.respData[i], 'clientCount', 0)
            }
          }
        }
      })
    },
    /**
     * 初始化获取排名
     * 时间参数为null
     * 默认全部客户类型 不关心有效还是无效
     */
    loadData () {
      var _this = this
      var paramClientCountRank = {
        'start': this.start,
        'end': this.end
      }
      this.$axios.post('/customerManage/rank', paramClientCountRank).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respData = resp.data
          // for (let i = 0; i < _this.respData.length; i++) {
          for (let i = 0; i < 17; i++) {
            if (_this.respData[i] === null) {
              break
            }
            if (_this.respData[i].clientCount === undefined) {
              console.log('if the clientCount is undefined, then push the 0 as the number of the clientcount')
              this.$set(_this.respData[i], 'clientCount', 0)
            }
          }
        }
      })
    },
    /**
     * 起始时间确认 弹窗关闭时触发
     */
    onConfirmStart (val) {
      var _this = this
      this.start = val
      var paramClientCountRank = {
        'start': this.start,
        'end': this.end
      }
      this.$axios.post('/customerManage/rank', paramClientCountRank).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].clientCount === undefined) {
              console.log('if the clientCount is undefined, then push the 0 as the number of the clientcount')
              this.$set(_this.respData[i], 'clientCount', 0)
            }
          }
        }
      })
    },
    /**
     * 终止时间确认 弹窗关闭时触发
     */
    onConfirmEnd (val) {
      var _this = this
      this.end = val
      var paramClientCountRank = {
        'start': this.start,
        'end': this.end
      }
      this.$axios.post('/customerManage/rank', paramClientCountRank).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data)
          _this.respData = resp.data
          for (let i = 0; i < _this.respData.length; i++) {
            if (_this.respData[i].clientCount === undefined) {
              console.log('if the clientCount is undefined, then push the 0 as the number of the clientcount')
              this.$set(_this.respData[i], 'clientCount', 0)
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
  .header{
    margin-top: -1.5em;
  }
</style>
