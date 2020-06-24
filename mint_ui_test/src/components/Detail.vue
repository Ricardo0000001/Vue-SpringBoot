<template>
  <div>
    <!--<load-more tip="详细信息" :show-loading="false" background-color="#fbf9fe"></load-more>-->
    <div>
      <br>
      <x-header class="header"  :left-options="{showBack: false}" left-options.preventGoBack="true" @on-click-back="goBack">工作汇报详情</x-header>
      <br>
      <x-table :full-bordered="true"  :content-bordered="true">
        <tbody>
        <tr>
          <td>客户名称</td>
          <!--<td>{{content[myIndex].clientName}}</td>-->
          <td>{{data.clientName}}</td>
        </tr>
        <tr>
          <td>日期</td>
          <!--<td>{{// content[myIndex].dateTime.substr(0, 10)}}</td>-->
          <td>{{data.dateTime.substr(0, 10)}}</td>
        </tr>
        <tr>
          <td>客户状态</td>
          <td style="color: #f76260; font-weight: 600">{{data.clientstate}}</td>
          <!--<td style="color: #f76260; font-weight: 600">{{content[myIndex].clientstate}}</td>-->
        </tr>
        <tr>
          <td>类型</td>
          <td>{{data.clientStyle}}</td>
          <!--<td>{{content[myIndex].clientStyle}}</td>-->
        </tr>
        <tr>
          <td>客户行业</td>
          <td>{{data.clientindustry}}</td>
          <!--<td>{{content[myIndex].clientStyle}}</td>-->
        </tr>
        <tr>
          <td>联系方式</td>
          <!--<td>{{content[myIndex].contactNumber}}</td>-->
          <td>{{data.contactNumber}}</td>
        </tr>
        <tr>
          <td>联系人员</td>
          <!--<td>{{content[myIndex].contactStaff}}</td>-->
          <td>{{data.contactStaff}}</td>
        </tr>
        <tr>
          <td>人员职称</td>
          <td>{{data.personTitle}}</td>
          <!--<td>{{content[myIndex].personTitle}}</td>-->
        </tr>
        <tr>
          <td>来源渠道</td>
          <td>{{data.sourceChannel}}</td>
          <!--<td>{{content[myIndex].sourceChannel}}</td>-->
        </tr>
        <tr>
          <td>电话沟通</td>
          <td>{{data.phoneVisit}}</td>
          <!--<td>{{content[myIndex].phoneVisit}}</td>-->
        </tr>
        <tr>
          <td>带看</td>
          <!--<td>{{content[myIndex].takeVisit}}</td>-->
          <td>{{data.takeVisit}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>需求面积</td>
          <!--<td>{{content[myIndex].needSquare}}</td>-->
          <td>{{data.needSquare | thousandsFilter(data.needSquare)}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>价格</td>
          <td>{{data.price}}</td>
          <!--<td>{{content[myIndex].faceProble}}</td>-->
        </tr>
        <tr v-if="styleQudao2">
          <td>意向园区</td>
          <!--<td>{{content[myIndex].wantPark}}</td>-->
          <td>{{data.wantPark}}</td>
        </tr>
        <tr v-if="styleQudao2">
          <td>意向楼座</td>
          <td>{{data.wantBuilding}}</td>
          <!--<td>{{content[myIndex].wantBuilding}}</td>-->
        </tr>
        <tr v-if="styleQudao2">
          <td>预计成交</td>
          <td>{{data.predictdealtime.substr(0, 10)}}</td>
          <!--<td>{{content[myIndex].predictdealtime.substr(0, 10)}}</td>-->
        </tr>
        <tr>
          <td>客户情况</td>
          <td>{{data.clientinfo}}</td>
          <!--<td>{{content[myIndex].faceProble}}</td>-->
        </tr>
        <tr>
          <td>所遇困难</td>
          <td>{{data.currentproblem}}</td>
          <!--<td>{{content[myIndex].faceProble}}</td>-->
        </tr>
        <tr>
          <td>需求支持</td>
          <td>{{data.requiredsupport}}</td>
          <!--<td>{{content[myIndex].wantSupport}}</td>-->
        </tr>
        <tr>
          <td>当前等级</td>
          <!--<td>{{content[myIndex].currentLevel}}</td>-->
          <td>{{data.currentLevel}}</td>
        </tr>
        <tr>
          <td>招商人员</td>
          <td>{{data.merchantStaff}}</td>
          <!--<td>{{content[myIndex].merchantStaff}}</td>-->
        </tr>
        <tr>
          <td>招商部门</td>
          <!--<td>{{content[myIndex].merchantDepartment}}</td>-->
          <td>{{data.merchantDepartment}}</td>
        </tr>
        </tbody>
      </x-table>
      <box gap="20px 20px">
      <!--<flexbox>-->
        <!--<flexbox-item>-->
          <!--&lt;!&ndash;<x-button type="default">default</x-button>&ndash;&gt;-->
        <!--</flexbox-item>-->
        <!--<flexbox-item>-->
          <!--&lt;!&ndash;<x-button type="primary">primary</x-button>&ndash;&gt;-->
        <!--</flexbox-item>-->
        <!--<flexbox-item>-->
          <x-button type="warn" @click.native="deleteItem">删除</x-button>
        <!--</flexbox-item>-->
      <!--</flexbox>-->
      </box>
      <!--<mt-button  class="deleteButton" type="danger"  @click.native="deleteItem">删除</mt-button>-->
      <divider>我是有底线的</divider>
    </div>
  </div>
</template>

<script>import { MessageBox } from 'mint-ui'
import { XTable, LoadMore, Divider, XHeader, XButton, Box, Flexbox, FlexboxItem } from 'vux'

export default {
  components: {
    XTable,
    LoadMore,
    Divider,
    XHeader,
    XButton,
    Box,
    Flexbox,
    FlexboxItem
  },
  data: function () {
    return {
      // 测试用
      styleQudao2: true,
      // 后台获取的数据
      data: [],
      myIndex: '',
      content: [],
      // 数据条目对应的id
      openid: '',
      id: '',
      tableHeader:
        [
          '序号', '日期', '客户名称', '类型(客户/渠道)', '联系方式', '联系人员', '人员职称', '客户渠道'
        ]

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
    // 获取了id
    this.id = this.$route.params.index
    this.openid = this.$route.params.openid
    this.selectDataId(this.$route.params.index)
  },
  methods: {
    /**
     * 点击返回时触发
     */
    goBack () {
      this.$router.go(-1)
    },
    /**
     * 根据获取的id去取一条值
     * /customerManage/{id}
     * 参数 ： id
     */
    selectDataId (id) {
      if (id == null) {
        console.log('id is null')
      }
      console.log('打印下', id)
      this.$axios.get('/customerManage/' + id).then((resp) => {
        if (resp.status === 200) {
          console.log('print the return data:', resp.data.data)
          this.data = resp.data.data
          if (this.data.clientStyle === '渠道') {
            this.styleQudao2 = false
          }
          if (this.data.dateTime === undefined || this.data.dateTime == null) {
            this.data.dateTime = '未知'
          }
          if (this.data.predictdealtime === undefined || this.data.predictdealtime == null) {
            this.data.predictdealtime = '尚未确定'
          }
        }
      })
    },
    /**
     *根据id去删除数据
     * 删除后返回上一页面
     */
    deleteItem: function () {
      this.$axios.delete('/customerManage/' + this.id).then((resp) => {
        if (resp.status === 200) {
          MessageBox.alert('删除成功！').then(action => {
            this.$router.go(-1)
          })
        }
      })
    }
  }
}
</script>

<style>

</style>
