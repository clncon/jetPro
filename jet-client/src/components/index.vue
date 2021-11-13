<template>
    <el-container style="height: 500px; border: 1px solid #eee">
      <el-header style="text-align: right; font-size: 12px">
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="openDialog()">上传任务</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span>JET-CLIENT</span>
      </el-header>
      <el-main>
        <el-table :data="tableData">
          <el-table-column prop="id" label="任务编号" width="200">
          </el-table-column>
          <el-table-column prop="taskName" label="任务名称" width="200">
          </el-table-column>
          <el-table-column prop="status" label="任务状态" width="200">
          </el-table-column>
          <el-table-column prop="submitTime" label="提交时间">
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-link @click="click('suspend',scope.row)">挂起</el-link> |  <el-link @click="click('resume',scope.row)">恢复</el-link> |  <el-link  @click="click('cancel',scope.row)">取消</el-link> | <el-link @click="click('restart',scope.row)">重启</el-link>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer>
        <span>version-0.0.1</span>
      </el-footer>
      <el-dialog
          :title = "'上传任务'"
          :visible.sync="showDialog"
          width="fit-content"
      >
        <el-upload
            class="upload-demo"
            drag
            action="#"
            :auto-upload="true"
            :before-upload="beforeUpload"
            :http-request="(params) =>uploadFiles(params)"
            :on-remove="(file, fileList) => {handleRemove(file, fileList)}"
            :on-change="handleChange"
            :file-list="fileList"
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">音频文件格式限制: .mp3 .wav .ogg 或 .wma，且大小不超过20M</div>
        </el-upload>

      </el-dialog>
    </el-container>
</template>
<style>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>
<script>
import {
  submit,
  restart,
  cancel,
  suspend,
  resume,
  listTasks
} from '../utils/api'
import _ from 'lodash'
export default {
  data () {
    return {
      tableData: [],
      showDialog: false,
      fileList: []
    }
  },
  mounted () {
    this.list()
  },
  methods: {
    list () {
      listTasks().then(res => {
        this.tableData = res.data
      })
    },
    openDialog () {
      this.showDialog = true
    },
    beforeUpload (file) {
      let extension = file.name.substring(file.name.lastIndexOf('.') + 1)
      let fileTypes = ['jar']
      if (!_.includes(fileTypes, _.toLower(extension))) {
        this.$message.error('文件格式限制: .jar格式!')
        return false
      }
      let isLt50M = file.size / 1024 / 1024 <= 50
      if (!isLt50M) {
        this.$message.error('上传文件大小不能超过 20MB!')
        return false
      }

      return true
    },
    uploadFiles (param) {
      this.file = param.file
      console.log('file', this.file)
      let form = new FormData() // 携带文件必须使用此对象
      if (this.file) {
        form.append('file', this.file) // 把文件实体添加到表单对象
      }
      submit(form).then(res => {
        if (res.status === 200) {
          this.$message.success(res.data)
        } else {
          this.$message.error('submit error !', res.data)
        }
        location.reload()
        this.showDialog = false
      })
    },
    handleRemove (file, fileList) {
      this.fileList.remove(file)
    },
    handleChange (file, fileList) {
      this.fileList = fileList.slice(-1)
    },
    click (event, row) {
      if (event === 'restart') {
        restart(row.id).then(res => {
          console.log(res.data)
          if (res.status === 200) {
            this.$message.success('restart success !')
          } else {
            this.$message.error('the job can not restart !')
          }
          location.reload()
        })
      }
      if (event === 'cancel') {
        cancel(row.id).then(res => {
          console.log(res.data)
          if (res.status === 200) {
            this.$message.success('cancel success !', res.data)
          } else {
            this.$message.error('the job can not cancel !')
          }
          location.reload()
        })
      }
      if (event === 'suspend') {
        suspend(row.id).then(res => {
          console.log(res.data)
          if (res.status === 200) {
            this.$message.success('suspend success !')
          } else {
            this.$message.error('the job can not suspend !')
          }
          location.reload()
        })
      }
      if (event === 'resume') {
        resume(row.id).then(res => {
          console.log(res.data)
          if (res.status === 200) {
            this.$message.success('resume success !')
          } else {
            this.$message.error('the job can not resume !')
          }
          location.reload()
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
