<template>
    <view class="content">
        <view>验证码已发送至您账号绑定的邮箱，请在5分钟内输入验证码：</view>
        <br>
        <uni-forms :modelValue="captcha">
            <uni-easyinput type="text" v-model="captcha" />
        </uni-forms>

        <button size="mini" @click="logoff(token, captcha)">
            确认注销
        </button>
    </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import { onMounted } from 'vue'

onMounted(() => {
    get_token()
})



const get_token = () => {
    token.value = uni.getStorageSync(
        'Authorization'
    )

    if (!token.value) {
        uni.redirectTo({ url: "/pages/user/login" })
    }
}
const token = ref()

const captcha = ref('')

const logoff = (token: string, captcha: string) => {
    uni.request({
        url: "/api/usr/logoff/" + captcha,
        method: "DELETE",
        header: { 'Authorization': token }
    }).then(res => {
        if (res.statusCode == 200) {
            uni.redirectTo({ url: "/pages/user/login" })
            uni.clearStorage()
        }
        uni.showToast({ title: res.data.toString(), icon: 'none' })
    }).catch(err => alert(err))
}


</script>

<style scoped lang="scss">
button {
    margin-top: 10px;
}
</style>