<template>
  <div>
    <div>
      <br>
      <x-header class="header"  :left-options="{showBack: false}" left-options.preventGoBack="true">工作汇报详情</x-header>
      <br>
      <x-table :full-bordered="true"  :content-bordered="true" :cell-bordered="true">
        <tbody>
        <tr>
          <td>客户名称</td>
          <td width="75%">{{data.clientName}}</td>
        </tr>
        <tr>
          <td>日期</td>
          <td>{{data.dateTime.substr(0, 10)}}</td>
        </tr>
        <tr>
          <td>客户状态</td>
          <td style="color: #f76260; font-weight: 600">{{data.clientstate}}</td>
        </tr>
        <tr>
          <td>类型</td>
          <td>{{data.clientStyle}}</td>
        </tr>
        <tr>
          <td>客户行业</td>
          <td>{{data.clientindustry}}</td>
        </tr>
        <tr>
          <td>联系方式</td>
          <td>{{data.contactNumber}}</td>
        </tr>
        <tr>
          <td>联系人员</td>
          <td>{{data.contactStaff}}</td>
        </tr>
        <tr>
          <td>人员职称</td>
          <td>{{data.personTitle}}</td>
        </tr>
        <tr>
          <td>来源渠道</td>
          <td>{{data.sourceChannel}}</td>
        </tr>
        <tr>
          <td>电话沟通</td>
          <td>{{data.phoneVisit}}</td>
        </tr>
        <tr>
          <td>带看</td>
          <td>{{data.takeVisit}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>需求面积</td>
          <td>{{data.needSquare | thousandsFilter(data.needSquare)}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>价格</td>
          <td>{{data.price}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>意向园区</td>
          <td>{{data.wantPark}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>意向楼座</td>
          <td>{{data.wantBuilding}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>预计成交</td>
          <td>{{data.predictdealtime.substr(0, 10)}}</td>
        </tr>
        <tr>
          <td>客户情况</td>
          <td>{{data.clientinfo}}</td>
        </tr>
        <tr>
          <td>所遇困难</td>
          <td>{{data.currentproblem}}</td>
        </tr>
        <tr>
          <td>需求支持</td>
          <td>{{data.requiredsupport}}</td>
        </tr>
        <tr>
          <td>当前等级</td>
          <td>{{data.currentLevel}}</td>
        </tr>
        <tr>
          <td>招商人员</td>
          <td>{{data.merchantStaff}}</td>
        </tr>
        <tr>
          <td>招商部门</td>
          <td>{{data.merchantDepartment}}</td>
        </tr>
        </tbody>
      </x-table>
      <div>
        <confirm v-model="show"
                 :title="$t('确认删除？')"
                 @on-confirm="onConfirm">
          <p style="text-align:center;">{{ $t('删除后无法恢复！') }}</p>
        </confirm>
      </div>
      <box gap="20px 20px">
      <flexbox>
        <flexbox-item>
        </flexbox-item>
        <flexbox-item>
        </flexbox-item>
        <flexbox-item>
          <x-button type="warn" @click.native="deleteItem" v-if="deletePermissionData">删除</x-button>
        </flexbox-item>
      </flexbox>
      </box>
      <divider>我是有底线的</divider>
    </div>
  </div>
</template>

<script>import { MessageBox } from 'mint-ui'
import { XTable, Divider, XHeader, XButton, Box, Flexbox, FlexboxItem, Confirm } from 'vux'

export default {
  components: {
    XTable,
    Divider,
    XHeader,
    XButton,
    Box,
    Flexbox,
    FlexboxItem,
    Confirm
  },
  data: function () {
    return {
      // 控制删除按钮的显示 默认为不显示 只有员工自己删除数据时显示 领导也不能删除
      deletePermissionData: false,
      // 删除按钮确认弹窗的控制
      show: false,
      // 如果是渠道类型 有些字段不用显示
      styleQudao2: true,
      // 后台获取的数据
      data: [],
      // 数据条目对应的id
      // openid: '',
      id: ''
    }
  },
  /**
   * 新增千分位过滤器 组件内过滤可以不改变原数字只用做数字显示
   */
  filters: {
    thousandsFilter (val) {
      if (!val) {
        return '未知'
      }
      var intPart = Number(val).toFixed(0) // 获取整数部分
      var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,') // 将整数部分逢三一断
      return intPartFormat
    }
  },
  /**
   *根据传入的id去数据库取数据
   */
  mounted: function () {
    // 获取了id 在删除的时候会用这个id
    this.id = this.$route.params.index
    // this.openid = this.$route.params.openid
    this.selectDataId(this.$route.params.index) // 根据传入的参数中的id去取得一条数据返回
    this.deletePermission() // 判断删除数据的权限
  },
  methods: {
    /**
     * 根据获取的id去取一条值
     * /customerManage/{id}
     * 参数 ： id
     */
    selectDataId (id) {
      var _this = this
      if (id == null) {
        alert('id is null! please contact the coder!')
      }
      this.$axios.get('/customerManage/' + id).then((resp) => {
        if (resp.status === 200) {
          this.data = resp.data.data // 将返回数据赋值给data
          if (this.data.clientStyle === '渠道') { // 判断客户类型是否是渠道 如果是渠道就隐去一些没有数据的字段
            this.styleQudao2 = false
          }
          // 这里的两个判断处理是用来避免返回数据中字段为空时报错
          if (this.data.dateTime === undefined || this.data.dateTime == null) {
            this.data.dateTime = ''
          }
          if (this.data.predictdealtime === undefined || this.data.predictdealtime == null) {
            this.data.predictdealtime = ''
          }
          // console.log('print the this.data.merchantStaff1111111111', this.data.merchantStaff)
          // console.log('print the window.localStorage.getItem(\'globalStaff\')1111111111', window.localStorage.getItem('globalStaff'))
          // console.log('prin the judage', this.data.merchantStaff !== window.localStorage.getItem('globalStaff'))
          // 当前数据条目的员工和查看这条数据的员工姓名不一致的时候就隐去删除按钮
          if (this.data.merchantStaff !== window.localStorage.getItem('globalStaff')) {
            _this.deletePermissionData = false
          } else {
            _this.deletePermissionData = true
          }
        }
      })
    },
    /**
     *根据id去删除数据
     * 删除后返回上一页面
     */
    deleteItem: function () {
      // 修改show值为true confirm弹窗显示
      this.show = true
    },
    /**
     * 弹窗点击确认后进行删除然后返回上一页面
     */
    onConfirm () {
      this.$axios.delete('/customerManage/' + this.id).then((resp) => {
        if (resp.status === 200) {
          MessageBox.alert('删除成功！').then(action => {
            this.$router.go(-1)
          })
        }
      })
    },
    /**
     * 判断删除权限
     * 如果登录用户名和当前数据记录的名字不一致就隐藏删除按钮
     */
    deletePermission () {
      if (this.data.merchantStaff !== window.localStorage.getItem('globalStaff')) {
        this.deletePermissionData = false
      }
    }
  }
}
</script>

<style>

</style>
