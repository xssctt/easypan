<template>
    <el-dialog
        width="60%"
        title="地图选点"
        :visible.sync="isDialog"
        append-to-body>
        <iframe width="100%" height="100%" style="border: none;width:100%;height: 500px;" :src="map_src"></iframe>
    </el-dialog>
</template>
  
  <script>
    export default {
      data() {
        return {
          map_src: 'https://mapapi.qq.com/web/mapComponents/locationPicker/v/index.html?search=1&type=1&key=xxxxxxxxx&referer=location',
          form: {
            //省市区ID
            id_area: [
              0,
              0,
              0
            ],
            address: '',
            lng: '',
            lat: '',
          },
          map_data: {
            url: 'https://mapapi.qq.com/web/mapComponents/locationPicker/v/index.html?search=1&type=1&key=xxxxxxxxx&referer=location',
            address: '',
            lng: '',
            lat: '',
          },
          isDialog: false, // 控制模态框
        }
      },
      created() {
        this.getInfo()
      },
      methods: {
        // 选择
        getInfo(){
            let that = this
            window.addEventListener('message', function(event) {
            // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
            var loc = event.data;
            //   console.log(loc)
            if (loc && loc.module == 'locationPicker') { //防止其他应用也会向该页面post信息，需判断module是否为'locationPicker'
                that.map_data.address = loc.poiaddress
                that.map_data.lat = loc.latlng.lat
                that.map_data.lng = loc.latlng.lng
                //调用父组件方法并传值给父组件
                // console.log('map_data', that.map_data);
                that.$emit('chooseOrgAddr',that.map_data)
            }
            }, false);
        },
        // 父组件调用方法,打开模态框
        openDialog() {
            this.isDialog = true
        },
        // 关闭模态框
        closeDialog() {
            this.isDialog = false
        },
      }
    }
  </script>
  
  <style>
 
  </style>