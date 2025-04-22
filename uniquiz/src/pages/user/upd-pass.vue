<template>
    <view class="content">

        <uni-forms :modelValue="msg">
            <uni-forms-item label="旧密码" name="old_password">
                <uni-easyinput type="text" v-model="msg.old_password" />
            </uni-forms-item>
            <uni-forms-item label="新密码" name="new_password">
                <uni-easyinput type="text" v-model="msg.new_password" />
            </uni-forms-item>
            <uni-forms-item name="code" label="验证码">
                <uni-easyinput type="text" v-model="msg.code" />
            </uni-forms-item>
            <view v-show="show">验证码已发送至您账号绑定的邮箱，有效期为5分钟</view>
        </uni-forms>

        <button size="mini" @click="send_code(token)">
            发送验证码
        </button>
        <button size="mini" @click="upd_password(token)">
            修改密码
        </button>

    </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import { onMounted } from 'vue'

onMounted(() => {
    get_token()
})

const show = ref(false)

const get_token = () => {
    token.value = uni.getStorageSync(
        'Authorization'
    )

    if (!token.value) {
        uni.redirectTo({ url: "/pages/msg/login" })
    }
}
const token = ref()

interface usr_password {
    old_password: string
    new_password: string
    code: string
}
const msg = ref<usr_password>({
    old_password: '',
    new_password: '',
    code: '',
})

const upd_password = (token: string) => {
    uni.request({
        url: "/api/usr/password",
        data: msg.value,
        method: "PUT",
        header: { 'Authorization': token }
    }).then(res => {
        uni.showToast({ title: res.data.toString(), icon: 'none' })
    }).catch(err => alert(err))
}

const send_code = (token: string) => {
    uni.request({
        url: "/api/email/password",
        header: { 'Authorization': token }
    }).then(res => {
        if (res.statusCode == 200) {
            show.value = true
        } else {
            uni.showToast({ title: res.data.toString(), icon: 'none' })
        }
    }).catch(err => alert(err))
}

</script>

<style scoped lang="scss">
.context {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

button {
    margin-top: 10px;
}
</style>